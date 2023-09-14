package xyz.shadowflare.crews.listeners

import com.tcoded.folialib.wrapper.task.WrappedTask
import xyz.shadowflare.crews.menuSystem.Menu

class MenuEvent : Listener {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var guiConfig: FileConfiguration = Crews.getPlugin().crewsGUIFileManager.getCrewGUIConfig()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    @EventHandler
    fun onMenuClick(event: InventoryClickEvent) {
        val holder: InventoryHolder = event.getInventory().getHolder()
        if (holder is Menu) {
            event.setCancelled(true)
            if (event.getCurrentItem() == null) {
                return
            }
            holder.handleMenu(event)
        }
    }

    @EventHandler
    fun onMenuClose(event: InventoryCloseEvent) {
        val holder: InventoryHolder = event.getInventory().getHolder()
        if (holder is Menu) {
            if ((holder as Menu).menuName.equalsIgnoreCase(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.name")))) {
                val wrappedTask: WrappedTask = CrewListGUI.task5
                if (!wrappedTask.isCancelled()) {
                    wrappedTask.cancel()
                    if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                        console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aAuto refresh task cancelled"))
                    }
                }
            }
        }
    }
}
