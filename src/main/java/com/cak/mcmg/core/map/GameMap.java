package com.cak.mcmg.core.map;

import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.Team;
import com.cak.mcmg.core.game.GameSpawnCategory;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

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
  
  public void loadSpawns(World world, GameSpawnCategory spawnType) {
    List<GameSpawn> spawns = new ArrayList<>(List.of(spawnType == GameSpawnCategory.GAME ? gameSpawns : lobbySpawns));
    
    if (this.spawnType == SpawnType.TEAM && spawnType == GameSpawnCategory.GAME) {
  
      HashMap<Team, GameSpawn> teamSpawns = new HashMap<>();
      
      for (GameSpawn spawn : spawns) {
        if (spawn.getTeam() != null)
          teamSpawns.put(spawn.getTeam(), spawn);
        else {
          throw new RuntimeException("Spawn given without a team, but tried to load it as a team spawn anyway!");
        }
      }
      
      for (Player player : Bukkit.getOnlinePlayers()) {
        teamSpawns.get(McsuPlayer.fromBukkit(player).getTeam()).teleportPlayerTo(player, world);
      }
      return;
    }
    
    if (this.spawnType == SpawnType.SHUFFLE || spawnType == GameSpawnCategory.LOBBY) {
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
