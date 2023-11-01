package com.cak.mcmg.core.game.functions;

import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.game.ActiveGame;
import com.cak.mcmg.core.game.GameFunction;
import com.cak.mcmg.core.game.GameInventory;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class GameInventoryHelper<T extends GameInventory> extends GameFunction {
  
  ArrayList<T> players = new ArrayList<>();
  GameInventoryFactory<T> playerFactory;
  Listener listener;
  
  public GameInventoryHelper(GameInventoryFactory<T> playerFactory) {
    this(playerFactory, null);
  }
  
  public GameInventoryHelper(GameInventoryFactory<T> playerFactory, Listener listener) {
    this.playerFactory = playerFactory;
    this.listener = listener;
  }
  
  @Override
  public void onEnable() {
    players = new ArrayList<>();
    
    for (McsuPlayer player : ActiveGame.getPlayers()) {
      players.add(playerFactory.build(player));
    }
  }
  
  @Override
  public Listener getListener() {
    return listener;
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
  
  public interface GameInventoryFactory<T extends GameInventory> {
    T build(McsuPlayer player);
  }
}
