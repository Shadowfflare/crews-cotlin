package xyz.shadowflare.crews.files

import org.bukkit.Bukkit

class CrewsFileManager {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    private var plugin: Crews? = null
    private var dataConfig: FileConfiguration? = null
    private var configFile: File? = null
    fun ClansFileManager(plugin: Crews?) {
        this.plugin = plugin
        saveDefaultClansConfig()
    }

    fun reloadClansConfig() {
        if (configFile == null) {
            configFile = File(plugin.getDataFolder(), "clans.yml")
        }
        dataConfig = YamlConfiguration.loadConfiguration(configFile)
        val defaultStream: InputStream = plugin.getResource("clans.yml")
        if (defaultStream != null) {
            val defaultConfig: YamlConfiguration = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            dataConfig.setDefaults(defaultConfig)
        }
    }

    val clansConfig: FileConfiguration?
        get() {
            if (dataConfig == null) {
                reloadClansConfig()
            }
            return dataConfig
        }

    fun saveClansConfig() {
        if (dataConfig == null || configFile == null) {
            return
        }
        try {
            clansConfig.save(configFile)
        } catch (e: IOException) {
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Could not save clans.yml"))
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Check the below message for the reasons!"))
            e.printStackTrace()
        }
    }

    fun saveDefaultClansConfig() {
        if (configFile == null) {
            configFile = File(plugin.getDataFolder(), "clans.yml")
        }
        if (!configFile.exists()) {
            plugin.saveResource("clans.yml", false)
        }
    }
}
