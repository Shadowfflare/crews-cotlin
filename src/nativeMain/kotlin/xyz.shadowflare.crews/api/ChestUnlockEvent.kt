package xyz.shadowflare.crews.api

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class ChestUnlockEvent(createdBy: Player, removedLockLocation: Location) : Event() {
    private val createdBy: Player
    private val removedLockLocation: Location

    init {
        this.createdBy = createdBy
        this.removedLockLocation = removedLockLocation
    }

    @get:Override
    val handlers: HandlerList
        get() = HANDLERS

    fun getCreatedBy(): Player {
        return createdBy
    }

    fun getRemovedLockLocation(): Location {
        return removedLockLocation
    }

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
    }
}
