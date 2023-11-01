package com.cak.mcsu.players;

import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.game.functions.MultipleLives;

public class BlockSumoPlayer extends MultipleLives.MultiLivesPlayer {
  
  static int maxLives = 5;
  int powerups = 0;
  boolean powerupsEnabled = true;
  boolean spawnProtectionEnabled = true;
  
  public BlockSumoPlayer(McsuPlayer player) {
    super(player, maxLives);
  }
  
  
  public int getLifePowerups() {
    return powerups;
  }
  public void addLifePowerup() {
    powerups++;
  }
  public void resetLifePowerups() {
    powerups = 0;
  }
  
  public void setPowerupsEnabled(boolean powerupsEnabled) {
    this.powerupsEnabled = powerupsEnabled;
  }
  public boolean hasPowerupsEnabled() {
    return powerupsEnabled;
  }
  
}
