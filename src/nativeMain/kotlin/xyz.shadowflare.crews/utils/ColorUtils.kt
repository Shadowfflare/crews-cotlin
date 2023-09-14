package xyz.shadowflare.crews.utils

import org.bukkit.ChatColor

object ColorUtils {
    const val WITH_DELIMITER = "((?<=%1\$s)|(?=%1\$s))"

    /**
     * @param text The string of text to apply color/effects to
     * @return Returns a string of text with color/effects applied
     */
    fun translateColorCodes(text: String): String {
        val texts: Array<String> = text.split(String.format(WITH_DELIMITER, "&"))
        val finalText = StringBuilder()
        var i = 0
        while (i < texts.size) {
            if (texts[i].equalsIgnoreCase("&")) {
                i++
                if (texts[i].charAt(0) === '#') {
                    finalText.append(net.md_5.bungee.api.ChatColor.of(texts[i].substring(0, 7)) + texts[i].substring(7))
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]))
                }
            } else {
                finalText.append(texts[i])
            }
            i++
        }
        return finalText.toString()
    }
}
