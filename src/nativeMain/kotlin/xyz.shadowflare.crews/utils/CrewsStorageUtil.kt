package xyz.gamlin.crews.utils

import org.bukkit.Bukkit

object CrewsStorageUtil {
    private val console: ConsoleCommandSender = Bukkit.getConsoleSender()
    private val STRIP_COLOR_PATTERN: Pattern = Pattern.compile("(?i)" + String.valueOf('&') + "[0-9A-FK-OR]")
    private val crewsList: Map<UUID, Crew> = HashMap()
    private val clansStorage: FileConfiguration = Crews.getPlugin().crewFileManager.getClansConfig()
    private val messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    private val clansConfig: FileConfiguration = Crews.getPlugin().getConfig()
    @Throws(IOException::class)
    fun saveClans() {
        for (entry in crewsList.entrySet()) {
            crewsStorage.set("crews.data." + entry.getKey() + ".crewOwner", entry.getValue().getClanOwner())
            crewsStorage.set("crews.data." + entry.getKey() + ".crewFinalName", entry.getValue().getClanFinalName())
            crewsStorage.set("crews.data." + entry.getKey() + ".crewPrefix", entry.getValue().getClanPrefix())
            crewsStorage.set("crews.data." + entry.getKey() + ".crewMembers", entry.getValue().getClanMembers())
            crewsStorage.set("crews.data." + entry.getKey() + ".crewAllies", entry.getValue().getClanAllies())
            crewsStorage.set("crews.data." + entry.getKey() + ".crewEnemies", entry.getValue().getClanEnemies())
            crewStorage.set("crews.data." + entry.getKey() + ".friendlyFire", entry.getValue().isFriendlyFireAllowed())
            crewStorage.set("crews.data." + entry.getKey() + ".crewPoints", entry.getValue().getClanPoints())
            if (entry.getValue().getClanHomeWorld() != null) {
                crewsStorage.set(
                    "crews.data." + entry.getKey() + ".crewHome.worldName",
                    entry.getValue().getClanHomeWorld()
                )
                crewsStorage.set("crews.data." + entry.getKey() + ".crewHome.x", entry.getValue().getClanHomeX())
                crewsStorage.set("crews.data." + entry.getKey() + ".crewHome.y", entry.getValue().getClanHomeY())
                crewsStorage.set("crews.data." + entry.getKey() + ".crewHome.z", entry.getValue().getClanHomeZ())
                crewsStorage.set("crews.data." + entry.getKey() + ".crewHome.yaw", entry.getValue().getClanHomeYaw())
                crewsStorage.set(
                    "crews.data." + entry.getKey() + ".crewHome.pitch",
                    entry.getValue().getClanHomePitch()
                )
            }
            if (entry.getValue().getMaxAllowedProtectedChests() > 0) {
                val chests: HashMap<String, Chest> = entry.getValue().getProtectedChests()
                crewsStorage.set(
                    "crews.data." + entry.getKey() + ".maxAllowedProtectedChests",
                    entry.getValue().getMaxAllowedProtectedChests()
                )
                for (chestLocation in chests.entrySet()) {
                    if (chestLocation.getValue().getChestWorldName() == null) {
                        console.sendMessage(
                            ColorUtils.translateColorCodes(
                                messagesConfig.getString("chest-location-save-failed-1")
                                    .replace("%CREW%", chestLocation.getValue().getChestWorldName().toString())
                            )
                        )
                        console.sendMessage(
                            ColorUtils.translateColorCodes(
                                messagesConfig.getString("chest-location-save-failed-2")
                                    .replace("%CREW%", chestLocation.getValue().getChestWorldName().toString())
                            )
                        )
                        continue
                    }
                    crewsStorage.set(
                        "crews.data." + entry.getKey() + ".protectedChests." + chestLocation.getKey() + ".chestUUID",
                        chestLocation.getKey()
                    )
                    crewsStorage.set(
                        "crews.data." + entry.getKey() + ".protectedChests." + chestLocation.getKey() + ".chestWorld",
                        chestLocation.getValue().getChestWorldName()
                    )
                    crewsStorage.set(
                        "crews.data." + entry.getKey() + ".protectedChests." + chestLocation.getKey() + ".chestX",
                        chestLocation.getValue().getChestLocationX()
                    )
                    crewsStorage.set(
                        "crews.data." + entry.getKey() + ".protectedChests." + chestLocation.getKey() + ".chestY",
                        chestLocation.getValue().getChestLocationY()
                    )
                    crewsStorage.set(
                        "crews.data." + entry.getKey() + ".protectedChests." + chestLocation.getKey() + ".chestZ",
                        chestLocation.getValue().getChestLocationZ()
                    )
                    crewsStorage.set(
                        "crews.data." + entry.getKey() + ".protectedChests." + chestLocation.getKey() + ".playersWithAccess",
                        chestLocation.getValue().getPlayersWithAccess()
                    )
                }
            }
        }
        Crews.getPlugin().crewsFileManager.saveClansConfig()
    }

    @Throws(IOException::class)
    fun restoreClans() {
        crewsList.clear()
        crewsStorage.getConfigurationSection("crews.data").getKeys(false).forEach { key ->
            val protectedChests: HashMap<String, Chest> = HashMap()
            val uuid: UUID = UUID.fromString(key)
            val crewFinalName: String = crewsStorage.getString("crews.data.$key.crewFinalName")
            val crewPrefix: String = crewnsStorage.getString("crews.data.$key.crewPrefix")
            val crewMembersConfigSection: List<String> = crewsStorage.getStringList("crews.data.$key.crewMembers")
            val crewAlliesConfigSection: List<String> = crewsStorage.getStringList("crews.data.$key.crewAllies")
            val crewEnemiesConfigSection: List<String> = crewsStorage.getStringList("crews.data.$key.crewEnemies")
            val crewMembers = ArrayList(crewMembersConfigSection)
            val crewAllies = ArrayList(crewAlliesConfigSection)
            val crewEnemies = ArrayList(crewEnemiesConfigSection)
            val friendlyFire: Boolean = crewsStorage.getBoolean("crews.data.$key.friendlyFire")
            val crewPoints: Int = crewsStorage.getInt("crews.data.$key.clanPoints")
            val crewHomeWorld: String = crewsStorage.getString("crews.data.$key.clanHome.worldName")
            val crewHomeX: Double = crewsStorage.getDouble("crews.data.$key.crewHome.x")
            val crewHomeY: Double = crewsStorage.getDouble("crews.data.$key.crewHome.y")
            val crewHomeZ: Double = crewsStorage.getDouble("crews.data.$key.crewHome.z")
            val crewHomeYaw = crewsStorage.getDouble("crews.data.$key.crewHome.yaw") as Float
            val crewHomePitch = crewsStorage.getDouble("crews.data.$key.crewHome.pitch") as Float
            val maxAllowedProtectedChests: Int = crewsStorage.getInt("crews.data.$key.maxAllowedProtectedChests")
            val crew: Crew = Clan(key, crewFinalName)
            if (!crewsStorage.getBoolean("name-strip-colour-complete") || crewFinalName.contains("&") || crewFinalName.contains(
                    "#"
                )
            ) {
                crew.setCrewFinalName(stripCrewNameColorCodes(crew))
            }
            crew.setCrewPrefix(crewPrefix)
            crew.setCrewMembers(crewMembers)
            crew.setCrewAllies(crewAllies)
            crew.setCrewEnemies(crewEnemies)
            crew.setFriendlyFireAllowed(friendlyFire)
            crew.setCrewPoints(crewPoints)
            if (crewHomeWorld != null) {
                crew.setCrewHomeWorld(crewHomeWorld)
                crew.setCrewHomeX(crewHomeX)
                crew.setCrewHomeY(crewHomeY)
                crew.setCrewHomeZ(crewHomeZ)
                crew.setCrewHomeYaw(crewHomeYaw)
                crew.setCrewHomePitch(crewHomePitch)
            }
            crew.setMaxAllowedProtectedChests(maxAllowedProtectedChests)
            val chestSection: ConfigurationSection =
                crewsStorage.getConfigurationSection("crews.data.$key.protectedChests")
            if (chestSection != null) {
                crewsStorage.getConfigurationSection("crews.data.$key.protectedChests").getKeys(false)
                    .forEach { configChest ->
                        val chestUUID: String =
                            crewsStorage.getString("crews.data.$key.protectedChests.$configChest.chestUUID")
                        val chestWorld: String =
                            crewsStorage.getString("crews.data.$key.protectedChests.$configChest.chestWorld")
                        val chestX: Double =
                            crewsStorage.getDouble("crews.data.$key.protectedChests.$configChest.chestX")
                        val chestY: Double =
                            crewsStorage.getDouble("crews.data.$key.protectedChests.$configChest.chestY")
                        val chestZ: Double =
                            crewsStorage.getDouble("crews.data.$key.protectedChests.$configChest.chestZ")
                        val playersWithAccessConfigSection: List<String> =
                            crewsStorage.getStringList("crews.data.$key.protectedChests.$configChest.playersWithAccess")
                        val playersWithAccess = ArrayList(playersWithAccessConfigSection)
                        val world: World = Bukkit.getWorld(chestWorld)
                        if (world != null) {
                            val location = Location(world, chestX, chestY, chestZ)
                            val chest = Chest(crew, location)
                            chest.setUUID(chestUUID)
                            chest.setChestLocationX(chestX)
                            chest.setChestLocationY(chestY)
                            chest.setChestLocationZ(chestZ)
                            chest.setPlayersWithAccess(playersWithAccess)
                            protectedChests.put(chestUUID, chest)
                        } else {
                            console.sendMessage(
                                ColorUtils.translateColorCodes(
                                    messagesConfig.getString("chest-location-load-failed")
                                        .replace("%WORLD%", chestWorld).replace("%CREW%", crewFinalName)
                                )
                            )
                        }
                    }
            }
            crew.setProtectedChests(protectedChests)
            crewsList.put(uuid, crew)
        }
        if (!crewsStorage.getBoolean("name-strip-colour-complete")) {
            crewsStorage.set("name-strip-colour-complete", true)
        }
    }

    fun createCrew(player: Player, crewName: String?): Crew {
        val ownerUUID: UUID = player.getUniqueId()
        val ownerUUIDString: String = player.getUniqueId().toString()
        val newCrew = Crew(ownerUUIDString, crewName)
        crewsList.put(ownerUUID, newCrew)
        return newCrew
    }

    fun isCrewExisting(player: Player): Boolean {
        val uuid: UUID = player.getUniqueId()
        return if (crewsList.containsKey(uuid)) {
            true
        } else false
    }

    @Throws(IOException::class)
    fun deleteCrew(player: Player): Boolean {
        val uuid: UUID = player.getUniqueId()
        val key: String = uuid.toString()
        if (findCrewByOwner(player) != null) {
            if (isCrewOwner(player)) {
                return if (crewsList.containsKey(uuid)) {
                    fireCrewDisbandEvent(player)
                    if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                        console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewDisbandEvent"))
                    }
                    crewsList.remove(uuid)
                    crewsStorage.set("crews.data.$key", null)
                    Crews.getPlugin().crewsFileManager.saveCrewsConfig()
                    true
                } else {
                    player.sendMessage(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString(
                                "crews-update-error-1"
                            )
                        )
                    )
                    false
                }
            }
        }
        return false
    }

    @Throws(IOException::class)
    fun deleteOfflineCrew(offlinePlayer: OfflinePlayer): Boolean {
        val uuid: UUID = offlinePlayer.getUniqueId()
        val key: String = uuid.toString()
        return if (findCrewByOfflineOwner(offlinePlayer) != null) {
            if (crewsList.containsKey(uuid)) {
                fireOfflineCrewDisbandEvent(offlinePlayer)
                if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired OfflineCrewDisbandEvent"))
                }
                crewsList.remove(uuid)
                crewsStorage.set("crews.data.$key", null)
                Crews.getPlugin().crewsFileManager.saveCrewsConfig()
                true
            } else {
                console.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesConfig.getString(
                            "crews-update-error-1"
                        )
                    )
                )
                false
            }
        } else false
    }

    fun isCrewOwner(player: Player): Boolean {
        val uuid: UUID = player.getUniqueId()
        val ownerUUID: String = uuid.toString()
        val crew: Crew? = crewsList[uuid]
        if (crew != null) {
            if (crew.getClanOwner() == null) {
                return false
            } else {
                if (crew.getCrewOwner().equals(ownerUUID)) {
                    return true
                }
            }
        }
        return false
    }

    fun findCrewByOwner(player: Player): Crew? {
        val uuid: UUID = player.getUniqueId()
        return crewsList[uuid]
    }

    fun findCrewByOfflineOwner(offlinePlayer: OfflinePlayer): Crew? {
        val uuid: UUID = offlinePlayer.getUniqueId()
        return crewsList[uuid]
    }

    fun findCrewByPlayer(player: Player): Crew? {
        for (crew in crewsList.values()) {
            if (findCrewByOwner(player) != null) {
                return crew
            }
            if (crew.getCrewMembers() != null) {
                for (member in crew.getCrewMembers()) {
                    if (member.equals(player.getUniqueId().toString())) {
                        return crew
                    }
                }
            }
        }
        return null
    }

    fun findCrewByOfflinePlayer(player: OfflinePlayer): Crew? {
        for (crew in crewsList.values()) {
            if (findCrewByOfflineOwner(player) != null) {
                return crew
            }
            if (crew.getCrewMembers() != null) {
                for (member in crew.getCrewMembers()) {
                    if (member.equals(player.getUniqueId().toString())) {
                        return crew
                    }
                }
            }
        }
        return null
    }

    fun updatePrefix(player: Player, prefix: String?) {
        val uuid: UUID = player.getUniqueId()
        if (!isCrewOwner(player)) {
            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-must-be-owner")))
            return
        }
        val crew: Crew? = crewsList[uuid]
        crew.setCrewPrefix(prefix)
    }

    fun addCrewMember(crew: Crew, player: Player): Boolean {
        val uuid: UUID = player.getUniqueId()
        val memberUUID: String = uuid.toString()
        crew.addCrewMember(memberUUID)
        if (crewsConfig.getBoolean("protections.chests.enabled")) {
            val crewChestList: HashMap<String, Chest> = crew.getProtectedChests()
            if (!crewChestList.isEmpty()) {
                for (chestEntry in crewChestList.entrySet()) {
                    val chest: Chest = chestEntry.getValue()
                    val playersWithAccess: ArrayList<String> = chest.getPlayersWithAccess()
                    playersWithAccess.add(memberUUID)
                    chest.setPlayersWithAccess(playersWithAccess)
                    crewChestList.replace(chestEntry.getKey(), chest)
                }
            }
        }
        return true
    }

    fun removeCrewMember(crew: Crew, player: Player): Boolean {
        val uuid: UUID = player.getUniqueId()
        val memberUUID: String = uuid.toString()
        crew.removeCrewMember(memberUUID)
        if (crewsConfig.getBoolean("protections.chests.enabled")) {
            val crewChestList: HashMap<String, Chest> = crew.getProtectedChests()
            if (!crewChestList.isEmpty()) {
                for (chestEntry in crewChestList.entrySet()) {
                    val chest: Chest = chestEntry.getValue()
                    val playersWithAccess: ArrayList<String> = chest.getPlayersWithAccess()
                    playersWithAccess.remove(memberUUID)
                    chest.setPlayersWithAccess(playersWithAccess)
                    crewChestList.replace(chestEntry.getKey(), chest)
                }
            }
        }
        return true
    }

    fun removeOfflineCrewMember(crew: Crew, offlinePlayer: OfflinePlayer): Boolean {
        val offlineUUID: UUID = offlinePlayer.getUniqueId()
        val offlineMemberUUID: String = offlineUUID.toString()
        crew.removeCrewMember(offlineMemberUUID)
        if (crewsConfig.getBoolean("protections.chests.enabled")) {
            val crewChestList: HashMap<String, Chest> = crew.getProtectedChests()
            if (!crewChestList.isEmpty()) {
                for (chestEntry in crewChestList.entrySet()) {
                    val chest: Chest = chestEntry.getValue()
                    val playersWithAccess: ArrayList<String> = chest.getPlayersWithAccess()
                    playersWithAccess.add(offlineMemberUUID)
                    chest.setPlayersWithAccess(playersWithAccess)
                    crewChestList.replace(chestEntry.getKey(), chest)
                }
            }
        }
        return true
    }

    fun addCrewEnemy(crewOwner: Player, enemyCrewOwner: Player) {
        val ownerUUID: UUID = crewOwner.getUniqueId()
        val enemyUUID: UUID = enemyCrewOwner.getUniqueId()
        val enemyOwnerUUID: String = enemyUUID.toString()
        val crew: Crew? = crewsList[ownerUUID]
        crew.addCrewEnemy(enemyOwnerUUID)
    }

    fun removeClanEnemy(crewOwner: Player, enemyCrewOwner: Player) {
        val ownerUUID: UUID = crewOwner.getUniqueId()
        val enemyUUID: UUID = enemyCrewOwner.getUniqueId()
        val enemyOwnerUUID: String = enemyUUID.toString()
        val crew: Crew? = crewsList[ownerUUID]
        crew.removeCrewEnemy(enemyOwnerUUID)
    }

    fun addCrewAlly(crewOwner: Player, allyCrewOwner: Player) {
        val ownerUUID: UUID = crewOwner.getUniqueId()
        val uuid: UUID = allyCrewOwner.getUniqueId()
        val allyUUID: String = uuid.toString()
        val crew: Crew? = crewsList[ownerUUID]
        crew.addCrewAlly(allyUUID)
    }

    fun removeCrewAlly(crewOwner: Player, allyCrewOwner: Player) {
        val ownerUUID: UUID = crewOwner.getUniqueId()
        val uuid: UUID = allyCrewOwner.getUniqueId()
        val allyUUID: String = uuid.toString()
        val crew: Crew? = crewsList[ownerUUID]
        crew.removeCrewAlly(allyUUID)
    }

    fun isHomeSet(crew: Crew): Boolean {
        return if (crew.getCrewHomeWorld() != null) {
            true
        } else false
    }

    fun deleteHome(crew: Crew) {
        val key: String = crew.getCrewOwner()
        crew.setCrewHomeWorld(null)
        crewsStorage.set("crews.data.$key.crewHome", null)
        Crews.getPlugin().crewsFileManager.saveCrewsConfig()
    }

    fun stripClanNameColorCodes(crew: Crew?): String? {
        val crewFinalName: String = clan.getCrewFinalName()
        if (crewsConfig.getBoolean("general.developer-debug-mode.enabled") || !crewsStorage.getBoolean("name-strip-colour-complete")
            || crewFinalName.contains("&") || crewFinalName.contains("#")
        ) {
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFound Colour Code To Strip"))
            console.sendMessage(ColorUtils.translateColorCodes("&aOriginal Name: ") + crewFinalName)
            console.sendMessage(
                ColorUtils.translateColorCodes("&aNew Name: ") + if (crewFinalName == null) null else STRIP_COLOR_PATTERN.matcher(
                    crewFinalName
                ).replaceAll("")
            )
        }
        return if (crewFinalName == null) null else STRIP_COLOR_PATTERN.matcher(crewFinalName).replaceAll("")
    }

    @Throws(IOException::class)
    fun transferClanOwner(originalCrew: Crew, originalCrewOwner: Player, newCrewOwner: Player): Clan? {
        if (findCrewByOwner(originalCrewOwner) != null) {
            if (isCrewOwner(originalCrewOwner)) {
                if (!isCrewOwner(newCrewOwner) && findCrewByPlayer(newCrewOwner) == null) {
                    val originalOwnerKey: String = originalCrewOwner.getUniqueId().toString()
                    val originalOwnerUUID: UUID = originalCrewOwner.getUniqueId()
                    val newOwnerUUID: UUID = newCrewOwner.getUniqueId()
                    val crewFinalName: String = originalCrew.getCrewFinalName()
                    val clanPrefix: String = originalCrew.getCrewPrefix()
                    val crewMembers: ArrayList<String> = ArrayList(originalCrew.getCrewMembers())
                    val crewAllies: ArrayList<String> = ArrayList(originalClan.getCrewAllies())
                    val crewEnemies: ArrayList<String> = ArrayList(originalClan.getCrewEnemies())
                    val friendlyFire: Boolean = originalCrew.isFriendlyFireAllowed()
                    val crewPoints: Int = originalCrew.getCrewPoints()
                    val crewHomeWorld: String = originalCrew.getCrewHomeWorld()
                    val crewHomeX: Double = originalCrew.getCrewHomeX()
                    val crewHomeY: Double = originalCrew.getCrewHomeY()
                    val crewHomeZ: Double = originalCrew.getCrewHomeZ()
                    val crewHomeYaw: Float = originalCrew.getCrewHomeYaw()
                    val crewHomePitch: Float = originalCrew.getCrewHomePitch()
                    val maxAllowedProtectedChests: Int = originalCrew.getMaxAllowedProtectedChests()
                    val protectedChests: HashMap<String, Chest> = originalCrew.getProtectedChests()
                    val newCrew = Crew(newOwnerUUID.toString(), crewFinalName)
                    newCrew.setCrewPrefix(crewPrefix)
                    newCrew.setCrewMembers(crewMembers)
                    newCrew.setCrewAllies(crewAllies)
                    newCrew.setCrewEnemies(crewEnemies)
                    newCrew.setFriendlyFireAllowed(friendlyFire)
                    newCrew.setCrewPoints(crewPoints)
                    newCrew.setCrewHomeWorld(crewHomeWorld)
                    newCrew.setCrewHomeX(crewHomeX)
                    newCrew.setCrewHomeY(crewHomeY)
                    newCrew.setCrewHomeZ(crewHomeZ)
                    newCrew.setCrewHomeYaw(crewHomeYaw)
                    newCrew.setCrewHomePitch(crewHomePitch)
                    newCrew.setMaxAllowedProtectedChests(maxAllowedProtectedChests)
                    newCrew.setProtectedChests(protectedChests)
                    crewsList.put(newOwnerUUID, newCrew)
                    if (crewsList.containsKey(originalOwnerUUID)) {
                        fireCrewTransferOwnershipEvent(originalCrewOwner, newCrewOwner, newCrew)
                        if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired ClanTransferOwnershipEvent"))
                        }
                        crewsList.remove(originalOwnerUUID)
                        clansStorage.set("crews.data.$originalOwnerKey", null)
                        Crews.getPlugin().crewsFileManager.saveCrewsConfig()
                    } else {
                        originalCrewOwner.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-1")))
                    }
                    return newCrew
                } else {
                    originalCrewOwner.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-ownership-transfer-failed-target-in-crew")))
                }
            } else {
                originalCrewOwner.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-must-be-owner")))
            }
        }
        return null
    }

    fun hasEnoughPoints(crew: Crew, points: Int): Boolean {
        return if (crew.getCrewPoints() >= points) {
            true
        } else false
    }

    fun addPoints(crew: Crew, points: Int) {
        val currentPointValue: Int = crew.getCrewPoints()
        crew.setCrewPoints(currentPointValue + points)
    }

    fun withdrawPoints(crew: Crew, points: Int): Boolean {
        val currentPointValue: Int = crew.getCrewPoints()
        if (currentPointValue != 0) {
            if (hasEnoughPoints(crew, points)) {
                crew.setCrewPoints(currentPointValue - points)
                return true
            }
            return false
        }
        return false
    }

    fun setPoints(crew: Crew, points: Int) {
        crew.setCrewPoints(points)
    }

    fun resetPoints(crew: Crew) {
        crew.setCrewPoints(0)
    }

    fun getChestLocation(chest: Chest): Location? {
        val worldName: String = chest.getChestWorldName()
        val chestX: Double = chest.getChestLocationX()
        val chestY: Double = chest.getChestLocationY()
        val chestZ: Double = chest.getChestLocationZ()
        val world: World = Bukkit.getWorld(worldName)
        return if (world != null) {
            Location(world, chestX, chestY, chestZ)
        } else null
    }

    fun isChestLocked(crew: Crew?, location: Location?): Boolean {
        val crewChestList: HashMap<String, Chest> = crew.getProtectedChests()
        for (chest in crewChestList.entrySet()) {
            val chestLocation: Location? = getChestLocation(chest.getValue())
            if (chestLocation != null) {
                if (chestLocation.equals(location)) {
                    return true
                }
            }
        }
        return false
    }

    fun isChestLocked(location: Location?): Boolean {
        val allChests: List<Chest> = globalLockedChests
        for (chest in allChests) {
            if (chest != null) {
                val worldName: String = chest.getChestWorldName()
                val world: World = Bukkit.getWorld(worldName)
                val chestLocation =
                    Location(world, chest.getChestLocationX(), chest.getChestLocationY(), chest.getChestLocationZ())
                if (chestLocation.equals(location)) {
                    return true
                }
            }
        }
        return false
    }

    fun addProtectedChest(crew: Crew, location: Location?, player: Player): Boolean {
        val crewChestList: HashMap<String, Chest> = crew.getProtectedChests()
        return if (isChestLocked(crew, location)) {
            player.sendMessage(
                ColorUtils.translateColorCodes(
                    messagesConfig.getString(
                        "failed-chest-already-protected"
                    )
                )
            )
            false
        } else {
            val newChestUUID: UUID = UUID.randomUUID()
            val chestUUID: String = newChestUUID.toString()
            val chest = Chest(crew, location)
            crewChestList[chestUUID] = chest
            true
        }
    }

    @Throws(IOException::class)
    fun removeProtectedChest(crewOwnerUUID: String?, location: Location?): Boolean {
        val uuid: UUID = UUID.fromString(crewOwnerUUID)
        val crew: Crew? = findCrewByOfflineOwner(Bukkit.getOfflinePlayer(uuid))
        val key: String = crew.getCrewOwner()
        val crewChestList: HashMap<String, Chest> = crew.getProtectedChests()
        if (isChestLocked(crew, location)) {
            for (chest in crewChestList.entrySet()) {
                val worldName: String = chest.getValue().getChestWorldName()
                val world: World = Bukkit.getWorld(worldName)
                val chestLocation = Location(
                    world,
                    chest.getValue().getChestLocationX(),
                    chest.getValue().getChestLocationY(),
                    chest.getValue().getChestLocationZ()
                )
                if (chestLocation.equals(getChestByLocation(crew, location))) {
                    val chestUUID: String = chest.getKey()
                    crewChestList.remove(chestUUID)
                    crew.setProtectedChests(crewChestList)
                    crewsList.replace(UUID.fromString(crew.getCrewOwner()), crew)
                    crewsStorage.set("crews.data.$key.protectedChests.$chestUUID", null)
                    Crews.getPlugin().crewsFileManager.saveCrewsConfig()
                    return true
                }
            }
        }
        return false
    }

    @Throws(IOException::class)
    fun removeProtectedChest(crew: Crew, location: Location?, player: Player): Boolean {
        val key: String = crew.getCrewOwner()
        if (isChestLocked(crew, location)) {
            if (removeProtectedChest(key, location)) {
                return true
            }
        } else {
            player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-chest-not-protected")))
            return false
        }
        return false
    }

    @Throws(IOException::class)
    fun removeProtectedChest(crewOwnerUUID: String?, location: Location?, player: Player?): Boolean {
        val uuid: UUID = UUID.fromString(crewOwnerUUID)
        val crew: Crew? = findCrewByOfflineOwner(Bukkit.getOfflinePlayer(uuid))
        return if (removeProtectedChest(crew, location, player)) {
            true
        } else false
    }

    fun getAllProtectedChestsByCrew(crew: Crew): Set<Map.Entry<String, Chest>> {
        return crew.getProtectedChests().entrySet()
    }

    fun getChestByLocation(crew: Crew?, location: Location?): Location? {
        val crewChestList: HashMap<String, Chest> = crew.getProtectedChests()
        for (chest in crewChestList.entrySet()) {
            val worldName: String = chest.getValue().getChestWorldName()
            val world: World = Bukkit.getWorld(worldName)
            val chestLocation = Location(
                world,
                chest.getValue().getChestLocationX(),
                chest.getValue().getChestLocationY(),
                chest.getValue().getChestLocationZ()
            )
            if (chestLocation.equals(location)) {
                return chestLocation
            }
        }
        return null
    }

    fun getChestByLocation(location: Location): Chest? {
        val allChests: List<Chest> = globalLockedChests
        for (chest in allChests) {
            val worldName: String = chest.getChestWorldName()
            val world: World = Bukkit.getWorld(worldName)
            val chestLocation =
                Location(world, chest.getChestLocationX(), chest.getChestLocationY(), chest.getChestLocationZ())
            if (location.equals(chestLocation)) {
                return chest
            }
        }
        return null
    }

    fun getAllProtectedChestsLocationsByCrew(crew: Crew): List<Location> {
        val crewChestList: HashMap<String, Chest> = crew.getProtectedChests()
        val allChestLocations: List<Location> = ArrayList()
        for (chest in crewChestList.entrySet()) {
            val worldName: String = chest.getValue().getChestWorldName()
            val world: World = Bukkit.getWorld(worldName)
            val chestLocation = Location(
                world,
                chest.getValue().getChestLocationX(),
                chest.getValue().getChestLocationY(),
                chest.getValue().getChestLocationZ()
            )
            allChestLocations.add(chestLocation)
        }
        return allChestLocations
    }

    fun getPlayersWithChestAccessByChest(chest: Chest): List<String> {
        return chest.getPlayersWithAccess()
    }

    fun getOfflinePlayersWithChestAccessByChest(chest: Chest): List<OfflinePlayer> {
        val playersWithAccess = getPlayersWithChestAccessByChest(chest)
        val offlinePlayersWithAccess: List<OfflinePlayer> = ArrayList()
        for (string in playersWithAccess) {
            val uuid: UUID = UUID.fromString(string)
            val offlinePlayer: OfflinePlayer = Bukkit.getOfflinePlayer(uuid)
            offlinePlayersWithAccess.add(offlinePlayer)
        }
        return offlinePlayersWithAccess
    }

    fun hasAccessToLockedChest(offlinePlayer: OfflinePlayer, chest: Chest): Boolean {
        return getOfflinePlayersWithChestAccessByChest(chest).contains(offlinePlayer)
    }

    val globalLockedChestLocations: List<Any>
        get() {
            val allLockedChest: List<Chest> = globalLockedChests
            val allLockedChestLocations: List<Location> = ArrayList()
            for (chest in allLockedChest) {
                val worldName: String = chest.getChestWorldName()
                val world: World = Bukkit.getWorld(worldName)
                val location =
                    Location(world, chest.getChestLocationX(), chest.getChestLocationY(), chest.getChestLocationZ())
                allLockedChestLocations.add(location)
            }
            return allLockedChestLocations
        }
    val globalLockedChests: List<Any>
        get() {
            val allLockedChests: List<Chest> = ArrayList()
            for (crew in crewsList.values()) {
                for (chests in crew.getProtectedChests().entrySet()) {
                    allLockedChests.add(chests.getValue())
                }
            }
            return allLockedChests
        }
    val crews: Set<Map.Entry<Any, Any>>
        get() = crewsList.entrySet()
    val rawCrewsList: Set<Any>
        get() = crewsList.keySet()
    val crewList: Collection<Any>
        get() = crewsList.values()

    private fun fireCrewDisbandEvent(player: Player) {
        val crewByOwner: Crew? = findCrewByOwner(player)
        val crewDisbandEvent = CrewDisbandEvent(player, crewByOwner.getCrewFinalName())
        Bukkit.getPluginManager().callEvent(crewDisbandEvent)
    }

    private fun fireOfflineCrewDisbandEvent(offlinePlayer: OfflinePlayer) {
        val crewByOfflineOwner: Crew? = findCrewByOfflineOwner(offlinePlayer)
        val crewOfflineDisbandEvent = CrewOfflineDisbandEvent(offlinePlayer, crewByOfflineOwner.getCrewFinalName())
        Bukkit.getPluginManager().callEvent(crewOfflineDisbandEvent)
    }

    private fun fireClanTransferOwnershipEvent(originalCrewOwner: Player, newCrewOwner: Player, newCrew: Crew) {
        val crewTransferOwnershipEvent =
            CrewTransferOwnershipEvent(originalCrewOwner, originalCrewOwner, newCrewOwner, newCrew)
        Bukkit.getPluginManager().callEvent(crewTransferOwnershipEvent)
    }
}
