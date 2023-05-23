package com.cak.mcsu.core.map;

import com.cak.mcsu.core.game.GameSpawnCategory;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GameMap {
  
  public static @Nullable GameMap getMap(String id) {
    return maps.stream().filter(map -> Objects.equals(map.getMapId(), id)).findFirst().orElse(null);
  }
  
  public static ArrayList<GameMap> maps = new ArrayList<>();
  
  final String gameId;
  final String mapId;
  final String mapName;
  final SpawnType spawnType;
  final ConfigurationSection gameData;
  
  final GameSpawn[] gameSpawns;
  final GameSpawn[] lobbySpawns;
  
  public GameMap(String gameId, String mapId, String mapName, String spawnType, ConfigurationSection gameData, GameSpawn[] gameSpawns, GameSpawn[] lobbySpawns) {
    this.gameId = gameId;
    this.mapId = mapId;
    this.mapName = mapName;
    this.spawnType = SpawnType.valueOf(spawnType.toUpperCase());
    this.gameData = gameData;
    this.gameSpawns = gameSpawns;
    this.lobbySpawns = lobbySpawns;
    
    maps.add(this);
  }
  
  public String getGameId() {
    return gameId;
  }
  
  public String getMapId() {
    return mapId;
  }
  
  public String getWorldId() {
    return getGameId() + "-" + getMapId();
  }
  
  public String getMapName() {
    return mapName;
  }
  
  public ConfigurationSection getGameData() {
    return gameData;
  }
  
  public GameSpawn[] getGameSpawns() {
    return gameSpawns;
  }
  
  public GameSpawn[] getLobbySpawns() {
    return lobbySpawns;
  }
  
  public void loadSpawns(World world, GameSpawnCategory spawn) {
    List<GameSpawn> spawns = new ArrayList<>(List.of(spawn == GameSpawnCategory.GAME ? gameSpawns : lobbySpawns));
    
    
    if (spawnType == SpawnType.SHUFFLE || spawn == GameSpawnCategory.LOBBY) {
      Collections.shuffle(spawns);
    }
    
    int i = 0;
    for (Player player : Bukkit.getOnlinePlayers()) {
      
      spawns.get(i).teleportPlayerTo(player, world);
      
      i++;
      i = i % spawns.size();
    }
    
  }
  
}
