package org.Jules.slashStringFolia.Commands;

import org.Jules.slashStringFolia.SlashStringFolia;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;

public class ReloadCommand implements CommandExecutor, TabCompleter {

    private final SlashStringFolia plugin;
    private final StringCommand stringCommand;

    public ReloadCommand(SlashStringFolia plugin, StringCommand stringCommand) {
        this.plugin = plugin;
        this.stringCommand = stringCommand;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {

            if (!sender.hasPermission("stringfolia.reload")) {
                sender.sendMessage("§cNo permission.");
                return true;
            }

            plugin.reloadConfig();
            stringCommand.reload();

            sender.sendMessage("§aSlashStringFolia config reloaded.");
            return true;
        }

        sender.sendMessage("§cUsage: /stringfolia reload");
        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String alias, String[] args) {

        if (args.length == 1 && "reload".startsWith(args[0].toLowerCase())) {
            return Collections.singletonList("reload");
        }

        return Collections.emptyList();
    }
}