package xyz.shadowflare.crews.commands.crewSubCommands

import org.bukkit.Bukkit

class CrewPlayerPointsSubCommand {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun clanPlayerPointsSubCommand(sender: CommandSender, args: Array<String>): Boolean {
        if (sender is Player) {
            if (!crewsConfig.getBoolean("points.player-points.enabled")) {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")))
                return true
            }
            if (args.size == 1) {
                if (args[0].equalsIgnoreCase("listall")) {
                    if (sender.hasPermission("crews.points.listall") || sender.hasPermission("crews.admin")
                        || sender.hasPermission("crews.*") || sender.isOp()
                    ) {
                        return listAllPoints(sender)
                    }
                }
            } else {
                val crewPlayer: CrewPlayer = UsermapStorageUtil.getCrewPlayerByBukkitPlayer(sender)
                if (crewPlayer != null) {
                    val playerPointValue: Int = crewPlayer.pointBalance
                    sender.sendMessage(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString("player-points-list-command")
                                .replace(POINT_PLACEHOLDER, String.valueOf(playerPointValue))
                        )
                    )
                }
            }
        } else if (sender is ConsoleCommandSender) {
            if (args.size == 1) {
                if (args[0].equalsIgnoreCase("listall")) {
                    return listAllPoints(sender)
                }
            } else {
                console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-only-command")))
            }
        }
        return true
    }

    private fun listAllPoints(sender: CommandSender): Boolean {
        val allUsersMap: HashMap<UUID, CrewPlayer> = HashMap(UsermapStorageUtil.getUsermap())
        val stringBuilder = StringBuilder()
        stringBuilder.append(ColorUtils.translateColorCodes(messagesConfig.getString("all-points-list-header")))
        for (entry in allUsersMap.entrySet()) {
            val crewPlayerName: String = entry.getValue().getLastPlayerName()
            val crewPlayerPointBalance: String = String.valueOf(entry.getValue().getPointBalance())
            stringBuilder.append(
                ColorUtils.translateColorCodes(
                    messagesConfig.getString("all-points-list-entry")
                        .replace("%PLAYER%", crewPlayerName).replace(POINT_PLACEHOLDER, crewPlayerPointBalance)
                )
            )
        }
        stringBuilder.append(ColorUtils.translateColorCodes(messagesConfig.getString("all-points-list-footer")))
        sender.sendMessage(ColorUtils.translateColorCodes(stringBuilder.toString()))
        return true
    }

    companion object {
        private const val POINT_PLACEHOLDER = "%POINTVALUE%"
    }
}
