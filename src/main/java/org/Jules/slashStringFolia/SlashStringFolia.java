package org.Jules.slashStringFolia;

import org.Jules.slashStringFolia.Commands.ReloadCommand;
import org.Jules.slashStringFolia.Commands.StringCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SlashStringFolia extends JavaPlugin {

    @Override
    public void onEnable() {

        saveDefaultConfig();

        StringCommand stringCommand = new StringCommand(this);
        ReloadCommand reloadCommand = new ReloadCommand(this, stringCommand);

        Objects.requireNonNull(getCommand("string")).setExecutor(stringCommand);

        Objects.requireNonNull(getCommand("stringfolia")).setExecutor(reloadCommand);
        Objects.requireNonNull(getCommand("stringfolia")).setTabCompleter(reloadCommand);

        getLogger().info("SlashStringFolia enabled.");
    }
}