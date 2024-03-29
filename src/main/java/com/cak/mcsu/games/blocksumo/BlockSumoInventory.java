package com.cak.mcsu.games.blocksumo;

import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.game.GameInventory;
import com.cak.mcmg.core.util.McsuItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BlockSumoInventory extends GameInventory {
  
  public BlockSumoInventory(McsuPlayer player) {
    super(player);
    apply();
  }
  
  public void apply() {
    Inventory inventory = player.toBukkit().getInventory();
  
    inventory.setItem(0, new McsuItemStack(Material.SHEARS).setUnbreakable(true));
    inventory.setItem(1, new ItemStack(player.getTeam().getWool(), 16));
  }
  
}
