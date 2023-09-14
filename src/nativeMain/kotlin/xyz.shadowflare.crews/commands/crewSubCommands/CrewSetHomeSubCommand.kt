package xyz.shadowflare.crews.commands.crewSubCommands

import org.bukkit.Bukkit

class CrewSetHomeSubCommand {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun setClanHomeSubCommand(sender: CommandSender): Boolean {
        if (sender is Player) {
            if (clansConfig.getBoolean("clan-home.enabled")) {
                if (CrewsStorageUtil.isCrewOwner(sender)) {
                    if (CrewsStorageUtil.findCrewByOwner(sender) != null) {
                        val crew: Crew = CrewsStorageUtil.findCrewByOwner(sender)
                        val location: Location = sender.getLocation()
                        fireClanHomeSetEvent(sender, crew, location)
                        if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewHomeSetEvent"))
                        }
                        crew.setCrewHomeWorld(sender.getLocation().getWorld().getName())
                        crew.setCrewHomeX(sender.getLocation().getX())
                        crew.setCrewHomeY(sender.getLocation().getY())
                        crew.setCrewHomeZ(sender.getLocation().getZ())
                        crew.setCrewHomeYaw(sender.getLocation().getYaw())
                        crew.setCrewHomePitch(sender.getLocation().getPitch())
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("successfully-set-crew-home")))
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
        private fun fireClanHomeSetEvent(player: Player, crew: Crew, homeLocation: Location) {
            val crewHomeCreateEvent = CrewHomeCreateEvent(player, crew, homeLocation)
            Bukkit.getPluginManager().callEvent(crewHomeCreateEvent)
        }
    }
}
