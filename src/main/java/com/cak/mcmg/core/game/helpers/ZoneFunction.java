package com.cak.mcmg.core.game.helpers;

import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.game.ActiveGame;
import com.cak.mcmg.core.game.GameFunction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class ZoneFunction extends GameFunction {
  
  public ZoneFunction() {
    this.setListener(new Listener() {
      @EventHandler
      public void onPlayerMoveEvent(PlayerMoveEvent moveEvent) {
        Location location = moveEvent.getTo();
        Player player = moveEvent.getPlayer();
        
        if (!ActiveGame.getAlivePlayers().contains(McsuPlayer.fromBukkit(player)))
          return;
    
        if (isInside(location.getX(), location.getY(), location.getZ())) {
      
          if (!playersInside.contains(player)) {
            playersInside.add(player);
            onEnter(player);
          }
      
          onInside(player);
      
        } else {
      
          if (playersInside.contains(player)) {
            playersInside.remove(player);
            onExit(player);
          }
      
        }
      }
    });
  }
  
  @Override
  public void onEnable() {
    super.onEnable();
  }
  
  public boolean isInside(double x, double y, double z) {
    return false;
  }
  
  ArrayList<Player> playersInside = new ArrayList<>();
  
  public void onEnter(Player player) {}
  public void onExit(Player player) {}
  public void onInside(Player player) {}
  
  public ArrayList<Player> getPlayersInside() {
    return playersInside;
  }
  
}
