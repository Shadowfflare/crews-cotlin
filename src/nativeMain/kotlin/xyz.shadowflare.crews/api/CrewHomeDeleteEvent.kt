package xyz.shadowflare.crews.api

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import xyz.shadowflare.crews.models.Clan

class CrewHomeDeleteEvent(createdBy: Player, crew: Crew) : Event() {
    private val createdBy: Player
    private val crew: Crew

    init {
        this.createdBy = createdBy
        this.crew = crew
    }

    @get:Override
    val handlers: HandlerList
        get() = HANDLERS

    fun getCreatedBy(): Player {
        return createdBy
    }

    val clan: Clan
        get() = crew

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
    }
}
