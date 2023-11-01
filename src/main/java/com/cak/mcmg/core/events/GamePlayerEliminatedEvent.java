package com.cak.mcmg.core.events;

import com.cak.mcmg.core.McsuPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GamePlayerEliminatedEvent extends Event {
  
  McsuPlayer player;
  
  public GamePlayerEliminatedEvent(McsuPlayer player) {
    this.player = player;
  }
  
  public McsuPlayer getPlayer() {
    return player;
  }
  
  private static final HandlerList handlers = new HandlerList();
  
  @Override
  public @NotNull HandlerList getHandlers() {
    return handlers;
  }
  
  public static HandlerList getHandlerList() {
    return handlers;
  }
  
}
