package xyz.gamlin.crews.commands.crewSubCommands

import org.bukkit.Bukkit

class CrewEnemySubCommand {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun crewEnemySubCommand(sender: CommandSender, args: Array<String>): Boolean {
        if (sender is Player) {
            if (args.size > 2) {
                if (args[1].equalsIgnoreCase("add")) {
                    if (args[2].length() > 1) {
                        if (CrewsStorageUtil.isCrewOwner(sender)) {
                            if (CrewsStorageUtil.findCrewByOwner(sender) != null) {
                                val crew: Crew = CrewsStorageUtil.findCrewByOwner(sender)
                                val enemyCrewOwner: Player = Bukkit.getPlayer(args[2])
                                if (enemyCrewOwner != null) {
                                    if (CrewsStorageUtil.findCrewByOwner(enemyCrewOwner) != null) {
                                        if (CrewsStorageUtil.findCrewByOwner(sender) !== CrewsStorageUtil.findCrewByOwner(
                                                enemyCrewOwner
                                            )
                                        ) {
                                            val enemyCrew: Crew = CrewsStorageUtil.findCrewByOwner(enemyCrewOwner)
                                            val enemyOwnerUUIDString: String = enemyCrew.crewOwner
                                            if (CrewsStorageUtil.findCrewByOwner(sender).getCrewEnemies()
                                                    .size() >= crewsConfig.getInt("max-crew-enemies")
                                            ) {
                                                val maxSize: Int = crewsConfig.getInt("max-crew-enemies")
                                                sender.sendMessage(
                                                    ColorUtils.translateColorCodes(
                                                        messagesConfig.getString(
                                                            "crew-enemy-max-amount-reached"
                                                        )
                                                    ).replace("%LIMIT%", String.valueOf(maxSize))
                                                )
                                                return true
                                            }
                                            if (crew.crewAllies.contains(enemyOwnerUUIDString)) {
                                                sender.sendMessage(
                                                    ColorUtils.translateColorCodes(
                                                        messagesConfig.getString(
                                                            "failed-cannot-enemy-allied-crew"
                                                        )
                                                    )
                                                )
                                                return true
                                            }
                                            if (crew.crewEnemies.contains(enemyOwnerUUIDString)) {
                                                sender.sendMessage(
                                                    ColorUtils.translateColorCodes(
                                                        messagesConfig.getString(
                                                            "failed-crew-already-your-enemy"
                                                        )
                                                    )
                                                )
                                                return true
                                            } else {
                                                CrewsStorageUtil.addCrewEnemy(sender, enemyCrewOwner)
                                                fireCrewEnemyAddEvent(sender, crew, enemyCrewOwner, enemyCrew)
                                                if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewEnemyAddEvent"))
                                                }
                                                sender.sendMessage(
                                                    ColorUtils.translateColorCodes(
                                                        messagesConfig.getString("added-crew-to-your-enemies").replace(
                                                            ENEMY_CREW, enemyCrew.crewFinalName
                                                        )
                                                    )
                                                )
                                                val titleMain: String = ColorUtils.translateColorCodes(
                                                    messagesConfig.getString("added-enemy-crew-to-your-enemies-title-1")
                                                        .replace(
                                                            CREW_OWNER, enemyCrewOwner.getName()
                                                        )
                                                )
                                                val titleAux: String = ColorUtils.translateColorCodes(
                                                    messagesConfig.getString("added-enemy-crew-to-your-enemies-title-2")
                                                        .replace(
                                                            CREW_OWNER, enemyCrewOwner.getName()
                                                        )
                                                )
                                                sender.sendTitle(titleMain, titleAux, 10, 70, 20)
                                                val playerClanMembers: ArrayList<String> =
                                                    CrewsStorageUtil.findCrewByOwner(sender).getCrewMembers()
                                                for (playerCrewMember in playerCrewMembers) {
                                                    if (playerCrewMember != null) {
                                                        val memberUUID: UUID = UUID.fromString(playerCrewMember)
                                                        val playerCrewPlayer: Player = Bukkit.getPlayer(memberUUID)
                                                        if (playerCrewPlayer != null) {
                                                            playerCrewPlayer.sendTitle(titleMain, titleAux, 10, 70, 20)
                                                        }
                                                    }
                                                }
                                            }
                                            if (enemyCrewOwner.isOnline()) {
                                                enemyCrewOwner.sendMessage(
                                                    ColorUtils.translateColorCodes(
                                                        messagesConfig.getString("crew-added-to-other-enemies").replace(
                                                            CREW_OWNER, sender.getName()
                                                        )
                                                    )
                                                )
                                                val titleMainEnemy: String = ColorUtils.translateColorCodes(
                                                    messagesConfig.getString("crew-added-to-other-enemies-title-1")
                                                        .replace(
                                                            CREW_OWNER, sender.getName()
                                                        )
                                                )
                                                val titleAuxEnemy: String = ColorUtils.translateColorCodes(
                                                    messagesConfig.getString("crew-added-to-other-enemies-title-2")
                                                        .replace(
                                                            CREW_OWNER, sender.getName()
                                                        )
                                                )
                                                enemyCrewOwner.sendTitle(titleMainEnemy, titleAuxEnemy, 10, 70, 20)
                                                val enemyCrewMembers: ArrayList<String> = enemyCrew.getCrewMembers()
                                                for (enemyCrewMember in enemyCrewMembers) {
                                                    if (enemyCrewMember != null) {
                                                        val memberUUID: UUID = UUID.fromString(enemyCrewMember)
                                                        val enemyCrewPlayer: Player = Bukkit.getPlayer(memberUUID)
                                                        if (enemyCrewPlayer != null) {
                                                            enemyCrewPlayer.sendTitle(
                                                                titleMainEnemy,
                                                                titleAuxEnemy,
                                                                10,
                                                                70,
                                                                20
                                                            )
                                                        }
                                                    }
                                                }
                                            } else {
                                                sender.sendMessage(
                                                    ColorUtils.translateColorCodes(
                                                        messagesConfig.getString("failed-to-add-crew-to-enemies")
                                                            .replace(
                                                                ENEMY_OWNER, args[2]
                                                            )
                                                    )
                                                )
                                            }
                                        } else {
                                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-cannot-enemy-your-own-crew")))
                                        }
                                    } else {
                                        sender.sendMessage(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("failed-enemy-player-not-crew-owner").replace(
                                                    ENEMY_OWNER, args[2]
                                                )
                                            )
                                        )
                                    }
                                } else {
                                    sender.sendMessage(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("enemy-crew-add-owner-offline").replace(
                                                ENEMY_OWNER, args[2]
                                            )
                                        )
                                    )
                                }
                            }
                        } else {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-must-be-owner")))
                        }
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-crew-enemy-command-usage")))
                    }
                    return true
                } else if (args[1].equalsIgnoreCase("remove")) {
                    if (args[2].length() > 1) {
                        if (CrewsStorageUtil.isCrewOwner(sender)) {
                            if (CrewsStorageUtil.findCrewByOwner(sender) != null) {
                                val enemyCrewOwner: Player = Bukkit.getPlayer(args[2])
                                if (enemyCrewOwner != null) {
                                    if (CrewsStorageUtil.findCrewByOwner(enemyCrewOwner) != null) {
                                        val enemyCrew: Crew = CrewsStorageUtil.findCrewByOwner(enemyCrewOwner)
                                        val enemyCrews: List<String> =
                                            CrewsStorageUtil.findCrewByOwner(sender).getCrewEnemies()
                                        val enemyCrewOwnerUUID: UUID = enemyCrewOwner.getUniqueId()
                                        val enemyCrewOwnerString: String = enemyCrewOwnerUUID.toString()
                                        if (enemyCrews.contains(enemyCrewOwnerString)) {
                                            fireCrewEnemyRemoveEvent(sender, enemyCrewOwner, enemyCrew)
                                            if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewEnemyRemoveEvent"))
                                            }
                                            CrewsStorageUtil.removeCrewEnemy(sender, enemyCrewOwner)
                                            sender.sendMessage(
                                                ColorUtils.translateColorCodes(
                                                    messagesConfig.getString("removed-crew-from-your-enemies").replace(
                                                        ENEMY_CREW, enemyCrew.crewFinalName
                                                    )
                                                )
                                            )
                                            val titleMain: String = ColorUtils.translateColorCodes(
                                                messagesConfig.getString("removed-enemy-crew-from-your-enemies-title-1")
                                                    .replace(
                                                        CREW_OWNER, enemyCrewOwner.getName()
                                                    )
                                            )
                                            val titleAux: String = ColorUtils.translateColorCodes(
                                                messagesConfig.getString("removed-enemy-crew-from-your-enemies-title-1")
                                                    .replace(
                                                        CREW_OWNER, enemyCrewOwner.getName()
                                                    )
                                            )
                                            sender.sendTitle(titleMain, titleAux, 10, 70, 20)
                                            val playerCrewMembers: ArrayList<String> =
                                                CrewsStorageUtil.findCrewByOwner(sender).getCrewMembers()
                                            for (playerCrewMember in playerCrewMembers) {
                                                if (playerCrewMember != null) {
                                                    val memberUUID: UUID = UUID.fromString(playerCrewMember)
                                                    val playerCrewPlayer: Player = Bukkit.getPlayer(memberUUID)
                                                    if (playerCrewPlayer != null) {
                                                        playerCrewPlayer.sendTitle(titleMain, titleAux, 10, 70, 20)
                                                    }
                                                }
                                            }
                                            if (enemyCrewOwner.isOnline()) {
                                                enemyCrewOwner.sendMessage(
                                                    ColorUtils.translateColorCodes(
                                                        messagesConfig.getString("crew-removed-from-other-enemies")
                                                            .replace(
                                                                ENEMY_OWNER, sender.getName()
                                                            )
                                                    )
                                                )
                                                val titleMainEnemy: String = ColorUtils.translateColorCodes(
                                                    messagesConfig.getString("crew-removed-from-other-enemies-title-1")
                                                        .replace(
                                                            CREW_OWNER, sender.getName()
                                                        )
                                                )
                                                val titleAuxEnemy: String = ColorUtils.translateColorCodes(
                                                    messagesConfig.getString("crew-removed-from-other-enemies-title-2")
                                                        .replace(
                                                            CREW_OWNER, sender.getName()
                                                        )
                                                )
                                                enemyCrewOwner.sendTitle(titleMainEnemy, titleAuxEnemy, 10, 70, 20)
                                                val enemyCrewMembers: ArrayList<String> = enemyCrew.getCrewMembers()
                                                for (enemyCrewMember in enemyCrewMembers) {
                                                    if (enemyCrewMember != null) {
                                                        val memberUUID: UUID = UUID.fromString(enemyCrewMember)
                                                        val enemyCrewPlayer: Player = Bukkit.getPlayer(memberUUID)
                                                        if (enemyCrewPlayer != null) {
                                                            enemyCrewPlayer.sendTitle(titleMain, titleAux, 10, 70, 20)
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            sender.sendMessage(
                                                ColorUtils.translateColorCodes(
                                                    messagesConfig.getString("failed-to-remove-crew-from-enemies")
                                                        .replace(
                                                            ENEMY_OWNER, args[2]
                                                        )
                                                )
                                            )
                                        }
                                    } else {
                                        sender.sendMessage(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("failed-enemy-player-not-crew-owner").replace(
                                                    ENEMY_OWNER, args[2]
                                                )
                                            )
                                        )
                                    }
                                } else {
                                    sender.sendMessage(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("enemy-crew-remove-owner-offline").replace(
                                                ENEMY_OWNER, args[2]
                                            )
                                        )
                                    )
                                }
                            }
                        } else {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-must-be-owner")))
                        }
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-crew-enemy-command-usage")))
                    }
                }
                return true
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-crew-enemy-command-usage")))
            }
        }
        return false
    }

    companion object {
        private const val ENEMY_CREW = "%ENEMYCREW%"
        private const val ENEMY_OWNER = "%ENEMYOWNER%"
        private const val CREW_OWNER = "%CREWOWNER%"
        private fun fireCrewEnemyRemoveEvent(player: Player, enemyCrewOwner: Player, enemyCrew: Crew) {
            val crewEnemyRemoveEvent =
                CrewEnemyRemoveEvent(player, CrewsStorageUtil.findCrewByPlayer(player), enemyCrew, enemyCrewOwner)
            Bukkit.getPluginManager().callEvent(crewEnemyRemoveEvent)
        }

        private fun fireCrewEnemyAddEvent(player: Player, crew: Crew, enemyCrewOwner: Player, enemyCrew: Crew) {
            val crewEnemyAddEvent = CrewEnemyAddEvent(player, crew, enemyCrew, enemyCrewOwner)
            Bukkit.getPluginManager().callEvent(crewEnemyAddEvent)
        }
    }
}
