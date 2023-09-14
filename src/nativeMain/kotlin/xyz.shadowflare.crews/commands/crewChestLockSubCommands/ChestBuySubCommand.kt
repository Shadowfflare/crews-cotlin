package xyz.shadowflare.crews.commands.crewChestLockSubCommands

import org.bukkit.Bukkit

class ChestBuySubCommand {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    private val purchasePrice: Int = crewsConfig.getInt("protections.chests.crew-points-purchase-value")
    fun chestBuySubCommand(sender: CommandSender, args: Array<String?>): Boolean {
        if (sender is Player) {
            if (args.size > 1) {
                if (args[1] != null) {
                    val amountOfChests: Int = Integer.parseInt(args[1])
                    if (amountOfChests != 0) {
                        val crewByOwner: Crew = CrewsStorageUtil.findCrewByOwner(sender)
                        val crewByPlayer: Crew = CrewsStorageUtil.findCrewByPlayer(sender)
                        return if (crewByOwner != null) {
                            addNewChestLock(crewByOwner, sender, amountOfChests)
                            true
                        } else if (crewByPlayer != null) {
                            addNewChestLock(crewByPlayer, sender, amountOfChests)
                            true
                        } else {
                            sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-not-in-crew")))
                            true
                        }
                    }
                }
            } else {
                if (args.size == 1) {
                    val crewByOwner: Crew = CrewsStorageUtil.findCrewByOwner(sender)
                    val crewByPlayer: Crew = CrewsStorageUtil.findCrewByPlayer(sender)
                    return if (crewByOwner != null) {
                        addNewChestLock(crewByOwner, sender)
                        true
                    } else if (crewByPlayer != null) {
                        addNewChestLock(crewByPlayer, sender)
                        true
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-not-in-crew")))
                        true
                    }
                }
            }
        }
        return true
    }

    private fun addNewChestLock(crew: Crew?, player: Player, amountOfChests: Int) {
        val maxAllowedChests: Int = crew.getMaxAllowedProtectedChests()
        if (CrewsStorageUtil.hasEnoughPoints(crew, purchasePrice * maxAllowedChests)) {
            if (CrewsStorageUtil.withdrawPoints(crew, purchasePrice * maxAllowedChests)) {
                crew.setMaxAllowedProtectedChests(maxAllowedChests + amountOfChests)
                player.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesConfig.getString("chest-purchased-successfully")
                            .replace(AMOUNT_PLACEHOLDER, String.valueOf(amountOfChests))
                    )
                )
                fireChestBuyEvent(player, crew, maxAllowedChests, maxAllowedChests + amountOfChests)
                if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired ChestBuyEvent"))
                }
            } else {
                player.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesConfig.getString("failed-not-enough-points")
                            .replace(AMOUNT_PLACEHOLDER, String.valueOf(amountOfChests))
                    )
                )
            }
        } else {
            player.sendMessage(
                ColorUtils.translateColorCodes(
                    messagesConfig.getString("failed-not-enough-points")
                        .replace(AMOUNT_PLACEHOLDER, String.valueOf(amountOfChests))
                )
            )
        }
    }

    private fun addNewChestLock(crew: Crew?, player: Player) {
        val maxAllowedChests: Int = crew.getMaxAllowedProtectedChests()
        if (CrewsStorageUtil.hasEnoughPoints(crew, purchasePrice * maxAllowedChests)) {
            if (CrewsStorageUtil.withdrawPoints(crew, purchasePrice * maxAllowedChests)) {
                crew.setMaxAllowedProtectedChests(maxAllowedChests + 1)
                player.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesConfig.getString("chest-purchased-successfully")
                            .replace(AMOUNT_PLACEHOLDER, String.valueOf(1))
                    )
                )
                fireChestBuyEvent(player, crew, maxAllowedChests, maxAllowedChests + 1)
            } else {
                player.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesConfig.getString("failed-not-enough-points")
                            .replace(AMOUNT_PLACEHOLDER, String.valueOf(1))
                    )
                )
            }
        } else {
            player.sendMessage(
                ColorUtils.translateColorCodes(
                    messagesConfig.getString("failed-not-enough-points")
                        .replace(AMOUNT_PLACEHOLDER, String.valueOf(1))
                )
            )
        }
    }

    companion object {
        private const val AMOUNT_PLACEHOLDER = "%AMOUNT%"
        private fun fireChestBuyEvent(player: Player, crew: Crew?, oldCrewMaxAllowedChests: Int, newChestCount: Int) {
            val chestBuyEvent = ChestBuyEvent(player, crew, oldCrewMaxAllowedChests, newChestCount)
            Bukkit.getPluginManager().callEvent(chestBuyEvent)
        }
    }
}
