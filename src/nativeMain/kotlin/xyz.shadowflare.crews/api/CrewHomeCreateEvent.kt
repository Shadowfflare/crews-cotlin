package xyz.shadowflare.Crews.api

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CrewHomeCreateEvent(createdBy: Player, crew: Crew, homeLocation: Location) : Event() {
    private val createdBy: Player
    private val crew: Crew
    private val homeLocation: Location

    init {
        this.createdBy = createdBy
        this.crew = crew
        this.homeLocation = homeLocation
    }

    @get:Override
    val handlers: HandlerList
        get() = HANDLERS

    fun getCreatedBy(): Player {
        return createdBy
    }

    fun getCrew(): Crew {
        return crew
    }

    fun getHomeLocation(): Location {
        return homeLocation
    }

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
    }
}
