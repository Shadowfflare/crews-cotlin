package xyz.shadowflare.crews.models

import kotlin.jvm.JvmField

class Crew {
    val crewOwner: String? = null
    var crewFinalName: String? = null
    var crewPrefix: String? = null
    @JvmField
    var crewMembers: ArrayList<String>? = null
    var crewAllies: ArrayList<String>? = null
    var crewEnemies: ArrayList<String>? = null
    var isFriendlyFireAllowed = false
    var crewPoints = 0
        private set
    private var crewHomeWorld: String? = null
    var crewHomeX = 0.0
    var crewHomeY = 0.0
    var crewHomeZ = 0.0
    var crewHomeYaw = 0f
    var crewHomePitch = 0f
    var maxAllowedProtectedChests = 0
    var protectedChests: HashMap<String, xyz.shadowflare.crews.models.Chest> = HashMap()
    fun Clan(crewOwner: String?, crewName: String?) {
        this.crewOwner = crewOwner
        crewFinalName = crewName
        crewPrefix = crewFinalName
        crewMembers = ArrayList()
        crewAllies = ArrayList()
        crewEnemies = ArrayList()
        isFriendlyFireAllowed = true
        crewPoints = 0
        crewHomeWorld = null
        maxAllowedProtectedChests = 0
    }

    fun addCrewMember(crewMember: String) {
        crewMembers!!.add(crewMember)
    }

    fun removeCrewMember(crewMember: String): Boolean {
        return crewMembers!!.remove(crewMember)
    }

    fun addCrewAlly(ally: String) {
        crewAllies!!.add(ally)
    }

    fun removeCrewAlly(allyUUID: String) {
        crewAllies!!.remove(allyUUID)
    }

    fun addCrewEnemy(enemy: String) {
        crewEnemies!!.add(enemy)
    }

    fun removeCrewEnemy(enemyUUID: String) {
        crewEnemies!!.remove(enemyUUID)
    }

    fun setClanPoints(crewPoints: Int) {
        this.crewPoints = crewPoints
    }

    fun getCrewHomeWorld(): String? {
        return crewHomeWorld
    }

    fun setCrewHomeWorld(clanHomeWorld: String?) {
        crewHomeWorld = crewHomeWorld
    }
}
