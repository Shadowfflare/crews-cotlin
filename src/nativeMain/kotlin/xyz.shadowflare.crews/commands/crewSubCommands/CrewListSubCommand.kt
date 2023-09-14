package xyz.shadowflare.crews.commands.crewSubCommands

import org.bukkit.command.CommandSender

class CrewListSubCommand {
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun clanListSubCommand(sender: CommandSender): Boolean {
        if (sender is Player) {
            val crews: Set<Map.Entry<UUID, Crew>> = CrewsStorageUtil.getCrews()
            val crewsString = StringBuilder()
            if (crews.size() === 0) {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("no-crews-to-list")))
            } else {
                crewsString.append(ColorUtils.translateColorCodes(messagesConfig.getString("crews-list-header") + "\n"))
                crews.forEach { (key, value) ->
                    crewsString.append(
                        ColorUtils.translateColorCodes(
                            clan.getValue().getClanFinalName() + "\n"
                        )
                    )
                }
                crewsString.append(" ")
                crewsString.append(ColorUtils.translateColorCodes(messagesConfig.getString("crews-list-footer")))
                sender.sendMessage(crewsString.toString())
            }
            return true
        }
        return false
    }
}
