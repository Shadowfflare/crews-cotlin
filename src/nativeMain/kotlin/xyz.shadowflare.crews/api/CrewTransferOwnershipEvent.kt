package xyz.shadowflare.crews.api

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import xyz.shadowflare.crews.models.Clan

class CrewTransferOwnershipEvent(createdBy: Player, originalCrewOwner: Player, newCrewOwner: Player, newCrew: Clan) :
    Event() {
    private val createdBy: Player
    private val originalCrewOwner: Player
    private val newCrewOwner: Player
    private val newCrew: Crew

    init {
        this.createdBy = createdBy
        this.originalCrewOwner = originalCrewOwner
        this.newCrewOwner = newCrewOwner
        this.newCrew = newCrew
    }

    @get:Override
    val handlers: HandlerList
        get() = HANDLERS

    fun getCreatedBy(): Player {
        return createdBy
    }

    fun getOriginalCrewOwner(): Player {
        return originalCrewOwner
    }

    fun getNewCrewOwner(): Player {
        return newCrewOwner
    }

    fun getNewCrew(): Crew {
        return newCrew
    }

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
        val handlerList: HandlerList
            get() = HANDLERS
    }
}
