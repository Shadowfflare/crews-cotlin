package xyz.shadowflare.crews

import com.rylinaux.plugman.api.PlugManAPI
import com.tcoded.folialib.FoliaLib
import com.tcoded.folialib.wrapper.task.WrappedTask
import io.papermc.lib.PaperLib
import org.bukkit.Bukkit
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.geysermc.floodgate.api.FloodgateApi
import xyz.shadowflare.crews.commands.commandTabCompleters.ChestCommandTabCompleter
import xyz.shadowflare.crews.expansions.PlayerCrewExpansion
import xyz.shadowflare.crews.files.CrewsFileManager
import xyz.shadowflare.crews.files.MessagesFileManager
import xyz.shadowflare.crews.files.UsermapFileManager
import xyz.shadowflare.crews.listeners.*
import xyz.shadowflare.crews.menuSystem.PlayerMenuUtility
import xyz.shadowflare.crews.updateSystem.JoinEvent
import xyz.shadowflare.crews.updateSystem.UpdateChecker
import xyz.shadowflare.crews.utils.*
import java.io.IOException
import java.util.UUID
import java.util.concurrent.TimeUnit

class Crews : JavaPlugin() {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    private val pluginsendMessage: PluginDescriptionFile = getDescription()
    private val pluginVersion: String = pluginsendMessage.getVersion()
    private val foliaLib: FoliaLib = FoliaLib(this)
    var messagesFileManager: MessagesFileManager? = null
    var crewsFileManager: CrewsFileManager? = null
    var crewsGUIFileManager: CrewsGUIFileManager? = null
    var usermapFileManager: UsermapFileManager? = null
    var teleportQueue: HashMap<UUID, WrappedTask> = HashMap()
    @Override
    fun onEnable() {
        //Plugin startup logic
        plugin = this

        //Server version compatibility check
        if (!(Bukkit.getServer().getVersion().contains("1.13") || Bukkit.getServer().getVersion().contains("1.14") ||
                    Bukkit.getServer().getVersion().contains("1.15") || Bukkit.getServer().getVersion()
                .contains("1.16") ||
                    Bukkit.getServer().getVersion().contains("1.17") || Bukkit.getServer().getVersion()
                .contains("1.18") ||
                    Bukkit.getServer().getVersion().contains("1.19") || Bukkit.getServer().getVersion()
                .contains("1.20"))
        ) {
            console.sendMessage(ColorUtils.translateColorCodes("&4-------------------------------------------"))
            console.sendMessage(
                ColorUtils.translateColorCodes(
                    "&6Crews: &4Your server version is: &d" + Bukkit.getServer().getVersion()
                )
            )
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4This plugin is only supported on the Minecraft versions listed below:"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &41.13.x"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &41.14.x"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &41.15.x"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &41.16.x"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &41.17.x"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &41.18.x"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &41.19.x"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &41.20.x"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Is now disabling!"))
            console.sendMessage(ColorUtils.translateColorCodes("&4-------------------------------------------"))
            Bukkit.getPluginManager().disablePlugin(this)
            return
        } else {
            console.sendMessage(ColorUtils.translateColorCodes("&a-------------------------------------------"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &aA supported Minecraft version has been detected"))
            console.sendMessage(
                ColorUtils.translateColorCodes(
                    "&6Crews: &aYour server version is: &d" + Bukkit.getServer().getVersion()
                )
            )
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &6Continuing plugin startup"))
            console.sendMessage(ColorUtils.translateColorCodes("&a-------------------------------------------"))
        }

        //Suggest PaperMC if not using
        if (foliaLib.isUnsupported() || foliaLib.isSpigot()) {
            PaperLib.suggestPaper(this)
        }

        //Check if PlugManX is enabled
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlugManX") || isPlugManXEnabled) {
            if (!PlugManAPI.iDoNotWantToBeUnOrReloaded("ClansLite")) {
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"))
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"))
                console.sendMessage(ColorUtils.translateColorCodes("&4WARNING WARNING WARNING WARNING!"))
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4You appear to be using an unsupported version of &d&lPlugManX"))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Please &4&lDO NOT USE PLUGMANX TO LOAD/UNLOAD/RELOAD THIS PLUGIN!"))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Please &4&lFULLY RESTART YOUR SERVER!"))
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4This plugin &4&lHAS NOT &4been validated to use this version of PlugManX!"))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4&lNo official support will be given to you if you use this!"))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4&lUnless Loving11ish has explicitly agreed to help!"))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Please add ClansLite to the ignored-plugins list in PlugManX's config.yml"))
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &6Continuing plugin startup"))
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"))
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"))
            } else {
                console.sendMessage(ColorUtils.translateColorCodes("&a-------------------------------------------"))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &aSuccessfully hooked into PlugManX"))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &aSuccessfully added ClansLite to ignoredPlugins list."))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &6Continuing plugin startup"))
                console.sendMessage(ColorUtils.translateColorCodes("&a-------------------------------------------"))
            }
        } else {
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &cPlugManX not found!"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &cDisabling PlugManX hook loader"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &6Continuing plugin startup"))
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"))
        }

        //Load the plugin configs
        getConfig().options().copyDefaults()
        saveDefaultConfig()

        //Load messages.yml
        messagesFileManager = MessagesFileManager()
        messagesFileManager.MessagesFileManager(this)

        //Load crewsgui.yml
        this.crewGUIFileManager = CrewGUIFileManager()
        crewGUIFileManager.CrewGUIFileManager(this)

        //Load crews.yml
        crewsFileManager = CrewsFileManager()
        crewsFileManager.CrewsFileManager(this)
        if (crewsFileManager != null) {
            if (crewsFileManager.getCrewsConfig().contains("crews.data")) {
                try {
                    ClansStorageUtil.restoreClans()
                } catch (e: IOException) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Failed to load data from crews.yml!"))
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4See below for errors!"))
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Disabling Plugin!"))
                    e.printStackTrace()
                    Bukkit.getPluginManager().disablePlugin(this)
                    return
                }
            }
        } else {
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Failed to load data from crews.yml!"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Disabling Plugin!"))
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        //Load usermap.yml
        usermapFileManager = UsermapFileManager()
        usermapFileManager.UsermapFileManager(this)
        if (usermapFileManager != null) {
            if (usermapFileManager.getUsermapConfig().contains("users.data")) {
                try {
                    UsermapStorageUtil.restoreUsermap()
                } catch (e: IOException) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Failed to load data from usermap.yml!"))
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4See below for errors!"))
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Disabling Plugin!"))
                    e.printStackTrace()
                    Bukkit.getPluginManager().disablePlugin(this)
                    return
                }
            }
        } else {
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Failed to load data from usermap.yml!"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Disabling Plugin!"))
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        //Register the plugin commands
        this.getCommand("crew").setExecutor(CrewCommand())
        this.getCommand("crewadmin").setExecutor(CrewAdmin())
        this.getCommand("crewchat").setExecutor(CrewChatCommand())
        this.getCommand("crewchatspy").setExecutor(CrewChatSpyCommand())
        this.getCommand("crewchest").setExecutor(CrewChestCommand())

        //Register the command tab completers
        this.getCommand("crew").setTabCompleter(CrewCommandTabCompleter())
        this.getCommand("crewchest").setTabCompleter(ChestCommandTabCompleter())
        this.getCommand("crewadmin").setTabCompleter(CrewAdminTabCompleter())

        //Register the plugin events
        this.getServer().getPluginManager().registerEvents(PlayerConnectionEvent(), this)
        this.getServer().getPluginManager().registerEvents(PlayerDisconnectEvent(), this)
        this.getServer().getPluginManager().registerEvents(PlayerMovementEvent(), this)
        this.getServer().getPluginManager().registerEvents(PlayerMessageEvent(), this)
        this.getServer().getPluginManager().registerEvents(PlayerDamageEvent(), this)
        this.getServer().getPluginManager().registerEvents(PlayerKillEvent(), this)
        this.getServer().getPluginManager().registerEvents(ChestBreakEvent(), this)
        this.getServer().getPluginManager().registerEvents(ChestOpenEvent(), this)
        this.getServer().getPluginManager().registerEvents(JoinEvent(), this)
        this.getServer().getPluginManager().registerEvents(MenuEvent(), this)

        //Update banned tags list
        CrewCommand.updateBannedTagsList()

        //Register PlaceHolderAPI hooks
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") || isPlaceholderAPIEnabled) {
            PlayerCrewExpansion().register()
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3PlaceholderAPI found!"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3External placeholders enabled!"))
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"))
        } else {
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &cPlaceholderAPI not found!"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &cExternal placeholders disabled!"))
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"))
        }

        //Register FloodgateApi hooks
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("floodgate") || isFloodgateEnabled) {
            floodgateApi = FloodgateApi.getInstance()
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3FloodgateApi found!"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3Full Bedrock support enabled!"))
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"))
        } else {
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3FloodgateApi not found!"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3Bedrock support may not function!"))
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"))
        }

        //Plugin startup message
        console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"))
        console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3Plugin by: &b&lShadowFlare"))
        console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3has been loaded successfully"))
        console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3Plugin Version: &d&l$pluginVersion"))
        if (getConfig().getBoolean("general.developer-debug-mode.enabled")) {
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aDeveloper debug mode enabled!"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aThis WILL fill the console"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &awith additional ClansLite information!"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aThis setting is not intended for "))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &acontinous use!"))
        }
        console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"))

        //Check for available updates
        UpdateChecker(97163).getVersion { version ->
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                console.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesFileManager.getMessagesConfig().getString("no-update-available.1")
                    )
                )
                console.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesFileManager.getMessagesConfig().getString("no-update-available.2")
                    )
                )
                console.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesFileManager.getMessagesConfig().getString("no-update-available.3")
                    )
                )
            } else {
                console.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesFileManager.getMessagesConfig().getString("update-available.1")
                    )
                )
                console.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesFileManager.getMessagesConfig().getString("update-available.2")
                    )
                )
                console.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesFileManager.getMessagesConfig().getString("update-available.3")
                    )
                )
            }
        }

        //Start auto save task
        foliaLib.getImpl().runLaterAsync(object : Runnable() {
            @Override
            fun run() {
                TaskTimerUtils.runClansAutoSave()
                console.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesFileManager.getMessagesConfig().getString("auto-save-started")
                    )
                )
            }
        }, 5L, TimeUnit.SECONDS)

        //Start auto invite clear task
        foliaLib.getImpl().runLaterAsync(object : Runnable() {
            @Override
            fun run() {
                TaskTimerUtils.runClanInviteClear()
                console.sendMessage(
                    ColorUtils.translateColorCodes(
                        messagesFileManager.getMessagesConfig().getString("auto-invite-wipe-started")
                    )
                )
            }
        }, 5L, TimeUnit.SECONDS)
    }

    @Override
    fun onDisable() {
        //Plugin shutdown logic

        //Unregister plugin listeners
        HandlerList.unregisterAll(this)

        //Safely stop the background tasks if running
        console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"))
        console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3Plugin by: &b&lShadowFlare"))
        try {
            if (!teleportQueue.isEmpty()) {
                for (wrappedTaskEntry in teleportQueue.entrySet()) {
                    val wrappedTask: WrappedTask = wrappedTaskEntry.getValue()
                    wrappedTask.cancel()
                    if (getConfig().getBoolean("general.developer-debug-mode.enabled")) {
                        console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aWrapped task: " + wrappedTask.toString()))
                        console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aTimed teleport task canceled successfully"))
                    }
                    teleportQueue.remove(wrappedTaskEntry.getKey())
                }
            }
            if (!TaskTimerUtils.autoSaveTask.isCancelled()) {
                if (getConfig().getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aWrapped task: " + TaskTimerUtils.autoSaveTask.toString()))
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aTimed task 1 canceled successfully"))
                }
                TaskTimerUtils.autoSaveTask.cancel()
            }
            if (!TaskTimerUtils.inviteClearTask.isCancelled()) {
                if (getConfig().getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aWrapped task: " + TaskTimerUtils.inviteClearTask.toString()))
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aTimed task 2 canceled successfully"))
                }
                TaskTimerUtils.inviteClearTask.cancel()
            }
            if (!ClanListGUI.task5.isCancelled()) {
                if (getConfig().getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aWrapped task: " + ClanListGUI.task5.toString()))
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aTimed task 5 canceled successfully"))
                }
                ClanListGUI.task5.cancel()
            }
            if (foliaLib.isUnsupported()) {
                Bukkit.getScheduler().cancelTasks(this)
                if (getConfig().getBoolean("general.developer-debug-mode.enabled")) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aBukkit scheduler tasks canceled successfully"))
                }
            }
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3Background tasks have disabled successfully!"))
        } catch (e: Exception) {
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3Background tasks have disabled successfully!"))
        }

        //Save clansList HashMap to crews.yml
        if (clansFileManager != null) {
            if (!CrewsStorageUtil.getRawCrewsList().isEmpty()) {
                try {
                    CrewsStorageUtil.saveCrews()
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3All crews saved to crews.yml successfully!"))
                } catch (e: IOException) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Failed to save crews to crews.yml!"))
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4See below error for reason!"))
                    e.printStackTrace()
                }
            }
        } else {
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Failed to save crews to crews.yml!"))
        }

        //Saver usermap to usermap.yml
        if (usermapFileManager != null) {
            if (!UsermapStorageUtil.getRawUsermapList().isEmpty()) {
                try {
                    UsermapStorageUtil.saveUsermap()
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3All users saved to usermap.yml successfully!"))
                } catch (e: IOException) {
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Failed to save usermap to usermap.yml!"))
                    console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4See below error for reason!"))
                    e.printStackTrace()
                }
            }
        } else {
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Failed to save usermap to usermap.yml!"))
        }

        //Final plugin shutdown message
        console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3Plugin Version: &d&l$pluginVersion"))
        console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3Has been shutdown successfully"))
        console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &3Goodbye!"))
        console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"))
        plugin = null
        floodgateApi = null
        messagesFileManager = null
        crewsFileManager = null
        crewsGUIFileManager = null
        usermapFileManager = null
    }

    val isFloodgateEnabled: Boolean
        get() = try {
            Class.forName("org.geysermc.floodgate.api.FloodgateApi")
            if (getConfig().getBoolean("general.developer-debug-mode.enabled")) {
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFound FloodgateApi class at:"))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &dorg.geysermc.floodgate.api.FloodgateApi"))
            }
            true
        } catch (e: ClassNotFoundException) {
            if (getConfig().getBoolean("general.developer-debug-mode.enabled")) {
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aCould not find FloodgateApi class at:"))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &dorg.geysermc.floodgate.api.FloodgateApi"))
            }
            false
        }
    val isPlugManXEnabled: Boolean
        get() = try {
            Class.forName("com.rylinaux.plugman.PlugMan")
            if (getConfig().getBoolean("general.developer-debug-mode.enabled")) {
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFound PlugManX main class at:"))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &dcom.rylinaux.plugman.PlugMan"))
            }
            true
        } catch (e: ClassNotFoundException) {
            if (getConfig().getBoolean("general.developer-debug-mode.enabled")) {
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aCould not find PlugManX main class at:"))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &dcom.rylinaux.plugman.PlugMan"))
            }
            false
        }
    val isPlaceholderAPIEnabled: Boolean
        get() = try {
            Class.forName("me.clip.placeholderapi.PlaceholderAPIPlugin")
            if (getConfig().getBoolean("general.developer-debug-mode.enabled")) {
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aFound PlaceholderAPI main class at:"))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &dme.clip.placeholderapi.PlaceholderAPIPlugin"))
            }
            true
        } catch (e: ClassNotFoundException) {
            if (getConfig().getBoolean("general.developer-debug-mode.enabled")) {
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &aCould not find PlaceholderAPI main class at:"))
                console.sendMessage(ColorUtils.translateColorCodes("&6Crews-Debug: &dme.clip.placeholderapi.PlaceholderAPIPlugin"))
            }
            false
        }

    companion object {
        var plugin: Crews? = null
            private set
        private var floodgateApi: FloodgateApi? = null
        var connectedPlayers: HashMap<Player, String> = HashMap()
        var bedrockPlayers: HashMap<Player, String> = HashMap()
        private val playerMenuUtilityMap: HashMap<Player, PlayerMenuUtility> = HashMap()
        fun getPlayerMenuUtility(player: Player): PlayerMenuUtility? {
            val playerMenuUtility: PlayerMenuUtility
            return if (!playerMenuUtilityMap.containsKey(player)) {
                playerMenuUtility = PlayerMenuUtility(player)
                playerMenuUtilityMap[player] = playerMenuUtility
                playerMenuUtility
            } else {
                playerMenuUtilityMap[player]
            }
        }

        fun getFloodgateApi(): FloodgateApi? {
            return floodgateApi
        }
    }
}
