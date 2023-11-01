package com.cak.mcmg.core.game;

import com.cak.mcmg.core.McsuPlayer;

public class GamePlayer {
  
  McsuPlayer player;
  
  public GamePlayer(McsuPlayer player) {
    this.player = player;
  }
  
  public McsuPlayer getPlayer() {
    return player;
  }
}
