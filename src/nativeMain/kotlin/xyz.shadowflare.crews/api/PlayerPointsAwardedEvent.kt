package xyz.shadowflare.crews.api

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerPointsAwardedEvent(
    createdBy: Player,
    killer: Player,
    victim: Player,
    @Nullable killerCrew: Crew?,
    @Nullable victimCrew: Crew?,
    pointValue: Int,
    isEnemyPointReward: Boolean
) : Event() {
    private val createdBy: Player
    private val killer: Player
    private val victim: Player
    private val killerCrew: Crew
    private val victimCrew: Crew
    val pointValue: Int
    val isEnemyPointReward: Boolean

    init {
        this.createdBy = createdBy
        this.killer = killer
        this.victim = victim
        killerCrew = killerCrew
        victimCrew = victimCrew
        this.pointValue = pointValue
        this.isEnemyPointReward = isEnemyPointReward
    }

    @get:Override
    val handlers: HandlerList
        get() = HANDLERS

    fun getCreatedBy(): Player {
        return createdBy
    }

    fun getKiller(): Player {
        return killer
    }

    fun getVictim(): Player {
        return victim
    }

    @Nullable
    fun getKillerCrew(): Crew {
        return killerCrew
    }

    @get:Nullable
    val victimClan: Crew
        get() = victimCrew

    companion object {
        private val HANDLERS: HandlerList = HandlerList()
    }
}
