package com.cak.mcmg.core.map;

import com.cak.mcmg.Main;
import com.cak.mcmg.core.Debug;
import org.bukkit.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

public class MapLoader {
  
  public static void saveWorld(String world, boolean buildWorld) {
    
    File worldDir = Main.plugin.getServer().getWorldContainer();
    String worldName = (buildWorld ? "build" : "map") + "-" + world;
    
    World saveWorld = Bukkit.getWorld(worldName);
    
    if (saveWorld == null) {
      throw new RuntimeException("World provided does not exist with name '" + worldName + "'");
    }
    
    
    File worldFolder = new File(Main.path + File.separator + "BuildWorld" + File.separator + world);
    if (worldFolder.exists()) {
      deleteDirectory(worldFolder);
      Debug.log("Overwritten world for '" + worldName + "' in devmaps during save");
    }
    
    
    saveWorld.save();
    
    try {
      copyDirectory(
          worldDir + File.separator + worldName,
          Main.path + File.separator + "BuildWorld" + File.separator + world
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
    
  }
  
  public static World loadWorld(String world) {
    
    File worldDir = Main.plugin.getServer().getWorldContainer();
    String worldName = "map-" + world;
    
    File worldFolder = new File(worldDir + File.separator + worldName);
    if (worldFolder.exists()) {
      deleteDirectory(worldFolder);
      Debug.log("Overwritten world for '" + worldName + "' during load");
    }
    
    try {
      copyDirectory(
          Main.path + File.separator + "Maps" + File.separator + world,
          worldDir + File.separator + worldName
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
    Bukkit.createWorld(new WorldCreator(worldName));
    
    World bukkitWorld = Bukkit.getWorld(worldName);
    assert bukkitWorld != null;
    bukkitWorld.setAutoSave(false);
    bukkitWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
    
    return bukkitWorld;
    
  }
  
  public static void clearWorld(String world) {
    
    File worldDir = Main.plugin.getServer().getWorldContainer();
    
    AtomicBoolean teleportedAny = new AtomicBoolean(false);
    
    Bukkit.getWorld(world).getPlayers().forEach(player -> {
      player.teleport(new Location(Bukkit.getWorld("world"), 20, 20, 20));
      teleportedAny.set(true);
    });
    if (teleportedAny.get()) {
      Main.warn("> [MapLoader.clearWorld] Player(s) in world '" + world + "' during unload, teleported them to 'world'");
    }
    
    boolean worldUnloaded = Bukkit.unloadWorld(world, false);
    
    if (worldUnloaded) {
      Debug.log("> Successfully unloaded world '" + world + "'");
    } else {
      Main.warn("> [MapLoader.clearWorld] Unsuccessfully loaded world '" + world + "'");
    }
    
    deleteDirectory(new File(worldDir + File.separator + world));
    
  }
  
  public static World createWorld(String world, boolean buildWorld) {
    
    File worldDir = Main.plugin.getServer().getWorldContainer();
    String worldName = (buildWorld ? "build" : "map") + "-" + world;
    
    File worldFolder = new File(worldDir + File.separator + worldName);
    if (worldFolder.exists()) {
      deleteDirectory(worldFolder);
      Debug.log("Overwritten world for '" + worldName + "' during load");
    }
    
    try {
      copyDirectory(
          Main.path + File.separator + "EmptyWorld",
          worldDir + File.separator + worldName
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
    Bukkit.createWorld(new WorldCreator(worldName));
    
    World bukkitWorld = Bukkit.getWorld(worldName);
    assert bukkitWorld != null;
    bukkitWorld.setAutoSave(false);
    
    return bukkitWorld;
    
  }
  
  static void copyDirectory(String sourceLoc, String destinationLoc) throws IOException {
    Files.walk(Paths.get(sourceLoc))
        .forEach(source -> {
          Path destination = Paths.get(destinationLoc, source.toString()
              .substring(sourceLoc.length()));
          try {
            Files.copy(source, destination);
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }
  
  static boolean deleteDirectory(File dir) {
    File[] allContents = dir.listFiles();
    if (allContents != null) {
      for (File file : allContents) {
        deleteDirectory(file);
      }
    }
    return dir.delete();
  }
  
}
