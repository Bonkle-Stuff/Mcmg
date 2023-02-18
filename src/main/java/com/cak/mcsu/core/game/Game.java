package com.cak.mcsu.core.game;

import com.cak.mcsu.core.Debug;
import com.cak.mcsu.games.BlockSumo;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Objects;

public class Game {

    public static void registerGames() {
        Debug.log("[Game.registerGames] Registering games:");
        BlockSumo.register();

        GameState.postRegister = true; //All game functions registered after will be cleared when game resets
    }

    public static @Nullable Game getGame(String gameId) {
        return registeredGames.stream().filter(game -> Objects.equals(game.id, gameId)).findFirst().orElse(null);
    }

    public static ArrayList<Game> registeredGames = new ArrayList<>();

    ArrayList<GameState> gameStates = new ArrayList<>();
    GameState lobbyGameState;
    GameState activeGameState;

    final String id;
    final String name;

    public Game(String id, String name) {
        this.id = id;
        this.name = name;

        Debug.log("> " + name);
        registeredGames.add(this);
    }

    public Game addGameState(GameState gameState) {
        gameStates.add(gameState);
        return this;
    }

    public Game addGameStates(GameState... gameStates) {
        for (GameState gameState : gameStates) {
            addGameState(gameState);
        }
        return this;
    }

    public @Nullable GameState getGameState(String id) {
        return gameStates.stream().filter(gameState -> Objects.equals(gameState.getId(), id)).findFirst().orElse(null);
    }

    public Game setLobbyGameState(GameState lobbyGameState) {
        addGameState(lobbyGameState); this.lobbyGameState = lobbyGameState;
        return this;
    }
    public Game setActiveGameState(GameState activeGameState) {
        addGameState(activeGameState); this.activeGameState = activeGameState;
        return this;
    }

    @Nullable public GameState getLobbyGameState() { return lobbyGameState; }
    @Nullable public GameState getActiveGameState() { return activeGameState; }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
