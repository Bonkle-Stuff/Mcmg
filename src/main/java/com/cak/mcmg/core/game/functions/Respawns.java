package com.cak.mcmg.core.game.functions;

import com.cak.mcmg.Main;
import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.game.ActiveGame;
import com.cak.mcmg.core.game.GameFunction;
import com.cak.mcmg.core.game.GamePlayer;
import com.cak.mcmg.core.lang.LivesLang;
import com.cak.mcmg.core.scoreboard.PlayerScoreboard;
import com.cak.mcmg.core.util.BukkitRunnableLater;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Respawns<T extends Respawns.RespawnsPlayer> extends GameFunction {
  
  GamePlayerHelper<T> helper;
  BukkitRunnable timer;
  
  public Respawns(GamePlayerHelper<T> helper) {
    this.helper = helper;
  }
  
  protected void onPlayerDeath(T player) {}
  protected void onPlayerRespawn(T player) {}
  protected boolean playerShouldRespawn(T player) {return true;}
  
  protected void sendPlayerTitle(T player, AtomicInteger respawnTimeLeft) {
    player.getPlayer().toBukkit().sendTitlePart(TitlePart.TITLE, LivesLang.respawningIn(respawnTimeLeft.get()));
  }
  
  @Override
  public Listener getListener() {
    return new EventListener() {
      @EventHandler
      public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        T respawningPlayer = helper.getPlayer(McsuPlayer.fromBukkit(event.getPlayer()));
        
        McsuPlayer mcsuPlayer = McsuPlayer.fromBukkit(player);
        PlayerScoreboard.playerScoreboards.get(mcsuPlayer).updatePlayersForScoreboard();
  
        Respawns.this.onPlayerDeath(respawningPlayer);
  
        if (!playerShouldRespawn(respawningPlayer)) {
          ActiveGame.eliminatePlayer(player);
          return;
        }
        AtomicInteger respawnTimeLeft = new AtomicInteger(5);
        
        timer = new BukkitRunnable() {
          @Override
          public void run() {
            if (!ActiveGame.isActive()) {
              timer.cancel();
              return;
            }
            
            if (respawnTimeLeft.get() == 0) {
              Arrays.stream(ActiveGame.getGameMap().getGameSpawns())
                  .filter(gameSpawn -> gameSpawn.getTeam() == respawningPlayer.getPlayer().getTeam())
                  .findFirst()
                  .orElse(null)
                  .teleportPlayerTo(player, ActiveGame.getWorld());
      
              player.setGameMode(GameMode.SURVIVAL);
              player.sendMessage(
                  ChatColor.GREEN + "" + ChatColor.BOLD + "Respawned!"
              );
              respawningPlayer.setSpawnProtectionEnabled(true);
      
              onPlayerRespawn(respawningPlayer);
      
              new BukkitRunnableLater(
                  () -> {
                    player.sendMessage(
                        ChatColor.AQUA + "" + ChatColor.BOLD + "Spawn protection disabled!"
                    );
                    respawningPlayer.setSpawnProtectionEnabled(false);
                  },
                  20 * 5
              );
      
              timer.cancel();
            } else {
              sendPlayerTitle(respawningPlayer, respawnTimeLeft);
            }
            respawnTimeLeft.getAndDecrement();
          }
        };
        timer.runTaskTimer(Main.plugin, 0, 20L);
        
      }
    };
  }
  
  public static class RespawnsPlayer extends GamePlayer {
    
    boolean spawnProtectionEnabled = false;
    
    public RespawnsPlayer(McsuPlayer player) {
      super(player);
    }
    
    public void setSpawnProtectionEnabled(boolean spawnProtectionEnabled) {
      this.spawnProtectionEnabled = spawnProtectionEnabled;
    }
    public boolean hasSpawnProtectionEnabled() {
      return spawnProtectionEnabled;
    }
    
  }
  
}
