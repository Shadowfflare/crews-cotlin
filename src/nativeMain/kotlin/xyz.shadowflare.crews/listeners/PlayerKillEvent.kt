package xyz.shadowflare.crews.listeners

import org.bukkit.Bukkit

class PlayerKillEvent : Listener {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    private val nonEnemyPointValue: Integer =
        crewsConfig.getInt("points.player-points.non-enemy-crew-point-amount-on-kill")
    private val enemyPointValue: Integer = crewsConfig.getInt("points.player-points.enemy-crew-point-amount-on-kill")
    @EventHandler
    fun onPlayerDeath(event: EntityDamageByEntityEvent) {
        if (!crewsConfig.getBoolean("points.player-points.enabled")) {
            return
        }
        if (event.getDamager() is Player) {
            if (event.getEntity() is Player) {
                if (victim.getLastDamage() >= victim.getHealth()) {
                    if (CrewsStorageUtil.findClanByPlayer(killer) == null || CrewsStorageUtil.findClanByPlayer(victim) == null) {
                        killer.sendMessage(
                            ColorUtils.translateColorCodes(
                                messagesConfig.getString("player-points-killer-non-enemy-received-success")
                                    .replace("%PLAYER%", victim.getName())
                                    .replace("%POINTVALUE%", nonEnemyPointValue.toString())
                            )
                        )
                        if (crewsConfig.getBoolean("points.player-points.take-points-from-victim")) {
                            if (UsermapStorageUtil.withdrawPoints(victim, nonEnemyPointValue)) {
                                victim.sendMessage(
                                    ColorUtils.translateColorCodes(
                                        messagesConfig.getString("player-points-victim-non-enemy-withdrawn-success")
                                            .replace("%KILLER%", killer.getName())
                                            .replace("%POINTVALUE%", nonEnemyPointValue.toString())
                                    )
                                )
                            } else {
                                victim.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-victim-withdraw-failed")))
                                console.sendMessage(
                                    ColorUtils.translateColorCodes(
                                        messagesConfig.getString("player-points-console-victim-point-withdraw-failed")
                                            .replace("%VICTIM%", victim.getName())
                                    )
                                )
                            }
                        }
                        UsermapStorageUtil.addPointsToOnlinePlayer(killer, nonEnemyPointValue)
                    } else if (CrewsStorageUtil.findCrewByOwner(killer) != null || CrewsStorageUtil.findCrewByOwner(
                            victim
                        ) != null
                    ) {
                        val killerCrewOwner: Crew = CrewsStorageUtil.findCrewByOwner(killer)
                        val victimCrewOwner: Crew = CrewsStorageUtil.findCrewByOwner(victim)
                        if (killerCrewOwner.crewEnemies.contains(victimCrewOwner.crewOwner) || victimCrewOwner.crewEnemies.contains(
                                killerCrewOwner.crewOwner
                            )
                        ) {
                            killer.sendMessage(
                                ColorUtils.translateColorCodes(
                                    messagesConfig.getString("player-points-killer-enemy-received-success")
                                        .replace("%PLAYER%", victim.getName())
                                        .replace("%ENEMYPOINTVALUE%", enemyPointValue.toString())
                                )
                            )
                            if (crewsConfig.getBoolean("points.player-points.take-points-from-victim")) {
                                if (UsermapStorageUtil.withdrawPoints(victim, enemyPointValue)) {
                                    victim.sendMessage(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("player-points-victim-enemy-withdrawn-success")
                                                .replace("%KILLER%", killer.getName())
                                                .replace("%ENEMYPOINTVALUE%", enemyPointValue.toString())
                                        )
                                    )
                                } else {
                                    victim.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-victim-withdraw-failed")))
                                    console.sendMessage(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("player-points-console-victim-point-withdraw-failed")
                                                .replace("%VICTIM%", victim.getName())
                                        )
                                    )
                                }
                            }
                            UsermapStorageUtil.addPointsToOnlinePlayer(killer, enemyPointValue)
                            firePlayerPointsAwardedEvent(
                                killer,
                                killer,
                                victim,
                                killerCrewOwner,
                                victimCrewOwner,
                                enemyPointValue,
                                true
                            )
                            if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired PlayerPointsAwardedEvent"))
                            }
                        } else {
                            killer.sendMessage(
                                ColorUtils.translateColorCodes(
                                    messagesConfig.getString("player-points-killer-non-enemy-received-success")
                                        .replace("%PLAYER%", victim.getName())
                                        .replace("%POINTVALUE%", nonEnemyPointValue.toString())
                                )
                            )
                            if (crewsConfig.getBoolean("points.player-points.take-points-from-victim")) {
                                if (UsermapStorageUtil.withdrawPoints(victim, nonEnemyPointValue)) {
                                    victim.sendMessage(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("player-points-victim-non-enemy-withdrawn-success")
                                                .replace("%KILLER%", killer.getName())
                                                .replace("%POINTVALUE%", nonEnemyPointValue.toString())
                                        )
                                    )
                                } else {
                                    victim.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-victim-withdraw-failed")))
                                    console.sendMessage(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("player-points-console-victim-point-withdraw-failed")
                                                .replace("%VICTIM%", victim.getName())
                                        )
                                    )
                                }
                            }
                            UsermapStorageUtil.addPointsToOnlinePlayer(killer, nonEnemyPointValue)
                            firePlayerPointsAwardedEvent(
                                killer,
                                killer,
                                victim,
                                killerCrewOwner,
                                victimCrewOwner,
                                nonEnemyPointValue,
                                false
                            )
                            if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired PlayerPointsAwardedEvent"))
                            }
                        }
                    } else {
                        if (CrewsStorageUtil.findClanByPlayer(killer) != null || CrewsStorageUtil.findClanByPlayer(
                                victim
                            ) != null
                        ) {
                            val killerCrew: Crew = CrewsStorageUtil.findCrewByPlayer(killer)
                            val victimCrew: Clan = CrewsStorageUtil.findCrewByPlayer(victim)
                            if (killercrew.getCrewEnemies() != null && !killerCrew.crewEnemies.isEmpty() || victimCrew.getCrewEnemies() != null && !victimCrew.getCrewEnemies()
                                    .isEmpty()
                            ) {
                                if (killerCrew.crewEnemies.contains(victimCrew.getCrewOwner()) || victimCrew.getCrewEnemies()
                                        .contains(killerCrew.crewOwner)
                                ) {
                                    killer.sendMessage(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("player-points-killer-enemy-received-success")
                                                .replace("%PLAYER%", victim.getName())
                                                .replace("%ENEMYPOINTVALUE%", enemyPointValue.toString())
                                        )
                                    )
                                    if (crewsConfig.getBoolean("points.player-points.take-points-from-victim")) {
                                        if (UsermapStorageUtil.withdrawPoints(victim, enemyPointValue)) {
                                            victim.sendMessage(
                                                ColorUtils.translateColorCodes(
                                                    messagesConfig.getString("player-points-victim-enemy-withdrawn-success")
                                                        .replace("%KILLER%", killer.getName())
                                                        .replace("%ENEMYPOINTVALUE%", enemyPointValue.toString())
                                                )
                                            )
                                        } else {
                                            victim.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-victim-withdraw-failed")))
                                            console.sendMessage(
                                                ColorUtils.translateColorCodes(
                                                    messagesConfig.getString("player-points-console-victim-point-withdraw-failed")
                                                        .replace("%VICTIM%", victim.getName())
                                                )
                                            )
                                        }
                                    }
                                    UsermapStorageUtil.addPointsToOnlinePlayer(killer, enemyPointValue)
                                    firePlayerPointsAwardedEvent(
                                        killer,
                                        killer,
                                        victim,
                                        killerCrew,
                                        victimCrew,
                                        enemyPointValue,
                                        true
                                    )
                                    if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                        console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite-Debug: &aFired PlayerPointsAwardedEvent"))
                                    }
                                } else {
                                    killer.sendMessage(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("player-points-killer-non-enemy-received-success")
                                                .replace("%PLAYER%", victim.getName())
                                                .replace("%POINTVALUE%", nonEnemyPointValue.toString())
                                        )
                                    )
                                    if (crewsConfig.getBoolean("points.player-points.take-points-from-victim")) {
                                        if (UsermapStorageUtil.withdrawPoints(victim, nonEnemyPointValue)) {
                                            victim.sendMessage(
                                                ColorUtils.translateColorCodes(
                                                    messagesConfig.getString("player-points-victim-non-enemy-withdrawn-success")
                                                        .replace("%KILLER%", killer.getName())
                                                        .replace("%POINTVALUE%", nonEnemyPointValue.toString())
                                                )
                                            )
                                        } else {
                                            victim.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-points-victim-withdraw-failed")))
                                            console.sendMessage(
                                                ColorUtils.translateColorCodes(
                                                    messagesConfig.getString("player-points-console-victim-point-withdraw-failed")
                                                        .replace("%VICTIM%", victim.getName())
                                                )
                                            )
                                        }
                                    }
                                    UsermapStorageUtil.addPointsToOnlinePlayer(killer, nonEnemyPointValue)
                                    firePlayerPointsAwardedEvent(
                                        killer,
                                        killer,
                                        victim,
                                        killerCrew,
                                        victimCrew,
                                        nonEnemyPointValue,
                                        false
                                    )
                                    if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                        console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired PlayerPointsAwardedEvent"))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private fun firePlayerPointsAwardedEvent(
            createdBy: Player,
            killer: Player,
            victim: Player,
            killerCrew: Crew,
            victimCrew: Crew,
            pointValue: Int,
            isEnemyPointReward: Boolean
        ) {
            val playerPointsAwardedEvent = PlayerPointsAwardedEvent(
                createdBy,
                killer,
                victim,
                killerCrew,
                victimCrew,
                pointValue,
                isEnemyPointReward
            )
            Bukkit.getPluginManager().callEvent(playerPointsAwardedEvent)
        }
    }
}
