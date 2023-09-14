package xyz.shadowflare.crews.utils

import org.bukkit.Bukkit

object CrewsInviteUtil {
    private val console: ConsoleCommandSender = Bukkit.getConsoleSender()
    private val clansConfig: FileConfiguration = Crews.getPlugin().getConfig()
    private val invitesList: Map<UUID, CrewInvite> = HashMap()
    fun createInvite(inviterUUID: String?, inviteeUUID: String?): CrewInvite? {
        val uuid: UUID = UUID.fromString(inviterUUID)
        clearExpiredInvites()
        return if (!invitesList.containsKey(uuid)) {
            invitesList.put(uuid, CrewInvite(inviterUUID, inviteeUUID))
            if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                console.sendMessage(
                    xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes(
                        "&6Crews-debug: &aCrew invite created"
                    )
                )
            }
            invitesList[uuid]
        } else {
            null
        }
    }

    fun searchInvitee(inviteeUUID: String?): Boolean {
        for (invite in invitesList.values()) {
            if (invite.getInvitee().equals(inviteeUUID)) {
                return true
            }
        }
        return false
    }

    fun clearExpiredInvites() {
        val expiryTime = 25 * 1000
        val currentTime = Date()
        for (crewInvite in invitesList.values()) {
            if (currentTime.getTime() - crewInvite.getInviteTime().getTime() > expiryTime) {
                invitesList.remove(crewInvite)
                if (clansConfig.getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6ClansLite-Debug: &aExpired clan invites removed"))
                }
            }
        }
    }

    @Throws(UnsupportedOperationException::class)
    fun emptyInviteList() {
        invitesList.clear()
    }

    fun removeInvite(inviterUUID: String?) {
        val uuid: UUID = UUID.fromString(inviterUUID)
        invitesList.remove(uuid)
    }

    val invites: Set<Map.Entry<Any, Any>>
        get() = invitesList.entrySet()

    @Deprecated
    fun getInviteOwner(inviterUUID: String): Player? {
        if (inviterUUID.length() > 36) {
            val uuid: UUID = UUID.fromString(inviterUUID)
            if (invitesList.containsKey(uuid)) {
                if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(
                        xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes(
                            "&6Crews-Debug: &aInvite owner uuid: &d$inviterUUID"
                        )
                    )
                }
                return Bukkit.getPlayer(uuid)
            }
        } else {
            console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews: &4An error occurred whilst getting an Invite Owner."))
            console.sendMessage(xyz.shadowflare.crews.utils.ColorUtils.translateColorCodes("&6Crews: &4Error: &3The provided UUID is too long."))
        }
        return null
    }
}
