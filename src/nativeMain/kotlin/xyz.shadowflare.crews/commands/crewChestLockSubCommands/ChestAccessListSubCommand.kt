package xyz.shadowflare.crews.commands.crewChestLockSubCommands

import org.bukkit.Bukkit

class ChestAccessListSubCommand {
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun chestAccessListSubCommand(sender: CommandSender): Boolean {
        if (sender is Player) {
            val offlinePlayer: OfflinePlayer = Bukkit.getOfflinePlayer(sender.getUniqueId())
            val block: Block = sender.getTargetBlockExact(5)
            if (block != null) {
                if (block.getType().equals(Material.CHEST)) {
                    val location: Location = block.getLocation()
                    if (CrewsStorageUtil.isChestLocked(location)) {
                        val chest: Chest = CrewsStorageUtil.getChestByLocation(location)
                        if (chest != null) {
                            if (CrewsStorageUtil.hasAccessToLockedChest(offlinePlayer, chest)) {
                                val offlinePlayersWithAccess: List<OfflinePlayer> =
                                    CrewsStorageUtil.getOfflinePlayersWithChestAccessByChest(chest)
                                val stringBuilder = StringBuilder()
                                stringBuilder.append(messagesConfig.getString("players-with-access-list.header"))
                                for (offlinePlayerWithAccess in offlinePlayersWithAccess) {
                                    val crewPlayer: CrewPlayer =
                                        UsermapStorageUtil.getCrewPlayerByBukkitOfflinePlayer(offlinePlayerWithAccess)
                                    if (crewPlayer != null) {
                                        val playerName: String = crewPlayer.getLastPlayerName()
                                        stringBuilder.append(
                                            messagesConfig.getString("players-with-access-list.player-entry")
                                                .replace(PLAYER_PLACEHOLDER, playerName)
                                        )
                                    }
                                }
                                stringBuilder.append(messagesConfig.getString("players-with-access-list.footer"))
                                sender.sendMessage(ColorUtils.translateColorCodes(stringBuilder.toString()))
                                return true
                            } else {
                                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-owned-by-another-crew-name-unknown")))
                            }
                            return true
                        }
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-chest-not-protected")))
                        return true
                    }
                } else {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("block-targeted-incorrect-material")))
                    return true
                }
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("block-targeted-incorrect-material")))
                return true
            }
        }
        return true
    }

    companion object {
        private const val PLAYER_PLACEHOLDER = "%PLAYER%"
    }
}
