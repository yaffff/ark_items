package com.ysf.ark_items.commands;

import com.ysf.ark_items.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ClearAllData implements CommandExecutor {

    private final ItemManager itemManager;

    public ClearAllData(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        itemManager.clearAllData();
        sender.sendMessage("§aAll items and attributes have been cleared!");
        return true;
    }
}
