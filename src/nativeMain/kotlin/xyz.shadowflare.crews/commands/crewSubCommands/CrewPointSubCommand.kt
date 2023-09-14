package xyz.shadowflare.crews.commands.crewSubCommands

import org.bukkit.Bukkit

class CrewPointSubCommand {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var clansConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun crewPointSubCommand(sender: CommandSender, args: Array<String?>): Boolean {
        if (sender is Player) {
            if (crewsConfig.getBoolean("points.player-points.enabled")) {
                if (args.size > 2) {
                    if (args[1].equalsIgnoreCase("deposit")) {
                        return if (args[2] != null) {
                            val depositValue: Int = Integer.parseInt(args[2])
                            if (depositValue != 0) {
                                val crew: Crew = CrewsStorageUtil.findCrewByPlayer(sender)
                                val crewPlayer: CrewPlayer = UsermapStorageUtil.getCrewPlayerByBukkitPlayer(sender)
                                val previousCrewPlayerPointValue: Int = crewPlayer.pointBalance
                                if (crew != null) {
                                    val previousCrewPointValue: Int = crew.getCrewPoints()
                                    if (UsermapStorageUtil.withdrawPoints(sender, depositValue)) {
                                        CrewsStorageUtil.addPoints(crew, depositValue)
                                        val newCrewPlayerPointValue: Int = crewPlayer.pointBalance
                                        val newCrewPointValue: Int = crew.getCrewPoints()
                                        fireCrewPointsAddedEvent(
                                            sender,
                                            crew,
                                            crewPlayer,
                                            previousCrewPlayerPointValue,
                                            newCrewPlayerPointValue,
                                            depositValue,
                                            previousCrewPointValue,
                                            newCrewPointValue
                                        )
                                        if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewPointsAddedEvent"))
                                        }
                                        sender.sendMessage(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("crew-deposit-points-success")
                                                    .replace(CREW_PLACEHOLDER, crew.getCrewFinalName()).replace(
                                                        POINT_PLACEHOLDER,
                                                        String.valueOf(depositValue)
                                                    )
                                            )
                                        )
                                    } else {
                                        sender.sendMessage(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("clan-deposit-points-failed")
                                                    .replace(CREW_PLACEHOLDER, crew.getCrewFinalName()).replace(
                                                        POINT_PLACEHOLDER,
                                                        String.valueOf(depositValue)
                                                    )
                                            )
                                        )
                                    }
                                    true
                                } else {
                                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-points-failed-not-in-crew")))
                                    true
                                }
                            } else {
                                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-deposit-points-invalid-point-amount")))
                                true
                            }
                        } else {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-deposit-points-incorrect-command")))
                            true
                        }
                    } else if (args[1].equalsIgnoreCase("withdraw")) {
                        return if (args[2] != null) {
                            val withdrawValue: Int = Integer.parseInt(args[2])
                            if (withdrawValue != 0) {
                                val crew: Crew = CrewsStorageUtil.findCrewByPlayer(sender)
                                val crewPlayer: CrewPlayer = UsermapStorageUtil.getClanPlayerByBukkitPlayer(sender)
                                val previousClanPlayerPointValue: Int = crewPlayer.pointBalance
                                if (clan != null) {
                                    val previousClanPointValue: Int = crew.getCrewPoints()
                                    if (CrewsStorageUtil.withdrawPoints(crew, withdrawValue)) {
                                        UsermapStorageUtil.addPointsToOnlinePlayer(sender, withdrawValue)
                                        val newCrewPlayerPointValue: Int = crewPlayer.pointBalance
                                        val newCrewPointValue: Int = crew.getCrewPoints()
                                        fireCrewPointsRemovedEvent(
                                            sender,
                                            crew,
                                            crewPlayer,
                                            previousCrewPlayerPointValue,
                                            newCrewPlayerPointValue,
                                            withdrawValue,
                                            previousCrewPointValue,
                                            newCrewPointValue
                                        )
                                        if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                            console.sendMessage(ColorUtils.translateColorCodes("&6Crew-Debug: &aFired CrewPointsRemovedEvent"))
                                        }
                                        sender.sendMessage(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("crew-withdraw-points-success")
                                                    .replace(CREW_PLACEHOLDER, crew.getCrewFinalName()).replace(
                                                        POINT_PLACEHOLDER,
                                                        String.valueOf(withdrawValue)
                                                    )
                                            )
                                        )
                                    } else {
                                        sender.sendMessage(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("crew-withdraw-points-failed")
                                                    .replace(CREW_PLACEHOLDER, crew.getCrewFinalName()).replace(
                                                        POINT_PLACEHOLDER,
                                                        String.valueOf(withdrawValue)
                                                    )
                                            )
                                        )
                                    }
                                    true
                                } else {
                                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-points-failed-not-in-crew")))
                                    true
                                }
                            } else {
                                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-withdraw-points-invalid-point-amount")))
                                true
                            }
                        } else {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-withdraw-points-incorrect-command")))
                            true
                        }
                    }
                }
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")))
            }
        }
        return true
    }

    companion object {
        private const val CLAN_PLACEHOLDER = "%CREW%"
        private const val POINT_PLACEHOLDER = "%POINTS%"
        private fun fireCrewPointsAddedEvent(
            createdBy: Player,
            playerCrew: Crew?,
            crewPlayer: CrewPlayer,
            previousCrewPlayerPointBalance: Int,
            newCrewPlayerPointBalance: Int,
            depositPointValue: Int,
            previousCrewPointBalance: Int,
            newCrewPointBalance: Int
        ) {
            val crewPointsAddedEvent = CrewPointsAddedEvent(
                createdBy,
                playerCrew,
                crewPlayer,
                previousCrewPlayerPointBalance,
                newCrewPlayerPointBalance,
                depositPointValue,
                previousCrewPointBalance,
                newCrewPointBalance
            )
            Bukkit.getPluginManager().callEvent(crewPointsAddedEvent)
        }

        private fun fireCrewPointsRemovedEvent(
            createdBy: Player,
            playerCrew: Crew,
            crewPlayer: CrewPlayer,
            previousCrewPlayerPointBalance: Int,
            newCrewPlayerPointBalance: Int,
            withdrawPointValue: Int,
            previousCrewPointBalance: Int,
            newCrewPointBalance: Int
        ) {
            val crewPointsRemovedEvent = CrewPointsRemovedEvent(
                createdBy,
                playerCrew,
                crewPlayer,
                previousCrewPlayerPointBalance,
                newCrewPlayerPointBalance,
                withdrawPointValue,
                previousCrewPointBalance,
                newCrewPointBalance
            )
            Bukkit.getPluginManager().callEvent(crewPointsRemovedEvent)
        }
    }
}
