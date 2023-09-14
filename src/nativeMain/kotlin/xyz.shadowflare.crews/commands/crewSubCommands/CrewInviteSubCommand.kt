package xyz.shadowflare.crews.commands.crewSubCommands

import org.bukkit.Bukkit

class CrewInviteSubCommand {
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun crewInviteSubCommand(sender: CommandSender, args: Array<String>): Boolean {
        if (sender is Player) {
            if (args.size == 2) {
                if (args[1].length() < 1) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-no-valid-player")))
                    return true
                }
                if (CrewsStorageUtil.findCrewByOwner(sender) == null) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-not-clan-owner")))
                    return true
                } else {
                    val invitedPlayerStr = args[1]
                    if (invitedPlayerStr.equalsIgnoreCase(sender.getName())) {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-self-error")))
                    } else {
                        val invitedPlayer: Player = Bukkit.getPlayer(invitedPlayerStr)
                        if (invitedPlayer == null) {
                            val playerNotFound: String =
                                ColorUtils.translateColorCodes(messagesConfig.getString("crew-invitee-not-found"))
                                    .replace(
                                        INVITED_PLAYER, invitedPlayerStr
                                    )
                            sender.sendMessage(playerNotFound)
                        } else if (CrewsStorageUtil.findCrewByPlayer(invitedPlayer) != null) {
                            val playerAlreadyInCrew: String =
                                ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-invited-already-in-crew"))
                                    .replace(
                                        INVITED_PLAYER, invitedPlayerStr
                                    )
                            sender.sendMessage(playerAlreadyInCrew)
                        } else {
                            val crew: Crew = CrewsStorageUtil.findCrewByOwner(sender)
                            if (!(sender.hasPermission("crews.maxclansize.*") || sender.hasPermission("crews.*") || sender.isOp())) {
                                if (!crewsConfig.getBoolean("crew-size.tiered-crew-system.enabled")) {
                                    if (crew.getCrewMembers()
                                            .size() >= crewsConfig.getInt("crew-size.default-max-crew-size")
                                    ) {
                                        val maxSize: Int = crewsConfig.getInt("crew-size.default-max-crew-size")
                                        sender.sendMessage(
                                            ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-max-size-reached"))
                                                .replace("%LIMIT%", String.valueOf(maxSize))
                                        )
                                        return true
                                    }
                                } else {
                                    if (sender.hasPermission("crews.maxclansize.group6")) {
                                        if (crew.getCrewMembers()
                                                .size() >= crewsConfig.getInt("crew-size.tiered-crew-system.permission-group-list.group-6")
                                        ) {
                                            val g6MaxSize: Int =
                                                crewsConfig.getInt("crew-size.tiered-crew-system.permission-group-list.group-6")
                                            sender.sendMessage(
                                                ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-max-size-reached"))
                                                    .replace("%LIMIT%", String.valueOf(g6MaxSize))
                                            )
                                            return true
                                        }
                                    } else if (sender.hasPermission("crews.maxclansize.group5")) {
                                        if (crew.getCrewMembers()
                                                .size() >= crewsConfig.getInt("crew-size.tiered-crew-system.permission-group-list.group-2")
                                        ) {
                                            val g5MaxSize: Int =
                                                crewsConfig.getInt("crew-size.tiered-crew-system.permission-group-list.group-5")
                                            sender.sendMessage(
                                                ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-max-size-reached"))
                                                    .replace("%LIMIT%", String.valueOf(g5MaxSize))
                                            )
                                            return true
                                        }
                                    } else if (sender.hasPermission("crews.maxclansize.group4")) {
                                        if (crew.getCrewMembers()
                                                .size() >= crewsConfig.getInt("crew-size.tiered-crew-system.permission-group-list.group-4")
                                        ) {
                                            val g4MaxSize: Int =
                                                crewsConfig.getInt("crew-size.tiered-crew-system.permission-group-list.group-4")
                                            sender.sendMessage(
                                                ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-max-size-reached"))
                                                    .replace("%LIMIT%", String.valueOf(g4MaxSize))
                                            )
                                            return true
                                        }
                                    } else if (sender.hasPermission("crews.maxclansize.group3")) {
                                        if (crew.getCrewMembers()
                                                .size() >= crewsConfig.getInt("crew-size.tiered-crew-system.permission-group-list.group-4")
                                        ) {
                                            val g3MaxSize: Int =
                                                crewsConfig.getInt("crew-size.tiered-crew-system.permission-group-list.group-3")
                                            sender.sendMessage(
                                                ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-max-size-reached"))
                                                    .replace("%LIMIT%", String.valueOf(g3MaxSize))
                                            )
                                            return true
                                        }
                                    } else if (sender.hasPermission("crews.maxclansize.group2")) {
                                        if (crew.getCrewMembers()
                                                .size() >= crewsConfig.getInt("crew-size.tiered-crew-system.permission-group-list.group-2")
                                        ) {
                                            val g2MaxSize: Int =
                                                crewsConfig.getInt("crew-size.tiered-crew-system.permission-group-list.group-2")
                                            sender.sendMessage(
                                                ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-max-size-reached"))
                                                    .replace("%LIMIT%", String.valueOf(g2MaxSize))
                                            )
                                            return true
                                        }
                                    } else if (sender.hasPermission("crews.maxclansize.group1")) {
                                        if (crew.getCrewMembers()
                                                .size() >= crewsConfig.getInt("crew-size.tiered-crew-system.permission-group-list.group-1")
                                        ) {
                                            val g1MaxSize: Int =
                                                crewsConfig.getInt("crew-size.tiered-clan-system.permission-group-list.group-1")
                                            sender.sendMessage(
                                                ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-max-size-reached"))
                                                    .replace("%LIMIT%", String.valueOf(g1MaxSize))
                                            )
                                            return true
                                        }
                                    }
                                }
                            }
                            if (Crews.getFloodgateApi() != null) {
                                return if (Crews.bedrockPlayers.containsKey(invitedPlayer)) {
                                    val bedrockInvitedPlayerUUIDString: String = Crews.bedrockPlayers.get(invitedPlayer)
                                    if (CrewsInviteUtil.createInvite(
                                            sender.getUniqueId().toString(),
                                            bedrockInvitedPlayerUUIDString
                                        ) != null
                                    ) {
                                        val confirmationString: String =
                                            ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-successful"))
                                                .replace(
                                                    INVITED_PLAYER,
                                                    invitedPlayer.getName()
                                                )
                                        sender.sendMessage(confirmationString)
                                        val invitationString: String =
                                            ColorUtils.translateColorCodes(messagesConfig.getString("crew-invited-player-invite-pending"))
                                                .replace("%CLANOWNER%", sender.getName())
                                        invitedPlayer.sendMessage(invitationString)
                                        true
                                    } else {
                                        val failureString: String =
                                            ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-failed"))
                                                .replace(
                                                    INVITED_PLAYER,
                                                    invitedPlayer.getName()
                                                )
                                        sender.sendMessage(failureString)
                                        true
                                    }
                                } else {
                                    if (CrewsInviteUtil.createInvite(
                                            sender.getUniqueId().toString(),
                                            invitedPlayer.getUniqueId().toString()
                                        ) != null
                                    ) {
                                        val confirmationString: String =
                                            ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-successful"))
                                                .replace(
                                                    INVITED_PLAYER,
                                                    invitedPlayer.getName()
                                                )
                                        sender.sendMessage(confirmationString)
                                        val invitationString: String =
                                            ColorUtils.translateColorCodes(messagesConfig.getString("crew-invited-player-invite-pending"))
                                                .replace("%CLANOWNER%", sender.getName())
                                        invitedPlayer.sendMessage(invitationString)
                                        true
                                    } else {
                                        val failureString: String =
                                            ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-failed"))
                                                .replace(
                                                    INVITED_PLAYER,
                                                    invitedPlayer.getName()
                                                )
                                        sender.sendMessage(failureString)
                                        true
                                    }
                                }
                            }
                            return if (CrewsInviteUtil.createInvite(
                                    sender.getUniqueId().toString(),
                                    invitedPlayer.getUniqueId().toString()
                                ) != null
                            ) {
                                val confirmationString: String =
                                    ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-successful"))
                                        .replace(
                                            INVITED_PLAYER,
                                            invitedPlayer.getName()
                                        )
                                sender.sendMessage(confirmationString)
                                val invitationString: String =
                                    ColorUtils.translateColorCodes(messagesConfig.getString("crew-invited-player-invite-pending"))
                                        .replace("%CLANOWNER%", sender.getName())
                                invitedPlayer.sendMessage(invitationString)
                                true
                            } else {
                                val failureString: String =
                                    ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-failed"))
                                        .replace(
                                            INVITED_PLAYER,
                                            invitedPlayer.getName()
                                        )
                                sender.sendMessage(failureString)
                                true
                            }
                        }
                    }
                }
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-invite-no-valid-player")))
            }
            return true
        }
        return false
    }

    companion object {
        private const val INVITED_PLAYER = "%INVITED%"
    }
}
