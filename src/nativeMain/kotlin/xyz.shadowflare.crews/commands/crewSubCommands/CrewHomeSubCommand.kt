package xyz.shadowflare.crews.commands.crewSubCommands

import org.bukkit.Bukkit

class CrewHomeSubCommand {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var crewsConfig: FileConfiguration = Crews.getPlugin().getConfig()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    var homeCoolDownTimer: HashMap<UUID, Long> = HashMap()
    fun tpCrewHomeSubCommand(sender: CommandSender): Boolean {
        if (sender is Player) {
            if (crewsConfig.getBoolean("crew-home.enabled")) {
                val uuid: UUID = sender.getUniqueId()
                if (CrewsStorageUtil.findCrewByOwner(sender) != null) {
                    val crewByOwner: Crew = CrewsStorageUtil.findCrewByOwner(sender)
                    if (crewByOwner.getCrewHomeWorld() != null) {
                        fireCrewHomePreTPEvent(sender, crewByOwner)
                        if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewHomePreTPEvent"))
                        }
                        if (homePreTeleportEvent.isCancelled()) {
                            if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aCrewHomePreTPEvent cancelled by external source"))
                            }
                            return true
                        }
                        val world: World = Bukkit.getWorld(crewByOwner.getCrewHomeWorld())
                        val x: Double = crewByOwner.crewHomeX
                        val y: Double = crewByOwner.crewHomeY + 0.2
                        val z: Double = crewByOwner.crewHomeZ
                        val yaw: Float = crewByOwner.crewHomeYaw
                        val pitch: Float = crewByOwner.crewHomePitch
                        if (crewsConfig.getBoolean("crew-home.cool-down.enabled")) {
                            if (homeCoolDownTimer.containsKey(uuid)) {
                                if (!(sender.hasPermission("crews.bypass.homecooldown") || sender.hasPermission("crews.bypass.*")
                                            || sender.hasPermission("crews.bypass") || sender.hasPermission("crews.*") || sender.isOp())
                                ) {
                                    if (homeCoolDownTimer[uuid] > System.currentTimeMillis()) {
                                        val timeLeft: Long =
                                            (homeCoolDownTimer[uuid] - System.currentTimeMillis()) / 1000
                                        sender.sendMessage(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("home-cool-down-timer-wait")
                                                    .replace(TIME_LEFT, Long.toString(timeLeft))
                                            )
                                        )
                                    } else {
                                        homeCoolDownTimer[uuid] =
                                            System.currentTimeMillis() + crewsConfig.getLong("crew-home.cool-down.time") * 1000
                                        val location = Location(world, x, y, z, yaw, pitch)
                                        if (crewsConfig.getBoolean("crew-home.delay-before-teleport.enabled")) {
                                            val teleportUtils = TeleportUtils()
                                            teleportUtils.teleportAsyncTimed(sender, crewByOwner, location)
                                        } else {
                                            val teleportUtils = TeleportUtils()
                                            teleportUtils.teleportAsync(sender, crewByOwner, location)
                                        }
                                    }
                                } else {
                                    val location = Location(world, x, y, z, yaw, pitch)
                                    if (crewsConfig.getBoolean("crew-home.delay-before-teleport.enabled")) {
                                        val teleportUtils = TeleportUtils()
                                        teleportUtils.teleportAsyncTimed(sender, crewByOwner, location)
                                    } else {
                                        val teleportUtils = TeleportUtils()
                                        teleportUtils.teleportAsync(sender, crewByOwner, location)
                                    }
                                }
                            } else {
                                homeCoolDownTimer[uuid] =
                                    System.currentTimeMillis() + crewsConfig.getLong("crew-home.cool-down.time") * 1000
                                val location = Location(world, x, y, z, yaw, pitch)
                                if (crewsConfig.getBoolean("crew-home.delay-before-teleport.enabled")) {
                                    val teleportUtils = TeleportUtils()
                                    teleportUtils.teleportAsyncTimed(sender, crewByOwner, location)
                                } else {
                                    val teleportUtils = TeleportUtils()
                                    teleportUtils.teleportAsync(sender, crewByOwner, location)
                                }
                            }
                        } else {
                            val location = Location(world, x, y, z, yaw, pitch)
                            if (crewsConfig.getBoolean("crew-home.delay-before-teleport.enabled")) {
                                val teleportUtils = TeleportUtils()
                                teleportUtils.teleportAsyncTimed(sender, crewByOwner, location)
                            } else {
                                val teleportUtils = TeleportUtils()
                                teleportUtils.teleportAsync(sender, crewByOwner, location)
                            }
                        }
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-no-home-set")))
                    }
                } else if (CrewsStorageUtil.findCrewByPlayer(sender) != null) {
                    val crewByPlayer: Crew = CrewsStorageUtil.findCrewByPlayer(sender)
                    fireCrewHomePreTPEvent(sender, crewByPlayer)
                    if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                        console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFired CrewHomePreTPEvent"))
                    }
                    if (homePreTeleportEvent.isCancelled()) {
                        if (crewsConfig.getBoolean("general.developer-debug-mode.enabled")) {
                            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aCrewHomePreTPEvent cancelled by external source"))
                        }
                        return true
                    }
                    if (crewByPlayer.getCrewHomeWorld() != null) {
                        val world: World = Bukkit.getWorld(crewByPlayer.getCrewHomeWorld())
                        val x: Double = crewByPlayer.crewHomeX
                        val y: Double = crewByPlayer.crewHomeY + 0.2
                        val z: Double = crewByPlayer.crewHomeZ
                        val yaw: Float = crewByPlayer.crewHomeYaw
                        val pitch: Float = crewByPlayer.crewHomePitch
                        if (crewsConfig.getBoolean("crew-home.cool-down.enabled")) {
                            if (homeCoolDownTimer.containsKey(uuid)) {
                                if (!(sender.hasPermission("crews.bypass.homecooldown") || sender.hasPermission("crews.bypass.*")
                                            || sender.hasPermission("crews.bypass") || sender.hasPermission("crews.*") || sender.isOp())
                                ) {
                                    if (homeCoolDownTimer[uuid] > System.currentTimeMillis()) {
                                        val timeLeft: Long =
                                            (homeCoolDownTimer[uuid] - System.currentTimeMillis()) / 1000
                                        sender.sendMessage(
                                            ColorUtils.translateColorCodes(
                                                messagesConfig.getString("home-cool-down-timer-wait")
                                                    .replace(TIME_LEFT, Long.toString(timeLeft))
                                            )
                                        )
                                    } else {
                                        homeCoolDownTimer[uuid] =
                                            System.currentTimeMillis() + crewsConfig.getLong("crew-home.cool-down.time") * 1000
                                        val location = Location(world, x, y, z, yaw, pitch)
                                        if (crewsConfig.getBoolean("crew-home.delay-before-teleport.enabled")) {
                                            val teleportUtils = TeleportUtils()
                                            teleportUtils.teleportAsyncTimed(sender, crewByPlayer, location)
                                        } else {
                                            val teleportUtils = TeleportUtils()
                                            teleportUtils.teleportAsync(sender, crewByPlayer, location)
                                        }
                                    }
                                } else {
                                    val location = Location(world, x, y, z, yaw, pitch)
                                    if (crewsConfig.getBoolean("crew-home.delay-before-teleport.enabled")) {
                                        val teleportUtils = TeleportUtils()
                                        teleportUtils.teleportAsyncTimed(sender, crewByPlayer, location)
                                    } else {
                                        val teleportUtils = TeleportUtils()
                                        teleportUtils.teleportAsync(sender, crewByPlayer, location)
                                    }
                                }
                            } else {
                                homeCoolDownTimer[uuid] =
                                    System.currentTimeMillis() + crewsConfig.getLong("crew-home.cool-down.time") * 1000
                                val location = Location(world, x, y, z, yaw, pitch)
                                if (crewsConfig.getBoolean("crew-home.delay-before-teleport.enabled")) {
                                    val teleportUtils = TeleportUtils()
                                    teleportUtils.teleportAsyncTimed(sender, crewByPlayer, location)
                                } else {
                                    val teleportUtils = TeleportUtils()
                                    teleportUtils.teleportAsync(sender, crewByPlayer, location)
                                }
                            }
                        } else {
                            val location = Location(world, x, y, z, yaw, pitch)
                            if (crewsConfig.getBoolean("crew-home.delay-before-teleport.enabled")) {
                                val teleportUtils = TeleportUtils()
                                teleportUtils.teleportAsyncTimed(sender, crewByPlayer, location)
                            } else {
                                val teleportUtils = TeleportUtils()
                                teleportUtils.teleportAsync(sender, crewByPlayer, location)
                            }
                        }
                    } else {
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-no-home-set")))
                    }
                } else {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("failed-tp-not-in-crew")))
                }
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("function-disabled")))
            }
            return true
        }
        return false
    }

    companion object {
        private const val TIME_LEFT = "%TIMELEFT%"
        private var homePreTeleportEvent: CrewHomePreTeleportEvent? = null
        private fun fireCrewHomePreTPEvent(player: Player, crew: Crew) {
            val crewHomePreTeleportEvent = CrewHomePreTeleportEvent(player, crew)
            Bukkit.getPluginManager().callEvent(crewHomePreTeleportEvent)
            homePreTeleportEvent = crewHomePreTeleportEvent
        }
    }
}
