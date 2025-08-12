package com.ysf.ark_items;

import com.ysf.ark_items.commands.*;

import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Ark_items extends JavaPlugin implements TabCompleter {


    private static Ark_items instance;
    private ItemManager itemManager;

    @Override
    public void onEnable() {
        instance = this;
        this.itemManager = new ItemManager(this);

        // Register commands
        getCommand("additem").setExecutor(new AddItemCommand(itemManager));
        getCommand("addattribute").setExecutor(new AddAttributeCommand(itemManager));
        getCommand("setattribute").setExecutor(new SetAttributeCommand(itemManager));
        getCommand("removeattribute").setExecutor(new RemoveAttributeCommand(itemManager));
        getCommand("clearitems").setExecutor(new ClearAllData(itemManager));

        getLogger().info("CustomItems plugin enabled.");

        CommandTabHandler tabHandler = new CommandTabHandler(itemManager);
        getCommand("addattribute").setTabCompleter(tabHandler);
        getCommand("setattribute").setTabCompleter(tabHandler);
        getCommand("removeattribute").setTabCompleter(tabHandler);
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public static Ark_items getInstance() {
        return instance;
    }


}




