package com.cak.mcmg.core.eventhandler;

import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.scoreboard.PlayerScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerConnectionEventHandler implements Listener {
  
  @EventHandler
  public static void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    
    new McsuPlayer(player);
    new PlayerScoreboard(player);
  }
}
