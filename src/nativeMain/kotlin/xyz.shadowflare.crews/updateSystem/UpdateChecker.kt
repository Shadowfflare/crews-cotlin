package xyz.shadowflare.crews.updateSystem

import com.tcoded.folialib.FoliaLib

class UpdateChecker(private val resourceId: Int) {
    var console: ConsoleCommandSender = Bukkit.getConsoleSender()
    var messagesConfig: FileConfiguration = Crews.getPlugin().messagesFileManager.getMessagesConfig()
    fun getVersion(consumer: Consumer<String?>) {
        val foliaLib = FoliaLib(Crews.getPlugin())
        foliaLib.getImpl().runAsync { task ->
            try {
                URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream()
                    .use { inputStream ->
                        Scanner(inputStream).use { scanner ->
                            if (scanner.hasNext()) {
                                consumer.accept(scanner.next())
                            }
                        }
                    }
            } catch (exception: IOException) {
                console.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("update-check-failure") + exception.getMessage()))
            }
        }
    }
}
