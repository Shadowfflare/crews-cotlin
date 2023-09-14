package xyz.shadowflare.crews.menuSystem.paginatedMenu

import com.tcoded.folialib.FoliaLib

class CrewListGUI(playerMenuUtility: PlayerMenuUtility?) : PaginatedMenu(playerMenuUtility) {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var guiConfig: FileConfiguration = Crews.getPlugin().crewsGUIFileManager.getCrewGUIConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()

    @get:Override
    val menuName: String
        get() = ColorUtils.translateColorCodes(guiConfig.getString("crew-list.name"))

    @get:Override
    val slots: Int
        get() = 54

    @Override
    fun handleMenu(event: InventoryClickEvent) {
        val player: Player = event.getWhoClicked() as Player
        val crews: ArrayList<Crew> = ArrayList(CrewsStorageUtil.getCrewsList())
        if (event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
            val onlineCrewOwner: Crew = CrewsStorageUtil.findClanByOwner(player)
            val onlineCrewPlayer: Crew = CrewsStorageUtil.findClanByPlayer(player)
            val target: UUID = UUID.fromString(
                event.getCurrentItem().getItemMeta().getPersistentDataContainer()
                    .get(NamespacedKey(Crews.getPlugin(), "uuid"), PersistentDataType.STRING)
            )
            if (onlineCrewOwner != null) {
                player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-failed-own-clan")))
                return
            }
            if (onlineCrewPlayer != null) {
                val ownerUUID: UUID = UUID.fromString(onlineCrewPlayer.getCrewOwner())
                if (ownerUUID.equals(target)) {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-failed-own-clan")))
                    return
                }
            }
            val playerMenuUtility: PlayerMenuUtility = Crews.getPlayerMenuUtility(player)
            playerMenuUtility.setOfflineCrewOwner(Bukkit.getOfflinePlayer(target))
            CrewJoinRequestMenu(Crews.getPlayerMenuUtility(player)).open()
            if (guiConfig.getBoolean("crew-list.icons.auto-refresh-data.enabled")) {
                task5.cancel()
                if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aAuto refresh task cancelled"))
                }
            }
        } else if (event.getCurrentItem().getType().equals(Material.BARRIER)) {
            player.closeInventory()
            if (guiConfig.getBoolean("crews-list.icons.auto-refresh-data.enabled")) {
                task5.cancel()
                if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aAuto refresh task cancelled"))
                }
            }
        } else if (event.getCurrentItem().getType().equals(Material.STONE_BUTTON)) {
            if (event.getCurrentItem().getItemMeta().getDisplayName()
                    .equalsIgnoreCase(ColorUtils.translateColorCodes(guiConfig.getString("crews-list.menu-controls.previous-page-icon-name")))
            ) {
                if (page == 0) {
                    player.sendMessage(ColorUtils.translateColorCodes(guiConfig.getString("crews-list.GUI-first-page")))
                } else {
                    page = page - 1
                    super.open()
                }
            } else if (event.getCurrentItem().getItemMeta().getDisplayName()
                    .equalsIgnoreCase(ColorUtils.translateColorCodes(guiConfig.getString("crews-list.menu-controls.next-page-icon-name")))
            ) {
                if (index + 1 < crews.size()) {
                    page = page + 1
                    super.open()
                } else {
                    player.sendMessage(ColorUtils.translateColorCodes(guiConfig.getString("crews-list.GUI-last-page")))
                }
            }
        }
    }

    @Override
    override fun setMenuItems() {
        addMenuControls()
        if (guiConfig.getBoolean("crews-list.icons.auto-refresh-data.enabled")) {
            val foliaLib = FoliaLib(Crews.getPlugin())
            task5 = foliaLib.getImpl().runTimerAsync(object : Runnable() {
                @Override
                fun run() {
                    //The thing you will be looping through to place items
                    val crews: ArrayList<Crew?> = ArrayList(CrewsStorageUtil.getClanList())

                    //Pagination loop template
                    if (crews != null && !crews.isEmpty()) {
                        for (i in 0 until maxItemsPerPage) {
                            index = maxItemsPerPage * page + i
                            if (index >= crews.size()) break
                            if (crews[index] != null) {

                                //Create an item from our collection and place it into the inventory
                                val crewOwnerUUIDString: String = crews[i].getCrewOwner()
                                val ownerUUID: UUID = UUID.fromString(crewOwnerUUIDString)
                                val crewOwnerPlayer: OfflinePlayer = Bukkit.getOfflinePlayer(ownerUUID)
                                val crew: Crew = CrewsStorageUtil.findClanByOfflineOwner(crewOwnerPlayer)
                                val playerHead = ItemStack(Material.PLAYER_HEAD, 1)
                                val skull: SkullMeta = playerHead.getItemMeta() as SkullMeta
                                skull.setOwningPlayer(getServer().getOfflinePlayer(ownerUUID))
                                playerHead.setItemMeta(skull)
                                if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aRetrieved player head sendMessage for UUID: &d$clanOwnerUUIDString"))
                                }
                                val meta: ItemMeta = playerHead.getItemMeta()
                                if (guiConfig.getBoolean("crew-list.icons.icon-display-name.use-crew-name")) {
                                    val displayName: String = ColorUtils.translateColorCodes(crew.getCrewFinalName())
                                    meta.setDisplayName(displayName)
                                } else {
                                    meta.setDisplayName(" ")
                                }
                                val lore: ArrayList<String> = ArrayList()
                                val crewMembersList: ArrayList<String> = crew.getCrewMembers()
                                val clanAlliesList: ArrayList<String> = crew.getCrewAllies()
                                val clanEnemiesList: ArrayList<String> = crew.getCrewEnemies()
                                lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.header")))
                                lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.prefix") + crew.getCrewPrefix()))
                                if (crewOwnerPlayer.isOnline()) {
                                    lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.owner-online") + crewOwnerPlayer.getName()))
                                } else {
                                    lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.owner-offline") + crewOwnerPlayer.getName()))
                                }
                                lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.members")))
                                for (string in crewMembersList) {
                                    val memberUUID: UUID = UUID.fromString(string)
                                    val member: OfflinePlayer = Bukkit.getOfflinePlayer(memberUUID)
                                    val offlineMemberName: String = member.getName()
                                    lore.add(ColorUtils.translateColorCodes(" &7- &3$offlineMemberName"))
                                    if (crewMembersList.size() >= 10) {
                                        val membersSize: Int = crewMembersList.size() - 10
                                        lore.add(ColorUtils.translateColorCodes("&3&o+ &r&6&l$membersSize&r &3&omore!"))
                                        break
                                    }
                                }
                                lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.allies")))
                                for (string in crewAlliesList) {
                                    val allyUUID: UUID = UUID.fromString(string)
                                    val ally: OfflinePlayer = Bukkit.getOfflinePlayer(allyUUID)
                                    val offlineAllyName: String = ally.getName()
                                    lore.add(ColorUtils.translateColorCodes(" &7- &3$offlineAllyName"))
                                    if (crewnAlliesList.size() >= 10) {
                                        val allySize: Int = crewAlliesList.size() - 10
                                        lore.add(ColorUtils.translateColorCodes("&3&o+ &r&6&l$allySize&r &3&omore!"))
                                        break
                                    }
                                }
                                lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.enemies")))
                                for (string in crewEnemiesList) {
                                    val enemyUUID: UUID = UUID.fromString(string)
                                    val enemy: OfflinePlayer = Bukkit.getOfflinePlayer(enemyUUID)
                                    val offlineEnemyName: String = enemy.getName()
                                    lore.add(ColorUtils.translateColorCodes(" &7- &3$offlineEnemyName"))
                                    if (crewEnemiesList.size() >= 10) {
                                        val enemySize: Int = crewEnemiesList.size() - 10
                                        lore.add(ColorUtils.translateColorCodes("&3&o+ &r&6&l$enemySize&r &3&omore!"))
                                        break
                                    }
                                }
                                lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.footer-1")))
                                lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.action")))
                                lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.footer-2")))
                                meta.setLore(lore)
                                meta.getPersistentDataContainer().set(
                                    NamespacedKey(Crews.getPlugin(), "uuid"),
                                    PersistentDataType.STRING,
                                    crew.getCrewOwner()
                                )
                                playerHead.setItemMeta(meta)
                                inventory.setItem(index, playerHead)
                                if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aAuto refresh task running"))
                                }
                            }
                        }
                    }
                }
            }, 0L, 5L, TimeUnit.SECONDS)
        } else {
            //The thing you will be looping through to place items
            val crews: ArrayList<Crew?> = ArrayList(CrewsStorageUtil.getCrewList())

            //Pagination loop template
            if (crews != null && !crews.isEmpty()) {
                for (i in 0 until maxItemsPerPage) {
                    index = maxItemsPerPage * page + i
                    if (index >= crews.size()) break
                    if (crews[index] != null) {

                        //Create an item from our collection and place it into the inventory
                        val crewOwnerUUIDString: String = crewss.get(i).getClanOwner()
                        val ownerUUID: UUID = UUID.fromString(crewOwnerUUIDString)
                        val crewOwnerPlayer: OfflinePlayer = Bukkit.getOfflinePlayer(ownerUUID)
                        val crew: Crews = CrewsStorageUtil.findClanByOfflineOwner(crewOwnerPlayer)
                        val playerHead = ItemStack(Material.PLAYER_HEAD, 1)
                        val skull: SkullMeta = playerHead.getItemMeta() as SkullMeta
                        skull.setOwningPlayer(getServer().getOfflinePlayer(ownerUUID))
                        playerHead.setItemMeta(skull)
                        if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aRetrieved player head sendMessage for UUID: &d$clanOwnerUUIDString"))
                        }
                        val meta: ItemMeta = playerHead.getItemMeta()
                        val displayName: String = ColorUtils.translateColorCodes(crew.getCrewFinalName())
                        meta.setDisplayName(displayName)
                        val lore: ArrayList<String> = ArrayList()
                        val crewMembersList: ArrayList<String> = crew.getCrewMembers()
                        val crewAlliesList: ArrayList<String> = crew.getCrewAllies()
                        val crewEnemiesList: ArrayList<String> = crew.getCrewEnemies()
                        lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.header")))
                        lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.prefix") + crew.getCrewPrefix()))
                        if (crewOwnerPlayer.isOnline()) {
                            lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.owner-online") + crewOwnerPlayer.getName()))
                        } else {
                            lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.owner-offline") + crewOwnerPlayer.getName()))
                        }
                        lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.members")))
                        for (string in crewMembersList) {
                            val memberUUID: UUID = UUID.fromString(string)
                            val member: OfflinePlayer = Bukkit.getOfflinePlayer(memberUUID)
                            val offlineMemberName: String = member.getName()
                            lore.add(ColorUtils.translateColorCodes(" &7- &3$offlineMemberName"))
                            if (crewMembersList.size() >= 10) {
                                val membersSize: Int = crewMembersList.size() - 10
                                lore.add(ColorUtils.translateColorCodes("&3&o+ &r&6&l$membersSize&r &3&omore!"))
                                break
                            }
                        }
                        lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.allies")))
                        for (string in crewAlliesList) {
                            val allyUUID: UUID = UUID.fromString(string)
                            val ally: OfflinePlayer = Bukkit.getOfflinePlayer(allyUUID)
                            val offlineAllyName: String = ally.getName()
                            lore.add(ColorUtils.translateColorCodes(" &7- &3$offlineAllyName"))
                            if (crewAlliesList.size() >= 10) {
                                val allySize: Int = crewAlliesList.size() - 10
                                lore.add(ColorUtils.translateColorCodes("&3&o+ &r&6&l$allySize&r &3&omore!"))
                                break
                            }
                        }
                        lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.enemies")))
                        for (string in crewEnemiesList) {
                            val enemyUUID: UUID = UUID.fromString(string)
                            val enemy: OfflinePlayer = Bukkit.getOfflinePlayer(enemyUUID)
                            val offlineEnemyName: String = enemy.getName()
                            lore.add(ColorUtils.translateColorCodes(" &7- &3$offlineEnemyName"))
                            if (crewEnemiesList.size() >= 10) {
                                val enemySize: Int = crewEnemiesList.size() - 10
                                lore.add(ColorUtils.translateColorCodes("&3&o+ &r&6&l$enemySize&r &3&omore!"))
                                break
                            }
                        }
                        lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.footer-1")))
                        lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.action")))
                        lore.add(ColorUtils.translateColorCodes(guiConfig.getString("crew-list.icons.lore.footer-2")))
                        meta.setLore(lore)
                        meta.getPersistentDataContainer().set(
                            NamespacedKey(Crews.getPlugin(), "uuid"),
                            PersistentDataType.STRING,
                            crew.getCrewOwner()
                        )
                        playerHead.setItemMeta(meta)
                        inventory.addItem(playerHead)
                        if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aAuto refresh task not running"))
                        }
                    }
                }
            }
        }
    }

    companion object {
        var task5: WrappedTask? = null
    }
}
