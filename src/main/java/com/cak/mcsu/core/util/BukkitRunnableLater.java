package com.cak.mcsu.core.util;

import com.cak.mcsu.Main;
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
