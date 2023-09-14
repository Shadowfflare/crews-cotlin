package xyz.shadowflare.crews.commands

import org.bukkit.command.Command

class CrewChatSpyCommand : CommandExecutor {
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    @Override
    fun onCommand(sender: CommandSender, cmd: Command?, label: String?, args: Array<String?>?): Boolean {
        if (sender is Player) {
            if (crewsConfig.getBoolean("crew-chat.chat-spy.enabled")) {
                if (sender.hasPermission("crews.chat.spy") || sender.hasPermission("crews.*")
                    || sender.hasPermission("crews.admin") || sender.isOp()
                ) {
                    if (UsermapStorageUtil.toggleChatSpy(sender)) {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chatspy-toggle-on")))
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chatspy-toggle-off")))
                    }
                } else {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("no-permission")))
                }
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")))
            }
            return true
        }
        return true
    }
}
