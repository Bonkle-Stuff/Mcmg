package com.cak.mcmg;

import com.cak.mcmg.core.Debug;
import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.Team;
import com.cak.mcmg.core.TimedEvent;
import com.cak.mcmg.core.commands.DevCommands;
import com.cak.mcmg.core.commands.GameCommands;
import com.cak.mcmg.core.commands.MiscCommands;
import com.cak.mcmg.core.eventhandler.ActivityRuleEventHandler;
import com.cak.mcmg.core.eventhandler.PlayerConnectionEventHandler;
import com.cak.mcmg.core.game.Game;
import com.cak.mcmg.core.map.MapConfigLoader;
import com.cak.mcmg.core.map.MapLoader;
import com.cak.mcmg.core.scoreboard.PlayerScoreboard;
import net.kyori.adventure.bossbar.BossBar;
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
  
  public static void warnf(String string, String... formatArguments) {
    plugin.getLogger().warning(string.formatted((Object[]) formatArguments));
  }
  public static void infof(String string, String... formatArguments) {
    plugin.getLogger().info(string.formatted((Object[]) formatArguments));
  }
  
  public static void warn(String string) {
    plugin.getLogger().warning(string);
  }
  public static void info(String string) {
    plugin.getLogger().info(string);
  }
  
  @Override
  public void onEnable() {
    
    // Plugin startup logic
    plugin = this;
    path = Main.plugin.getDataFolder().getAbsolutePath();
    
    //Clears some existing maps
    Main.info("[Main.onEnable] Existing maps: ");
    for (World world : Bukkit.getWorlds()) {
      Debug.log("> " + world.getName());
      
      if (world.getName().startsWith("map-")) {
        Main.info("> Deleting game map world: " + world.getName());
        MapLoader.clearWorld(world.getName());
      } else if (world.getName().startsWith("build-")) {
        Main.warn("> Detected a build world (maybe clear this?): " + world.getName());
      }
    }
    
    //Register players
    Bukkit.getOnlinePlayers().forEach(McsuPlayer::new);
    
    Bukkit.getOnlinePlayers().forEach(p -> {
      Iterable<? extends BossBar> bossBars = p.activeBossBars();
      bossBars.forEach(p::hideBossBar);
    });
    
    //Register EventHandlers5
    getServer().getPluginManager().registerEvents(new ActivityRuleEventHandler(), this);
    getServer().getPluginManager().registerEvents(new PlayerConnectionEventHandler(), this);
    
    //Initialise components
    Team.loadConfig();
    TimedEvent.initClock();
    Game.registerGames();
    MapConfigLoader.load();
    
    //Scoreboard
    McsuPlayer.players.forEach(PlayerScoreboard::new);
    new BukkitRunnable() {
      @Override
      public void run() {
        PlayerScoreboard.updateAll();
      }
    }.runTaskTimer(Main.plugin, 20L, 20L);
    
    //Commands
    GameCommands.register();
    DevCommands.register();
    MiscCommands.register();
    
  }
  
  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
