package xyz.shadowflare.crews.commands.crewSubCommands

import org.bukkit.Bukkit

class CrewDelHomeSubCommand {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun deleteClanHomeSubCommand(sender: CommandSender): Boolean {
        if (sender is Player) {
            if (crewsConfig.getBoolean("crew-home.enabled")) {
                if (CrewsStorageUtil.findCrewByOwner(sender) != null) {
                    val crewByOwner: Crew = CrewsStorageUtil.findCrewByOwner(sender)
                    if (CrewsStorageUtil.isHomeSet(crewByOwner)) {
                        fireCrewHomeDeleteEvent(sender, crewByOwner)
                        if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewHomeDeleteEvent"))
                        }
                        CrewsStorageUtil.deleteHome(crewByOwner)
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-deleted-crew-home")))
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-no-home-set")))
                    }
                } else {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-must-be-owner")))
                }
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")))
            }
            return true
        }
        return false
    }

    companion object {
        private fun fireCrewHomeDeleteEvent(player: Player, crew: Crew) {
            val crewHomeDeleteEvent = CrewHomeDeleteEvent(player, crew)
            Bukkit.getPluginManager().callEvent(crewHomeDeleteEvent)
        }
    }
}
