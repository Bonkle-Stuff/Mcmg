package com.cak.mcsu.players;

import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.game.functions.Respawns;

public class KingOfTheHillPlayer extends Respawns.RespawnsPlayer {
  
  float winPercentage = 0;
  
  public KingOfTheHillPlayer(McsuPlayer player) {
    super(player);
  }
  
  public float getWinPercentage() {
    return winPercentage;
  }
  public void addWinPercentage(float i) {
    winPercentage += i;
  }
}
