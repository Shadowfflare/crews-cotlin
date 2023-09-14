package xyz.shadowflare.crews.menuSystem

import org.bukkit.Material

abstract class PaginatedMenu(playerMenuUtility: PlayerMenuUtility?) : Menu(playerMenuUtility) {
    var guiConfig: FileConfiguration = Crews.getPlugin().crewGUIFileManager.getCrewGUIConfig()
    protected var page = 0
    var maxItemsPerPage = 45
        protected set
    protected var index = 0
    fun addMenuControls() {
        inventory.setItem(
            48,
            makeItem(
                Material.STONE_BUTTON,
                ColorUtils.translateColorCodes(guiConfig.getString("crew-list.menu-controls.previous-page-icon-name"))
            )
        )
        inventory.setItem(
            49,
            makeItem(
                Material.BARRIER,
                ColorUtils.translateColorCodes(guiConfig.getString("crew-list.menu-controls.close-go-back-icon-name"))
            )
        )
        inventory.setItem(
            50,
            makeItem(
                Material.STONE_BUTTON,
                ColorUtils.translateColorCodes(guiConfig.getString("crew-list.menu-controls.next-page-icon-name"))
            )
        )
    }
}
