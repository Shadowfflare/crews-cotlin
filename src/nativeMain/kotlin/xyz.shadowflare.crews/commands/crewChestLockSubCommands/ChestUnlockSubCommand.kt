package xyz.shadowflare.crews.commands.crewChestLockSubCommands

import org.bukkit.*

class ChestUnlockSubCommand {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun chestUnlockSubCommand(sender: CommandSender): Boolean {
        if (sender is Player) {
            val offlinePlayer: OfflinePlayer = Bukkit.getOfflinePlayer(sender.getUniqueId())
            val block: Block = sender.getTargetBlockExact(5)
            if (block != null) {
                if (block.getType().equals(Material.CHEST)) {
                    val location: Location = block.getLocation()
                    val x = Math.round(location.getX()) as Int
                    val y = Math.round(location.getY()) as Int
                    val z = Math.round(location.getZ()) as Int
                    if (CrewsStorageUtil.isChestLocked(location)) {
                        val chest: Chest = CrewsStorageUtil.getChestByLocation(location)
                        if (CrewsStorageUtil.hasAccessToLockedChest(offlinePlayer, chest)) {
                            val tileState: TileState = block.getState() as TileState
                            val container: PersistentDataContainer = tileState.getPersistentDataContainer()
                            val crewOwnerUUIDString: String = container.get(
                                NamespacedKey(Crews.getPlugin(), "owningCrewOwnerUUID"),
                                PersistentDataType.STRING
                            )
                            if (crewOwnerUUIDString != null) {
                                try {
                                    if (CrewsStorageUtil.removeProtectedChest(crewOwnerUUIDString, location, sender)) {
                                        fireChestUnlockEvent(sender, location)
                                        if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired ChestUnlockEvent"))
                                        }
                                        sender.sendMessage(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("chest-protection-removed-successfully")
                                                    .replace(X_PLACEHOLDER, String.valueOf(x))
                                                    .replace(Y_PLACEHOLDER, String.valueOf(y))
                                                    .replace(Z_PLACEHOLDER, String.valueOf(z))
                                            )
                                        )
                                        container.remove(NamespacedKey(Crews.getPlugin(), "owningCrewName"))
                                        container.remove(NamespacedKey(Crews.getPlugin(), "owningCrewOwnerUUID"))
                                        tileState.update()
                                        return true
                                    }
                                } catch (e: IOException) {
                                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-1")))
                                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-2")))
                                    e.printStackTrace()
                                }
                                return true
                            }
                        } else {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-owned-by-another-crew-name-unknown")))
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
        private const val X_PLACEHOLDER = "%X%"
        private const val Y_PLACEHOLDER = "%Y%"
        private const val Z_PLACEHOLDER = "%Z%"
        private fun fireChestUnlockEvent(player: Player, removedLockLocation: Location) {
            val chestUnlockEvent = ChestUnlockEvent(player, removedLockLocation)
            Bukkit.getPluginManager().callEvent(chestUnlockEvent)
        }
    }
}
