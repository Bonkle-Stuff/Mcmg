package com.cak.mcsu.loottables;

import com.cak.mcmg.core.util.LootTable;
import com.cak.mcmg.core.util.McsuItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class BlockSumoLoot {
  
  static McsuItemStack knockbackISword = new McsuItemStack(Material.WOODEN_SWORD)
      .forceAddEnchantment(Enchantment.KNOCKBACK, 1)
      .modifyItemMetaRi(itemMeta -> {
        ((Damageable) itemMeta).setDamage(57);
        return itemMeta;
      })
      .setName("Stick - Knockback I");
  
  static McsuItemStack knockbackIISword = new McsuItemStack(Material.WOODEN_SWORD)
      .forceAddEnchantment(Enchantment.KNOCKBACK, 2)
      .modifyItemMetaRi(itemMeta -> {
        ((Damageable) itemMeta).setDamage(57);
        return itemMeta;
      })
      .setName("Stick - Knockback II");
  
  public static LootTable powerupLootTableI = new LootTable(
      new ItemStack(Material.FISHING_ROD), 1,
      new ItemStack(Material.COBWEB, 4), 4,
      new ItemStack(Material.ENDER_PEARL), 3,
      new ItemStack(Material.TNT), 4,
      new McsuItemStack(Material.FIRE_CHARGE)
          .setName(ChatColor.GOLD + "Fireball"), 2
  );
  
  public static LootTable powerupLootTableII = new LootTable(
      new ItemStack(Material.FISHING_ROD), 1,
      new ItemStack(Material.COBWEB, 4), 2,
      new ItemStack(Material.ENDER_PEARL), 3,
      new ItemStack(Material.TNT), 6,
      knockbackISword, 5,
      new McsuItemStack(Material.FIRE_CHARGE)
          .setName(ChatColor.GOLD + "Fireball"), 3
  );
  
  public static LootTable powerupLootTableIII = new LootTable(
      new ItemStack(Material.FISHING_ROD), 1,
      new ItemStack(Material.COBWEB, 4), 4,
      new ItemStack(Material.ENDER_PEARL), 3,
      new ItemStack(Material.TNT), 8,
      knockbackISword, 1,
      knockbackIISword, 3,
      new McsuItemStack(Material.FIRE_CHARGE)
          .setName(ChatColor.GOLD + "Fireball"), 4
  );
  
  public static LootTable centerLootTable = new LootTable(
      new ItemStack(Material.NETHERITE_BOOTS), 1,
      new ItemStack(Material.ENDER_PEARL), 3,
      new ItemStack(Material.TNT, 2), 8,
      knockbackISword, 1,
      new McsuItemStack(Material.FIRE_CHARGE)
          .setName(ChatColor.GOLD + "Fireball"), 4
  );
  
}
