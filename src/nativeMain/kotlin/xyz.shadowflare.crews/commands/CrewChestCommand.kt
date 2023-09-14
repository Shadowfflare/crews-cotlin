package xyz.shadowflare.crews.commands

import org.bukkit.Bukkit

class CrewChestCommand : CommandExecutor {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    @Override
    fun onCommand(sender: CommandSender, command: Command?, label: String?, args: Array<String?>): Boolean {
        if (sender is Player) {
            if (!crewsConfig.getBoolean("protections.chests.enabled")) {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")))
                return true
            }
            if (args.size < 1) {
                sendUsage(sender)
                return true
            } else {
                when (args[0]) {
                    "lock" -> return ChestLockSubCommand().crewChestLockSubCommand(sender)
                    "unlock" -> return ChestUnlockSubCommand().chestUnlockSubCommand(sender)
                    "buylock" -> return ChestBuySubCommand().chestBuySubCommand(sender, args)
                    "accesslist" -> return ChestAccessListSubCommand().chestAccessListSubCommand(sender)
                    else -> sendUsage(sender)
                }
            }
        }

//----------------------------------------------------------------------------------------------------------------------
        if (sender is ConsoleCommandSender) {
            if (!crewsConfig.getBoolean("protections.chests.enabled")) {
                console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")))
                return true
            }
            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-only-command")))
        }
        return true
    }

    private fun sendUsage(player: Player) {
        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-command-incorrect-usage.line-1")))
        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-command-incorrect-usage.line-2")))
        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-command-incorrect-usage.line-3")))
        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-command-incorrect-usage.line-4")))
        player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-command-incorrect-usage.line-5")))
    }
}
