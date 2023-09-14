package xyz.shadowflare.crews.commands.crewSubCommands

import org.apache.commons.lang3.StringUtils

class CrewCreateSubCommand {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    var MIN_CHAR_LIMIT: Int = crewsConfig.getInt("crew-tags.min-character-limit")
    var MAX_CHAR_LIMIT: Int = crewsConfig.getInt("crew-tags.max-character-limit")
    var crews: Set<Map.Entry<UUID, Crew>> = CrewsStorageUtil.getCrews()
    var crewNamesList: ArrayList<String> = ArrayList()
    fun createCrewSubCommand(sender: CommandSender, args: Array<String>, bannedTags: List<String?>): Boolean {
        if (sender is Player) {
            crews.forEach { crews -> crewNamesList.add(crews.getValue().getCrewFinalName()) }
            if (args.size >= 2) {
                if (bannedTags.contains(args[1])) {
                    sender.sendMessage(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString("crew-name-is-banned").replace(
                                CREW_PLACEHOLDER, args[1]
                            )
                        )
                    )
                    return true
                }
                if (crewNamesList.contains(args[1])) {
                    sender.sendMessage(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString("crew-name-already-taken").replace(
                                CREW_PLACEHOLDER, args[1]
                            )
                        )
                    )
                    return true
                }
                for (names in crewNamesList) {
                    if (StringUtils.containsAnyIgnoreCase(names, args[1])) {
                        sender.sendMessage(
                            ColorUtils.translateColorCodes(
                                messagesConfig.getString("crew-name-already-taken").replace(
                                    CREW_PLACEHOLDER, args[1]
                                )
                            )
                        )
                        return true
                    }
                }
                if (args[1].contains("&") || args[1].contains("#")) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-name-cannot-contain-colours")))
                    return true
                }
                if (CrewsStorageUtil.isCrewOwner(sender)) {
                    sender.sendMessage(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString("crew-creation-failed").replace(
                                CREW_PLACEHOLDER, ColorUtils.translateColorCodes(
                                    args[1]
                                )
                            )
                        )
                    )
                    return true
                }
                if (CrewsStorageUtil.findCrewByPlayer(sender) != null) {
                    sender.sendMessage(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString("crew-creation-failed").replace(
                                CREW_PLACEHOLDER, ColorUtils.translateColorCodes(
                                    args[1]
                                )
                            )
                        )
                    )
                    return true
                }
                return if (args[1].length() < MIN_CHAR_LIMIT) {
                    val minCharLimit: Int = crewsConfig.getInt("crew-tags.min-character-limit")
                    sender.sendMessage(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString("crew-name-too-short")
                                .replace("%CHARMIN%", Integer.toString(minCharLimit))
                        )
                    )
                    true
                } else if (args[1].length() > MAX_CHAR_LIMIT) {
                    val maxCharLimit: Int = crewsConfig.getInt("crew-tags.max-character-limit")
                    sender.sendMessage(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString("crew-name-too-long")
                                .replace("%CHARMAX%", Integer.toString(maxCharLimit))
                        )
                    )
                    true
                } else {
                    if (!CrewsStorageUtil.isCrewExisting(sender)) {
                        val crew: Crew = CrewsStorageUtil.createCrew(sender, args[1])
                        val crewCreated: String =
                            ColorUtils.translateColorCodes(messagesConfig.getString("clan-created-successfully"))
                                .replace(
                                    CREW_PLACEHOLDER, ColorUtils.translateColorCodes(
                                        args[1]
                                    )
                                )
                        sender.sendMessage(crewCreated)
                        fireClanCreateEvent(sender, crew)
                        if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewCreateEvent"))
                        }
                        if (crewsConfig.getBoolean("crew-creation.announce-to-all")) {
                            if (crewsConfig.getBoolean("crew-creation.send-as-title")) {
                                for (onlinePlayers in Crews.connectedPlayers.keySet()) {
                                    onlinePlayers.sendTitle(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("crew-created-broadcast-title-1")
                                                .replace(CREW_OWNER, sender.getName())
                                                .replace(
                                                    CREW_PLACEHOLDER, ColorUtils.translateColorCodes(
                                                        args[1]
                                                    )
                                                )
                                        ),
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("crew-created-broadcast-title-2")
                                                .replace(CREW_OWNER, sender.getName())
                                                .replace(
                                                    CREW_PLACEHOLDER, ColorUtils.translateColorCodes(
                                                        args[1]
                                                    )
                                                )
                                        ),
                                        30, 30, 30
                                    )
                                }
                            } else {
                                Bukkit.broadcastMessage(
                                    ColorUtils.translateColorCodes(
                                        messagesConfig.getString("crew-created-broadcast-chat")
                                            .replace(CREW_OWNER, sender.getName())
                                            .replace(
                                                CREW_PLACEHOLDER, ColorUtils.translateColorCodes(
                                                    args[1]
                                                )
                                            )
                                    )
                                )
                            }
                        }
                    } else {
                        val clanNotCreated: String =
                            ColorUtils.translateColorCodes(messagesConfig.getString("crew-creation-failed")).replace(
                                CREW_PLACEHOLDER, ColorUtils.translateColorCodes(
                                    args[1]
                                )
                            )
                        sender.sendMessage(clanNotCreated)
                    }
                    crewNamesList.clear()
                    true
                }
            }
        }
        return false
    }

    companion object {
        private const val CREW_PLACEHOLDER = "%CREW%"
        private const val CREW_OWNER = "%CREWOWNER%"
        private fun fireClanCreateEvent(player: Player, crew: Crew) {
            val crewCreateEvent = CrewCreateEvent(player, crew)
            Bukkit.getPluginManager().callEvent(crewCreateEvent)
        }
    }
}
