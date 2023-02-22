package com.cak.mcsu.core.game.functions;

import com.cak.mcsu.Main;
import com.cak.mcsu.core.game.GameFunction;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskInterval extends GameFunction {

    BukkitRunnable runnable;

    Runnable task;
    long delay, interval;

    public TaskInterval(Runnable task, long delay, long interval) {
        this.task = task;
        this.delay = delay;
        this.interval = interval;
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        };
    }

    @Override
    public void onEnable() {
        runnable.runTaskTimer(Main.plugin, delay, interval);
    }

    @Override
    public void onDisable() {
        runnable.cancel();
    }
}
