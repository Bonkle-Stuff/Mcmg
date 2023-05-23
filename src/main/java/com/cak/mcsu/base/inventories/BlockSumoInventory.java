package com.cak.mcsu.base.inventories;

import com.cak.mcsu.core.McsuPlayer;
import com.cak.mcsu.core.game.GameInventory;
import com.cak.mcsu.core.util.McsuItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BlockSumoInventory extends GameInventory {
  
  public BlockSumoInventory(McsuPlayer player) {
    
    super(player);
    
    Inventory inventory = player.toBukkit().getInventory();
    
    inventory.setItem(0, new McsuItemStack(Material.SHEARS).setUnbreakable(true));
    inventory.setItem(1, new ItemStack(player.getTeam().getWool(), 16));
    
  }
  
}
