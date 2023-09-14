package xyz.shadowflare.crews.commands.crewChestLockSubCommands

import org.bukkit.Bukkit

class ChestLockSubCommand {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun crewChestLockSubCommand(sender: CommandSender): Boolean {
        if (sender is Player) {
            val block: Block = sender.getTargetBlockExact(5)
            val crewByOwner: Crew = CrewsStorageUtil.findCrewByOwner(sender)
            val crewByPlayer: Crew = CrewsStorageUtil.findCrewByPlayer(sender)
            if (crewByOwner != null) {
                if (block != null) {
                    if (block.getType().equals(Material.CHEST)) {
                        val maxAllowedChests: Int = crewByOwner.getMaxAllowedProtectedChests()
                        if (CrewsStorageUtil.getAllProtectedChestsByCrew(crewByOwner).size() >= maxAllowedChests) {
                            sender.sendMessage(
                                ColorUtils.translateColorCodes(
                                    messagesConfig.getString("chest-max-amount-reached")
                                        .replace(LIMIT_PLACEHOLDER, String.valueOf(maxAllowedChests))
                                )
                            )
                            return true
                        }
                        lockTargetedChest(crewByOwner, block, sender)
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("block-targeted-incorrect-material")))
                        return true
                    }
                } else {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("block-targeted-incorrect-material")))
                    return true
                }
            } else {
                if (crewByPlayer != null) {
                    if (block != null) {
                        if (block.getType().equals(Material.CHEST)) {
                            val maxAllowedChests: Int = crewByPlayer.getMaxAllowedProtectedChests()
                            if (CrewsStorageUtil.getAllProtectedChestsByCrew(crewByPlayer).size() >= maxAllowedChests) {
                                sender.sendMessage(
                                    ColorUtils.translateColorCodes(
                                        messagesConfig.getString("chest-max-amount-reached")
                                            .replace(LIMIT_PLACEHOLDER, String.valueOf(maxAllowedChests))
                                    )
                                )
                                return true
                            }
                            lockTargetedChest(crewByPlayer, block, sender)
                        } else {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("block-targeted-incorrect-material")))
                        }
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("block-targeted-incorrect-material")))
                    }
                    return true
                }
            }
            if (crewByOwner == null || crewByPlayer == null) {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-not-in-crew")))
                return true
            }
        }
        return true
    }

    private fun lockTargetedChest(crew: Crew?, block: Block?, player: Player) {
        val location: Location = block.getLocation()
        val x = Math.round(location.getX()) as Int
        val y = Math.round(location.getY()) as Int
        val z = Math.round(location.getZ()) as Int
        if (CrewsStorageUtil.addProtectedChest(crew, location, player)) {
            player.sendMessage(
                ColorUtils.translateColorCodes(
                    messagesConfig.getString("chest-protected-successfully")
                        .replace(X_PLACEHOLDER, String.valueOf(x))
                        .replace(Y_PLACEHOLDER, String.valueOf(y))
                        .replace(Z_PLACEHOLDER, String.valueOf(z))
                )
            )
            val tileState: TileState = block.getState() as TileState
            val container: PersistentDataContainer = tileState.getPersistentDataContainer()
            container.set(
                NamespacedKey(Crews.getPlugin(), "owningCrewName"),
                PersistentDataType.STRING,
                crew.getCrewFinalName()
            )
            container.set(
                NamespacedKey(Crews.getPlugin(), "owningCrewOwnerUUID"),
                PersistentDataType.STRING,
                crew.getCrewOwner()
            )
            tileState.update()
            fireChestLockEvent(player, crew, CrewsStorageUtil.getChestByLocation(location))
            if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired ChestLockEvent"))
            }
        }
    }

    companion object {
        private const val LIMIT_PLACEHOLDER = "%LIMIT%"
        private const val X_PLACEHOLDER = "%X%"
        private const val Y_PLACEHOLDER = "%Y%"
        private const val Z_PLACEHOLDER = "%Z%"
        private fun fireChestLockEvent(player: Player, crew: Crew?, chest: Chest) {
            val chestLockEvent = ChestLockEvent(player, crew, chest)
            Bukkit.getPluginManager().callEvent(chestLockEvent)
        }
    }
}
