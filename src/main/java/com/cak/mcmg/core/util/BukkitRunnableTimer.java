package com.cak.mcmg.core.util;

import com.cak.mcmg.Main;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class BukkitRunnableTimer extends BukkitRunnable {
  
  Consumer<BukkitRunnableTimer> onTick;
  
  public BukkitRunnableTimer(Runnable onTick, long delay, long period) {
    this.onTick = bukkitRunnableTimer -> onTick.run();
    this.runTaskTimer(Main.plugin, delay, period);
  }
  
  public BukkitRunnableTimer(Consumer<BukkitRunnableTimer> onTick, long delay, long period) {
    this.onTick = onTick;
    this.runTaskTimer(Main.plugin, delay, period);
  }
  
  @Override
  public void run() {
    onTick.accept(this);
  }
  
}
