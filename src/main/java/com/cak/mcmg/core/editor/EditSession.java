package com.cak.mcmg.core.editor;

import com.cak.mcmg.Main;
import com.cak.mcmg.core.Debug;
import com.cak.mcmg.core.map.GameMap;
import com.cak.mcmg.core.map.GameSpawn;
import com.cak.mcmg.core.util.ColorConverter;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class EditSession {
  
  public static @Nullable EditSession getEditSession(Player player) {
    return editSessions.stream().filter(editSession -> editSession.editWorld.equals(player.getWorld())).findFirst().orElse(null);
  }
  
  public static ArrayList<EditSession> editSessions = new ArrayList<>();
  
  static Particle.DustOptions gameSpawnOptions = new Particle.DustOptions(Color.fromRGB(25, 50, 55), 2);
  static Particle.DustOptions lobbySpawnOptions = new Particle.DustOptions(Color.fromRGB(100, 255, 175), 2);
  
  GameMap map;
  World editWorld;
  boolean isBuildWorld;
  String worldName;
  
  BukkitRunnable spawnDisplayer = null;
  
  public EditSession(GameMap map, World editWorld, String worldName) {
    editSessions.add(this);
    this.map = map;
    this.editWorld = editWorld;
    this.worldName = worldName;
  }
  
  public void showParticles() {
    if (spawnDisplayer != null && !spawnDisplayer.isCancelled())
      spawnDisplayer.cancel();
    
    spawnDisplayer = new BukkitRunnable() {
      @Override
      public void run() {
        for (GameSpawn spawn : map.getGameSpawns()) {
          showParticles(spawn, false);
        }
        for (GameSpawn spawn : map.getLobbySpawns()) {
          showParticles(spawn, true);
        }
      }
    };
    spawnDisplayer.runTaskTimer(Main.plugin, 0L, 5L);
  }
  
  private void showParticles(GameSpawn spawn, boolean isLobby) {
    double[] location = spawn.getLocation();
  
    Particle.DustOptions dustOptions = isLobby ? gameSpawnOptions : lobbySpawnOptions;
  
    if (spawn.getTeam() != null) {
      ColorConverter converter = ColorConverter.fromCode(spawn.getTeam().getChatColor().getChar());
    
      Debug.log(String.valueOf(converter.g));
    
      dustOptions = new Particle.DustOptions(
          Color.fromRGB(converter.r, converter.g, converter.b),
          2
      );
    }
  
    editWorld.spawnParticle(
        Particle.REDSTONE,
        location[0],
        location[1],
        location[2],
        0,
        0,
        0,
        0,
        dustOptions
    );
  }
  
  public void hideParticles() {
    if (spawnDisplayer != null && !spawnDisplayer.isCancelled())
      spawnDisplayer.cancel();
  }
  
  public String getWorldName() {
    return worldName;
  }
  
  public boolean getIsBuildWorld() {
    return isBuildWorld;
  }
}
