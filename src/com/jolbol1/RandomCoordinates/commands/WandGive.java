package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by James on 04/07/2016.
 */
public class WandGive implements CommandInterface {

    private final MessageManager messages = new MessageManager();

    @Override
    public void onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("wand")) {
                if (RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.Portals") || RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(sender, "Random.*")) {
                    if (sender instanceof Player) {
                        final Player p = (Player) sender;

                        p.getInventory().addItem(wand());
                        messages.wandGiven(sender);
                    }
                } else {
                    messages.noPermission(sender);
                }
            }

        }
    }

    /**
     * Creates a item for the /RC wand
     * @return the Wand ItemStack
     */
    public ItemStack wand() {
        final ItemStack wand = new ItemStack(Material.GOLD_AXE);
        final ItemMeta itemMeta = wand.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "Random Wand");
        final List<String> lore = new ArrayList<>();
        lore.add("Wand for portal creation!");
        itemMeta.setLore(lore);
        wand.setItemMeta(itemMeta);
        return wand;
    }


}
