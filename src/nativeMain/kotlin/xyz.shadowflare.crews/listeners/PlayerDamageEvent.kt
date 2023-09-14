package xyz.shadowflare.crews.listeners

import org.bukkit.Bukkit

class PlayerDamageEvent : Listener {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerHit(event: EntityDamageByEntityEvent) {
        if (event.getEntity() is Player) {
            val hurtUUID: String = hurtPlayer.getUniqueId().toString()
            if (event.getDamager() is Player) {
                attackingPlayer.setInvulnerable(false)
                val attackingCrew: Crew = CrewsStorageUtil.findCrewByOwner(attackingPlayer)
                val victimCrew: Crew = CrewsStorageUtil.findCrewByOwner(hurtPlayer)
                if (attackingCrew != null) {
                    val attackingCrewMembers: ArrayList<String> = attackingCrew.getCrewMembers()
                    if (attackingCrewMembers.contains(hurtUUID) || attackingCrew.getCrewOwner().equals(hurtUUID)) {
                        if (crewsConfig.getBoolean("protections.pvp.pvp-command-enabled")) {
                            if (!attackingCrew.isFriendlyFireAllowed()) {
                                if (crewsConfig.getBoolean("protections.pvp.enable-bypass-permission")) {
                                    if (attackingPlayer.hasPermission("crews.bypass.pvp")
                                        || attackingPlayer.hasPermission("crews.bypass.*")
                                        || attackingPlayer.hasPermission("crews.bypass")
                                        || attackingPlayer.hasPermission("crews.*")
                                        || attackingPlayer.isOp()
                                    ) {
                                        return
                                    }
                                }
                                event.setCancelled(true)
                                fireClanFriendlyFireAttackEvent(
                                    hurtPlayer,
                                    attackingPlayer,
                                    hurtPlayer,
                                    attackingCrew,
                                    victimCrew
                                )
                                if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired ClanFriendlyFireAttackEvent"))
                                }
                                attackingPlayer.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("friendly-fire-is-disabled")))
                            }
                        } else {
                            event.setCancelled(false)
                        }
                    }
                } else {
                    val attackingCrewByPlayer: Crew = CrewsStorageUtil.findCrewByPlayer(attackingPlayer)
                    val victimCrewByPlayer: Crew = CrewsStorageUtil.findCrewByPlayer(hurtPlayer)
                    if (attackingCrewByPlayer != null) {
                        val attackingMembers: ArrayList<String> = attackingCrewByPlayer.getCrewMembers()
                        if (attackingMembers.contains(hurtUUID) || attackingCrewByPlayer.getCrewOwner()
                                .equals(hurtUUID)
                        ) {
                            if (crewsConfig.getBoolean("protections.pvp.pvp-command-enabled")) {
                                if (!attackingCrewByPlayer.isFriendlyFireAllowed()) {
                                    if (crewsConfig.getBoolean("protections.pvp.enable-bypass-permission")) {
                                        if (attackingPlayer.hasPermission("crews.bypass.pvp")
                                            || attackingPlayer.hasPermission("crews.bypass.*")
                                            || attackingPlayer.hasPermission("crews.bypass")
                                            || attackingPlayer.hasPermission("crews.*")
                                            || attackingPlayer.isOp()
                                        ) {
                                            return
                                        }
                                    }
                                    event.setCancelled(true)
                                    fireClanFriendlyFireAttackEvent(
                                        hurtPlayer,
                                        attackingPlayer,
                                        hurtPlayer,
                                        attackingCrewByPlayer,
                                        victimCrewByPlayer
                                    )
                                    if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                        console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewFriendlyFireAttackEvent"))
                                    }
                                    attackingPlayer.sendMessage(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString(
                                                "friendly-fire-is-disabled"
                                            )
                                        )
                                    )
                                }
                            } else {
                                event.setCancelled(false)
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private fun fireClanFriendlyFireAttackEvent(
            createdBy: Player,
            attackingPlayer: Player,
            victimPlayer: Player,
            attackingCrew: Crew?,
            victimCrew: Crew
        ) {
            val crewFriendlyFireAttackEvent =
                CrewFriendlyFireAttackEvent(createdBy, attackingPlayer, victimPlayer, attackingCrew, victimCrew)
            Bukkit.getPluginManager().callEvent(crewFriendlyFireAttackEvent)
        }
    }
}