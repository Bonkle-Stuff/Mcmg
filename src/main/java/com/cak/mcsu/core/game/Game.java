package com.cak.mcsu.core.game;

import com.cak.mcsu.base.AllGames;
import com.cak.mcsu.core.Debug;
import com.cak.mcsu.core.scoreboard.PrefixProvider;

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
    GameState.postRegister = true; //All game functions registered after will be cleared when game resets
  }

  public static void registerGames(Game... games) {
     registeredGames.addAll(games);
  }
  
  public static @Nullable Game getGame(String gameId) {
    return registeredGames.stream().filter(game -> Objects.equals(game.id, gameId)).findFirst().orElse(null);
  }
  
  public static ArrayList<Game> registeredGames = new ArrayList<>();
  
  final String id;
  final String name;
  
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
  
  public List<GameState> createGameStates() {return null;}
  public GameState createLobbyGameState() {return new GameState("Lobby");}
  public GameState createActiveGameState() {return new GameState("Active");}
  public PrefixProvider createPrefixProvider() {return null;}
  
  @Nullable
  public GameState getLobbyGameState() {
    return lobbyGameState;
  }
  
  @Nullable
  public GameState getActiveGameState() {
    return activeGameState;
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
