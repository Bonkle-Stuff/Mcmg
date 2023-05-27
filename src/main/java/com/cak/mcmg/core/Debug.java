package com.cak.mcmg.core;

import com.cak.mcmg.Main;
import org.bukkit.Bukkit;

public class Debug {
  
  public static void log(String str) {
    if (Main.debugEnabled) {
      Bukkit.getLogger().info(str);
    }
  }
  
}
