package com.ysf.ark_items.commands;

import com.ysf.ark_items.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetAttributeCommand implements CommandExecutor {
    private final ItemManager itemManager;

    public SetAttributeCommand(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /setattribute <itemName> <attrName> <value>");
            return true;
        }

        String itemName = args[0];
        String attrName = args[1];
        String valueStr = args[2];

        if (!itemManager.getData().attributes.containsKey(attrName)) {
            sender.sendMessage("§cAttribute not found.");
            return true;
        }

        String type = itemManager.getData().attributes.get(attrName).type;
        Object value;

        try {
            switch (type) {
                case "string": value = valueStr; break;
                case "boolean": value = Boolean.parseBoolean(valueStr); break;
                case "int": value = Integer.parseInt(valueStr); break;
                case "float": value = Float.parseFloat(valueStr); break;
                case "long": value = Long.parseLong(valueStr); break;
                default:
                    sender.sendMessage("§cUnknown attribute type.");
                    return true;
            }
        } catch (Exception e) {
            sender.sendMessage("§cInvalid value type.");
            return true;
        }

        if (itemManager.setAttribute(itemName, attrName, value)) {
            sender.sendMessage("§aUpdated §e" + itemName + "§a's §e" + attrName + "§a to §e" + value);
        } else {
            sender.sendMessage("§cFailed to update attribute.");
        }
        return true;
    }
}
