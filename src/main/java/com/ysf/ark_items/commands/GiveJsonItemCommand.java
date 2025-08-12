package com.ysf.ark_items.commands;

import com.ysf.ark_items.ItemManager;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class GiveJsonItemCommand implements CommandExecutor, TabCompleter {

    private final ItemManager itemManager;

    public GiveJsonItemCommand(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can run this command.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("Usage: /givecustomitem <itemkey>");
            return true;
        }

        String itemKey = args[0];
        if (!itemManager.getData().items.containsKey(itemKey)) {
            player.sendMessage("Item key not found: " + itemKey);
            return true;
        }

        // Get material from EverythingIsAStick attribute (stored as string)
        String materialName = itemManager.getItemAttribute(itemKey, "EverythingIsAStick", String.class);
        if (materialName == null) {
            player.sendMessage("Material attribute missing for item: " + itemKey);
            return true;
        }

        var material = org.bukkit.Material.matchMaterial(materialName);
        if (material == null) {
            player.sendMessage("Invalid material: " + materialName);
            return true;
        }

        // Create ItemStack with that material
        ItemStack item = new ItemStack(material);

        // Modify ItemMeta
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {

            meta.displayName(Component.text(itemKey).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GOLD));

            // Get setItemModel string attribute from JSON
            String modelName = itemManager.getItemAttribute(itemKey, "setItemModel", String.class);
            if (modelName != null && !modelName.isEmpty()) {
                meta.setItemModel(new NamespacedKey("minecraft", modelName.toLowerCase()));
            }


            int damagelore = itemManager.getItemAttribute(itemKey,"meleeDamage", Integer.class);
            if (damagelore > 0) {
            }


            int weight = itemManager.getItemAttribute(itemKey,"Weight", Integer.class);



            meta.lore(itemManager.buildItemLore(itemKey));
            item.setItemMeta(meta);
        }

        // Give item to player
        player.getInventory().addItem(item);
        player.sendMessage("Gave you custom item: " + itemKey);

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> keys = new ArrayList<>(itemManager.getData().items.keySet());
            String partial = args[0].toLowerCase();
            keys.removeIf(key -> !key.toLowerCase().startsWith(partial));
            return keys;
        }
        return List.of();
    }
}
