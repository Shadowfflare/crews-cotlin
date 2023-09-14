package xyz.shadowflare.crews.api

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CrewChatMessageSendEvent(
    createdBy: Player,
    crew: Crew,
    prefix: String,
    message: String,
    recipients: ArrayList<String>
) : Event() {
    private val createdBy: Player
    private val crew: Crew
    val prefix: String
    val message: String
    val recipients: ArrayList<String>

    init {
        this.createdBy = createdBy
        this.crew = crew
        this.prefix = prefix
        this.message = message
        this.recipients = recipients
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

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
        val handlerList: HandlerList
            get() = HANDLERS
    }
}
