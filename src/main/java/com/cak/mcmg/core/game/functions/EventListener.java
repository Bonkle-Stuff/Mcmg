package com.cak.mcmg.core.game.functions;

import com.cak.mcmg.core.game.GameFunction;
import org.bukkit.event.Listener;

public class EventListener extends GameFunction implements Listener {
  
  public EventListener() {
    setListener(this);
  }
  
  
}
