package com.cak.mcmg.core.game.functions;

import com.cak.mcmg.core.game.ActiveGame;
import com.cak.mcmg.core.game.helpers.ZoneFunction;
import org.bukkit.Location;

public class RadiusZone extends ZoneFunction {
  
  int zoneRadiusSquared;
  Location origin;
  
  public RadiusZone(int radius, Location origin) {
    super();
    zoneRadiusSquared = (int) Math.pow(radius, 2);
    this.origin = origin;
  }
  
  @Override
  public boolean isInside(double x, double y, double z) {
    origin.setWorld(ActiveGame.getWorld());
    return origin.distanceSquared(new Location(ActiveGame.getWorld(), x, y, z)) < zoneRadiusSquared;
  }
}
