package com.cak.mcmg.core.util;

import com.cak.mcmg.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class BukkitRunnableLater extends BukkitRunnable {
  
  Runnable onTick;
  
  public BukkitRunnableLater(Runnable onTick, long delay) {
    this.onTick = onTick;
    this.runTaskLater(Main.plugin, delay);
  }
  
  @Override
  public void run() {
    onTick.run();
  }
  
}
