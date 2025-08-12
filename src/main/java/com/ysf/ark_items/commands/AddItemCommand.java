package com.ysf.ark_items.commands;


import com.ysf.ark_items.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AddItemCommand implements CommandExecutor {
    private final ItemManager itemManager;

    public AddItemCommand(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /additem <itemName>");
            return true;
        }
        String name = String.join(" ", args);
        if (itemManager.addItem(name)) {
            sender.sendMessage("§aItem added: §e" + name);
        } else {
            sender.sendMessage("§cItem already exists.");
        }
        return true;
    }
}
