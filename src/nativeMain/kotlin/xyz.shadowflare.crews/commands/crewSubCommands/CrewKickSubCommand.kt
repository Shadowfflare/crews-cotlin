package xyz.shadowflare.crews.commands.crewSubCommands

import org.bukkit.Bukkit

class CrewKickSubCommand {
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun crewKickSubCommand(sender: CommandSender, args: Array<String>): Boolean {
        if (sender is Player) {
            if (args.size == 2) {
                if (args[1].length() > 1) {
                    val targetCrew: Crew = CrewsStorageUtil.findCrewByOwner(sender)
                    if (CrewsStorageUtil.findCrewByOwner(sender) != null) {
                        val playerToKick: Player = Bukkit.getPlayer(args[1])
                        val offlinePlayerToKick: OfflinePlayer =
                            UsermapStorageUtil.getBukkitOfflinePlayerByName(args[1])
                        if (playerToKick != null) {
                            if (!sender.getName().equalsIgnoreCase(args[1])) {
                                val playerCrew: Crew = CrewsStorageUtil.findCrewByPlayer(playerToKick)
                                if (targetCrew.equals(playerCrew)) {
                                    if (CrewsStorageUtil.removeCrewMember(targetCrew, playerToKick)) {
                                        val playerKickedMessage: String =
                                            ColorUtils.translateColorCodes(messagesConfig.getString("crew-member-kick-successful"))
                                                .replace(
                                                    PLAYER_TO_KICK, args[1]
                                                )
                                        sender.sendMessage(playerKickedMessage)
                                        if (playerToKick.isOnline()) {
                                            val kickMessage: String =
                                                ColorUtils.translateColorCodes(messagesConfig.getString("crew-kicked-player-message"))
                                                    .replace(
                                                        CREW_PLACEHOLDER, targetCrew.crewFinalName
                                                    )
                                            playerToKick.sendMessage(kickMessage)
                                            return true
                                        }
                                    } else {
                                        val differentCrewMessage: String =
                                            ColorUtils.translateColorCodes(messagesConfig.getString("targeted-player-is-not-in-your-crew"))
                                                .replace(
                                                    PLAYER_TO_KICK, args[1]
                                                )
                                        sender.sendMessage(differentCrewMessage)
                                    }
                                } else {
                                    val differentCrewMessage: String =
                                        ColorUtils.translateColorCodes(messagesConfig.getString("targeted-player-is-not-in-your-crew"))
                                            .replace(
                                                PLAYER_TO_KICK, args[1]
                                            )
                                    sender.sendMessage(differentCrewMessage)
                                }
                            } else {
                                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-cannot-kick-yourself")))
                            }
                        } else if (offlinePlayerToKick != null) {
                            if (!sender.getName().equalsIgnoreCase(args[1])) {
                                val offlinePlayerCrew: Crew =
                                    CrewsStorageUtil.findCrewByOfflinePlayer(offlinePlayerToKick)
                                if (targetCrew.equals(offlinePlayerCrew)) {
                                    if (CrewsStorageUtil.removeOfflineCrewMember(targetCrew, offlinePlayerToKick)) {
                                        val playerKickedMessage: String =
                                            ColorUtils.translateColorCodes(messagesConfig.getString("crew-member-kick-successful"))
                                                .replace(
                                                    PLAYER_TO_KICK, args[1]
                                                )
                                        sender.sendMessage(playerKickedMessage)
                                    } else {
                                        val differentCrewMessage: String =
                                            ColorUtils.translateColorCodes(messagesConfig.getString("targeted-player-is-not-in-your-crew"))
                                                .replace(
                                                    PLAYER_TO_KICK, args[1]
                                                )
                                        sender.sendMessage(differentCrewMessage)
                                    }
                                    return true
                                } else {
                                    val differentClanMessage: String =
                                        ColorUtils.translateColorCodes(messagesConfig.getString("targeted-player-is-not-in-your-crew"))
                                            .replace(
                                                PLAYER_TO_KICK, args[1]
                                            )
                                    sender.sendMessage(differentClanMessage)
                                }
                            } else {
                                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-cannot-kick-yourself")))
                            }
                        } else {
                            sender.sendMessage(
                                ColorUtils.translateColorCodes(
                                    messagesConfig.getString("could-not-find-specified-player").replace(
                                        PLAYER_TO_KICK, args[1]
                                    )
                                )
                            )
                        }
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("must-be-owner-to-kick")))
                    }
                } else {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-kick-command-usage")))
                }
            }
            return true
        }
        return false
    }

    companion object {
        private const val CREW_PLACEHOLDER = "%CREW%"
        private const val PLAYER_TO_KICK = "%KICKEDPLAYER%"
    }
}
