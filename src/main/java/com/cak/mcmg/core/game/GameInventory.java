package com.cak.mcmg.core.game;

import com.cak.mcmg.core.McsuPlayer;

public abstract class GameInventory {
  
  protected McsuPlayer player;
  
  public GameInventory(McsuPlayer player) {
    this.player = player;
  }
  
  public McsuPlayer getPlayer() {
    return player;
  }
  
  public abstract void apply();
  
}
