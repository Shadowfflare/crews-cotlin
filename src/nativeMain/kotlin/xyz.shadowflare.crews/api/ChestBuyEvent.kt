package xyz.shadowflare.crews.api

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class ChestBuyEvent(createdBy: Player, owningCrew: Crew, oldCrewMaxAllowedChests: Int, newChestCount: Int) : Event() {
    private val createdBy: Player
    private val owningCrew: Crew
    val oldClanMaxAllowedChests: Int
    val newChestCount: Int

    init {
        this.createdBy = createdBy
        this.owningCrew = owningCrew
        oldClanMaxAllowedChests = oldCrewMaxAllowedChests
        this.newChestCount = newChestCount
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

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
    }
}
