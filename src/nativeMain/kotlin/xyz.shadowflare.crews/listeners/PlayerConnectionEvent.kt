package xyz.shadowflare.crews.listeners

import org.bukkit.Bukkit

class PlayerConnectionEvent : Listener {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player: Player = event.getPlayer()
        Crews.connectedPlayers.put(player, player.getName())
        if (!UsermapStorageUtil.isUserExisting(player)) {
            UsermapStorageUtil.addToUsermap(player)
            return
        }
        if (UsermapStorageUtil.hasPlayerNameChanged(player)) {
            UsermapStorageUtil.updatePlayerName(player)
            if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aUpdated player name"))
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    fun onBedrockPlayerJoin(event: PlayerJoinEvent) {
        val player: Player = event.getPlayer()
        val uuid: UUID = player.getUniqueId()
        if (Crews.getFloodgateApi() != null) {
            if (Crews.getFloodgateApi().isFloodgatePlayer(uuid)) {
                if (!UsermapStorageUtil.isUserExisting(player)) {
                    UsermapStorageUtil.addBedrockPlayerToUsermap(player)
                    return
                }
                if (UsermapStorageUtil.hasPlayerNameChanged(player)) {
                    UsermapStorageUtil.updatePlayerName(player)
                    if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                        console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aUpdated bedrock player name"))
                    }
                }
                if (UsermapStorageUtil.hasBedrockPlayerJavaUUIDChanged(player)) {
                    UsermapStorageUtil.updateBedrockPlayerJavaUUID(player)
                    if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                        console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aUpdated bedrock player Java UUID"))
                    }
                }
                Crews.bedrockPlayers.put(player, Crews.getFloodgateApi().getPlayer(uuid).getJavaUniqueId().toString())
                if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aAdded bedrock player to connected bedrock players hashmap"))
                }
            }
        }
    }
}
