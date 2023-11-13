package com.cak.mcmg.core.game;

import com.cak.mcmg.Main;
import com.cak.mcmg.core.Debug;
import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.Team;
import com.cak.mcmg.core.TimedEvent;
import com.cak.mcmg.core.eventhandler.ActivityRule;
import com.cak.mcmg.core.events.GamePlayerEliminatedEvent;
import com.cak.mcmg.core.lang.WinnerLang;
import com.cak.mcmg.core.map.GameMap;
import com.cak.mcmg.core.map.MapLoader;
import com.cak.mcmg.core.scoreboard.PlayerScoreboard;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Objects;

public class ActiveGame {
  
  static ArrayList<McsuPlayer> alivePlayers = new ArrayList<>();
  static ArrayList<McsuPlayer> players = new ArrayList<>();
  static Game game;
  static GameMap gameMap;
  static boolean isActive;
  static boolean hasStarted;
  static World world;
  
  public static ArrayList<McsuPlayer> getAlivePlayers() {
    return alivePlayers;
  }
  
  public static ArrayList<McsuPlayer> getPlayers() {
    return players;
  }
  
  public static void eliminatePlayer(Player player) {
    eliminatePlayer(McsuPlayer.fromBukkit(player));
  }
  
  public static void eliminatePlayer(McsuPlayer player) {
    alivePlayers.remove(player);
    Bukkit.getServer().getPluginManager().callEvent(new GamePlayerEliminatedEvent(player));
  }
  
  public static void playGame(Game newGame) {
    game = newGame;
    gameMap = GameMap.maps.stream().filter(map -> Objects.equals(map.getGameId(), newGame.id)).findFirst().orElseThrow();
    hasStarted = false;
    isActive = true;
    
    alivePlayers = new ArrayList<>();
    alivePlayers.addAll(McsuPlayer.players);
    players = new ArrayList<>();
    players.addAll(McsuPlayer.players);
    
    world = MapLoader.loadWorld(gameMap.getWorldId());
    gameMap.loadSpawns(world, GameSpawnCategory.LOBBY);
    ActivityRule.clearAll();
    alivePlayers.forEach(ActiveGame::clearPlayer);
    
    game.getGameStates().forEach(GameState::setup);
    GameState lobbyState = game.getLobbyGameState();
    if (lobbyState != null) {
      lobbyState.setEnabled(true);
    }
    
    new TimedEvent("Teleport to game spawns", 15, false)
        .setOnTick((t) -> {})
        .setOnEnd(() -> {
          Debug.log("Teleporting players to game spawns for '" + game.getName() + "'");
          
          gameMap.loadSpawns(world, GameSpawnCategory.GAME);
  
          ActivityRule.PLAYER_MOVEMENT.setEnabled(false);
        });
    
    new TimedEvent("Game Starts", 20, true)
        .setOnTick((t) -> {
        })
        .setOnEnd(() -> {
          Debug.log("Starting game '" + game.getName() + "'");
  
          ActivityRule.PLAYER_MOVEMENT.setEnabled(true);
          hasStarted = true;
  
          PlayerScoreboard.setGamePrefixProvider(game.getGamePrefixProvider());
          
          if (lobbyState != null) {
            lobbyState.setEnabled(false);
          }
          
          GameState activeState = game.getActiveGameState();
          if (activeState != null) {
            activeState.setEnabled(true);
          }
          
        });
    
  }
  
  public static void endWithWinner(Team winner) {
  
    Main.infof("%s wins blocksumo!", winner.getName());
    
    for (McsuPlayer mcsuPlayer : McsuPlayer.players) {
      Player player = mcsuPlayer.toBukkit();
      if (mcsuPlayer.getTeam() == winner) {
        player.sendTitlePart(TitlePart.TITLE, WinnerLang.titleWinner(winner));
        player.sendTitlePart(TitlePart.SUBTITLE, WinnerLang.subtitleWinner(winner));
      } else {
        player.sendTitlePart(TitlePart.TITLE, WinnerLang.titleLooser(winner));
        player.sendTitlePart(TitlePart.SUBTITLE, WinnerLang.subtitleLooser(winner));
      }
      
      endGame();
    }
    
  }
  
  private static void endGame() {
    ActivityRule.clearAll();
    
    for (GameState gameState : game.getGameStates()) {
      gameState.reset();
    }
  }
  
  private static void clearPlayer(McsuPlayer player) {
    Player bukkitPlayer = player.toBukkit();
    bukkitPlayer.setHealth(Objects.requireNonNull(bukkitPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
    bukkitPlayer.setSaturation(1);
    bukkitPlayer.setFoodLevel(20);
    bukkitPlayer.getInventory().clear();
    bukkitPlayer.setGameMode(GameMode.SURVIVAL);
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
  
  public static boolean hasStarted() {
    return hasStarted;
  }
  
  public static boolean isActive() {
    return isActive;
  }
  
}
