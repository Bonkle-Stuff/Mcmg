package com.cak.mcmg.core.util;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class LootTable {
  
  ArrayList<LootOption> options = new ArrayList<>();
  
  /**
   * Takes <code>ItemStack</code> alongside a <code>weight</code>, and creates a loot table with the specified parameters
   * <br/><br/>
   * Example:
   * <code><pre>
   *     new LootTable(
   *         new ItemStack(Material.IRON_BLOCK), 1, (25% of total weight)
   *         new ItemStack(Material.IRON_INGOT), 3, (75% of total weight)
   *     )
   * </pre></code>
   *
   * @param args Arguments: <br/>
   *             <code>ItemStack item</code> item for this option <br/>
   *             <code>int weight</code> chance for being picked for this option (if an item has a weight of 1 and the total sum of weights is 10, the chance is 10%)
   */
  public LootTable(Object... args) {
    
    for (int i = 0; i < args.length; i += 2) {
      options.add(new LootOption(
          (ItemStack) args[i],
          (int) args[i + 1]
      ));
    }
    
  }
  
  LootOption chooseOption() {
    
    int totalWeight = 0;
    
    for (LootOption option : options) {
      totalWeight += option.weight();
    }
    
    int pickedInt = new Random().nextInt(totalWeight) + 1;
    
    int currentWeight = 0;
    
    for (LootOption option : options) {
      
      if (numInRange(currentWeight, currentWeight + option.weight(), pickedInt) && option.weight() != 0) {
        return option;
      }
      
      currentWeight += option.weight();
    }
    
    return null;
    
  }
  
  public ItemStack generate() {
    return chooseOption().stack();
  }
  
  public ItemStack[] generate(int length) {
    ArrayList<ItemStack> itemStacks = new ArrayList<>();
    
    for (int i = 0; i < length; i++) {
      itemStacks.add(chooseOption().stack());
    }
    
    return itemStacks.toArray(new ItemStack[0]);
  }
  
  public static boolean numInRange(double a, double b, double point) { //copied from actionzone lol //copied from gamemode option lol //copied from mcsu rf lol
    return Math.abs(a - point) + Math.abs(b - point) == Math.abs(a - b);
  }
  
  record LootOption(ItemStack stack, int weight) {
  }
  
}
