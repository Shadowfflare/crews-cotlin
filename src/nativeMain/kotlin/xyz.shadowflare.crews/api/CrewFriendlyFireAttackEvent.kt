package xyz.shadowflare.crews.api

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CrewFriendlyFireAttackEvent(
    createdBy: Player,
    attackingPlayer: Player,
    victimPlayer: Player,
    attackingCrew: Crew,
    victimCrew: Crew
) : Event() {
    private val createdBy: Player
    private val attackingPlayer: Player
    private val victimPlayer: Player
    private val attackingCrew: Crew
    private val victimCrew: Crew

    init {
        this.createdBy = createdBy
        this.attackingPlayer = attackingPlayer
        this.victimPlayer = victimPlayer
        this.attackingCrew = attackingCrew
        this.victimCrew = victimCrew
    }

    @get:Override
    val handlers: HandlerList
        get() = HANDLERS

    fun getCreatedBy(): Player {
        return createdBy
    }

    fun getAttackingPlayer(): Player {
        return attackingPlayer
    }

    fun getVictimPlayer(): Player {
        return victimPlayer
    }

    fun getAttackingCrew(): Crew {
        return attackingCrew
    }

    val victimClan: Crew
        get() = victimCrew

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
    }
}
