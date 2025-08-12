package com.ysf.ark_items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager {
    private final File file;
    private final Gson gson;
    private Data data;

    public static class AttributeDefinition {
        public String type;
        public Object defaultValue;
    }

    public static class Data {
        public Map<String, AttributeDefinition> attributes = new HashMap<>();
        public Map<String, Map<String, Object>> items = new HashMap<>();
    }

    public ItemManager(Ark_items plugin) {
        file = new File(plugin.getDataFolder(), "items.json");
        gson = new GsonBuilder().setPrettyPrinting().create();

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        loadData();
    }

    public void loadData() {
        try {
            if (!file.exists()) {
                data = new Data();
                saveData();
                return;
            }

            FileReader reader = new FileReader(file);
            Type type = new TypeToken<Data>() {}.getType();
            Data loadedData = gson.fromJson(reader, type);
            reader.close();

            if (loadedData == null) loadedData = new Data();

            if (data == null) {
                data = loadedData;
            } else {
                data.attributes.putAll(loadedData.attributes);
                data.items.putAll(loadedData.items);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (data == null) data = new Data();
        }
    }

    public void saveData() {
        try {
            FileWriter writer = new FileWriter(file);
            gson.toJson(data, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean addItem(String name) {
        if (data.items.containsKey(name)) return false;
        Map<String, Object> attrs = new HashMap<>();
        for (String attr : data.attributes.keySet()) {
            attrs.put(attr, data.attributes.get(attr).defaultValue);
        }
        data.items.put(name, attrs);
        saveData();
        return true;
    }

    public boolean removeItem(String name) {
        if (!data.items.containsKey(name)) return false;
        data.items.remove(name);
        saveData();
        return true;
    }

    public void clearAllData() {
        data = new Data();
        saveData();
    }

    public boolean addAttribute(String name, String type, Object defaultValue) {
        if (data.attributes.containsKey(name)) return false;
        AttributeDefinition def = new AttributeDefinition();
        def.type = type.toLowerCase();
        def.defaultValue = defaultValue;
        data.attributes.put(name, def);

        for (Map<String, Object> item : data.items.values()) {
            item.put(name, defaultValue);
        }
        saveData();
        return true;
    }

    public boolean setAttribute(String itemName, String attrName, Object value) {
        if (!data.items.containsKey(itemName)) return false;
        if (!data.attributes.containsKey(attrName)) return false;

        String type = data.attributes.get(attrName).type;
        if (!isValueOfType(value, type)) return false;

        data.items.get(itemName).put(attrName, value);
        saveData();
        return true;
    }

    public boolean removeAttribute(String name) {
        if (!data.attributes.containsKey(name)) return false;
        data.attributes.remove(name);
        for (Map<String, Object> itemAttrs : data.items.values()) {
            itemAttrs.remove(name);
        }
        saveData();
        return true;
    }

    private boolean isValueOfType(Object value, String type) {
        switch (type) {
            case "string": return value instanceof String;
            case "boolean": return value instanceof Boolean;
            case "int": return value instanceof Integer;
            case "float": return value instanceof Float;
            case "long": return value instanceof Long;
            case "material": return value instanceof Material;
            default: return false;
        }
    }

    public ItemStack getCustomItem(String itemKey) {
        if (!data.items.containsKey(itemKey)) return null;

        // Get Material
        String matName = getItemAttribute(itemKey, "EverythingIsAStick", String.class);
        Material mat = matName != null ? Material.matchMaterial(matName) : null;
        if (mat == null) return null;

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            // Name
            meta.displayName(Component.text(itemKey)
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.GOLD));

            // Model
            String modelName = getItemAttribute(itemKey, "setItemModel", String.class);
            if (modelName != null && !modelName.isEmpty()) {
                meta.setItemModel(new NamespacedKey("minecraft", modelName.toLowerCase()));
            }

            meta.lore(buildItemLore(itemKey));
            item.setItemMeta(meta);
        }
        return item;
    }

    public List<Component> buildItemLore(String itemKey) {
        List<Component> lore = new ArrayList<>();

        Integer damage = getItemAttribute(itemKey, "meleeDamage", Integer.class);
        if (damage != null && damage > 0) {
            lore.add(Component.text(ChatColor.RED + "Damage: " + damage));
        }

        Integer weight = getItemAttribute(itemKey, "Weight", Integer.class);
        if (weight != null) {
            lore.add(Component.text(ChatColor.BLUE + "Weight: " + weight));
        }

        // Add more attributes here — only need to do it once
        Boolean placeable = getItemAttribute(itemKey, "isPlaceable", Boolean.class);
        if (placeable != null && placeable) {
            lore.add(Component.text(ChatColor.GREEN + "Placeable"));
        }

        return lore;
    }

    public List<String> getAllItemKeys() {
        if (data != null && data.items != null) {
            return new ArrayList<>(data.items.keySet());
        }
        return new ArrayList<>();
    }


    public Data getData() {
        return data;
    }
    @SuppressWarnings("unchecked")
    public <T> T getItemAttribute(String itemName, String attrName, Class<T> type) {
        if (!data.items.containsKey(itemName)) return null;
        Object value = data.items.get(itemName).get(attrName);
        if (value == null) return null;

        // Convert Material stored as string
        if (type == Material.class) {
            if (value instanceof String) {
                try {
                    return (T) Material.valueOf((String) value);
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
            return null; // Not a string, can't be a Material
        }

        // Convert number types from Double to desired type
        if (value instanceof Number) {
            Number num = (Number) value;
            if (type == Integer.class) return (T) Integer.valueOf(num.intValue());
            if (type == Float.class) return (T) Float.valueOf(num.floatValue());
            if (type == Long.class) return (T) Long.valueOf(num.longValue());
            if (type == Double.class) return (T) Double.valueOf(num.doubleValue());
        }

        // If the type matches directly
        if (type.isInstance(value)) {
            return type.cast(value);
        }

        // Special case: Boolean stored as string ("true"/"false")
        if (type == Boolean.class && value instanceof String) {
            return (T) Boolean.valueOf((String) value);
        }

        return null; // Can't convert
    }



}
