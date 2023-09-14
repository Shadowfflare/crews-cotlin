package xyz.shadowflare.crews.listeners

import org.bukkit.Bukkit

class PlayerDisconnectEvent : Listener {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player: Player = event.getPlayer()
        Crews.connectedPlayers.remove(player)
        if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aPlayer removed from connected players list"))
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    fun onBedrockPlayerQuit(event: PlayerQuitEvent) {
        val player: Player = event.getPlayer()
        if (Crews.getFloodgateApi() != null) {
            if (Crews.bedrockPlayers.containsKey(player)) {
                Crews.bedrockPlayers.remove(player)
                if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aBedrock player removed from bedrock players list"))
                }
            }
        }
    }
}
