package com.cak.mcmg.core.game;

import com.cak.mcmg.core.Debug;
import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.TimedEvent;
import com.cak.mcmg.core.eventhandler.ActivityRule;
import com.cak.mcmg.core.map.GameMap;
import com.cak.mcmg.core.map.MapLoader;
import com.cak.mcmg.core.scoreboard.PlayerScoreboard;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class ActiveGame {
  
  static ArrayList<McsuPlayer> alivePlayers = new ArrayList<>();
  static ArrayList<McsuPlayer> players = new ArrayList<>();
  static Game game;
  static GameMap gameMap;
  static World world;
  
  public static ArrayList<McsuPlayer> getAlivePlayers() {
    return alivePlayers;
  }
  
  public static ArrayList<McsuPlayer> getPlayers() {
    return players;
  }
  
  public static void eliminatePlayer(McsuPlayer player) {
    alivePlayers.remove(player);
  }
  
  public static void eliminatePlayer(Player player) {
    alivePlayers.remove(McsuPlayer.fromBukkit(player));
  }
  
  public static void playGame(Game newGame) {
    game = newGame;
    gameMap = GameMap.maps.get(0);
    
    alivePlayers = new ArrayList<>();
    alivePlayers.addAll(McsuPlayer.players);
    players = new ArrayList<>();
    players.addAll(McsuPlayer.players);
    
    
    world = MapLoader.loadWorld(gameMap.getWorldId());
    gameMap.loadSpawns(world, GameSpawnCategory.LOBBY);
    ActivityRule.clearAll();
    
    game.getGameStates().forEach(GameState::setup);
    GameState lobbyState = game.getLobbyGameState();
    if (lobbyState != null) {
      lobbyState.setEnabled(true);
    }
    
    
    new TimedEvent("Game Starts", 20, true)
        .setOnTick((t) -> {
        })
        .setOnEnd(() -> {
          Debug.log("Starting game '" + game.getName() + "'");
          
          PlayerScoreboard.setGamePrefixProvider(game.getGamePrefixProvider());
          
          if (lobbyState != null) {
            lobbyState.setEnabled(false);
          }
          
          gameMap.loadSpawns(world, GameSpawnCategory.GAME);
          
          GameState activeState = game.getActiveGameState();
          if (activeState != null) {
            activeState.setEnabled(true);
          }
          
        });
    
  }
  
  public static Game getGame() {
    return game;
  }
  
  public static GameMap getGameMap() {
    return gameMap;
  }
  
  public static World getWorld() {
    return world;
  }
  
  public static ConfigurationSection getMapGameData() {
    return gameMap.getGameData();
  }
  
  public static @Nullable String getCurrentCountDown() {
    TimedEvent timedEvent = TimedEvent.getTimedEvents().stream()
        .filter(t -> t.doesDisplay()).findFirst().orElse(null);
    
    return (timedEvent != null ? ChatColor.BOLD + "" + ChatColor.BLUE + timedEvent.getName() +
        ChatColor.RESET + "" + ChatColor.GRAY + ": " + ChatColor.RESET + "" + ChatColor.BOLD + timedEvent.getTimeLeftString() : null);
  }
}
