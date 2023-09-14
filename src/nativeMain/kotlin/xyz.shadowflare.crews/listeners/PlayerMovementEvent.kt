package xyz.shadowflare.crews.listeners

import com.tcoded.folialib.wrapper.task.WrappedTask

class PlayerMovementEvent : Listener {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var config: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player: Player = event.getPlayer()
        val uuid: UUID = player.getUniqueId()
        if (!config.getBoolean("crew-home.delay-before-teleport.cancel-teleport-on-move")) {
            return
        }
        if (event.getFrom().getX() !== event.getTo().getX()
            || event.getFrom().getY() !== event.getTo().getY()
            || event.getFrom().getZ() !== event.getTo().getZ()
        ) {
            if (Crews.getPlugin().teleportQueue.containsKey(uuid)) {
                if (config.getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aPlayer " + player.getName() + " has a pending teleport"))
                }
                try {
                    val wrappedTask: WrappedTask = Crews.getPlugin().teleportQueue.get(uuid)
                    if (config.getBoolean("general.developer-debug-mode.enabled")) {
                        console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aWrapped task: " + wrappedTask.toString()))
                    }
                    wrappedTask.cancel()
                    if (config.getBoolean("general.developer-debug-mode.enabled")) {
                        console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aWrapped task canceled"))
                    }
                    Crews.getPlugin().teleportQueue.remove(uuid)
                    if (config.getBoolean("general.developer-debug-mode.enabled")) {
                        console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aPlayer " + player.getName() + " has had teleport canceled and removed from queue"))
                    }
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("timed-teleport-failed-player-moved")))
                } catch (e: Exception) {
                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("move-event-cancel-failed")))
                    e.printStackTrace()
                }
            }
        }
    }
}
