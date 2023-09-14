package xyz.shadowflare.crews.commands

import org.bukkit.Bukkit

class CrewChatCommand : CommandExecutor {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    var chatCoolDownTimer: HashMap<UUID, Long> = HashMap()
    @Override
    fun onCommand(sender: CommandSender, cmd: Command?, label: String?, args: Array<String?>): Boolean {
        if (sender is Player) {
            val uuid: UUID = sender.getUniqueId()
            if (!crewsConfig.getBoolean("crew-chat.enabled")) {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")))
                return true
            }
            if (args.size < 1) {
                sender.sendMessage(
                    ColorUtils.translateColorCodes(
                        """
                        &6Crews crew chat usage:&3
                        /crewchat <message>
                        """.trimIndent()
                    )
                )
                return true
            } else {
                val onlinePlayers: ArrayList<Player> = ArrayList(Bukkit.getServer().getOnlinePlayers())
                val playersWithSpyPerms: ArrayList<Player> = ArrayList()
                for (p in onlinePlayers) {
                    val crewPlayer: CrewPlayer = UsermapStorageUtil.getClanPlayerByBukkitPlayer(p)
                    if (crewPlayer.getCanChatSpy() && p.hasPermission("crews.chat.spy")) {
                        playersWithSpyPerms.add(p)
                    }
                }
                val crewByMember: Crew = CrewsStorageUtil.findCrewByPlayer(sender)
                val crewByOwner: Crew = CrewsStorageUtil.findCrewByOwner(sender)
                val chatSpyPrefix: String = crewsConfig.getString("crew-chat.chat-spy.chat-spy-prefix")
                val messageString = StringBuilder()
                messageString.append(crewsConfig.getString("crew-chat.chat-prefix")).append(" ")
                messageString.append("&d").append(sender.getName()).append(":&r").append(" ")
                for (arg in args) {
                    messageString.append(arg).append(" ")
                }
                if (crewsConfig.getBoolean("crew-chat.cool-down.enabled")) {
                    if (chatCoolDownTimer.containsKey(uuid)) {
                        if (!(sender.hasPermission("crews.bypass.chatcooldown") || sender.hasPermission("crews.bypass.*")
                                    || sender.hasPermission("crews.bypass") || sender.hasPermission("crews.*") || sender.isOp())
                        ) {
                            if (chatCoolDownTimer[uuid] > System.currentTimeMillis()) {

                                //If player still has time left on cool down
                                val timeLeft: Long = (chatCoolDownTimer[uuid] - System.currentTimeMillis()) / 1000
                                sender.sendMessage(
                                    ColorUtils.translateColorCodes(
                                        messagesConfig.getString("home-cool-down-timer-wait")
                                            .replace(TIME_LEFT, timeLeft.toString())
                                    )
                                )
                            } else {

                                //Add player to cool down and run message
                                chatCoolDownTimer[uuid] =
                                    System.currentTimeMillis() + crewsConfig.getLong("crew-chat.cool-down.time") * 1000
                                if (crewByMember != null) {
                                    val playerCrewMembers: ArrayList<String> = crewByMember.getCrewMembers()
                                    fireClanChatMessageSendEvent(
                                        sender,
                                        crewByMember,
                                        crewsConfig.getString("crew-chat.chat-prefix"),
                                        messageString.toString(),
                                        playerCrewMembers
                                    )
                                    if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                        console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewChatMessageSendEvent"))
                                    }
                                    for (playerCrewMember in playerCrewMembers) {
                                        if (playerCrewMember != null) {
                                            val memberUUID: UUID = UUID.fromString(playerCrewMember)
                                            val ownerUUID: UUID = UUID.fromString(crewByMember.getCrewOwner())
                                            val playerCrewPlayer: Player = Bukkit.getPlayer(memberUUID)
                                            val playerCrewOwner: Player = Bukkit.getPlayer(ownerUUID)
                                            if (playerCrewPlayer != null) {
                                                if (playerCrewOwner != null) {
                                                    playerCrewOwner.sendMessage(
                                                        ColorUtils.translateColorCodes(messageString.toString())
                                                            .replace(
                                                                CREW_PLACEHOLDER,
                                                                ColorUtils.translateColorCodes(crewByMember.getCrewPrefix())
                                                            )
                                                    )
                                                }
                                                playerCrewPlayer.sendMessage(
                                                    ColorUtils.translateColorCodes(messageString.toString()).replace(
                                                        CREW_PLACEHOLDER,
                                                        ColorUtils.translateColorCodes(crewByMember.getCrewPrefix())
                                                    )
                                                )
                                                if (crewsConfig.getBoolean("crew-chat.chat-spy.enabled")) {
                                                    for (p in playersWithSpyPerms) {
                                                        p.sendMessage(
                                                            ColorUtils.translateColorCodes("$chatSpyPrefix $messageString")
                                                                .replace(
                                                                    CREW_PLACEHOLDER,
                                                                    ColorUtils.translateColorCodes(crewByMember.getCrewPrefix())
                                                                )
                                                        )
                                                    }
                                                }
                                                return true
                                            }
                                        }
                                    }
                                }
                                if (crewByOwner != null) {
                                    val ownerCrewMembers: ArrayList<String> = crewByOwner.getCrewMembers()
                                    fireClanChatMessageSendEvent(
                                        sender,
                                        crewByOwner,
                                        crewsConfig.getString("crew-chat.chat-prefix"),
                                        messageString.toString(),
                                        ownerCrewMembers
                                    )
                                    if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                        console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewChatMessageSendEvent"))
                                    }
                                    for (ownerCrewMember in ownerCrewMembers) {
                                        if (ownerCrewMember != null) {
                                            val memberUUID: UUID = UUID.fromString(ownerCrewMember)
                                            val ownerCrewPlayer: Player = Bukkit.getPlayer(memberUUID)
                                            if (ownerCrewPlayer != null) {
                                                ownerCrewPlayer.sendMessage(
                                                    ColorUtils.translateColorCodes(messageString.toString()).replace(
                                                        CREW_PLACEHOLDER,
                                                        ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix())
                                                    )
                                                )
                                                sender.sendMessage(
                                                    ColorUtils.translateColorCodes(messageString.toString()).replace(
                                                        CREW_PLACEHOLDER,
                                                        ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix())
                                                    )
                                                )
                                                if (crewsConfig.getBoolean("crew-chat.chat-spy.enabled")) {
                                                    for (p in playersWithSpyPerms) {
                                                        p.sendMessage(
                                                            ColorUtils.translateColorCodes("$chatSpyPrefix $messageString")
                                                                .replace(
                                                                    CREW_PLACEHOLDER,
                                                                    ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix())
                                                                )
                                                        )
                                                    }
                                                }
                                                return true
                                            }
                                        }
                                    }
                                    sender.sendMessage(
                                        ColorUtils.translateColorCodes(messageString.toString()).replace(
                                            CREW_PLACEHOLDER,
                                            ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix())
                                        )
                                    )
                                } else {
                                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-must-be-in-crew")))
                                }
                            }
                        } else {

                            //If player has cool down bypass
                            if (crewByMember != null) {
                                val playerClanMembers: ArrayList<String> = crewByMember.getCrewMembers()
                                fireClanChatMessageSendEvent(
                                    sender,
                                    crewByMember,
                                    crewsConfig.getString("crew-chat.chat-prefix"),
                                    messageString.toString(),
                                    playerCrewMembers
                                )
                                if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewChatMessageSendEvent"))
                                }
                                for (playerCrewMember in playerCrewMembers) {
                                    if (playerCrewMember != null) {
                                        val memberUUID: UUID = UUID.fromString(playerCrewMember)
                                        val ownerUUID: UUID = UUID.fromString(crewByMember.getCrewOwner())
                                        val playerCrewPlayer: Player = Bukkit.getPlayer(memberUUID)
                                        val playerCrewOwner: Player = Bukkit.getPlayer(ownerUUID)
                                        if (playerCrewPlayer != null) {
                                            if (playerCrewOwner != null) {
                                                playerCrewOwner.sendMessage(
                                                    ColorUtils.translateColorCodes(messageString.toString()).replace(
                                                        CREW_PLACEHOLDER,
                                                        ColorUtils.translateColorCodes(crewByMember.getCrewPrefix())
                                                    )
                                                )
                                            }
                                            playerCrewPlayer.sendMessage(
                                                ColorUtils.translateColorCodes(messageString.toString()).replace(
                                                    CREW_PLACEHOLDER,
                                                    ColorUtils.translateColorCodes(crewByMember.getCrewPrefix())
                                                )
                                            )
                                            if (crewsConfig.getBoolean("crew-chat.chat-spy.enabled")) {
                                                for (p in playersWithSpyPerms) {
                                                    p.sendMessage(
                                                        ColorUtils.translateColorCodes("$chatSpyPrefix $messageString")
                                                            .replace(
                                                                CREW_PLACEHOLDER,
                                                                ColorUtils.translateColorCodes(crewByMember.getCrewPrefix())
                                                            )
                                                    )
                                                }
                                            }
                                            return true
                                        }
                                    }
                                }
                            }
                            if (crewByOwner != null) {
                                val ownerCrewMembers: ArrayList<String> = crewByOwner.getCrewMembers()
                                fireClanChatMessageSendEvent(
                                    sender,
                                    crewByOwner,
                                    crewsConfig.getString("crew-chat.chat-prefix"),
                                    messageString.toString(),
                                    ownerCrewMembers
                                )
                                if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewChatMessageSendEvent"))
                                }
                                for (ownerCrewMember in ownerCrewMembers) {
                                    if (ownerCrewMember != null) {
                                        val memberUUID: UUID = UUID.fromString(ownerCrewMember)
                                        val ownerCrewPlayer: Player = Bukkit.getPlayer(memberUUID)
                                        if (ownerCrewPlayer != null) {
                                            ownerCrewPlayer.sendMessage(
                                                ColorUtils.translateColorCodes(messageString.toString()).replace(
                                                    CREW_PLACEHOLDER,
                                                    ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix())
                                                )
                                            )
                                            sender.sendMessage(
                                                ColorUtils.translateColorCodes(messageString.toString()).replace(
                                                    CREW_PLACEHOLDER,
                                                    ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix())
                                                )
                                            )
                                            if (crewsConfig.getBoolean("crew-chat.chat-spy.enabled")) {
                                                for (p in playersWithSpyPerms) {
                                                    p.sendMessage(
                                                        ColorUtils.translateColorCodes("$chatSpyPrefix $messageString")
                                                            .replace(
                                                                CREW_PLACEHOLDER,
                                                                ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix())
                                                            )
                                                    )
                                                }
                                            }
                                            return true
                                        }
                                    }
                                }
                                sender.sendMessage(
                                    ColorUtils.translateColorCodes(messageString.toString()).replace(
                                        CREW_PLACEHOLDER, ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix())
                                    )
                                )
                            } else {
                                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-must-be-in-crew")))
                            }
                        }
                    } else {

                        //If player not in cool down hashmap
                        chatCoolDownTimer[uuid] =
                            System.currentTimeMillis() + crewsConfig.getLong("crew-chat.cool-down.time") * 1000
                        if (crewByMember != null) {
                            val playerCrewMembers: ArrayList<String> = crewByMember.getCrewMembers()
                            fireCrewChatMessageSendEvent(
                                sender,
                                crewByMember,
                                crewsConfig.getString("crew-chat.chat-prefix"),
                                messageString.toString(),
                                playerCrewMembers
                            )
                            if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewChatMessageSendEvent"))
                            }
                            for (playerCrewMember in playerCrewMembers) {
                                if (playerCrewMember != null) {
                                    val memberUUID: UUID = UUID.fromString(playerCrewMember)
                                    val ownerUUID: UUID = UUID.fromString(crewByMember.getCrewOwner())
                                    val playerCrewPlayer: Player = Bukkit.getPlayer(memberUUID)
                                    val playerCrewOwner: Player = Bukkit.getPlayer(ownerUUID)
                                    if (playerCrewPlayer != null) {
                                        if (playerCrewOwner != null) {
                                            playerCrewOwner.sendMessage(
                                                ColorUtils.translateColorCodes(messageString.toString()).replace(
                                                    CREW_PLACEHOLDER,
                                                    ColorUtils.translateColorCodes(crewByMember.getCrewPrefix())
                                                )
                                            )
                                        }
                                        playerCrewPlayer.sendMessage(
                                            ColorUtils.translateColorCodes(messageString.toString()).replace(
                                                CREW_PLACEHOLDER,
                                                ColorUtils.translateColorCodes(crewByMember.getCrewPrefix())
                                            )
                                        )
                                        if (crewsConfig.getBoolean("crew-chat.chat-spy.enabled")) {
                                            for (p in playersWithSpyPerms) {
                                                p.sendMessage(
                                                    ColorUtils.translateColorCodes("$chatSpyPrefix $messageString")
                                                        .replace(
                                                            CREW_PLACEHOLDER,
                                                            ColorUtils.translateColorCodes(crewByMember.getCrewPrefix())
                                                        )
                                                )
                                            }
                                        }
                                        return true
                                    }
                                }
                            }
                        }
                        if (crewByOwner != null) {
                            val ownerCrewMembers: ArrayList<String> = crewByOwner.getCrewMembers()
                            fireClanChatMessageSendEvent(
                                sender,
                                crewByOwner,
                                crewsConfig.getString("crew-chat.chat-prefix"),
                                messageString.toString(),
                                ownerCrewMembers
                            )
                            if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewChatMessageSendEvent"))
                            }
                            for (ownerCrewMember in ownerCrewMembers) {
                                if (ownerCrewMember != null) {
                                    val memberUUID: UUID = UUID.fromString(ownerCrewMember)
                                    val ownerCrewPlayer: Player = Bukkit.getPlayer(memberUUID)
                                    if (ownerCrewPlayer != null) {
                                        ownerCrewPlayer.sendMessage(
                                            ColorUtils.translateColorCodes(messageString.toString()).replace(
                                                CREW_PLACEHOLDER,
                                                ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix())
                                            )
                                        )
                                        sender.sendMessage(
                                            ColorUtils.translateColorCodes(messageString.toString()).replace(
                                                CREW_PLACEHOLDER,
                                                ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix())
                                            )
                                        )
                                        if (crewsConfig.getBoolean("crew-chat.chat-spy.enabled")) {
                                            for (p in playersWithSpyPerms) {
                                                p.sendMessage(
                                                    ColorUtils.translateColorCodes("$chatSpyPrefix $messageString")
                                                        .replace(
                                                            Crew_PLACEHOLDER,
                                                            ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix())
                                                        )
                                                )
                                            }
                                        }
                                        return true
                                    }
                                }
                            }
                            sender.sendMessage(
                                ColorUtils.translateColorCodes(messageString.toString()).replace(
                                    CREW_PLACEHOLDER, ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix())
                                )
                            )
                        } else {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-must-be-in-crew")))
                        }
                    }
                } else {

                    //If cool down disabled
                    if (crewByMember != null) {
                        val playerCrewMembers: ArrayList<String> = crewByMember.getCrewMembers()
                        fireClanChatMessageSendEvent(
                            sender,
                            crewByMember,
                            crewsConfig.getString("crew-chat.chat-prefix"),
                            messageString.toString(),
                            playerCrewMembers
                        )
                        if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewChatMessageSendEvent"))
                        }
                        for (playerCrewMember in playerCrewMembers) {
                            if (playerCrewMember != null) {
                                val memberUUID: UUID = UUID.fromString(playerCrewMember)
                                val ownerUUID: UUID = UUID.fromString(crewByMember.getClanOwner())
                                val playerCrewPlayer: Player = Bukkit.getPlayer(memberUUID)
                                val playerCrewOwner: Player = Bukkit.getPlayer(ownerUUID)
                                if (playerCrewPlayer != null) {
                                    if (playerCrewOwner != null) {
                                        playerCrewOwner.sendMessage(
                                            ColorUtils.translateColorCodes(messageString.toString()).replace(
                                                CREW_PLACEHOLDER,
                                                ColorUtils.translateColorCodes(crewByMember.getCrewPrefix())
                                            )
                                        )
                                    }
                                    playerCrewPlayer.sendMessage(
                                        ColorUtils.translateColorCodes(messageString.toString()).replace(
                                            CREW_PLACEHOLDER,
                                            ColorUtils.translateColorCodes(crewByMember.getCrewPrefix())
                                        )
                                    )
                                    if (crewsConfig.getBoolean("crew-chat.chat-spy.enabled")) {
                                        for (p in playersWithSpyPerms) {
                                            p.sendMessage(
                                                ColorUtils.translateColorCodes("$chatSpyPrefix $messageString").replace(
                                                    CREW_PLACEHOLDER,
                                                    ColorUtils.translateColorCodes(crewByMember.getCrewPrefix())
                                                )
                                            )
                                        }
                                    }
                                    return true
                                }
                            }
                        }
                    }
                    if (crewByOwner != null) {
                        val ownerCrewMembers: ArrayList<String> = crewByOwner.getCrewMembers()
                        fireClanChatMessageSendEvent(
                            sender,
                            crewByOwner,
                            crewsConfig.getString("crew-chat.chat-prefix"),
                            messageString.toString(),
                            ownerCrewMembers
                        )
                        if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewChatMessageSendEvent"))
                        }
                        for (ownerCrewMember in ownerCrewMembers) {
                            if (ownerCrewMember != null) {
                                val memberUUID: UUID = UUID.fromString(ownerCrewMember)
                                val ownerCrewPlayer: Player = Bukkit.getPlayer(memberUUID)
                                if (ownerCrewPlayer != null) {
                                    ownerCrewPlayer.sendMessage(
                                        ColorUtils.translateColorCodes(messageString.toString()).replace(
                                            CREW_PLACEHOLDER,
                                            ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix())
                                        )
                                    )
                                    sender.sendMessage(
                                        ColorUtils.translateColorCodes(messageString.toString()).replace(
                                            CREW_PLACEHOLDER,
                                            ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix())
                                        )
                                    )
                                    if (crewsConfig.getBoolean("crew-chat.chat-spy.enabled")) {
                                        for (p in playersWithSpyPerms) {
                                            p.sendMessage(
                                                ColorUtils.translateColorCodes("$chatSpyPrefix $messageString").replace(
                                                    CREW_PLACEHOLDER,
                                                    ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix())
                                                )
                                            )
                                        }
                                    }
                                    return true
                                }
                            }
                        }
                        sender.sendMessage(
                            ColorUtils.translateColorCodes(messageString.toString()).replace(
                                CREW_PLACEHOLDER, ColorUtils.translateColorCodes(crewByOwner.getCrewPrefix())
                            )
                        )
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-must-be-in-crew")))
                    }
                }
            }
        } else {
            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-only-command")))
        }
        return true
    }

    companion object {
        private const val TIME_LEFT = "%TIMELEFT%"
        private const val CREW_PLACEHOLDER = "%CREW%"
        private fun fireCrewChatMessageSendEvent(
            player: Player,
            crew: Crew,
            prefix: String,
            message: String,
            recipients: ArrayList<String>
        ) {
            val crewChatMessageSendEvent = CrewChatMessageSendEvent(player, crew, prefix, message, recipients)
            Bukkit.getPluginManager().callEvent(crewChatMessageSendEvent)
        }
    }
}
