package xyz.shadowflare.crews.api

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CrewHomeTeleportEvent(
    isAsync: Boolean,
    createdBy: Player,
    crew: Crew,
    homeLocation: Location,
    tpFromLocation: Location
) : Event(isAsync) {
    private val createdBy: Player
    private val crew: Crew
    private val homeLocation: Location
    private val tpFromLocation: Location

    init {
        this.createdBy = createdBy
        this.crew = crew
        this.homeLocation = homeLocation
        this.tpFromLocation = tpFromLocation
    }

    @Deprecated
    fun ClanHomeTeleportEvent(createdBy: Player, crew: Crew, homeLocation: Location, tpFromLocation: Location) {
        this.createdBy = createdBy
        this.crew = crew
        this.homeLocation = homeLocation
        this.tpFromLocation = tpFromLocation
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

    fun getTpFromLocation(): Location {
        return tpFromLocation
    }

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
    }
}
