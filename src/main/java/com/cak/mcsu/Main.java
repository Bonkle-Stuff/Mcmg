package com.cak.mcsu;

import com.cak.mcsu.core.Debug;
import com.cak.mcsu.core.McsuPlayer;
import com.cak.mcsu.core.Team;
import com.cak.mcsu.core.TimedEvent;
import com.cak.mcsu.core.commands.DevCommands;
import com.cak.mcsu.core.commands.GameCommands;
import com.cak.mcsu.core.eventhandler.ActivityRuleEventHandler;
import com.cak.mcsu.core.game.Game;
import com.cak.mcsu.core.map.MapConfigLoader;
import com.cak.mcsu.core.map.MapLoader;
import com.cak.mcsu.core.scoreboard.PlayerScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Main extends JavaPlugin {

    public static JavaPlugin plugin;
    public static String path;

    //?Enables Debug.Log statements
    public static boolean debugEnabled = true;

    public static String scoreboardName = ChatColor.RED + "" + ChatColor.BOLD + "> MCSU <";

    public static void warn(String string) {
        plugin.getLogger().warning(string);
    }

    @Override
    public void onEnable() {

        // Plugin startup logic
        plugin = this;
        path = Main.plugin.getDataFolder().getAbsolutePath();

        //Clears some existing maps
        Debug.log("[Main.onEnable] Existing maps: ");
        for (World world : Bukkit.getWorlds()) {
            Debug.log("> " + world.getName());

            if (world.getName().startsWith("map-") || world.getName().startsWith("dev-")) {
                Debug.log("> Deleting game map world: " + world.getName());
                MapLoader.clearWorld(world.getName());
            }
        }

        //Register players
        Bukkit.getOnlinePlayers().forEach(McsuPlayer::new);

        //Register EventHandlers
        getServer().getPluginManager().registerEvents(new ActivityRuleEventHandler(), this);

        //Initialise components
        Team.loadConfig();
        TimedEvent.initClock();
        Game.registerGames();
        MapConfigLoader.load();

        //Scoreboard
        Bukkit.getOnlinePlayers().forEach(PlayerScoreboard::new);
        new BukkitRunnable() {
            @Override public void run() { PlayerScoreboard.updateAll(); }
        }.runTaskTimer(Main.plugin, 20L, 20L);

        //Commands
        GameCommands.register();
        DevCommands.register();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
