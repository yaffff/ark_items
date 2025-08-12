package com.ysf.ark_items.commands;

import com.ysf.ark_items.ItemManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AddAttributeCommand implements CommandExecutor {
    private final ItemManager itemManager;

    public AddAttributeCommand(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /addattribute <attrName> <type> <defaultValue>");
            return true;
        }

        String name = args[0];
        String type = args[1].toLowerCase();
        String valueStr = args[2];
        Object defaultValue;

        try {
            switch (type) {
                case "string": defaultValue = valueStr; break;
                case "boolean": defaultValue = Boolean.parseBoolean(valueStr); break;
                case "int": defaultValue = Integer.parseInt(valueStr); break;
                case "float": defaultValue = Float.parseFloat(valueStr); break;
                case "long": defaultValue = Long.parseLong(valueStr); break;
                case "material":
                    try {
                        defaultValue = Material.valueOf(valueStr.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage("§cInvalid material: §e" + valueStr);
                        return true;
                    }
                    break;

                default:
                    sender.sendMessage("§cInvalid type. Use string, boolean, int, float, or long.");
                    return true;
            }
        } catch (Exception e) {
            sender.sendMessage("§cInvalid value for type: " + type);
            return true;
        }

        if (itemManager.addAttribute(name, type, defaultValue)) {
            sender.sendMessage("§aAttribute added: §e" + name);
        } else {
            sender.sendMessage("§cAttribute already exists.");
        }
        return true;
    }
}
