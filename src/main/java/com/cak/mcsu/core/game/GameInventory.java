package com.cak.mcsu.core.game;

import com.cak.mcsu.core.McsuPlayer;

public class GameInventory {
  
  McsuPlayer player;
  
  public GameInventory(McsuPlayer player) {
    this.player = player;
  }
  
  public McsuPlayer getPlayer() {
    return player;
  }
}
