package xyz.shadowflare.crews.api

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import xyz.shadowflare.crews.models.Clan

class CrewAllyAddEvent(createdBy: Player, clan: Clan, allyClan: Clan, allyClanCreatedBy: Player) : Event() {
    private val createdBy: Player
    private val crew: Clan
    private val allyCrewCreatedBy: Player
    private val allyCrew: Clan

    init {
        this.createdBy = createdBy
        crew = clan
        allyCrewCreatedBy = allyClanCreatedBy
        allyCrew = allyClan
    }

    @get:Override
    val handlers: HandlerList
        get() = HANDLERS

    fun getCreatedBy(): Player {
        return createdBy
    }

    val clan: Clan
        get() = crew
    val allyClanCreatedBy: Player
        get() = allyCrewCreatedBy
    val allyClan: Clan
        get() = allyCrew

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
        val handlerList: HandlerList
            get() = HANDLERS
    }
}
