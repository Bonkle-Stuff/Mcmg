package com.cak.mcsu.games.koth;

import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.game.GameInventory;
import com.cak.mcmg.core.util.McsuItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;

public class KingOfTheHillInventory extends GameInventory {
  
  public static int stickLevel;
  
  public KingOfTheHillInventory(McsuPlayer player) {
    super(player);
    apply();
  }
  
  public void apply() {
    Inventory inventory = player.toBukkit().getInventory();
  
    inventory.setItem(0, getStickForLevel());
  }
  
  public static McsuItemStack getStickForLevel() {
    return switch (stickLevel) {
      case 1 -> new McsuItemStack(Material.STICK).setName("Whacker");
      case 2 -> new McsuItemStack(Material.STICK).forceAddEnchantment(Enchantment.KNOCKBACK, 1).setName("Whacker 2.0");
      case 3 -> new McsuItemStack(Material.STICK).forceAddEnchantment(Enchantment.KNOCKBACK, 2).setName("Whacker 3.0");
      default -> throw new IllegalStateException("Unexpected value: " + stickLevel);
    };
  }
  
}
