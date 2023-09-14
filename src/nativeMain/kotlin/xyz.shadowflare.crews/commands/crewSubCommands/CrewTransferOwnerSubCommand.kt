package xyz.shadowflare.crews.commands.crewSubCommands

import org.bukkit.Bukkit

class CrewTransferOwnerSubCommand {
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun transferCrewOwnerSubCommand(sender: CommandSender, args: Array<String?>): Boolean {
        if (sender is Player) {
            if (args.size > 1) {
                val PLAYER_PLACEHOLDER = "%PLAYER%"
                val newCrewOwner: Player = Bukkit.getPlayerExact(args[1])
                if (newCrewOwner != null) {
                    if (newCrewOwner !== sender) {
                        if (CrewsStorageUtil.isCrewOwner(sender)) {
                            val originalCrew: Crew = CrewsStorageUtil.findCrewByOwner(sender)
                            if (originalCrew != null) {
                                try {
                                    val newCrew: Crew =
                                        CrewsStorageUtil.transferCrewOwner(originalCrew, sender, newCrewOwner)
                                    if (newCrew != null) {
                                        val OLD_OWNER_PLACEHOLDER = "%OLDOWNER%"
                                        val NEW_CREW_NAME = "%CREW%"
                                        sender.sendMessage(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("crew-ownership-transfer-successful")
                                                    .replace(PLAYER_PLACEHOLDER, newCrewOwner.getName())
                                            )
                                        )
                                        newCrewOwner.sendMessage(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("crew-ownership-transfer-new-owner")
                                                    .replace(OLD_OWNER_PLACEHOLDER, sender.getName())
                                                    .replace(NEW_CREW_NAME, newCrew.getCrewFinalName())
                                            )
                                        )
                                        return true
                                    }
                                } catch (e: IOException) {
                                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-1")))
                                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crews-update-error-2")))
                                    e.printStackTrace()
                                }
                            }
                        } else {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("crew-must-be-owner")))
                        }
                    } else {
                        sender.sendMessage(
                            ColorUtils.translateColorCodes(
                                messagesConfig.getString("crew-ownership-transfer-failed-cannot-transfer-to-self")
                                    .replace(PLAYER_PLACEHOLDER, args[1])
                            )
                        )
                    }
                } else {
                    sender.sendMessage(
                        ColorUtils.translateColorCodes(
                            messagesConfig.getString("crew-ownership-transfer-failure-owner-offline")
                                .replace(PLAYER_PLACEHOLDER, args[1])
                        )
                    )
                }
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("incorrect-crew-transfer-ownership-command-usage")))
            }
        }
        return true
    }
}
