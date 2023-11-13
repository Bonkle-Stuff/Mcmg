package com.cak.mcsu.games.rocket_spleef;

import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.game.GameInventory;
import com.cak.mcmg.core.util.McsuItemStack;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Map;

public class RocketSpleefInventory extends GameInventory {
  
  public RocketSpleefInventory(McsuPlayer player) {
    super(player);
    apply();
  }
  
  @Override
  public void apply() {
    McsuItemStack toolItemStack = new McsuItemStack(Material.CROSSBOW);
    toolItemStack.addEnchantments(Map.of(
            Enchantment.QUICK_CHARGE, 3,
            Enchantment.DURABILITY, 3
    ));
  
    ItemStack fireworksItemStack = new ItemStack(
        Material.FIREWORK_ROCKET
    );
  
    fireworksItemStack.setAmount(64);
    FireworkMeta fireworkMeta = (FireworkMeta) fireworksItemStack.getItemMeta();
    fireworkMeta.setPower(100);
    fireworkMeta.addEffect(
        FireworkEffect
            .builder()
            .withFade(Color.WHITE)
            .withColor(Color.AQUA)
            .trail(true)
            .build()
    );
    fireworksItemStack.setItemMeta(fireworkMeta);
  
    PlayerInventory playerInventory = player.toBukkit().getInventory();
  
    playerInventory.setItem(
        0,
        toolItemStack
    );
    playerInventory.setItemInOffHand(
        fireworksItemStack
    );
  }
}
