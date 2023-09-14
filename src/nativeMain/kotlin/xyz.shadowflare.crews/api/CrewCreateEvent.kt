package xyz.shadowflare.crews.api

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CrewCreateEvent : Event() {
    private val createdBy: Player? = null
    private var crew: Crew? = null
    fun ClanCreateEvent(createdBy: Player?, crewName: Crew?) {
        this.createdBy = createdBy
        crew = crewName
    }

    @get:Override
    val handlers: HandlerList
        get() = HANDLERS

    fun getCreatedBy(): Player? {
        return createdBy
    }

    fun getCrew(): Crew? {
        return crew
    }

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
        val handlerList: HandlerList
            get() = HANDLERS
    }
}
