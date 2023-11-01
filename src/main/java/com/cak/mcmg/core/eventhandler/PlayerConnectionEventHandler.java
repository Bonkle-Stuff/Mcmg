package com.cak.mcmg.core.eventhandler;

import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.Team;
import com.cak.mcmg.core.game.ActiveGame;
import com.cak.mcmg.core.scoreboard.PlayerScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionEventHandler implements Listener {
  
  @EventHandler
  public static void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    
    if (ActiveGame.hasStarted()) {
    
    }
    
    new McsuPlayer(player);
    Team.tryAssignByConfig(player);
    new PlayerScoreboard(McsuPlayer.fromBukkit(player));
  }
  
  @EventHandler
  public static void onPlayerLeave(PlayerQuitEvent event) {
    McsuPlayer mcsuPlayer = McsuPlayer.fromBukkit(event.getPlayer());
  
    ActiveGame.getGame().handlePlayerLeft(mcsuPlayer);
    
    PlayerScoreboard.playerScoreboards.remove(mcsuPlayer);
    mcsuPlayer.getTeam().removePlayer(mcsuPlayer);
    McsuPlayer.players.remove(mcsuPlayer);
  }
  
}
