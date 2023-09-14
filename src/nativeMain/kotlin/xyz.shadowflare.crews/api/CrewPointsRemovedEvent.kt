package xyz.shadowflare.crews.api

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CrewPointsRemovedEvent(
    createdBy: Player, playerCrew: Crew,
    previousCrewPlayerPointBalance: Int, newCrewPlayerPointBalance: Int,
    withdrawPointValue: Int, previousClanPointBalance: Int, newCrewPointBalance: Int
) : Event() {
    private val createdBy: Player
    private val playerCrew: Crew
    private val crewPlayer: CrewPlayer
    val previousCrewPlayerPointBalance: Int
    val newCrewPlayerPointBalance: Int
    val withdrawPointValue: Int
    val previousCrewPointBalance: Int
    val newCrewPointBalance: Int

    init {
        this.createdBy = createdBy
        this.playerCrew = playerCrew
        crewPlayer = CrewPlayer
        this.previousCrewPlayerPointBalance = previousCrewPlayerPointBalance
        this.newCrewPlayerPointBalance = newCrewPlayerPointBalance
        this.withdrawPointValue = withdrawPointValue
        previousCrewPointBalance = previousClanPointBalance
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
