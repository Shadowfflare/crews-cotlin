package xyz.shadowflare.crews.commands

import com.tcoded.folialib.FoliaLib

class CrewAdmin : CommandExecutor {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    private val onlinePlayers: ArrayList<Player> = ArrayList(Bukkit.getOnlinePlayers())
    @Override
    fun onCommand(sender: CommandSender, cmd: Command?, label: String?, args: Array<String>): Boolean {
        if (sender is Player) {
            if (args.size > 0) {
                if (args[0].equalsIgnoreCase("save")) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("saving-crews-start")))
                    try {
                        if (!CrewsStorageUtil.getRawCrewsList().isEmpty()) {
                            CrewsStorageUtil.saveCrews()
                        } else {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("save-failed-no-crews")))
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-save-error-1")))
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-save-error-2")))
                    }
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("save-completed")))
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-begin")))
                    for (p in onlinePlayers) {
                        if (p.getName().equalsIgnoreCase(sender.getName())) {
                            continue
                        }
                        if (!onlinePlayers.isEmpty()) {
                            p.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-broadcast-start")))
                        }
                    }
                    val foliaLib = FoliaLib(Crews.getPlugin())
                    Crews.getPlugin().onDisable()
                    foliaLib.getImpl().runLater(object : Runnable() {
                        @Override
                        fun run() {
                            Bukkit.getPluginManager().getPlugin("Crews").onEnable()
                        }
                    }, 5L, TimeUnit.SECONDS)
                    foliaLib.getImpl().runLater(object : Runnable() {
                        @Override
                        fun run() {
                            Crews.getPlugin().reloadConfig()
                            xyz.shadowflare.crews.commands.CrewCommand.updateBannedTagsList()
                            Crews.getPlugin().messagesFileManager.reloadMessagesConfig()
                            Crews.getPlugin().crewGUIFileManager.reloadClanGUIConfig()
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-successful")))
                            for (p in onlinePlayers) {
                                if (p.getName().equalsIgnoreCase(sender.getName())) {
                                    continue
                                }
                                if (!onlinePlayers.isEmpty()) {
                                    p.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-successful")))
                                }
                            }
                        }
                    }, 5L, TimeUnit.SECONDS)
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("disband")) {
                    if (args.size == 2) {
                        if (args[1].length() > 1) {
                            val onlinePlayerOwner: Player = Bukkit.getPlayer(args[1])
                            val offlinePlayerOwner: OfflinePlayer = UsermapStorageUtil.getBukkitOfflinePlayerByName(
                                args[1]
                            )
                            if (onlinePlayerOwner != null) {
                                try {
                                    if (CrewsStorageUtil.deleteCrew(onlinePlayerOwner)) {
                                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-successfully-disbanded")))
                                    } else {
                                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-admin-disband-failure")))
                                    }
                                } catch (e: IOException) {
                                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-1")))
                                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-2")))
                                    e.printStackTrace()
                                }
                            } else if (offlinePlayerOwner != null) {
                                try {
                                    if (CrewsStorageUtil.deleteOfflineCrew(offlinePlayerOwner)) {
                                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-successfully-disbanded")))
                                    } else {
                                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-admin-disband-failure")))
                                    }
                                } catch (e: IOException) {
                                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-1")))
                                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-2")))
                                    e.printStackTrace()
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
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-disband-command-usage")))
                        }
                    }
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("about")) {
                    sender.sendMessage(ColorUtils.translateColorCodes("&3~~~~~~~~~~ &6&n Crews&r &3~~~~~~~~~~"))
                    sender.sendMessage(
                        ColorUtils.translateColorCodes(
                            "&3Version: &6" + Crews.getPlugin().getDescription().getVersion()
                        )
                    )
                    sender.sendMessage(
                        ColorUtils.translateColorCodes(
                            "&3Authors: &6" + Crews.getPlugin().getDescription().getAuthors()
                        )
                    )
                    sender.sendMessage(
                        ColorUtils.translateColorCodes(
                            "&3Description: &6" + Crews.getPlugin().getDescription().getDescription()
                        )
                    )
                    sender.sendMessage(ColorUtils.translateColorCodes("&3Website: "))
                    sender.sendMessage(
                        ColorUtils.translateColorCodes(
                            "&6" + Crews.getPlugin().getDescription().getWebsite()
                        )
                    )
                    sender.sendMessage(ColorUtils.translateColorCodes("&3Discord:"))
                    sender.sendMessage(ColorUtils.translateColorCodes("&6https://discord.gg/A33nQGNcUb"))
                    sender.sendMessage(ColorUtils.translateColorCodes("&3~~~~~~~~~~ &6&n Crews&r &3~~~~~~~~~~"))
                }

//----------------------------------------------------------------------------------------------------------------------
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crewadmin-command-incorrect-usage.line-1")))
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crewadmin-command-incorrect-usage.line-2")))
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crewadmin-command-incorrect-usage.line-3")))
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crewadmin-command-incorrect-usage.line-4")))
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crewadmin-command-incorrect-usage.line-5")))
            }
        }

//----------------------------------------------------------------------------------------------------------------------


//----------------------------------------------------------------------------------------------------------------------
        if (sender is ConsoleCommandSender) {
            if (args.size > 0) {
                if (args[0].equalsIgnoreCase("save")) {
                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("saving-crews-start")))
                    try {
                        if (!CrewsStorageUtil.getRawCrewsList().isEmpty()) {
                            CrewsStorageUtil.saveCrews()
                        } else {
                            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("save-failed-no-crews")))
                        }
                    } catch (e: IOException) {
                        console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-save-error-1")))
                        console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-save-error-2")))
                        e.printStackTrace()
                    }
                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("save-completed")))
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("reload")) {
                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-begin")))
                    for (p in onlinePlayers) {
                        if (!onlinePlayers.isEmpty()) {
                            p.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-broadcast-start")))
                        }
                    }
                    val foliaLib = FoliaLib(Crews.getPlugin())
                    Crews.getPlugin().onDisable()
                    foliaLib.getImpl().runLater(object : Runnable() {
                        @Override
                        fun run() {
                            Bukkit.getPluginManager().getPlugin("Crews").onEnable()
                        }
                    }, 5L, TimeUnit.SECONDS)
                    foliaLib.getImpl().runLater(object : Runnable() {
                        @Override
                        fun run() {
                            Crews.getPlugin().reloadConfig()
                            xyz.shadowflare.crews.commands.CrewCommand.updateBannedTagsList()
                            Crews.getPlugin().messagesFileManager.reloadMessagesConfig()
                            Crews.getPlugin().crewGUIFileManager.reloadClanGUIConfig()
                            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-successful")))
                            for (p in onlinePlayers) {
                                if (!onlinePlayers.isEmpty()) {
                                    p.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-reload-successful")))
                                }
                            }
                        }
                    }, 5L, TimeUnit.SECONDS)
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("disband")) {
                    if (args.size == 2) {
                        if (args[1].length() > 1) {
                            val onlinePlayerOwner: Player = Bukkit.getPlayer(args[1])
                            val offlinePlayerOwner: OfflinePlayer = UsermapStorageUtil.getBukkitOfflinePlayerByName(
                                args[1]
                            )
                            if (onlinePlayerOwner != null) {
                                try {
                                    if (CrewssStorageUtil.deleteCrew(onlinePlayerOwner)) {
                                        console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-successfully-disbanded")))
                                    } else {
                                        console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-admin-disband-failure")))
                                    }
                                } catch (e: IOException) {
                                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-1")))
                                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-2")))
                                    e.printStackTrace()
                                }
                            } else if (offlinePlayerOwner != null) {
                                try {
                                    if (CrewsStorageUtil.deleteOfflineCrew(offlinePlayerOwner)) {
                                        console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("clan-successfully-disbanded")))
                                    } else {
                                        console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-admin-disband-failure")))
                                    }
                                } catch (e: IOException) {
                                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-1")))
                                    console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-2")))
                                    e.printStackTrace()
                                }
                            } else {
                                console.sendMessage(
                                    ColorUtils.translateColorCodes(
                                        messagesConfig.getString("could-not-find-specified-player").replace(
                                            PLAYER_TO_KICK, args[1]
                                        )
                                    )
                                )
                            }
                        } else {
                            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-disband-command-usage")))
                        }
                    }
                }

//----------------------------------------------------------------------------------------------------------------------
                if (args[0].equalsIgnoreCase("about")) {
                    console.sendMessage(ColorUtils.translateColorCodes("&3~~~~~~~~~~ &6Crews &3~~~~~~~~~~"))
                    console.sendMessage(
                        ColorUtils.translateColorCodes(
                            "&3Version: &6" + Crews.getPlugin().getDescription().getVersion()
                        )
                    )
                    console.sendMessage(
                        ColorUtils.translateColorCodes(
                            "&3Authors: &6" + Crews.getPlugin().getDescription().getAuthors()
                        )
                    )
                    console.sendMessage(
                        ColorUtils.translateColorCodes(
                            "&3Description: &6" + Crews.getPlugin().getDescription().getDescription()
                        )
                    )
                    console.sendMessage(ColorUtils.translateColorCodes("&3Website: "))
                    console.sendMessage(
                        ColorUtils.translateColorCodes(
                            "&6" + Crews.getPlugin().getDescription().getWebsite()
                        )
                    )
                    console.sendMessage(ColorUtils.translateColorCodes("&3Discord:"))
                    console.sendMessage(ColorUtils.translateColorCodes("&6" * https))
                    //discord.gg/A33nQGNcUb"));
                    console.sendMessage(ColorUtils.translateColorCodes("&3~~~~~~~~~~ &6Crews &3~~~~~~~~~~"))
                }

//----------------------------------------------------------------------------------------------------------------------
            } else {
                console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crewadmin-command-incorrect-usage.line-1")))
                console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crewadmin-command-incorrect-usage.line-2")))
                console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crewadmin-command-incorrect-usage.line-3")))
                console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crewadmin-command-incorrect-usage.line-4")))
                console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crewadmin-command-incorrect-usage.line-5")))
            }
        }
        return true
    }

    companion object {
        private const val PLAYER_TO_KICK = "%KICKEDPLAYER%"
    }
}
