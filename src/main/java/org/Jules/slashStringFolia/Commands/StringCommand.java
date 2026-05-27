package org.Jules.slashStringFolia.Commands;

import org.Jules.slashStringFolia.SlashStringFolia;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class StringCommand implements CommandExecutor {

    private final SlashStringFolia plugin;

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    private long cooldownSeconds;
    private String cooldownMsg;
    private String successMsg;

    public StringCommand(SlashStringFolia plugin) {
        this.plugin = plugin;
        reload();
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();

        cooldowns.entrySet().removeIf(e ->
                (now - e.getValue()) > (cooldownSeconds * 1000L * 2)
        );

        Long last = cooldowns.get(uuid);
        if (last != null) {
            long timeLeft = (last / 1000 + cooldownSeconds) - (now / 1000);

            if (timeLeft > 0) {
                player.sendMessage(cooldownMsg.replace("%time%", String.valueOf(timeLeft)));
                return true;
            }
        }

        cooldowns.put(uuid, now);

        World world = player.getWorld();
        var loc = player.getLocation();
        var inv = player.getInventory();

        ItemStack stringStack = new ItemStack(Material.STRING, 64);

        for (int slot = 0; slot < 36; slot++) {

            ItemStack item = inv.getItem(slot);

            if (item == null || item.getType() == Material.AIR) {
                inv.setItem(slot, stringStack.clone());
            } else {
                Item dropped = world.dropItemNaturally(loc, stringStack.clone());
                dropped.setPickupDelay(0);
            }
        }

        player.sendMessage(successMsg);
        return true;
    }

    public void reload() {
        this.cooldownSeconds = plugin.getConfig().getLong("cooldown-seconds");
        this.cooldownMsg = Objects.requireNonNull(plugin.getConfig().getString("cooldown-message")).replace("&", "§");
        this.successMsg = Objects.requireNonNull(plugin.getConfig().getString("success-message")).replace("&", "§");
    }
}