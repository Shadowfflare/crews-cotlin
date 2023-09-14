package xyz.shadowflare.utils

import org.bukkit.Bukkit

object UsermapStorageUtil {
    private val console: ConsoleCommandSender = Bukkit.getConsoleSender()
    private val usermap: Map<UUID, CrewPlayer> = HashMap()
    private val clansConfig: FileConfiguration = Crews.getPlugin().getConfig()
    private val usermapConfig: FileConfiguration = Crews.getPlugin().usermapFileManager.getUsermapConfig()
    private val messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    private const val PLAYER_PLACEHOLDER = "%PLAYER%"
    @Throws(IOException::class)
    fun saveUsermap() {
        for (entry in usermap.entrySet()) {
            usermapConfig.set("users.data." + entry.getKey() + ".javaUUID", entry.getValue().getJavaUUID())
            usermapConfig.set("users.data." + entry.getKey() + ".lastPlayerName", entry.getValue().getLastPlayerName())
            usermapConfig.set("users.data." + entry.getKey() + ".pointBalance", entry.getValue().getPointBalance())
            usermapConfig.set("users.data." + entry.getKey() + ".canChatSpy", entry.getValue().getCanChatSpy())
            usermapConfig.set("users.data." + entry.getKey() + ".isBedrockPlayer", entry.getValue().isBedrockPlayer())
            if (entry.getValue().isBedrockPlayer()) {
                usermapConfig.set("users.data." + entry.getKey() + ".bedrockUUID", entry.getValue().getBedrockUUID())
            }
        }
        Crews.getPlugin().usermapFileManager.saveUsermapConfig()
    }

    @Throws(IOException::class)
    fun restoreUsermap() {
        usermap.clear()
        usermapConfig.getConfigurationSection("users.data").getKeys(false).forEach { key ->
            val uuid: UUID = UUID.fromString(key)
            val javaUUID: String = usermapConfig.getString("users.data.$key.javaUUID")
            val lastPlayerName: String = usermapConfig.getString("users.data.$key.lastPlayerName")
            val pointBalance: Int = usermapConfig.getInt("users.data.$key.pointBalance")
            val canChatSpy: Boolean = usermapConfig.getBoolean("users.data.$key.canChatSpy")
            val isBedrockPlayer: Boolean = usermapConfig.getBoolean("users.data.$key.isBedrockPlayer")
            val bedrockUUID: String = usermapConfig.getString("users.data.$key.bedrockUUID")
            val crewPlayer = CrewPlayer(javaUUID, lastPlayerName)
            crewPlayer.setPointBalance(pointBalance)
            crewPlayer.setCanChatSpy(canChatSpy)
            crewPlayer.setBedrockPlayer(isBedrockPlayer)
            crewPlayer.setBedrockUUID(bedrockUUID)
            usermap.put(uuid, crewPlayer)
        }
    }

    fun addToUsermap(player: Player) {
        val uuid: UUID = player.getUniqueId()
        val javaUUID: String = uuid.toString()
        val lastPlayerName: String = player.getName()
        val crewPlayer = CrewPlayer(javaUUID, lastPlayerName)
        usermap.put(uuid, crewPlayer)
    }

    fun addBedrockPlayerToUsermap(player: Player) {
        val uuid: UUID = player.getUniqueId()
        if (Crews.getFloodgateApi() != null) {
            val floodgatePlayer: FloodgatePlayer = Crews.getFloodgateApi().getPlayer(uuid)
            val bedrockPlayerUUID: UUID = floodgatePlayer.getJavaUniqueId()
            val javaUUID: String = floodgatePlayer.getJavaUniqueId().toString()
            val lastPlayerName: String = floodgatePlayer.getUsername()
            val crewPlayer = CrewPlayer(javaUUID, lastPlayerName)
            crewPlayer.setBedrockPlayer(true)
            crewPlayer.setBedrockUUID(floodgatePlayer.getCorrectUniqueId().toString())
            usermap.put(bedrockPlayerUUID, crewPlayer)
        }
    }

    fun isUserExisting(player: Player): Boolean {
        val uuid: UUID = player.getUniqueId()
        return if (usermap.containsKey(uuid)) {
            true
        } else false
    }

    fun getClanPlayerByBukkitPlayer(player: Player): CrewPlayer? {
        val uuid: UUID = player.getUniqueId()
        if (usermap.containsKey(uuid)) {
            return usermap[uuid]
        } else {
            console.sendMessage(
                ColorUtils.translateColorCodes(
                    messagesConfig.getString("crew-player-not-found-1")
                        .replace(PLAYER_PLACEHOLDER, player.getName())
                )
            )
            console.sendMessage(
                ColorUtils.translateColorCodes(
                    messagesConfig.getString("crew-player-not-found-2")
                        .replace(PLAYER_PLACEHOLDER, player.getName())
                )
            )
        }
        return null
    }

    fun getClanPlayerByBukkitOfflinePlayer(offlinePlayer: OfflinePlayer): CrewPlayer? {
        val uuid: UUID = offlinePlayer.getUniqueId()
        if (usermap.containsKey(uuid)) {
            return usermap[uuid]
        } else {
            console.sendMessage(
                ColorUtils.translateColorCodes(
                    messagesConfig.getString("crew-player-not-found-1")
                        .replace(PLAYER_PLACEHOLDER, offlinePlayer.getName())
                )
            )
            console.sendMessage(
                ColorUtils.translateColorCodes(
                    messagesConfig.getString("crew-player-not-found-2")
                        .replace(PLAYER_PLACEHOLDER, offlinePlayer.getName())
                )
            )
        }
        return null
    }

    fun getBukkitPlayerByName(name: String?): Player? {
        for (crewPlayer in usermap.values()) {
            if (crewPlayer.getLastPlayerName().equalsIgnoreCase(name)) {
                return Bukkit.getPlayer(crewPlayer.getLastPlayerName())
            } else {
                console.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesConfig.getString("crew-player-not-found-1")
                            .replace(PLAYER_PLACEHOLDER, name)
                    )
                )
                console.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesConfig.getString("crew-player-not-found-2")
                            .replace(PLAYER_PLACEHOLDER, name)
                    )
                )
            }
        }
        return null
    }

    fun getBukkitOfflinePlayerByName(name: String?): OfflinePlayer? {
        for (crewPlayer in usermap.values()) {
            if (crewPlayer.getLastPlayerName().equalsIgnoreCase(name)) {
                return Bukkit.getOfflinePlayer(UUID.fromString(clanPlayer.getJavaUUID()))
            } else {
                console.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesConfig.getString("crew-player-not-found-1")
                            .replace(PLAYER_PLACEHOLDER, name)
                    )
                )
                console.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesConfig.getString("crew-player-not-found-2")
                            .replace(PLAYER_PLACEHOLDER, name)
                    )
                )
            }
        }
        return null
    }

    fun hasPlayerNameChanged(player: Player): Boolean {
        for (crewPlayer in usermap.values()) {
            if (!player.getName().equals(crewPlayer.getLastPlayerName())) {
                return true
            }
        }
        return false
    }

    fun hasBedrockPlayerJavaUUIDChanged(player: Player): Boolean {
        val uuid: UUID = player.getUniqueId()
        for (crewPlayer in usermap.values()) {
            if (Crews.getFloodgateApi() != null) {
                val floodgatePlayer: FloodgatePlayer = Crews.getFloodgateApi().getPlayer(uuid)
                if (!floodgatePlayer.getJavaUniqueId().toString().equals(crewPlayer.getBedrockUUID())) {
                    return true
                }
            }
        }
        return false
    }

    fun updatePlayerName(player: Player) {
        val uuid: UUID = player.getUniqueId()
        val newPlayerName: String = player.getName()
        val crewPlayer: CrewPlayer? = usermap[uuid]
        crewPlayer.setLastPlayerName(newPlayerName)
        usermap.replace(uuid, crewPlayer)
    }

    fun updateBedrockPlayerJavaUUID(player: Player) {
        val uuid: UUID = player.getUniqueId()
        val crewPlayer: CrewPlayer? = usermap[uuid]
        if (Crews.getFloodgateApi() != null) {
            val floodgatePlayer: FloodgatePlayer = Crews.getFloodgateApi().getPlayer(uuid)
            val newJavaUUID: String = floodgatePlayer.getJavaUniqueId().toString()
            crewPlayer.setJavaUUID(newJavaUUID)
            usermap.replace(uuid, crewPlayer)
        }
    }

    fun toggleChatSpy(player: Player): Boolean {
        val uuid: UUID = player.getUniqueId()
        val crewPlayer: CrewPlayer? = usermap[uuid]
        return if (!crewPlayer.getCanChatSpy()) {
            crewPlayer.setCanChatSpy(true)
            fireClanChatSpyToggledEvent(player, crewPlayer, true)
            if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewChatSpyToggledEvent"))
            }
            true
        } else {
            crewPlayer.setCanChatSpy(false)
            fireCrewChatSpyToggledEvent(player, crewPlayer, false)
            if (clansConfig.getBoolean("general.developer-debug-mode.enabled")) {
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewChatSpyToggledEvent"))
            }
            false
        }
    }

    fun hasEnoughPoints(player: Player, points: Int): Boolean {
        val uuid: UUID = player.getUniqueId()
        val crewPlayer: CrewPlayer? = usermap[uuid]
        return if (crewPlayer.getPointBalance() >= points) {
            true
        } else false
    }

    fun getPointBalanceByBukkitPlayer(player: Player): Int {
        val uuid: UUID = player.getUniqueId()
        val crewPlayer: CrewPlayer? = usermap[uuid]
        return crewPlayer.getPointBalance()
    }

    fun getPointBalanceByBukkitOfflinePlayer(offlinePlayer: OfflinePlayer): Int {
        val uuid: UUID = offlinePlayer.getUniqueId()
        val crewPlayer: CrewPlayer? = usermap[uuid]
        return crewPlayer.getPointBalance()
    }

    fun addPointsToOnlinePlayer(player: Player, value: Int) {
        val uuid: UUID = player.getUniqueId()
        val crewPlayer: CrewPlayer? = usermap[uuid]
        val currentPointBalance: Int = crewPlayer.getPointBalance()
        crewPlayer.setPointBalance(currentPointBalance + value)
        usermap.replace(uuid, crewPlayer)
    }

    fun addPointsToOfflinePlayer(offlinePlayer: OfflinePlayer, value: Int) {
        val uuid: UUID = offlinePlayer.getUniqueId()
        val crewPlayer: CrewPlayer? = usermap[uuid]
        val currentPointBalance: Int = crewPlayer.getPointBalance()
        crewPlayer.setPointBalance(currentPointBalance + value)
        usermap.replace(uuid, crewPlayer)
    }

    fun withdrawPoints(player: Player, points: Int): Boolean {
        val uuid: UUID = player.getUniqueId()
        val crewPlayer: CrewPlayer? = usermap[uuid]
        val currentPointValue: Int = crewPlayer.getPointBalance()
        if (currentPointValue != 0) {
            if (hasEnoughPoints(player, points)) {
                crewPlayer.setPointBalance(currentPointValue - points)
                return true
            }
            return false
        }
        return false
    }

    fun resetOnlinePlayerPointBalance(player: Player) {
        val uuid: UUID = player.getUniqueId()
        val crewPlayer: CrewPlayer? = usermap[uuid]
        crewPlayer.setPointBalance(0)
        usermap.replace(uuid, crewPlayer)
    }

    fun resetOfflinePlayerPointBalance(offlinePlayer: OfflinePlayer) {
        val uuid: UUID = offlinePlayer.getUniqueId()
        val crewPlayer: CrewPlayer? = usermap[uuid]
        crewPlayer.setPointBalance(0)
        usermap.replace(uuid, crewPlayer)
    }

    val rawUsermapList: Set<Any>
        get() = usermap.keySet()
    val allPlayerPointsValues: List<String>
        get() {
            val pointValues: List<String> = ArrayList()
            for (crewPlayer in usermap.values()) {
                val value: String = String.valueOf(crewPlayer.getPointBalance())
                pointValues.add(value)
            }
            return pointValues
        }

    fun getUsermap(): Map<UUID, CrewPlayer> {
        return usermap
    }

    private fun fireClanChatSpyToggledEvent(player: Player, crewPlayer: CrewPlayer?, chatSpyToggledState: Boolean) {
        val crewChatSpyToggledEvent = CrewChatSpyToggledEvent(player, crewPlayer, chatSpyToggledState)
        Bukkit.getPluginManager().callEvent(crewChatSpyToggledEvent)
    }
}
