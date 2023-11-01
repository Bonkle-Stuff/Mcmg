package com.cak.mcmg.core.game;

import com.cak.mcmg.core.Debug;
import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.scoreboard.PrefixProvider;
import com.cak.mcsu.AllGames;
import org.bukkit.GameMode;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Game {
  
  public static void registerGames() {
    Debug.log("[Game.registerGames] Registering games:");
    AllGames.register();
    for (Game game : registeredGames)
      game.registerAllComponents();
    GameState.postRegister = true; //All game functions created after will be cleared when game resets (stop a change mid game from being permanent)
  }

  public static void registerGames(Game... games) {
     registeredGames.addAll(List.of(games));
  }
  
  public static @Nullable Game getGame(String gameId) {
    return registeredGames.stream().filter(game -> Objects.equals(game.id, gameId)).findFirst().orElse(null);
  }
  
  public static ArrayList<Game> registeredGames = new ArrayList<>();
  
  final String id;
  final String name;
  
  
  private GameState lobbyGameState;
  private GameState activeGameState;
  private final List<GameState> gameStates = new ArrayList<>();
  private PrefixProvider gamePrefixProvider;
  
  public Game(String id, String name) {
    this.id = id;
    this.name = name;
    
    Debug.log("> " + name + " (ID: " + id + ")");
    registeredGames.add(this);
  }
  
  public @Nullable GameState getGameState(String id) {
    return gameStates.stream().filter(gameState -> Objects.equals(gameState.getId(), id)).findFirst().orElse(null);
  }
  
  public void registerAllComponents() {
    
    lobbyGameState = createLobbyGameState();
    addGameState(lobbyGameState);
    activeGameState = createActiveGameState();
    addGameState(activeGameState);
    
    for (GameState gameState : createGameStates())
      addGameState(gameState);
    
    gamePrefixProvider = createPrefixProvider();
  }
  
  protected Game addGameState(GameState gameState) {
    gameStates.add(gameState);
    return this;
  }
  
  public List<GameState> createGameStates() {return List.of();}
  public GameState createLobbyGameState() {return new GameState("Lobby");}
  public GameState createActiveGameState() {return new GameState("Active");}
  public PrefixProvider createPrefixProvider() {return null;}
  
  /**Eliminate the player as they left the game*/
  public void handlePlayerLeft(McsuPlayer mcsuPlayer) {
    if (ActiveGame.getAlivePlayers().contains(mcsuPlayer) && ActiveGame.hasStarted()) {
      mcsuPlayer.toBukkit().setGameMode(GameMode.SPECTATOR);
    }
  }
  
  public GameState getLobbyGameState() {
    return lobbyGameState;
  }
  
  public GameState getActiveGameState() {
    return activeGameState;
  }
  
  public List<GameState> getGameStates() {
    return gameStates;
  }
  
  @Nullable
  public PrefixProvider getGamePrefixProvider() {
    return gamePrefixProvider;
  }
  
  public String getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }

}
