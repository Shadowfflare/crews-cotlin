package xyz.shadowflare.crews.commands.commandTabCompleters

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class ChestCommandTabCompleter : TabCompleter {
    var arguments: List<String> = ArrayList()
    @Nullable
    @Override
    fun onTabComplete(
        @NotNull sender: CommandSender?,
        @NotNull command: Command?,
        @NotNull label: String?,
        args: Array<String>
    ): List<String>? {
        if (arguments.isEmpty()) {
            arguments.add("lock")
            arguments.add("unlock")
            arguments.add("buylock")
            arguments.add("accesslist")
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
