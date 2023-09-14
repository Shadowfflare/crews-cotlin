package xyz.shadowflare.crews.menuSystem

import org.bukkit.Bukkit

abstract class Menu(protected var playerMenuUtility: PlayerMenuUtility) : InventoryHolder {
    protected var inventory: Inventory? = null
    abstract val menuName: String?
    abstract val slots: Int
    abstract fun handleMenu(e: InventoryClickEvent?)
    abstract fun setMenuItems()
    fun open() {
        inventory = Bukkit.createInventory(this, slots, menuName)
        setMenuItems()
        playerMenuUtility.getOwner().openInventory(inventory)
    }

    @Override
    fun getInventory(): Inventory? {
        return inventory
    }

    fun makeItem(material: Material?, displayName: String?, vararg lore: String?): ItemStack {
        val item = ItemStack(material)
        val itemMeta: ItemMeta = item.getItemMeta()
        itemMeta.setDisplayName(displayName)
        itemMeta.setLore(Arrays.asList(lore))
        item.setItemMeta(itemMeta)
        return item
    }
}
