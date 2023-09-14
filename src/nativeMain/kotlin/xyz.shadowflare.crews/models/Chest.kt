package xyz.shadowflare.crews.models

import org.bukkit.Location

class Chest(owningCrew: Crew, location: Location) {
    var uUID: String
    var chestWorldName: String
    var chestLocationX: Double
    var chestLocationY: Double
    var chestLocationZ: Double
    var playersWithAccess: ArrayList<String?> = ArrayList()

    init {
        uUID = java.util.UUID.randomUUID().toString()
        chestWorldName = location.getWorld().getName()
        chestLocationX = location.getX()
        chestLocationY = location.getY()
        chestLocationZ = location.getZ()
        val crewOwner = owningCrew.crewOwner
        val crewMembers: List<String>? = owningCrew.crewMembers
        playersWithAccess.add(crewOwner)
        playersWithAccess.addAll(crewMembers!!)
    }
}
