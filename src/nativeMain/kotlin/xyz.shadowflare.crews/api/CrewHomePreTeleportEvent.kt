package xyz.shadowflare.crews.api

import org.bukkit.entity.Player

class CrewHomePreTeleportEvent(createdBy: Player, crew: Crew) : Event(), Cancellable {
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

    fun getCrew(): Clan {
        return crew
    }

    @get:Override
    @set:Override
    var isCancelled: Boolean
        get() = false
        set(b) {}

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
    }
}
