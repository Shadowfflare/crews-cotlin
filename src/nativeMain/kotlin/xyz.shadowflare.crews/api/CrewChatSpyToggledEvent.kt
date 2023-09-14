package xyz.shadowflare.crews.api

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CrewChatSpyToggledEvent : Event() {
    private val createdBy: Player? = null
    private val crewPlayer: CrewPlayer? = null
    var isCrewChatSpyState = false
    fun ClanChatSpyToggledEvent(createdBy: Player?, crewPlayer: CrewPlayer?, crewChatSpyState: Boolean) {
        this.createdBy = createdBy
        this.crewPlayer = crewPlayer
        isCrewChatSpyState = crewChatSpyState
    }

    @get:Override
    val handlers: HandlerList
        get() = HANDLERS

    fun getCreatedBy(): Player? {
        return createdBy
    }

    fun getCrewPlayer(): CrewPlayer? {
        return crewPlayer
    }

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
        val handlerList: HandlerList
            get() = HANDLERS
    }
}
