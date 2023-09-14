package xyz.shadowflare.crews.files

import org.bukkit.Bukkit

class CrewGUIFileManager(plugin: Crews) {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    private val plugin: Crews
    private var dataConfig: FileConfiguration? = null
    private var configFile: File? = null

    init {
        this.plugin = plugin
        saveDefaultClanGUIConfig()
    }

    fun reloadClanGUIConfig() {
        if (configFile == null) {
            configFile = File(plugin.getDataFolder(), "crewgui.yml")
        }
        dataConfig = YamlConfiguration.loadConfiguration(configFile)
        val defaultStream: InputStream = plugin.getResource("crewgui.yml")
        if (defaultStream != null) {
            val defaultConfig: YamlConfiguration = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            dataConfig.setDefaults(defaultConfig)
        }
    }

    val crewGUIConfig: FileConfiguration?
        get() {
            if (dataConfig == null) {
                reloadClanGUIConfig()
            }
            return dataConfig
        }

    fun saveCrewGUIConfig() {
        if (dataConfig == null || configFile == null) {
            return
        }
        try {
            crewGUIConfig.save(configFile)
        } catch (e: IOException) {
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Could not save crewgui.yml"))
            console.sendMessage(ColorUtils.translateColorCodes("&6Crews: &4Check the below message for the reasons!"))
            e.printStackTrace()
        }
    }

    fun saveDefaultCrewGUIConfig() {
        if (configFile == null) {
            configFile = File(plugin.getDataFolder(), "crewgui.yml")
        }
        if (!configFile.exists()) {
            plugin.saveResource("crewgui.yml", false)
        }
    }
}
