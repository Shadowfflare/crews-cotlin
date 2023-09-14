package xyz.shadowflare.crews.utils

import com.tcoded.folialib.FoliaLib

class TeleportUtils {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    private val foliaLib: FoliaLib = FoliaLib(Crews.getPlugin())
    var wrappedTask: WrappedTask? = null
    var config: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun teleportAsync(player: Player, clan: Clan, location: Location) {
        val originLocation: Location = player.getLocation()
        PaperLib.teleportAsync(player, location)
        fireClanHomeTeleportEvent(false, player, clan, originLocation, location)
        if (config.getBoolean("general.developer-debug-mode.enabled")) {
            console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CresHomeTeleportEvent in sync mode"))
        }
        player.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes(messagesConfig.getString("non-timed-teleporting-complete")))
    }

    fun teleportAsyncTimed(player: Player, clan: Clan?, location: Location) {
        val originLocation: Location = player.getLocation()
        player.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes(messagesConfig.getString("timed-teleporting-begin-tp")))
        wrappedTask = foliaLib.getImpl().runTimerAsync(object : Runnable() {
            var time: Int = config.getInt("crew-home.delay-before-teleport.time")
            @Override
            fun run() {
                if (!Crews.getPlugin().teleportQueue.containsKey(player.getUniqueId())) {
                    Crews.getPlugin().teleportQueue.put(player.getUniqueId(), getWrappedTask())
                    if (config.getBoolean("general.developer-debug-mode.enabled")) {
                        console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aPlayer " + player.getName() + " has been added to teleport queue"))
                    }
                }
                if (time == 0) {
                    Crews.getPlugin().teleportQueue.remove(player.getUniqueId())
                    if (config.getBoolean("general.developer-debug-mode.enabled")) {
                        console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aPlayer " + player.getName() + " has been removed from the teleport queue"))
                    }
                    PaperLib.teleportAsync(player, location)
                    player.sendMessage(
                        xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes(
                            messagesConfig.getString(
                                "timed-teleporting-complete"
                            )
                        )
                    )
                    if (foliaLib.isFolia()) {
                        fireClanHomeTeleportEvent(true, player, crew, originLocation, location)
                        if (config.getBoolean("general.developer-debug-mode.enabled")) {
                            console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aDetected running on Folia"))
                            console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aFired HomeTeleportEvent in async mode"))
                        }
                    }
                    getWrappedTask().cancel()
                    if (config.getBoolean("general.developer-debug-mode.enabled")) {
                        console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aWrapped task: " + getWrappedTask().toString()))
                        console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &ateleportPlayerAsyncTimed task canceled"))
                    }
                    return
                } else {
                    time--
                    if (config.getBoolean("general.developer-debug-mode.enabled")) {
                        console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &ateleportPlayerAsyncTimed task running"))
                        console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aWrapped task: " + getWrappedTask().toString()))
                    }
                }
            }
        }, 0, 1L, TimeUnit.SECONDS)
    }

    fun getWrappedTask(): WrappedTask? {
        return wrappedTask
    }

    private fun fireClanHomeTeleportEvent(
        isAsync: Boolean,
        player: Player,
        clan: Clan,
        originLocation: Location,
        homeLocation: Location
    ) {
        val homeTeleportEvent = CrewHomeTeleportEvent(isAsync, player, crew, originLocation, homeLocation)
        Bukkit.getPluginManager().callEvent(homeTeleportEvent)
    }
}
