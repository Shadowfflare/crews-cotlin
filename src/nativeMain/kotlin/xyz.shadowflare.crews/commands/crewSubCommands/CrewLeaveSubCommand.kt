package xyz.gamlin.crews.commands.crewSubCommands

import org.bukkit.command.CommandSender

class CrewLeaveSubCommand {
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun clanLeaveSubCommand(sender: CommandSender): Boolean {
        if (sender is Player) {
            if (CrewsStorageUtil.findCrewByOwner(sender) != null) {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-crew-owner")))
                return true
            }
            val targetCrew: Crew = CrewsStorageUtil.findCrewByPlayer(sender)
            if (targetCrew != null) {
                if (CrewsStorageUtil.removeClanMember(targetCrew, sender)) {
                    val leaveMessage: String =
                        ColorUtils.translateColorCodes(messagesConfig.getString("crew-leave-successful")).replace(
                            CREW_PLACEHOLDER, targetCrew.crewFinalName
                        )
                    sender.sendMessage(leaveMessage)
                } else {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-leave-failed")))
                }
            }
            return true
        }
        return true
    }

    companion object {
        private const val CREW_PLACEHOLDER = "%CREW%"
    }
}
