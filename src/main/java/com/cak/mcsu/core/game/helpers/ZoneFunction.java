package com.cak.mcsu.core.game.helpers;

import com.cak.mcsu.core.game.GameFunction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ZoneFunction extends GameFunction {
  
  public boolean isInside(double x, double y, double z) {
    return false;
  }
  
  ArrayList<Player> playersInside = new ArrayList<>();
  
  Consumer<Player> onEnter;
  Consumer<Player> onExit;
  Consumer<Player> onInside;
  
  Listener listener = new Listener() {
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent moveEvent) {
      Location location = moveEvent.getTo();
      Player player = moveEvent.getPlayer();
      
      if (isInside(location.getX(), location.getY(), location.getZ())) {
        
        if (!playersInside.contains(player)) {
          playersInside.add(player);
          if (onEnter != null)
            onEnter.accept(player);
        }
        
        if (onInside != null)
          onInside.accept(player);
        
      } else {
        
        if (playersInside.contains(player)) {
          playersInside.remove(player);
          if (onExit != null)
            onExit.accept(player);
        }
        
      }
    }
  };
  
  @Override
  public Listener getListener() {
    return listener;
  }
  
  public void setOnEnter(Consumer<Player> onEnter) {
    this.onEnter = onEnter;
  }
  
  public void setOnExit(Consumer<Player> onExit) {
    this.onExit = onExit;
  }
  
  public void setOnInside(Consumer<Player> onInside) {
    this.onInside = onInside;
  }
}
