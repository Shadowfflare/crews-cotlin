package xyz.shadowflare.crews.commands.crewSubCommands

import org.bukkit.Bukkit

class CrewJoinSubCommand {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun crewJoinSubCommand(sender: CommandSender): Boolean {
        if (sender is Player) {
            val inviterUUIDString: AtomicReference<String> = AtomicReference("")
            val crewInvitesList: Set<Map.Entry<UUID, CrewInvite>> = CrewsInviteUtil.invites
            if (CrewsInviteUtil.searchInvitee(sender.getUniqueId().toString())) {
                crewInvitesList.forEach { invites -> inviterUUIDString.set(invites.getValue().getInviter()) }
                console.sendMessage(String.valueOf(inviterUUIDString.get()))
                val inviterPlayer: Player = Bukkit.getPlayer(UUID.fromString(inviterUUIDString.get()))
                val crew: Crew = CrewsStorageUtil.findCrewByOwner(inviterPlayer)
                if (crew != null) {
                    if (CrewsStorageUtil.addCrewMember(crew, sender)) {
                        CrewsInviteUtil.removeInvite(inviterUUIDString.get())
                        val joinMessage: String =
                            ColorUtils.translateColorCodes(messagesConfig.getString("crew-join-successful")).replace(
                                CREW_PLACEHOLDER, crew.crewFinalName
                            )
                        sender.sendMessage(joinMessage)
                        if (crewsConfig.getBoolean("crew-join.announce-to-all")) {
                            if (crewsConfig.getBoolean("crew-join.send-as-title")) {
                                for (onlinePlayers in Crews.connectedPlayers.keySet()) {
                                    onlinePlayers.sendTitle(
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("crew-join-broadcast-title-1")
                                                .replace(PLAYER_PLACEHOLDER, sender.getName())
                                                .replace(
                                                    CREW_PLACEHOLDER,
                                                    ColorUtils.translateColorCodes(crew.crewFinalName)
                                                )
                                        ),
                                        ColorUtils.translateColorCodes(
                                            messagesConfig.getString("crew-join-broadcast-title-2")
                                                .replace(PLAYER_PLACEHOLDER, sender.getName())
                                                .replace(
                                                    CREW_PLACEHOLDER,
                                                    ColorUtils.translateColorCodes(crew.crewFinalName)
                                                )
                                        ),
                                        30, 30, 30
                                    )
                                }
                            } else {
                                Bukkit.broadcastMessage(
                                    ColorUtils.translateColorCodes(
                                        messagesConfig.getString("crew-join-broadcast-chat")
                                            .replace(PLAYER_PLACEHOLDER, sender.getName())
                                            .replace(
                                                CREW_PLACEHOLDER,
                                                ColorUtils.translateColorCodes(crew.crewFinalName)
                                            )
                                    )
                                )
                            }
                        }
                    } else {
                        val failureMessage: String =
                            ColorUtils.translateColorCodes(messagesConfig.getString("crew-join-failed")).replace(
                                CREW_PLACEHOLDER, crew.crewFinalName
                            )
                        sender.sendMessage(failureMessage)
                    }
                } else {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-join-failed-no-valid-clan")))
                }
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-join-failed-no-invite")))
            }
            return true
        }
        return false
    }

    companion object {
        private const val PLAYER_PLACEHOLDER = "%PLAYER%"
        private const val CREW_PLACEHOLDER = "%CREW%"
    }
}
