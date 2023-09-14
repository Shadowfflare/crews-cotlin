package xyz.gamlin.crews.commands.crewSubCommands

import org.bukkit.command.CommandSender

class CrewPvpSubCommand {
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun clanPvpSubCommand(sender: CommandSender): Boolean {
        if (sender is Player) {
            if (crewsConfig.getBoolean("protections.pvp.pvp-command-enabled")) {
                if (CrewsStorageUtil.isCrewOwner(sender)) {
                    if (CrewsStorageUtil.findCrewByOwner(sender) != null) {
                        val crew: Crew = CrewsStorageUtil.findCrewByOwner(sender)
                        if (crew.isFriendlyFireAllowed) {
                            crew.isFriendlyFireAllowed = false
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("disabled-friendly-fire")))
                        } else {
                            crew.isFriendlyFireAllowed = true
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("enabled-friendly-fire")))
                        }
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-not-in-crew")))
                    }
                } else {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-must-be-owner")))
                }
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")))
            }
            return true
        }
        return false
    }
}
