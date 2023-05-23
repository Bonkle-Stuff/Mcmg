package com.cak.mcsu.base.loottables;

import com.cak.mcsu.core.util.LootTable;
import com.cak.mcsu.core.util.McsuItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class BlockSumoLoot {
  
  public static LootTable powerupLootTableI = new LootTable(
      new ItemStack(Material.FISHING_ROD), 1,
      new ItemStack(Material.COBWEB, 4), 4,
      new ItemStack(Material.ENDER_PEARL), 3,
      new ItemStack(Material.TNT), 4,
      new McsuItemStack(Material.STICK)
          .forceAddEnchantment(Enchantment.KNOCKBACK, 2)
          .setName("Stick - Knockback II"), 1,
      new McsuItemStack(Material.STICK)
          .forceAddEnchantment(Enchantment.KNOCKBACK, 1)
          .setName("Stick - Knockback I"), 3,
      new McsuItemStack(Material.FIRE_CHARGE)
          .setName(ChatColor.GOLD + "Fireball"), 2
  );
  
  public static LootTable powerupLootTableII = new LootTable(
      new ItemStack(Material.FISHING_ROD), 1,
      new ItemStack(Material.COBWEB, 4), 2,
      new ItemStack(Material.ENDER_PEARL), 3,
      new ItemStack(Material.TNT), 4,
      new McsuItemStack(Material.STICK)
          .forceAddEnchantment(Enchantment.KNOCKBACK, 2)
          .setName("Stick - Knockback II"), 2,
      new McsuItemStack(Material.STICK)
          .forceAddEnchantment(Enchantment.KNOCKBACK, 1)
          .setName("Stick - Knockback I"), 2,
      new McsuItemStack(Material.FIRE_CHARGE)
          .setName(ChatColor.GOLD + "Fireball"), 2
  );
  
  public static LootTable powerupLootTableIII = new LootTable(
      new ItemStack(Material.FISHING_ROD), 1,
      new ItemStack(Material.COBWEB, 4), 4,
      new ItemStack(Material.ENDER_PEARL), 3,
      new ItemStack(Material.TNT), 4,
      new McsuItemStack(Material.STICK)
          .forceAddEnchantment(Enchantment.KNOCKBACK, 2)
          .setName("Stick - Knockback II"), 5,
      new McsuItemStack(Material.STICK)
          .forceAddEnchantment(Enchantment.KNOCKBACK, 1)
          .setName("Stick - Knockback I"), 1,
      new McsuItemStack(Material.FIRE_CHARGE)
          .setName(ChatColor.GOLD + "Fireball"), 3
  );
  
}
