package xyz.shadowflare.crew.api

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import xyz.shadowflare.crews.models.Clan

class CrewAllyRemoveEvent(createdBy: Player, crew: Crew, exAllyCrew: Crew, exAllyCrewCreatedBy: Player) : Event() {
    private val createdBy: Player
    private val crew: Crew
    val exAllyClanCreatedBy: Player?
        get() = exAllyCrewCreatedBy
    private val exAllyCrew: Crew

    init {
        this.createdBy = createdBy
        this.crew = crew
        exAllyCrewCreatedBy = exAllyCrewCreatedBy
        this.exAllyCrew = exAllyCrew
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

    val exAllyClan: Clan
        get() = exAllyCrew

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
        val handlerList: HandlerList
            get() = HANDLERS
    }
}
