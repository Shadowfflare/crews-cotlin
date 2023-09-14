package xyz.shadowflare.crews.files

import org.bukkit.Bukkit

class MessagesFileManager(plugin: Crews) {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    private val plugin: Crews
    private var dataConfig: FileConfiguration? = null
    private var configFile: File? = null

    init {
        this.plugin = plugin
        saveDefaultMessagesConfig()
    }

    fun reloadMessagesConfig() {
        if (configFile == null) {
            configFile = File(plugin.getDataFolder(), "messages.yml")
        }
        dataConfig = YamlConfiguration.loadConfiguration(configFile)
        val defaultStream: InputStream = plugin.getResource("messages.yml")
        if (defaultStream != null) {
            val defaultConfig: YamlConfiguration = YamlConfiguration.loadConfiguration(InputStreamReader(defaultStream))
            dataConfig.setDefaults(defaultConfig)
        }
    }

    val messagesConfig: FileConfiguration?
        get() {
            if (dataConfig == null) {
                reloadMessagesConfig()
            }
            return dataConfig
        }

    fun saveMessagesConfig() {
        if (dataConfig == null || configFile == null) {
            return
        }
        try {
            messagesConfig.save(configFile)
        } catch (e: IOException) {
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Could not save messages.yml"))
            console.sendMessage(ColorUtils.translateColorCodes("&6ClansLite: &4Check the below message for the reasons!"))
            e.printStackTrace()
        }
    }

    fun saveDefaultMessagesConfig() {
        if (configFile == null) {
            configFile = File(plugin.getDataFolder(), "messages.yml")
        }
        if (!configFile.exists()) {
            plugin.saveResource("messages.yml", false)
        }
    }
}