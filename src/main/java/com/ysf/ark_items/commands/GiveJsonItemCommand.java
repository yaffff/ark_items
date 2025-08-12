package com.ysf.ark_items.commands;

import com.ysf.ark_items.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GiveJsonItemCommand implements CommandExecutor, TabCompleter {

    private final ItemManager itemManager;

    public GiveJsonItemCommand(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage("Usage: /" + label + " <itemKey>");
            return true;
        }

        String itemKey = args[0];
        ItemStack item = itemManager.getCustomItem(itemKey);

        if (item == null || item.getType().isAir()) {
            player.sendMessage("§cUnknown Ark_items key: " + itemKey);
            return true;
        }

        player.getInventory().addItem(item);
        player.sendMessage("§aGave you Ark item: §e" + itemKey);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> matches = new ArrayList<>();
            for (String key : itemManager.getAllItemKeys()) {
                if (key.toLowerCase().startsWith(args[0].toLowerCase())) {
                    matches.add(key);
                }
            }
            return matches;
        }
        return new ArrayList<>();
    }
}
