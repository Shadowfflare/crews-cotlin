package xyz.shadowflare.crews.commands.crewSubCommands

import org.bukkit.Bukkit

class CrewAllySubCommand {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun crewAllySubCommand(sender: CommandSender, args: Array<String>): Boolean {
        if (sender is Player) {
            if (args.size > 2) {
                if (args[1].equalsIgnoreCase("add")) {
                    if (args[2].length() > 1) {
                        if (CrewsStorageUtil.isCrewOwner(sender)) {
                            if (CrewsStorageUtil.findCrewByOwner(sender) != null) {
                                val crew: Crew = CrewsStorageUtil.findCrewByOwner(sender)
                                val allyCrewOwner: Player = Bukkit.getPlayer(args[2])
                                if (allyCrewOwner != null) {
                                    if (CrewsStorageUtil.findCrewByOwner(allyCrewOwner) != null) {
                                        if (CrewsStorageUtil.findCrewByOwner(sender) !== CrewsStorageUtil.findCrewByOwner(
                                                allyCrewOwner
                                            )
                                        ) {
                                            val allyCrew: Crew = CrewsStorageUtil.findCrewByOwner(allyCrewOwner)
                                            val allyOwnerUUIDString: String = allyCrew.getCrewOwner()
                                            if (CrewsStorageUtil.findCrewByOwner(sender).getClanAllies()
                                                    .size() >= crewsConfig.getInt("max-crew-allies")
                                            ) {
                                                val maxSize: Int = crewsConfig.getInt("max-clan-allies")
                                                sender.sendMessage(
                                                    ColorUtils.translateColorCodes(
                                                        messagesConfig.getString(
                                                            "crew-ally-max-amount-reached"
                                                        )
                                                    ).replace("%LIMIT%", String.valueOf(maxSize))
                                                )
                                                return true
                                            }
                                            if (crew.getCrewEnemies().contains(allyOwnerUUIDString)) {
                                                sender.sendMessage(
                                                    ColorUtils.translateColorCodes(
                                                        messagesConfig.getString(
                                                            "failed-cannot-ally-enemy-crew"
                                                        )
                                                    )
                                                )
                                                return true
                                            }
                                            if (crew.getCrewAllies().contains(allyOwnerUUIDString)) {
                                                sender.sendMessage(
                                                    ColorUtils.translateColorCodes(
                                                        messagesConfig.getString(
                                                            "failed-crew-already-your-ally"
                                                        )
                                                    )
                                                )
                                                return true
                                            } else {
                                                CrewsStorageUtil.addCrewAlly(sender, allyCrewOwner)
                                                fireCrewAllyAddEvent(sender, crew, allyCrewOwner, allyCrew)
                                                if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewAllyAddEvent"))
                                                }
                                                sender.sendMessage(
                                                    ColorUtils.translateColorCodes(
                                                        messagesConfig.getString("added-crew-to-your-allies").replace(
                                                            ALLY_CREW, allyCrew.getCrewFinalName()
                                                        )
                                                    )
                                                )
                                            }
                                            if (allyCrewOwner.isOnline()) {
                                                allyCrewOwner.sendMessage(
                                                    ColorUtils.translateColorCodes(
                                                        messagesConfig.getString("crew-added-to-other-allies").replace(
                                                            CREW_OWNER, sender.getName()
                                                        )
                                                    )
                                                )
                                            } else {
                                                sender.sendMessage(
                                                    ColorUtils.translateColorCodes(
                                                        messagesConfig.getString("failed-to-add-crew-to-allies")
                                                            .replace(
                                                                ALLY_OWNER, args[2]
                                                            )
                                                    )
                                                )
                                            }
                                        } else {
                                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-cannot-ally-your-own-crew")))
                                        }
                                    } else {
                                        sender.sendMessage(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("failed-player-not-crew-owner").replace(
                                                    ALLY_OWNER, args[2]
                                                )
                                            )
                                        )
                                    }
                                } else {
                                    sender.sendMessage(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("ally-crew-add-owner-offline").replace(
                                                ALLY_OWNER, args[2]
                                            )
                                        )
                                    )
                                }
                            }
                        } else {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-must-be-owner")))
                        }
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-cre-ally-command-usage")))
                    }
                    return true
                } else if (args[1].equalsIgnoreCase("remove")) {
                    if (args[2].length() > 1) {
                        if (CrewsStorageUtil.isCrewOwner(sender)) {
                            if (CrewsStorageUtil.findCrewByOwner(sender) != null) {
                                val allyCrewOwner: Player = Bukkit.getPlayer(args[2])
                                if (allyCrewOwner != null) {
                                    if (CrewsStorageUtil.findCrewByOwner(allyCrewOwner) != null) {
                                        val allyCrew: Crew = CrewsStorageUtil.findCrewByOwner(allyCrewOwner)
                                        val alliedCrews: List<String> =
                                            CrewsStorageUtil.findCrewByOwner(sender).getCrewAllies()
                                        val allyCrewOwnerUUID: UUID = allyCrewOwner.getUniqueId()
                                        val allyCrewOwnerString: String = allyCrewOwnerUUID.toString()
                                        if (alliedCrews.contains(allyCrewOwnerString)) {
                                            fireCrewAllyRemoveEvent(sender, allyCrewOwner, allyCrew)
                                            if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewAllyRemoveEvent"))
                                            }
                                            CrewsStorageUtil.removeCrewAlly(sender, allyCrewOwner)
                                            sender.sendMessage(
                                                ColorUtils.translateColorCodes(
                                                    messagesConfig.getString("removed-ctre-from-your-allies").replace(
                                                        ALLY_CREW, allyCrew.getCrewFinalName()
                                                    )
                                                )
                                            )
                                            if (allyCrewOwner.isOnline()) {
                                                allyCrewOwner.sendMessage(
                                                    ColorUtils.translateColorCodes(
                                                        messagesConfig.getString("crew-removed-from-other-allies")
                                                            .replace(
                                                                CREW_OWNER, sender.getName()
                                                            )
                                                    )
                                                )
                                            }
                                        } else {
                                            sender.sendMessage(
                                                ColorUtils.translateColorCodes(
                                                    messagesConfig.getString("failed-to-remove-crew-from-allies")
                                                        .replace(
                                                            ALLY_OWNER, args[2]
                                                        )
                                                )
                                            )
                                        }
                                    } else {
                                        sender.sendMessage(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("failed-player-not-crew-owner").replace(
                                                    ALLY_OWNER, args[2]
                                                )
                                            )
                                        )
                                    }
                                } else {
                                    sender.sendMessage(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("ally-crew-remove-owner-offline").replace(
                                                ALLY_OWNER, args[2]
                                            )
                                        )
                                    )
                                }
                            }
                        } else {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-must-be-owner")))
                        }
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-crew-ally-command-usage")))
                    }
                }
                return true
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-crew-ally-command-usage")))
            }
            return true
        }
        return false
    }

    companion object {
        private const val ALLY_CREW = "%ALLYCREW%"
        private const val ALLY_OWNER = "%ALLYOWNER%"
        private const val CREW_OWNER = "%CREWOWNER%"
        private fun fireCrewAllyRemoveEvent(player: Player, allyCrewOwner: Player, allyCrew: Crew) {
            val crewAllyRemoveEvent =
                CrewAllyRemoveEvent(player, CrewsStorageUtil.findCrewByOwner(player), allyCrew, allyCrewOwner)
            Bukkit.getPluginManager().callEvent(crewAllyRemoveEvent)
        }

        private fun fireCrewAllyAddEvent(player: Player, crew: Crew, allyCrewOwner: Player, allyCrew: Crew) {
            val crewAllyAddEvent = CrewAllyAddEvent(player, crew, allyCrew, allyCrewOwner)
            Bukkit.getPluginManager().callEvent(crewAllyAddEvent)
        }
    }
}
