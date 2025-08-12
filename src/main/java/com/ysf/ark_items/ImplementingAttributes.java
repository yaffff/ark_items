package com.ysf.ark_items;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ImplementingAttributes implements Listener {

    private final ItemManager itemManager;

    public ImplementingAttributes(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() != Material.STICK) return;
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        // Get plain text display name from item meta
        String displayName = PlainTextComponentSerializer.plainText().serialize(item.getItemMeta().displayName());

        // Get meleeDamage value from your ItemManager using display name as key
        Integer damage = itemManager.getItemAttribute(displayName, "meleeDamage", Integer.class);

        if (damage != null && damage > 0) {
            event.setDamage(damage);
        }
    }
}
