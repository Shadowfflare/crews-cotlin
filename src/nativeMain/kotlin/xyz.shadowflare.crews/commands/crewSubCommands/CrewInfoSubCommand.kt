package xyz.shadowflare.crews.commands.crewSubCommands

import org.bukkit.Bukkit

class CrewInfoSubCommand {
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun crewInfoSubCommand(sender: CommandSender): Boolean {
        if (sender is Player) {
            val crewByOwner: Crew = CrewsStorageUtil.findCrewByOwner(sender)
            val crewByPlayer: Crew = CrewsStorageUtil.findCrewByPlayer(sender)
            if (crewByOwner != null) {
                val crewMembers: ArrayList<String> = crewByOwner.getCrewMembers()
                val crewAllies: ArrayList<String> = crewByOwner.getCrewAllies()
                val crewEnemies: ArrayList<String> = crewByOwner.getCrewEnemies()
                val crewInfo = StringBuilder(
                    ColorUtils.translateColorCodes(messagesConfig.getString("crew-info-header"))
                        .replace(CREW_PLACEHOLDER, ColorUtils.translateColorCodes(crewByOwner.getCrewFinalName()))
                        .replace("%CREWPREFIX%", ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix()))
                )
                val crewOwnerUUID: UUID = UUID.fromString(crewByOwner.getCrewOwner())
                val crewOwner: Player = Bukkit.getPlayer(crewOwnerUUID)
                if (crewOwner != null) {
                    crewInfo.append(
                        ColorUtils.translateColorCodes(messagesConfig.getString("crew-info-owner-online")).replace(
                            OWNER, crewOwner.getName()
                        )
                    )
                } else {
                    val uuid: UUID = UUID.fromString(crewByOwner.getCrewOwner())
                    val offlineOwner: String = Bukkit.getOfflinePlayer(uuid).getName()
                    crewInfo.append(
                        ColorUtils.translateColorCodes(messagesConfig.getString("crew-info-owner-offline")).replace(
                            OWNER, offlineOwner
                        )
                    )
                }
                if (crewMembers.size() > 0) {
                    val crewMembersSize: Int = crewMembers.size()
                    crewInfo.append(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString("crew-info-members-header")
                                .replace("%NUMBER%", ColorUtils.translateColorCodes(String.valueOf(crewMembersSize)))
                        )
                    )
                    for (crewMember in crewMembers) {
                        if (crewMember != null) {
                            val memberUUID: UUID = UUID.fromString(crewMember)
                            val crewPlayer: Player = Bukkit.getPlayer(memberUUID)
                            if (crewPlayer != null) {
                                crewInfo.append(
                                    ColorUtils.translateColorCodes(messagesConfig.getString("crew-info-members-online") + "\n")
                                        .replace(
                                            CREW_MEMBER, crewPlayer.getName()
                                        )
                                )
                            } else {
                                val uuid: UUID = UUID.fromString(crewMember)
                                val offlinePlayer: String = Bukkit.getOfflinePlayer(uuid).getName()
                                crewInfo.append(
                                    ColorUtils.translateColorCodes(messagesConfig.getString("crew-info-members-offline") + "\n")
                                        .replace(
                                            CREW_MEMBER, offlinePlayer
                                        )
                                )
                            }
                        }
                    }
                }
                if (crewAllies.size() > 0) {
                    crewInfo.append(" ")
                    crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-info-allies-header")))
                    for (crewAlly in crewAllies) {
                        if (crewAlly != null) {
                            val allyOwner: Player = Bukkit.getPlayer(crewAlly)
                            if (allyOwner != null) {
                                val allyCrew: Crew = CrewsStorageUtil.findCrewByOwner(allyOwner)
                                if (allyCrew != null) {
                                    val crewAllyName: String = allyCrew.getCrewFinalName()
                                    crewInfo.append(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("crew-ally-members").replace(
                                                ALLY_CREW, crewAllyName
                                            )
                                        )
                                    )
                                }
                            } else {
                                val uuid: UUID = UUID.fromString(crewAlly)
                                val offlineOwnerPlayer: OfflinePlayer = Bukkit.getOfflinePlayer(uuid)
                                val offlineAllyCrew: Crew = ClansStorageUtil.findCrewByOfflineOwner(offlineOwnerPlayer)
                                if (offlineAllyCrew != null) {
                                    val offlineAllyName: String = offlineAllyCrew.getCrewFinalName()
                                    if (offlineAllyName != null) {
                                        crewInfo.append(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("crew-ally-members").replace(
                                                    ALLY_CREW, offlineAllyName
                                                )
                                            )
                                        )
                                    } else {
                                        crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-ally-members-not-found")))
                                    }
                                }
                            }
                        }
                    }
                }
                if (crewEnemies.size() > 0) {
                    crewInfo.append(" ")
                    crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-info-enemies-header")))
                    for (crewEnemy in crewEnemies) {
                        if (crewEnemy != null) {
                            val enemyOwner: Player = Bukkit.getPlayer(crewEnemy)
                            if (enemyOwner != null) {
                                val enemyCrew: Crew = CrewsStorageUtil.findCrewByOwner(enemyOwner)
                                if (enemyCrew != null) {
                                    val crewEnemyName: String = enemyCrew.getCrewFinalName()
                                    crewInfo.append(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("clan-enemy-members").replace(
                                                ENEMY_CREW, crewEnemyName
                                            )
                                        )
                                    )
                                }
                            } else {
                                val uuid: UUID = UUID.fromString(crewEnemy)
                                val offlineOwnerPlayer: OfflinePlayer = Bukkit.getOfflinePlayer(uuid)
                                val offlineEnemyCrew: Crew = CrewsStorageUtil.findCrewByOfflineOwner(offlineOwnerPlayer)
                                if (offlineEnemyCrew != null) {
                                    val offlineEnemyName: String = offlineEnemyCrew.getCrewFinalName()
                                    if (offlineEnemyName != null) {
                                        crewInfo.append(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("crew-enemy-members").replace(
                                                    ENEMY_CREW, offlineEnemyName
                                                )
                                            )
                                        )
                                    } else {
                                        crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-enemy-members-not-found")))
                                    }
                                }
                            }
                        }
                    }
                }
                crewInfo.append(" ")
                if (crewByOwner.isFriendlyFireAllowed()) {
                    crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-pvp-status-enabled")))
                } else {
                    crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-pvp-status-disabled")))
                }
                if (CrewsStorageUtil.isHomeSet(crewByOwner)) {
                    crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-home-set-true")))
                } else {
                    crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-home-set-false")))
                }
                crewInfo.append(" ")
                crewInfo.append(
                    ColorUtils.translateColorCodes(
                        messagesConfig.getString("crew-points-value").replace(
                            POINTS_PLACEHOLDER, String.valueOf(crewByOwner.getCrewPoints())
                        )
                    )
                )
                crewInfo.append(" ")
                if (crewsConfig.getBoolean("protections.chests.enabled")) {
                    crewInfo.append(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString("crew-chest-amount").replace(
                                CHEST_PLACEHOLDER, String.valueOf(crewByOwner.getProtectedChests().size())
                            )
                        )
                    )
                    crewInfo.append(" ")
                    crewInfo.append(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString("crew-total-allowed-chests").replace(
                                TOTAL_CHEST_ALLOWED, String.valueOf(crewByOwner.getMaxAllowedProtectedChests())
                            )
                        )
                    )
                    crewInfo.append(" ")
                }
                crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-info-footer")))
                sender.sendMessage(crewInfo.toString())
            } else if (crewByPlayer != null) {
                val crewMembers: ArrayList<String> = crewByPlayer.getCrewMembers()
                val crewAllies: ArrayList<String> = crewByPlayer.getCrewAllies()
                val crewEnemies: ArrayList<String> = crewByPlayer.getCrewEnemies()
                val clanInfo = StringBuilder(
                    ColorUtils.translateColorCodes(messagesConfig.getString("crew-info-header"))
                        .replace(CREW_PLACEHOLDER, ColorUtils.translateColorCodes(crewByPlayer.getCrewFinalName()))
                        .replace("%CREWPREFIX%", ColorUtils.translateColorCodes(crewByPlayer.getCrewPrefix()))
                )
                val crewOwnerUUID: UUID = UUID.fromString(crewByPlayer.getCrewOwner())
                val crewOwner: Player = Bukkit.getPlayer(crewOwnerUUID)
                if (crewOwner != null) {
                    crewInfo.append(
                        ColorUtils.translateColorCodes(messagesConfig.getString("crew-info-owner-online")).replace(
                            OWNER, crewOwner.getName()
                        )
                    )
                } else {
                    val uuid: UUID = UUID.fromString(crewByPlayer.getCrewOwner())
                    val offlineOwner: String = Bukkit.getOfflinePlayer(uuid).getName()
                    crewInfo.append(
                        ColorUtils.translateColorCodes(messagesConfig.getString("crew-info-owner-offline")).replace(
                            OWNER, offlineOwner
                        )
                    )
                }
                if (crewMembers.size() > 0) {
                    val crewMembersSize: Int = crewMembers.size()
                    crewInfo.append(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString("crew-info-members-header")
                                .replace("%NUMBER%", ColorUtils.translateColorCodes(String.valueOf(crewMembersSize)))
                        )
                    )
                    for (crewMember in crewMembers) {
                        if (crewMember != null) {
                            val memberUUID: UUID = UUID.fromString(crewMember)
                            val crewPlayer: Player = Bukkit.getPlayer(memberUUID)
                            if (crewPlayer != null) {
                                crewInfo.append(
                                    ColorUtils.translateColorCodes(messagesConfig.getString("crew-info-members-online") + "\n")
                                        .replace(
                                            CREW_MEMBER, crewPlayer.getName()
                                        )
                                )
                            } else {
                                val uuid: UUID = UUID.fromString(crewMember)
                                val offlinePlayer: String = Bukkit.getOfflinePlayer(uuid).getName()
                                crewInfo.append(
                                    ColorUtils.translateColorCodes(messagesConfig.getString("crew-info-members-offline") + "\n")
                                        .replace(
                                            CREW_MEMBER, offlinePlayer
                                        )
                                )
                            }
                        }
                    }
                }
                if (crewAllies.size() > 0) {
                    crewInfo.append(" ")
                    crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-info-allies-header")))
                    for (crewAlly in crewAllies) {
                        if (crewAlly != null) {
                            val allyOwner: Player = Bukkit.getPlayer(crewAlly)
                            if (allyOwner != null) {
                                val allyCrew: Crew = CrewsStorageUtil.findCrewByOwner(allyOwner)
                                if (allyCrew != null) {
                                    val crewAllyName: String = allyCrew.getCrewFinalName()
                                    crewInfo.append(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("crew-ally-members").replace(
                                                ALLY_CREW, crewAllyName
                                            )
                                        )
                                    )
                                }
                            } else {
                                val uuid: UUID = UUID.fromString(crewAlly)
                                val offlineOwnerPlayer: OfflinePlayer = Bukkit.getOfflinePlayer(uuid)
                                val offlineAllyCrew: Crew = CrewsStorageUtil.findCrewByOfflineOwner(offlineOwnerPlayer)
                                if (offlineAllyCrew != null) {
                                    val offlineAllyName: String = offlineAllyCrew.getCrewFinalName()
                                    if (offlineAllyName != null) {
                                        crewInfo.append(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("crew-ally-members").replace(
                                                    ALLY_CREW, offlineAllyName
                                                )
                                            )
                                        )
                                    } else {
                                        crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-ally-members-not-found")))
                                    }
                                }
                            }
                        }
                    }
                }
                if (crewEnemies.size() > 0) {
                    crewInfo.append(" ")
                    crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-info-enemies-header")))
                    for (crewEnemy in crewEnemies) {
                        if (crewEnemy != null) {
                            val enemyOwner: Player = Bukkit.getPlayer(crewEnemy)
                            if (enemyOwner != null) {
                                val enemyCrew: Crew = CrewsStorageUtil.findCrewByOwner(enemyOwner)
                                if (enemyCrew != null) {
                                    val crewEnemyName: String = enemyCrew.getCrewFinalName()
                                    crewInfo.append(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("crew-enemy-members").replace(
                                                ENEMY_CREW, crewEnemyName
                                            )
                                        )
                                    )
                                }
                            } else {
                                val uuid: UUID = UUID.fromString(crewEnemy)
                                val offlineOwnerPlayer: OfflinePlayer = Bukkit.getOfflinePlayer(uuid)
                                val offlineEnemyCrew: Crew = CrewsStorageUtil.findCrewByOfflineOwner(offlineOwnerPlayer)
                                if (offlineEnemyCrew != null) {
                                    val offlineEnemyName: String = offlineEnemyCrew.getCrewFinalName()
                                    if (offlineEnemyName != null) {
                                        crewInfo.append(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("crew-enemy-members").replace(
                                                    ENEMY_CREW, offlineEnemyName
                                                )
                                            )
                                        )
                                    } else {
                                        crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-enemy-members-not-found")))
                                    }
                                }
                            }
                        }
                    }
                }
                crewInfo.append(" ")
                if (crewByPlayer.isFriendlyFireAllowed()) {
                    crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-pvp-status-enabled")))
                } else {
                    crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-pvp-status-disabled")))
                }
                if (CrewsStorageUtil.isHomeSet(crewByPlayer)) {
                    crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-home-set-true")))
                } else {
                    crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-home-set-false")))
                }
                crewInfo.append(" ")
                crewInfo.append(
                    ColorUtils.translateColorCodes(
                        messagesConfig.getString("crew-points-value").replace(
                            POINTS_PLACEHOLDER, String.valueOf(crewByPlayer.getCrewPoints())
                        )
                    )
                )
                crewInfo.append(" ")
                if (crewsConfig.getBoolean("protections.chests.enabled")) {
                    crewInfo.append(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString("crew-chest-amount").replace(
                                CHEST_PLACEHOLDER, String.valueOf(crewByPlayer.getProtectedChests().size())
                            )
                        )
                    )
                    crewInfo.append(" ")
                    crewInfo.append(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString("crew-total-allowed-chests").replace(
                                TOTAL_CHEST_ALLOWED, String.valueOf(crewByPlayer.getMaxAllowedProtectedChests())
                            )
                        )
                    )
                    crew.append(" ")
                }
                crewInfo.append(ColorUtils.translateColorCodes(messagesConfig.getString("crew-info-footer")))
                sender.sendMessage(crewInfo.toString())
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("not-in-crew")))
            }
            return true
        }
        return false
    }

    companion object {
        private const val CREW_PLACEHOLDER = "%CREW%"
        private const val OWNER = "%OWNER%"
        private const val CREW_MEMBER = "%MEMBER%"
        private const val ALLY_CREW = "%ALLYCREW%"
        private const val ENEMY_CREW = "%ENEMYCREW%"
        private const val POINTS_PLACEHOLDER = "%POINTS%"
        private const val CHEST_PLACEHOLDER = "%CHESTS%"
        private const val TOTAL_CHEST_ALLOWED = "%MAXALLOWED%"
    }
}
