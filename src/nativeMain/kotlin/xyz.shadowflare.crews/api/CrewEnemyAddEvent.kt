package xyz.shadowflare.crews.api

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CrewEnemyAddEvent(createdBy: Player, crew: Crew, enemyCrew: Crew, enemyCrewCreatedBy: Player) : Event() {
    private val createdBy: Player
    private val crew: Crew
    private val enemyCrewCreatedBy: Player
    private val enemyCrew: Crew

    init {
        this.createdBy = createdBy
        this.crew = crew
        this.enemyCrewCreatedBy = enemyCrewCreatedBy
        this.enemyCrew = enemyCrew
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

    fun getEnemyCrewCreatedBy(): Player {
        return enemyCrewCreatedBy
    }

    fun getEnemyCrew(): Crew {
        return enemyCrew
    }

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
        val handlerList: HandlerList
            get() = HANDLERS
    }
}
