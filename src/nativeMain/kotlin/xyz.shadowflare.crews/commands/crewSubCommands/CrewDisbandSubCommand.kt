package xyz.shadowflare.crews.commands.crewSubCommands

import org.bukkit.command.CommandSender

class CrewDisbandSubCommand {
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun disbandCrewSubCommand(sender: CommandSender): Boolean {
        if (sender is Player) {
            try {
                if (CrewsStorageUtil.deleteCrew(sender)) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-successfully-disbanded")))
                } else {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-disband-failure")))
                }
            } catch (e: IOException) {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-1")))
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-2")))
                e.printStackTrace()
            }
            return true
        }
        return false
    }
}
