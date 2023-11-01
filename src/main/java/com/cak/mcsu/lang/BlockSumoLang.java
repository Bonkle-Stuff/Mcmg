package com.cak.mcsu.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;

public class BlockSumoLang {

  static TextColor heartColor = TextColor.color(227, 48, 69);

  public static Component finalDeath() {
    return Component.text("Final Death!").color(heartColor);
  }

  public static String receivedPowerup(int current, int max) {
    return String.format(
        ChatColor.BLUE + "You have received a powerup! [%d/%d]",
        current, max
      );
  }

}
