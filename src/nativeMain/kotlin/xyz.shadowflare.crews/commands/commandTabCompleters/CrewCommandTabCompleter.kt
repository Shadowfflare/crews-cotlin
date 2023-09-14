package xyz.shadowflare.crews.commands.commandTabCompleters

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class CrewCommandTabCompleter : TabCompleter {
    var arguments: List<String> = ArrayList()
    @Nullable
    @Override
    fun onTabComplete(
        @NotNull sender: CommandSender?,
        @NotNull command: Command?,
        @NotNull label: String?,
        @NotNull args: Array<String>
    ): List<String>? {
        if (arguments.isEmpty()) {
            arguments.add("create")
            arguments.add("disband")
            arguments.add("invite")
            arguments.add("join")
            arguments.add("leave")
            arguments.add("kick")
            arguments.add("info")
            arguments.add("list")
            arguments.add("prefix")
            arguments.add("transfer")
            arguments.add("ally add")
            arguments.add("ally remove")
            arguments.add("enemy add")
            arguments.add("enemy remove")
            arguments.add("pvp")
            arguments.add("sethome")
            arguments.add("delhome")
            arguments.add("home")
            arguments.add("playerpoints")
            arguments.add("points deposit")
            arguments.add("points withdraw")
        }
        val result: List<String> = ArrayList()
        if (args.size == 1) {
            for (a in arguments) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(a)
                }
            }
            return result
        }
        return null
    }
}
