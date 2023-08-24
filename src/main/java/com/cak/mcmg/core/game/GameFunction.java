package com.cak.mcmg.core.game;

import com.cak.mcmg.Main;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class GameFunction {
  
  private Listener listener;
  boolean isTemporary;
  boolean currentlyEnabled = false;
  
  public void setEnabled(boolean enabled) {
    if (currentlyEnabled != enabled) {
      
      currentlyEnabled = enabled;
      onEnabledToggle(enabled);
      
      if (enabled) {
        onEnable();
        if (getListener() != null)
          Main.plugin.getServer().getPluginManager().registerEvents(getListener(), Main.plugin);
      } else {
        onDisable();
        if (getListener() != null)
          HandlerList.unregisterAll(getListener());
      }
      
    }
  }
  
  public void setListener(Listener listener) {
    this.listener = listener;
  }
  
  public Listener getListener() {
    return listener;
  }
  
  public boolean getIsTemporary() {
    return isTemporary;
  }
  
  public void setIsTemporary(boolean isTemporary) {
    this.isTemporary = isTemporary;
  }
  
  //Common methods
  public void onEnable() {
  }
  
  public void onDisable() {
  }
  
  public void onEnabledToggle(boolean enabled) {
  }
  
}
