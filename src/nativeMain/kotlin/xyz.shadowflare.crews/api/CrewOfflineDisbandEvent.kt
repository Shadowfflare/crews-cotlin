package xyz.shadowflare.crews.api

import org.bukkit.OfflinePlayer
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CrewOfflineDisbandEvent(createdBy: OfflinePlayer, crewName: String) : Event() {
    private val createdBy: OfflinePlayer
    val crew: String

    init {
        this.createdBy = createdBy
        crew = crewName
    }

    @get:Override
    val handlers: HandlerList
        get() = HANDLERS

    fun getCreatedBy(): OfflinePlayer {
        return createdBy
    }

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
        val handlerList: HandlerList
            get() = HANDLERS
    }
}
