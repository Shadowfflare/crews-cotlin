package xyz.shadowflare.crews.commands

import com.tcoded.folialib.FoliaLib

class CrewCommand : CommandExecutor {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    @Override
    fun onCommand(sender: CommandSender, cmd: Command?, label: String?, args: Array<String?>): Boolean {
        if (sender is Player) {
            if (args.size < 1) {
                if (crewsConfig.getBoolean("use-global-GUI-system")) {
                    CrewListGUI(Crews.getPlayerMenuUtility(sender)).open()
                    return true
                }
                if (crewsConfig.getBoolean("crew-home.enabled") && crewsConfig.getBoolean("protections.pvp.pvp-command-enabled")
                    && crewsConfig.getBoolean("points.player-points.enabled")
                ) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-1")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-2")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-3")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-4")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-5")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-6")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-7")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-8")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-9")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-10")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-11")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-12")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-13")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-14")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-15")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-16")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-17")))
                    return true
                } else if (crewsConfig.getBoolean("crew-home.enabled")) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-1")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-2")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-3")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-4")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-5")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-6")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-7")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-8")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-9")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-10")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-11")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-12")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-13")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-15")))
                    return true
                } else if (crewsConfig.getBoolean("protections.pvp.pvp-command-enabled")) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-1")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-2")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-3")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-4")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-5")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-6")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-7")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-8")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-9")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-10")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-11")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-12")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-13")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-14")))
                    return true
                } else if (crewsConfig.getBoolean("points.player-points.enabled")) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-1")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-2")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-3")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-4")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-5")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-6")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-7")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-8")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-9")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-10")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-11")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-12")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-13")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-16")))
                    return true
                } else {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-1")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-2")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-3")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-4")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-5")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-6")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-7")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-8")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-9")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-10")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-11")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-12")))
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-command-incorrect-usage.line-13")))
                }
                return true
            } else {
                when (args[0]) {
                    "create" -> return CrewCreateSubCommand().createCrewSubCommand(sender, args, bannedTags)
                    "disband" -> return CrewDisbandSubCommand().disbandCrewSubCommand(sender)
                    "invite" -> return CrewInviteSubCommand().crewInviteSubCommand(sender, args)
                    "prefix" -> return CrewPrefixSubCommand().crewPrefixSubCommand(sender, args, bannedTags)
                    "transfer" -> return CrewTransferOwnerSubCommand().transferCrewOwnerSubCommand(sender, args)
                    "list" -> return CrewListSubCommand().crewListSubCommand(sender)
                    "join" -> return CrewJoinSubCommand().crewJoinSubCommand(sender)
                    "kick" -> return CrewKickSubCommand().crewKickSubCommand(sender, args)
                    "info" -> return CrewInfoSubCommand().crewInfoSubCommand(sender)
                    "leave" -> return CrewLeaveSubCommand().crewLeaveSubCommand(sender)
                    "ally" -> return CrewAllySubCommand().crewAllySubCommand(sender, args)
                    "enemy" -> return CrewEnemySubCommand().crewnEnemySubCommand(sender, args)
                    "pvp" -> return CrewPvpSubCommand().crewPvpSubCommand(sender)
                    "sethome" -> return CrewSetHomeSubCommand().setCrewHomeSubCommand(sender)
                    "delhome" -> return CrewDelHomeSubCommand().deleteCrewHomeSubCommand(sender)
                    "home" -> return xyz.shadowflare.crews.commands.crewSubCommands.CrewHomeSubCommand()
                        .tpCrewHomeSubCommand(sender)

                    "points" -> return CrewPointSubCommand().crewPointSubCommand(sender, args)
                    "playerpoints" -> return CrewPlayerPointsSubCommand().crewPlayerPointsSubCommand(sender, args)
                    else -> sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-command-usage")))
                }
            }
        }

//----------------------------------------------------------------------------------------------------------------------
        if (sender is ConsoleCommandSender) {
            console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("player-only-command")))
        }
        // If the player (or console) uses our command correct, we can return true
        return true
    }

    companion object {
        var task1: WrappedTask? = null
        private var bannedTags: List<String>? = null
        fun updateBannedTagsList() {
            val foliaLib = FoliaLib(Crews.getPlugin())
            task1 = foliaLib.getImpl().runLaterAsync({
                bannedTags = Crews.getPlugin().getConfig().getStringList("crew-tags.disallowed-tags")
            }, 1L, TimeUnit.SECONDS)
        }
    }
}
