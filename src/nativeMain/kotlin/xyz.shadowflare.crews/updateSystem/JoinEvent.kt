package xyz.shadowflare.crews.updateSystem

import org.bukkit.configuration.file.FileConfiguration

class JoinEvent : Listener {
    var clansConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    var notifiedPlayerUUID: List<UUID> = ArrayList()
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player: Player = event.getPlayer()
        if (player.hasPermission("crews.update") || player.hasPermission("crewslite.*") || player.isOp()) {
            if (crewsConfig.getBoolean("plugin-update-notifications.enabled")) {
                if (!notifiedPlayerUUID.contains(player.getUniqueId())) {
                    UpdateChecker(97163).getVersion(error.NonExistentClass { version ->
                        try {
                            if (!Crews.getPlugin().getDescription().getVersion().equalsIgnoreCase(version)) {
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("update-available.1")))
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("update-available.2")))
                                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("update-available.3")))
                                notifiedPlayerUUID.add(player.getUniqueId())
                            }
                        } catch (e: NullPointerException) {
                            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("Update-check-failure")))
                        }
                    })
                }
            }
        }
    }
}
