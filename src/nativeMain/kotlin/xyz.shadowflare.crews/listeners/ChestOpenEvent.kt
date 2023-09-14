package xyz.shadowflare.crews.listeners

import org.bukkit.*

class ChestOpenEvent : Listener {
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    @EventHandler
    fun onChestOpen(event: PlayerInteractEvent) {
        if (!crewsConfig.getBoolean("protections.chests.enabled")) {
            return
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            val block: Block = event.getClickedBlock()
            if (block != null) {
                if (block.getType().equals(Material.CHEST)) {
                    val chestLocation: Location = block.getLocation()
                    if (!CrewsStorageUtil.isChestLocked(chestLocation)) {
                        return
                    }
                    val tileState: TileState = block.getState() as TileState
                    val container: PersistentDataContainer = tileState.getPersistentDataContainer()
                    val owningCrewName: String =
                        container.get(NamespacedKey(Crews.getPlugin(), "owningCrewName"), PersistentDataType.STRING)
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
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val CREW_PLACEHOLDER = "%CREW%"
    }
}
