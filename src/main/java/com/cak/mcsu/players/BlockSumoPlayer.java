package com.cak.mcsu.players;

import com.cak.mcsu.lang.BlockSumoLang;
import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.game.GamePlayer;
import net.kyori.adventure.text.Component;

public class BlockSumoPlayer extends GamePlayer {
  
  static int maxLives = 5;
  int lives = maxLives;
  int powerups = 0;
  boolean powerupsEnabled = true;
  boolean spawnProtectionEnabled = true;
  
  public BlockSumoPlayer(McsuPlayer player) {
    super(player);
  }
  
  public Component getLivesPrefix() {
    return BlockSumoLang.livesBar(maxLives, lives);
  }
  
  public Component getLivesTabString() {
    return BlockSumoLang.lives(lives);
  }
  
  public void setLives(int lives) {
    this.lives = lives;
  }
  
  public void removeLife() {
    this.lives--;
  }
  
  public int getLives() {
    return lives;
  }
  
  public int getLifePowerups() {
    return powerups;
  }
  public void incrementLifePowerups() {
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
  
  public void setSpawnProtectionEnabled(boolean spawnProtectionEnabled) {
    this.spawnProtectionEnabled = spawnProtectionEnabled;
  }
  public boolean hasSpawnProtectionEnabled() {
    return spawnProtectionEnabled;
  }
  
}
