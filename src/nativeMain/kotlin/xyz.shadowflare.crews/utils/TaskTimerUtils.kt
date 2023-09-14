package xyz.shadowflare.crews.utils

import com.tcoded.folialib.FoliaLib

object TaskTimerUtils {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var config: FileConfiguration = Crews.getPlugin().getConfig()
    var foliaLib: FoliaLib = FoliaLib(Crews.getPlugin())
    var autoSaveTask: WrappedTask? = null
    var inviteClearTask: WrappedTask? = null
    fun runClansAutoSave() {
        autoSaveTask = foliaLib.getImpl().runTimerAsync({
            try {
                ClansStorageUtil.saveClans()
                if (Crews.getPlugin().getConfig().getBoolean("general.show-auto-save-task-message.enabled")) {
                    console.sendMessage(
                        xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes(
                            Crews.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-save-complete")
                        )
                    )
                }
                if (config.getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aWrapped task: " + autoSaveTask.toString()))
                    console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aTimed task canceled successfully"))
                }
            } catch (e: IOException) {
                console.sendMessage(
                    xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes(
                        Crews.getPlugin().messagesFileManager.getMessagesConfig().getString("auto-save-failed")
                    )
                )
                e.printStackTrace()
                if (config.getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aWrapped task: " + autoSaveTask.toString()))
                    console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aTimed task canceled successfully"))
                }
            }
            if (config.getBoolean("general.developer-debug-mode.enabled")) {
                console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aWrapped task: " + autoSaveTask.toString()))
                console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aTimed task canceled successfully"))
            }
        }, 1L, 900L, TimeUnit.SECONDS)
    }

    fun runClanInviteClear() {
        inviteClearTask = foliaLib.getImpl().runTimerAsync({
            try {
                xyz.shadowflare.crews.utils.CrewsInviteUtil.emptyInviteList()
                if (Crews.getPlugin().getConfig().getBoolean("general.show-auto-invite-wipe-message.enabled")) {
                    console.sendMessage(
                        xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes(
                            Crews.getPlugin().messagesFileManager.getMessagesConfig()
                                .getString("auto-invite-wipe-complete")
                        )
                    )
                }
                if (config.getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aWrapped task: " + inviteClearTask.toString()))
                    console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aTimed task canceled successfully"))
                }
            } catch (exception: UnsupportedOperationException) {
                console.sendMessage(
                    xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes(
                        Crews.getPlugin().messagesFileManager.getMessagesConfig().getString("invite-wipe-failed")
                    )
                )
                if (config.getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aWrapped task: " + inviteClearTask.toString()))
                    console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aTimed task canceled successfully"))
                }
            }
            runClanInviteClear()
            if (config.getBoolean("general.developer-debug-mode.enabled")) {
                console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aWrapped task: " + inviteClearTask.toString()))
                console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews-Debug: &aTimed task canceled successfully"))
            }
        }, 1L, 900L, TimeUnit.SECONDS)
    }
}
