package xyz.shadowflare.crews.menuSystem.menu

import org.bukkit.Material
import xyz.shadowflare.crews.menuSystem.Menu

class CrewJoinRequestMenu(playerMenuUtility: PlayerMenuUtility?) : Menu(playerMenuUtility) {
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    var guiConfig: FileConfiguration = Crews.getPlugin().crewGUIFileManager.getCrewGUIConfig()

    @get:Override
    override val menuName: String
        get() = ColorUtils.translateColorCodes(guiConfig.getString("crew-join.name"))

    @get:Override
    override val slots: Int
        get() = 9

    @Override
    override fun handleMenu(event: InventoryClickEvent) {
        val player: Player = event.getWhoClicked() as Player
        if (event.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
            val targetCrewOwner: Player = playerMenuUtility.getOfflineCrewOwner().getPlayer()
            if (targetCrewOwner != null) {
                targetCrewOwner.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesConfig.getString("crew-invite-request")
                            .replace("%PLAYER%", player.getName())
                    )
                )
                player.closeInventory()
                player.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesConfig.getString("crew-invite-sent-successfully")
                            .replace("%CREWOWNER%", targetCrewOwner.getName())
                    )
                )
            } else {
                player.closeInventory()
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-request-failed")))
            }
        } else if (event.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE)) {
            CrewListGUI(Crews.getPlayerMenuUtility(player)).open()
        }
    }

    @Override
    override fun setMenuItems() {
        val sendJoinRequestItem = ItemStack(Material.LIME_STAINED_GLASS_PANE, 1)
        val sendMeta: ItemMeta = sendJoinRequestItem.getItemMeta()
        sendMeta.setDisplayName(ColorUtils.translateColorCodes(guiConfig.getString("crew-join.icons.send-request-name")))
        sendJoinRequestItem.setItemMeta(sendMeta)
        val cancelJoinRequestItem = ItemStack(Material.RED_STAINED_GLASS_PANE, 1)
        val cancelMeta: ItemMeta = sendJoinRequestItem.getItemMeta()
        cancelMeta.setDisplayName(ColorUtils.translateColorCodes(guiConfig.getString("crew-join.icons.cancel-request-name")))
        cancelJoinRequestItem.setItemMeta(cancelMeta)
        inventory.setItem(0, sendJoinRequestItem)
        inventory.setItem(8, cancelJoinRequestItem)
    }
}
