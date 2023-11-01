package com.cak.mcmg.core.map;

import com.cak.mcmg.core.Team;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class GameSpawn {
  
  final double[] location;
  final double[] direction;
  @Nullable
  final Team team;
  
  public GameSpawn(double[] location, double[] direction, @Nullable Team team) {
    this.location = location;
    this.direction = direction;
    this.team = team;
  }
  
  public double[] getLocation() {
    return location;
  }
  
  public double[] getDirection() {
    return direction;
  }
  @Nullable
  public Team getTeam() {
    return team;
  }
  
  public Location toBukkitPosition(World world) {
    return new Location(world, location[0], location[1], location[2]);
  }
  
  public void teleportPlayerTo(Player player, World world) {
  
    player.teleport(
        new Location(
            world,
            location[0],
            location[1],
            location[2],
            (float) direction[0],
            (float) direction[1]
        )
    );
    
  }
  
}
