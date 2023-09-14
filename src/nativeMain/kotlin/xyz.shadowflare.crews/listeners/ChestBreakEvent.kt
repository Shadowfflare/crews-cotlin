package xyz.shadowflare.crews.listeners

import org.bukkit.*

class ChestBreakEvent : Listener {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    @EventHandler
    fun onChestBreak(event: BlockBreakEvent) {
        if (!crewsConfig.getBoolean("protections.chests.enabled")) {
            return
        }
        if (event.getBlock().getType().equals(Material.CHEST)) {
            val chestLocation: Location = event.getBlock().getLocation()
            val x: Double = Math.round(chestLocation.getX())
            val y: Double = Math.round(chestLocation.getY())
            val z: Double = Math.round(chestLocation.getZ())
            if (!CrewsStorageUtil.isChestLocked(chestLocation)) {
                return
            }
            val tileState: TileState = event.getBlock().getState() as TileState
            val container: PersistentDataContainer = tileState.getPersistentDataContainer()
            val owningCrewName: String =
                container.get(NamespacedKey(Crews.getPlugin(), "owningCrewName"), PersistentDataType.STRING)
            val owningCrewOwnerUUID: String =
                container.get(NamespacedKey(Crews.getPlugin(), "owningCrewOwnerUUID"), PersistentDataType.STRING)
            val player: Player = event.getPlayer()
            val offlinePlayer: OfflinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId())
            val chest: Chest = CrewsStorageUtil.getChestByLocation(chestLocation)
            if (chest != null) {
                if (!CrewsStorageUtil.hasAccessToLockedChest(offlinePlayer, chest)) {
                    if (!(player.hasPermission("crews.bypass.chests") || player.hasPermission("crews.bypass.*")
                                || player.hasPermission("crews.bypass") || player.hasPermission("crews.*") || player.isOp())
                    ) {
                        event.setCancelled(true)
                        if (owningCrewName != null) {
                            player.sendMessage(
                                ColorUtils.translateColorCodes(
                                    messagesConfig.getString("chest-owned-by-another-crew")
                                        .replace(CREW_PLACEHOLDER, owningCrewName)
                                )
                            )
                        } else {
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("chest-owned-by-another-crew-name-unknown")))
                        }
                    } else {
                        try {
                            if (CrewsStorageUtil.removeProtectedChest(owningCrewOwnerUUID, chestLocation, player)) {
                                player.sendMessage(
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
                            }
                        } catch (e: IOException) {
                            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-1")))
                            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-2")))
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    fun onTNTDestruction(event: EntityExplodeEvent) {
        if (!crewsConfig.getBoolean("protections.chests.enabled")) {
            return
        }
        if (event.getEntity() is TNTPrimed) {
            for (block in ArrayList(event.blockList())) {
                if (block.getType().equals(Material.CHEST)) {
                    val chestLocation: Location = block.getLocation()
                    if (CrewsStorageUtil.isChestLocked(chestLocation)) {
                        val tileState: TileState = block.getState() as TileState
                        val container: PersistentDataContainer = tileState.getPersistentDataContainer()
                        val owningCrewOwnerUUID: String = container.get(
                            NamespacedKey(Crews.getPlugin(), "owningCrewOwnerUUID"),
                            PersistentDataType.STRING
                        )
                        if (!crewsConfig.getBoolean("protections.chests.enable-TNT-destruction")) {
                            event.blockList().remove(block)
                        } else {
                            removeLockedChest(owningCrewOwnerUUID, chestLocation, container, tileState)
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    fun onCreeperDestruction(event: EntityExplodeEvent) {
        if (!crewsConfig.getBoolean("protections.chests.enabled")) {
            return
        }
        if (event.getEntity() is Creeper) {
            for (block in ArrayList(event.blockList())) {
                if (block.getType().equals(Material.CHEST)) {
                    val chestLocation: Location = block.getLocation()
                    if (CrewsStorageUtil.isChestLocked(chestLocation)) {
                        val tileState: TileState = block.getState() as TileState
                        val container: PersistentDataContainer = tileState.getPersistentDataContainer()
                        val owningCrewOwnerUUID: String = container.get(
                            NamespacedKey(Crews.getPlugin(), "owningCrewOwnerUUID"),
                            PersistentDataType.STRING
                        )
                        if (!crewsConfig.getBoolean("protections.chests.enable-creeper-destruction")) {
                            event.blockList().remove(block)
                        } else {
                            removeLockedChest(owningCrewOwnerUUID, chestLocation, container, tileState)
                        }
                    }
                }
            }
        }
    }

    private fun removeLockedChest(
        owningCrewOwnerUUID: String,
        chestLocation: Location,
        container: PersistentDataContainer,
        tileState: TileState
    ) {
        try {
            if (CrewsStorageUtil.removeProtectedChest(owningCrewOwnerUUID, chestLocation)) {
                container.remove(NamespacedKey(Crews.getPlugin(), "owningCrewName"))
                container.remove(NamespacedKey(Crews.getPlugin(), "owningCrewOwnerUUID"))
                tileState.update()
            }
        } catch (e: IOException) {
            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-1")))
            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-2")))
            e.printStackTrace()
        }
    }

    companion object {
        private const val CREW_PLACEHOLDER = "%CREW%"
        private const val X_PLACEHOLDER = "%X%"
        private const val Y_PLACEHOLDER = "%Y%"
        private const val Z_PLACEHOLDER = "%Z%"
    }
}
