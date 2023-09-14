package xyz.shadowflare.crews.api

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import xyz.shadowflare.crews.models.Chest

class ChestLockEvent(createdBy: Player, crew: Crew, chest: Chest) : Event() {
    private val createdBy: Player
    private val owningCrew: Crew
    private val chest: Chest
    private val chestLocation: Location

    init {
        this.createdBy = createdBy
        owningCrew = crew
        this.chest = chest
        chestLocation = Location(
            Bukkit.getWorld(chest.getChestWorldName()),
            chest.getChestLocationX(),
            chest.getChestLocationY(),
            chest.getChestLocationZ()
        )
    }

    @get:Override
    val handlers: HandlerList
        get() = HANDLERS

    fun getCreatedBy(): Player {
        return createdBy
    }

    fun getOwningCrew(): Crew {
        return owningCrew
    }

    fun getChest(): Chest {
        return chest
    }

    fun getChestLocation(): Location {
        return chestLocation
    }

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
    }
}
