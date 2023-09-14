package xyz.shadowflare.crews.commands.crewSubCommands

import org.bukkit.command.CommandSender

class CrewPrefixSubCommand {
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    var MIN_CHAR_LIMIT: Int = crewsConfig.getInt("crew-tags.min-character-limit")
    var MAX_CHAR_LIMIT: Int = crewsConfig.getInt("crew-tags.max-character-limit")
    var crews: Set<Map.Entry<UUID, Crew>> = CrewsStorageUtil.getCrews()
    var crewsPrefixList: ArrayList<String> = ArrayList()
    fun crewPrefixSubCommand(sender: CommandSender, args: Array<String>, bannedTags: List<String?>): Boolean {
        if (sender is Player) {
            crews.forEach { crews -> crewsPrefixList.add(crews.getValue().getCrewPrefix()) }
            if (args.size == 2) {
                if (bannedTags.contains(args[1])) {
                    sender.sendMessage(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString("crew-prefix-is-banned").replace("%CREWPREFIX%", args[1])
                        )
                    )
                    return true
                }
                if (crewsPrefixList.contains(args[1])) {
                    sender.sendMessage(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString("crew-prefix-already-taken").replace("%CREWPREFIX%", args[1])
                        )
                    )
                    return true
                }
                return if (CrewsStorageUtil.isCrewOwner(sender)) {
                    if (args[1].length() >= MIN_CHAR_LIMIT && args[1].length() <= MAX_CHAR_LIMIT) {
                        val playerCrew: Crew = CrewsStorageUtil.findCrewByOwner(sender)
                        CrewsStorageUtil.updatePrefix(sender, args[1])
                        val prefixConfirmation: String =
                            ColorUtils.translateColorCodes(messagesConfig.getString("crew-prefix-change-successful"))
                                .replace("%CREWPREFIX%", playerCrew.crewPrefix)
                        sender.sendMessage(prefixConfirmation)
                        crewsPrefixList.clear()
                        true
                    } else if (args[1].length() > MAX_CHAR_LIMIT) {
                        val maxCharLimit: Int = crewsConfig.getInt("crew-tags.max-character-limit")
                        sender.sendMessage(
                            ColorUtils.translateColorCodes(
                                messagesConfig.getString("crew-prefix-too-long")
                                    .replace("%CHARMAX%", String.valueOf(maxCharLimit))
                            )
                        )
                        crewsPrefixList.clear()
                        true
                    } else {
                        val minCharLimit: Int = crewsConfig.getInt("crew-tags.min-character-limit")
                        sender.sendMessage(
                            ColorUtils.translateColorCodes(
                                messagesConfig.getString("crew-prefix-too-short")
                                    .replace("%CHARMIN%", String.valueOf(minCharLimit))
                            )
                        )
                        crewsPrefixList.clear()
                        true
                    }
                } else {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("must-be-owner-to-change-prefix")))
                    crewsPrefixList.clear()
                    true
                }
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-invalid-prefix")))
                crewsPrefixList.clear()
            }
            return true
        }
        return false
    }
}
