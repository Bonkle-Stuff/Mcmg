package com.cak.mcsu.core.game.functions;

import com.cak.mcsu.core.game.GameFunction;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

/**
 * Stop players from breaking the map, but allows player placed blocks (since enable) to be broken
 */
public class MapProtection extends GameFunction {
  
  ArrayList<Location> placedPositions = new ArrayList<>();
  
  public MapProtection() {
    this.setListener(new Listener() {
      
      @EventHandler
      public void onBlockPlaceEvent(BlockPlaceEvent event) {
        placedPositions.add(event.getBlock().getLocation());
      }
      
      @EventHandler
      public void onBlockBreakEvent(BlockBreakEvent event) {
        event.setCancelled(!placedPositions.contains(event.getBlock().getLocation()));
      }
      
    });
  }
  
  public void onEnable() {
    placedPositions = new ArrayList<>();
  }
  
  
}
