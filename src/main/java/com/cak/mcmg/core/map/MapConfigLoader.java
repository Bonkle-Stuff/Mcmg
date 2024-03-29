package com.cak.mcmg.core.map;

import com.cak.mcmg.Main;
import com.cak.mcmg.core.config.ConfigValidator;
import com.cak.mcmg.core.Debug;
import com.cak.mcmg.core.Team;
import com.cak.mcmg.core.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapConfigLoader {
  
  public static void load() {//Get configfiles in mapconfig from datafolder and create necassary gamemaps
    
    File mapConfigDir = new File(Main.plugin.getDataFolder().getAbsolutePath() + File.separator + "MapConfig");
  
    if (!mapConfigDir.exists()) {
      Bukkit.getLogger().warning(String.format("[MapConfigLoader.load] Could not read map config (Folder MapConfig does not exist)! [Path : %s]", mapConfigDir.getAbsoluteFile()));
      return;
    }
    
    File[] configFiles = mapConfigDir.listFiles();
    
    if (configFiles == null) {
      Bukkit.getLogger().warning(String.format("[MapConfigLoader.load] Could not read map config (listFiles() is null)! [Path : %s]", mapConfigDir.getAbsoluteFile()));
      return;
    }
    
    Debug.log("[MapConfigLoader.load] Loading Maps: ");
    
    for (File configFile : configFiles) {
      
      loadConfigFile(configFile, mapConfigDir);
      
    }
    
  }
  
  public static void loadConfigFile(File configFile, File mapConfigDir) {
    
    
    Debug.log("> Loading: " + configFile.getName());
    
    YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(
        mapConfigDir + File.separator + configFile.getName()
    ));
    
    Game.getGame(config.getString("gameId"));
    
    String gameId = config.getString("gameId");
    String id = config.getString("id");
    String name = config.getString("name");
    String spawnType = config.getString("spawnType");
    
    ConfigurationSection gameData = config.getConfigurationSection("gameData");
    
    if (ConfigValidator.warnNullAny("MapConfigLoader.loadConfigFile", " for map " + configFile.getName(),
        "Game Id", gameId,
        "Id", id,
        "Name", name,
        "Spawn Type", spawnType
    )) {
      return;
    }
    
    if (configFile.getName().substring(configFile.getName().indexOf(".")).equals(gameId + "-" + id)) {
      Main.warn(">> Name mismatch with '" + configFile.getName() + "' and game id '" + gameId + "', map id '" + id + "'");
    }
    
    assert spawnType != null;
    GameMap map = new GameMap(
        gameId, id, name, spawnType, gameData,
        loadSpawns(config, "gameSpawns"),
        loadSpawns(config, "lobbySpawns")
    );//Registered to gamemaps in constructor
    
    Debug.log(">> Registered map: " + map.getMapName());
  }
  
  static GameSpawn[] loadSpawns(YamlConfiguration config, String name) {//Return a list of game spawns
    List<HashMap<String, String>> spawnData = (List<HashMap<String, String>>) config.getList(name);
    
    if (spawnData == null) {
      Bukkit.getLogger().warning(">> [MapConfigLoader.loadSpawns] Could not read spawns for map config ('" + name + "' is null)!");
      return new GameSpawn[0];
    }
    
    GameSpawn[] spawns = new GameSpawn[spawnData.size()];
    
    for (int i = 0; i < spawnData.size(); i++) {
      
      double[] loc = Arrays.stream(spawnData.get(i).get("loc").split(" ")).mapToDouble(Double::valueOf).toArray();
      double[] dir = Arrays.stream(spawnData.get(i).get("dir").split(" ")).mapToDouble(Double::valueOf).toArray();
      
      Team team = Team.get(spawnData.get(i).get("team"));
  
      if (team == null) {
        Main.warnf("Could not resolve team '%s'", spawnData.get(i).get("team"));
      }
      
      spawns[i] = new GameSpawn(loc, dir, team);
    }
    
    return spawns;
  }
  
}
