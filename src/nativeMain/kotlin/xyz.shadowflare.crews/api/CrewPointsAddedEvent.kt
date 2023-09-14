package xyz.shadowflare.crews.api

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CrewPointsAddedEvent(
    createdBy: Player, playerCrew: Crew, crewPlayer: CrewPlayer,
    previousCrewPlayerPointBalance: Int, newCrewPlayerPointBalance: Int,
    depositPointValue: Int, previousCrewPointBalance: Int, newCrewPointBalance: Int
) : Event() {
    private val createdBy: Player
    private val playerCrew: Crew
    private val crewPlayer: CrewPlayer
    val previousCrewPlayerPointBalance: Int
    val newCrewPlayerPointBalance: Int
    val depositPointValue: Int
    val previousCrewPointBalance: Int
    val newCrewPointBalance: Int

    init {
        this.createdBy = createdBy
        this.playerCrew = playerCrew
        this.crewPlayer = crewPlayer
        this.previousCrewPlayerPointBalance = previousCrewPlayerPointBalance
        this.newCrewPlayerPointBalance = newCrewPlayerPointBalance
        this.depositPointValue = depositPointValue
        this.previousCrewPointBalance = previousCrewPointBalance
        this.newCrewPointBalance = newCrewPointBalance
    }

    @get:Override
    val handlers: HandlerList
        get() = HANDLERS

    fun getCreatedBy(): Player {
        return createdBy
    }

    fun getPlayerCrew(): Crew {
        return playerCrew
    }

    fun getCrewPlayer(): CrewPlayer {
        return crewPlayer
    }

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
    }
}
