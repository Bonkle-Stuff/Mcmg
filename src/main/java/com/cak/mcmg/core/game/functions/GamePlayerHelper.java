package com.cak.mcmg.core.game.functions;

import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.game.ActiveGame;
import com.cak.mcmg.core.game.GameFunction;
import com.cak.mcmg.core.game.GamePlayer;

import java.util.ArrayList;

public class GamePlayerHelper<T extends GamePlayer> extends GameFunction {
  
  ArrayList<T> players = new ArrayList<>();
  GamePlayerFactory<T> playerFactory;
  
  public GamePlayerHelper(GamePlayerFactory<T> playerFactory) {
    this.playerFactory = playerFactory;
  }
  
  @Override
  public void onEnable() {
    players = new ArrayList<>();
    
    for (McsuPlayer player : ActiveGame.getPlayers()) {
      players.add(playerFactory.build(player));
    }
  }
  
  @Override
  public void onDisable() {
    players = new ArrayList<>();
  }
  
  public T getPlayer(McsuPlayer bukkitPlayer) {
    return players.stream().filter(player -> player.getPlayer().equals(bukkitPlayer)).findFirst().orElse(null);
  }
  
  public ArrayList<T> getPlayers() {
    return players;
  }
  
  public static interface GamePlayerFactory<T extends GamePlayer> {
    T build(McsuPlayer player);
  }
}
