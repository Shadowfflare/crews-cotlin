package xyz.shadowflare.crews.listeners

import org.apache.commons.lang3.StringUtils

class PlayerMessageEvent : Listener {
    var configFile: FileConfiguration = Crews.getPlugin().getConfig()
    @EventHandler
    fun onChatPlayer(event: AsyncPlayerChatEvent) {
        val crewMergeTag = "{CREW}"
        val player: Player = event.getPlayer()
        var format: String = event.getFormat()
        val openBracket: String = configFile.getString("crew-tags.brackets-opening")
        val closeBracket: String = configFile.getString("crew-tags.brackets-closing")
        if (CrewsStorageUtil.findCrewByPlayer(player) == null) {
            format = StringUtils.replace(format, crewMergeTag, "")
        } else if (CrewsStorageUtil.findCrewByOwner(player) != null) {
            val crew: Crew = CrewsStorageUtil.findCrewByOwner(player)
            if (configFile.getBoolean("clan-tags.prefix-add-brackets")) {
                format = if (configFile.getBoolean("clan-tags.prefix-add-space-after")) {
                    StringUtils.replace(
                        format,
                        crewMergeTag,
                        ColorUtils.translateColorCodes(openBracket + crew.getCrewPrefix() + closeBracket + "&r ")
                    )
                } else {
                    StringUtils.replace(
                        format,
                        crewMergeTag,
                        ColorUtils.translateColorCodes(openBracket + crew.getCrewPrefix() + closeBracket + "&r")
                    )
                }
                event.setFormat(format)
                return
            } else {
                format = if (configFile.getBoolean("crew-tags.prefix-add-space-after")) {
                    StringUtils.replace(
                        format,
                        crewMergeTag,
                        ColorUtils.translateColorCodes(crew.getCrewPrefix() + " &r")
                    )
                } else {
                    StringUtils.replace(
                        format,
                        crewMergeTag,
                        ColorUtils.translateColorCodes(crew.getCrewPrefix() + "&r")
                    )
                }
                event.setFormat(format)
                return
            }
        } else {
            val crew: Crew = CrewsStorageUtil.findCrewByPlayer(player)
            if (configFile.getBoolean("crew-tags.prefix-add-brackets")) {
                format = if (configFile.getBoolean("crew-tags.prefix-add-space-after")) {
                    StringUtils.replace(
                        format,
                        crewMergeTag,
                        ColorUtils.translateColorCodes(openBracket + crew.getCrewPrefix() + closeBracket + "&r ")
                    )
                } else {
                    StringUtils.replace(
                        format,
                        crewMergeTag,
                        ColorUtils.translateColorCodes(openBracket + crew.getClanPrefix() + closeBracket + "&r")
                    )
                }
                event.setFormat(format)
                return
            } else {
                format = if (configFile.getBoolean("crew-tags.prefix-add-space-after")) {
                    StringUtils.replace(
                        format,
                        crewMergeTag,
                        ColorUtils.translateColorCodes(crew.getCrewPrefix() + " &r")
                    )
                } else {
                    StringUtils.replace(
                        format,
                        crewMergeTag,
                        ColorUtils.translateColorCodes(crew.getCrewPrefix() + "&r")
                    )
                }
                event.setFormat(format)
                return
            }
        }
        event.setFormat(format)
    }
}
