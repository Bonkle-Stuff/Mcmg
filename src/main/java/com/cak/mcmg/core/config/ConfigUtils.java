package com.cak.mcmg.core.config;

import com.cak.mcmg.core.game.ActiveGame;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Arrays;

public class ConfigUtils {
  
  public static double[] getDoubleArray(String configValue) {
    if (configValue == null || configValue.length() == 0)
      throw new IllegalArgumentException("Given an invalid string as configValue");
    return Arrays.stream(configValue.split(" ")).mapToDouble(Double::valueOf).toArray();
  }
  
  public static Location getLocation(World world, String configValue) {
    double[] pos = getDoubleArray(configValue);
    return new Location(world, pos[0], pos[1], pos[2]);
  }
  public static Location getLocationInActiveGame(String configValue) {
    return getLocation(ActiveGame.getWorld(), configValue);
  }
  
}
