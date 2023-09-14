package xyz.shadowflare.crews.files

import org.bukkit.Bukkit

class UsermapFileManager(plugin: Crews) {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    private val plugin: Crews
    private var dataConfig: FileConfiguration? = null
    private var configFile: File? = null

    init {
        this.plugin = plugin
        saveDefaultUsermapConfig()
    }

    fun reloadUsermapConfig() {
        if (configFile == null) {
            configFile = File(plugin.getDataFolder(), "usermap.yml")
        }
        dataConfig = YamlConfiguration.loadConfiguration(configFile)
        val defaultStream: InputStream = plugin.getResource("usermap.yml")
        if (defaultStream != null) {
            val defaultConfig: YamlConfiguration = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            dataConfig.setDefaults(defaultConfig)
        }
    }

    val usermapConfig: FileConfiguration?
        get() {
            if (dataConfig == null) {
                reloadUsermapConfig()
            }
            return dataConfig
        }

    fun saveUsermapConfig() {
        if (dataConfig == null || configFile == null) {
            return
        }
        try {
            usermapConfig.save(configFile)
        } catch (e: IOException) {
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Could not save usermap.yml"))
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Check the below message for the reasons!"))
            e.printStackTrace()
        }
    }

    fun saveDefaultUsermapConfig() {
        if (configFile == null) {
            configFile = File(plugin.getDataFolder(), "usermap.yml")
        }
        if (!configFile.exists()) {
            plugin.saveResource("usermap.yml", false)
        }
    }
}
