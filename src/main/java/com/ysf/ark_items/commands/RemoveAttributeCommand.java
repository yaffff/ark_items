package com.ysf.ark_items.commands;

import com.ysf.ark_items.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class RemoveAttributeCommand implements CommandExecutor {

    private final ItemManager itemManager;

    public RemoveAttributeCommand(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /removeattribute <attributeName>");
            return true;
        }

        String attrName = args[0];
        if (itemManager.removeAttribute(attrName)) {
            sender.sendMessage("§aAttribute '" + attrName + "' removed from all items.");
        } else {
            sender.sendMessage("§cAttribute '" + attrName + "' does not exist.");
        }

        return true;
    }
}
