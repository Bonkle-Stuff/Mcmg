package com.cak.mcsu.core.game.functions;

import com.cak.mcsu.core.game.GameFunction;
import org.bukkit.event.Listener;

public class EventListener extends GameFunction implements Listener {
  
  public EventListener() {
    setListener(this);
  }
  
  
}
