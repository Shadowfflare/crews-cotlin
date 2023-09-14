package xyz.shadowflare.crews.expansions

import me.clip.placeholderapi.expansion.PlaceholderExpansion

class PlayerCrewExpansion : PlaceholderExpansion() {
    @get:NotNull
    @get:Override
    val identifier: String
        get() = "crews"

    @get:NotNull
    @get:Override
    val author: String
        get() = Crews.getPlugin().getDescription().getAuthors().get(0)

    @get:NotNull
    @get:Override
    val version: String
        get() = Crews.getPlugin().getDescription().getVersion()

    @Override
    fun persist(): Boolean {
        return true
    }

    @Override
    fun onRequest(player: OfflinePlayer?, params: String): String? {
        val configFile: FileConfiguration = Crews.getPlugin().getConfig()
        val crewOwner: Crew = CrewsStorageUtil.findCrewByOfflineOwner(player)
        val crewMember: Crew = CrewsStorageUtil.findCrewByOfflinePlayer(player)
        val crewPlayer: CrewPlayer = UsermapStorageUtil.getClanPlayerByBukkitOfflinePlayer(player)
        if (params.equalsIgnoreCase("crewName")) {
            //%crews_crewName%
            return if (crewOwner != null) {
                ColorUtils.translateColorCodes(crewOwner.getCrewFinalName() + "&r ")
            } else if (crewMember != null) {
                ColorUtils.translateColorCodes(crewMember.getCrewFinalName() + "&r ")
            } else {
                ""
            }
        }
        if (params.equalsIgnoreCase("crewPrefix")) {
            //%crew_crewPrefix%
            val openBracket: String = configFile.getString("crew-tags.brackets-opening")
            val closeBracket: String = configFile.getString("crew-tags.brackets-closing")
            return if (crewOwner != null) {
                if (configFile.getBoolean("crew-tags.prefix-add-brackets")) {
                    if (configFile.getBoolean("crew-tags.prefix-add-space-after")) {
                        ColorUtils.translateColorCodes(openBracket + crewOwner.getCrewPrefix() + closeBracket + "&r ")
                    } else {
                        ColorUtils.translateColorCodes(openBracket + crewOwner.getCrewPrefix() + closeBracket + "&r")
                    }
                } else {
                    if (configFile.getBoolean("clan-tags.prefix-add-space-after")) {
                        ColorUtils.translateColorCodes(crewOwner.getCrewPrefix() + "&r ")
                    } else {
                        ColorUtils.translateColorCodes(crewOwner.getCrewPrefix() + "&r")
                    }
                }
            } else if (crewMember != null) {
                if (configFile.getBoolean("crew-tags.prefix-add-brackets")) {
                    if (configFile.getBoolean("crew-tags.prefix-add-space-after")) {
                        ColorUtils.translateColorCodes(openBracket + crewMember.getCrewPrefix() + closeBracket + "&r ")
                    } else {
                        ColorUtils.translateColorCodes(openBracket + crewMember.getCrewPrefix() + closeBracket + "&r")
                    }
                } else {
                    if (configFile.getBoolean("crew-tags.prefix-add-space-after")) {
                        ColorUtils.translateColorCodes(crewMember.getCrewPrefix() + "&r ")
                    } else {
                        ColorUtils.translateColorCodes(crewMember.getCrewPrefix() + "&r")
                    }
                }
            } else {
                ""
            }
        }
        if (params.equalsIgnoreCase("friendlyFire")) {
            //%clansLite_friendlyFire%
            return if (crewOwner != null) {
                String.valueOf(crewOwner.isFriendlyFireAllowed())
            } else if (crewMember != null) {
                String.valueOf(crewMember.isFriendlyFireAllowed())
            } else {
                ""
            }
        }
        if (params.equalsIgnoreCase("crewHomeSet")) {
            //%crews_crewHomeSet%
            return if (crewOwner != null) {
                String.valueOf(CrewsStorageUtil.isHomeSet(crewOwner))
            } else if (crewMember != null) {
                String.valueOf(CrewsStorageUtil.isHomeSet(crewMember))
            } else {
                ""
            }
        }
        if (params.equalsIgnoreCase("crewMembersSize")) {
            //%crews_crewMembersSize%
            return if (crewOwner != null) {
                String.valueOf(crewOwner.getCrewMembers().size())
            } else if (crewMember != null) {
                String.valueOf(crewMember.getCrewMembers().size())
            } else {
                ""
            }
        }
        if (params.equalsIgnoreCase("crewAllySize")) {
            //%crews_crewAllySize%
            return if (crewOwner != null) {
                String.valueOf(crewOwner.getCrewAllies().size())
            } else if (crewMember != null) {
                String.valueOf(crewMember.getCrewAllies().size())
            } else {
                ""
            }
        }
        if (params.equalsIgnoreCase("crewEnemySize")) {
            //%crews_crewEnemySize%
            return if (crewOwner != null) {
                String.valueOf(crewOwner.getCrewEnemies().size())
            } else if (crewMember != null) {
                String.valueOf(crewMember.getCrewEnemies().size())
            } else {
                ""
            }
        }
        if (params.equalsIgnoreCase("playerPointBalance")) {
            //%crews_playerPointBalance%
            return if (crewPlayer != null) {
                String.valueOf(crewPlayer.getPointBalance())
            } else {
                ""
            }
        }
        if (params.equalsIgnoreCase("crewPointBalance")) {
            //%crews_crewPointBalance%
            return if (crewOwner != null) {
                String.valueOf(crewOwner.getCrewPoints())
            } else if (crewMember != null) {
                String.valueOf(crewMember.getCrewPoints())
            } else {
                ""
            }
        }
        if (params.equalsIgnoreCase("crewChestMaxAllowed")) {
            //%crews_crewChestMaxAllowed%
            return if (crewOwner != null) {
                String.valueOf(crewOwner.getMaxAllowedProtectedChests())
            } else if (crewMember != null) {
                String.valueOf(crewMember.getMaxAllowedProtectedChests())
            } else {
                ""
            }
        }
        return if (params.equalsIgnoreCase("crewChestCurrentLocked")) {
            //%crews_crewChestCurrentLocked%
            if (crewOwner != null) {
                String.valueOf(crewOwner.getProtectedChests().size())
            } else if (crewMember != null) {
                String.valueOf(crewMember.getProtectedChests().size())
            } else {
                ""
            }
        } else null
    }
}
