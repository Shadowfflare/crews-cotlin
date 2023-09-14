package xyz.shadowflare.crews.menuSystem

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class PlayerMenuUtility(p: Player) {
    private val owner: Player
    var offlineCrewOwner: OfflinePlayer? = null
    var offlineCrewMember: OfflinePlayer? = null

    init {
        owner = p
    }

    fun getOwner(): Player {
        return owner
    }

    fun getOfflineCrewOwner(): OfflinePlayer? {
        return offlineCrewOwner
    }

    fun getOfflineCrewMember(): OfflinePlayer? {
        return offlineCrewMember
    }

    fun setOfflineCrewOwner(offlineCrewOwner: OfflinePlayer?) {
        this.offlineCrewOwner = offlineCrewOwner
    }

    fun setOfflineCrewMember(offlineCrewMember: OfflinePlayer?) {
        this.offlineCrewMember = offlineCrewMember
    }
}
