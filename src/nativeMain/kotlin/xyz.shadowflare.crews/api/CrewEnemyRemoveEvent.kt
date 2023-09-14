package xyz.shadowflare.crews.api

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CrewEnemyRemoveEvent(createdBy: Player, crew: Crew, exEnemyCrew: Crew, exEnemyCrewCreatedBy: Player) : Event() {
    private val createdBy: Player
    private val crew: Crew
    private val exEnemyCrewCreatedBy: Player
    private val exEnemyCrew: Crew

    init {
        this.createdBy = createdBy
        this.crew = crew
        this.exEnemyCrewCreatedBy = exEnemyCrewCreatedBy
        this.exEnemyCrew = exEnemyCrew
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

    fun getExEnemyCrewCreatedBy(): Player {
        return exEnemyCrewCreatedBy
    }

    fun getExEnemyCrew(): Crew {
        return exEnemyCrew
    }

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
        val handlerList: HandlerList
            get() = HANDLERS
    }
}
