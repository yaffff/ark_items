package com.ysf.ark_items;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CommandTabHandler implements TabCompleter {

    private final ItemManager itemManager;

    public CommandTabHandler(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        String name = cmd.getName().toLowerCase();

        switch (name) {
            case "removeattribute":
                if (args.length == 1) {
                    return filter(itemManager.getData().attributes.keySet(), args[0]);
                }
                break;

            case "setattribute":
                if (args.length == 1) {
                    return filter(itemManager.getData().items.keySet(), args[0]);
                }
                if (args.length == 2) {
                    return filter(itemManager.getData().attributes.keySet(), args[1]);
                }
                if (args.length == 3) {
                    String attrName = args[1];
                    ItemManager.AttributeDefinition def = itemManager.getData().attributes.get(attrName);
                    if (def != null) {
                        switch (def.type) {
                            case "boolean":
                                return filter(Arrays.asList("true", "false"), args[2]);
                            case "material":
                                return filter(Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()), args[2].toUpperCase());
                        }
                    }
                }
                break;

            case "addattribute":
                if (args.length == 2) {
                    return filter(Arrays.asList("string", "boolean", "int", "float", "long", "material"), args[1].toLowerCase());
                }
                if (args.length == 3) {
                    String type = args[1].toLowerCase();
                    switch (type) {
                        case "boolean":
                            return filter(Arrays.asList("true", "false"), args[2].toLowerCase());
                        case "material":
                            return filter(Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()), args[2].toUpperCase());
                    }
                }
                break;
        }
        return new ArrayList<>();
    }

    private List<String> filter(Iterable<String> source, String input) {
        List<String> result = new ArrayList<>();
        for (String s : source) {
            if (s.toLowerCase(Locale.ROOT).startsWith(input.toLowerCase(Locale.ROOT))) {
                result.add(s);
            }
        }
        return result;
    }
    
}
