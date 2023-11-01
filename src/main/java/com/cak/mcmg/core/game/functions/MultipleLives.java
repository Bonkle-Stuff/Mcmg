package com.cak.mcmg.core.game.functions;

import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.lang.LivesLang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;

import java.util.concurrent.atomic.AtomicInteger;

public class MultipleLives<T extends MultipleLives.MultiLivesPlayer> extends Respawns<T> {
  
  public MultipleLives(GamePlayerHelper<T> helper) {
    super(helper);
  }
  
  @Override
  protected void onPlayerDeath(T player) {
    player.removeLife();
    super.onPlayerDeath(player);
  }
  
  @Override
  protected void sendPlayerTitle(T player, AtomicInteger respawnTimeLeft) {
    player.getPlayer().toBukkit().sendTitlePart(TitlePart.TITLE, LivesLang.respawningIn(respawnTimeLeft.get()));
    super.sendPlayerTitle(player, respawnTimeLeft);
  }
  
  public static class MultiLivesPlayer extends RespawnsPlayer {
  
    int maxLives;
    int lives;
  
    public MultiLivesPlayer(McsuPlayer player, int maxLives) {
      super(player);
      this.maxLives = maxLives;
      this.lives = maxLives;
    }
    
    public Component getLivesPrefix() {
      return LivesLang.livesBar(maxLives, lives);
    }
  
    public Component getLivesTabString() {
      return LivesLang.lives(lives);
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
  
  }
  
}
