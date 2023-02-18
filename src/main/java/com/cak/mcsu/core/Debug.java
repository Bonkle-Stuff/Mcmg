package com.cak.mcsu.core;

import com.cak.mcsu.Main;
import org.bukkit.Bukkit;

public class Debug {

    public static void log(String str) {
        if (Main.debugEnabled) {
            Bukkit.getLogger().info(str);
        }
    }

}
