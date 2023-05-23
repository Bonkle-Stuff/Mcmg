package com.cak.mcsu.core.eventhandler;

import com.cak.mcsu.core.game.ActiveGame;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.UUID;

public class ActivityRuleEventHandler implements Listener {
  
  //>Tile Breaking & Drops
  @EventHandler
  public void onBlockBreakEvent(BlockBreakEvent event) {
    
    event.setCancelled(!ActivityRule.TILE_BREAKING.isEnabled());
    event.setDropItems(ActivityRule.TILE_DROP.isEnabled());
    
  }
  
  //>Explosion Tile Breaking & Drops
  @EventHandler
  public void onEntityExplodeEvent(EntityExplodeEvent event) {
    
    if (!ActivityRule.EXPLOSION_GRIEFING.isEnabled() || !ActivityRule.TILE_BREAKING.isEnabled()) {
      event.setCancelled(true);
      event.getEntity().getWorld().createExplosion(event.getLocation(),5,false,false);
    } else if (!ActivityRule.TILE_DROP.isEnabled()) {
      event.setYield(0);
    }
    
  }
  
  //>Replenish blocks & Auto Ignite TNT
  @EventHandler
  public void onBlockPlaceEvent(BlockPlaceEvent event) {
    
    event.setCancelled(!ActivityRule.TILE_PLACING.isEnabled());
    
    if (event.getBlockPlaced().getType() == Material.TNT) {
      
      event.getBlockPlaced().setType(Material.AIR);
      event.getBlockPlaced().getWorld().spawnEntity(
          event.getBlockPlaced().getLocation(), EntityType.PRIMED_TNT
      );
      
    }
    
  }
  
  //>Cancel drops
  @EventHandler
  public void onItemDropEvent(PlayerDropItemEvent event) {
    
    event.setCancelled(!ActivityRule.DROP_ITEMS.isEnabled());
    
  }
  
  //>TNT Explosion Damage
  @EventHandler
  public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
    
    if (!(event.getEntity() instanceof Player))
      return;
    
    if (!ActivityRule.EXPLOSION_DAMAGE.isEnabled() && event.getDamager() instanceof TNTPrimed) {
      event.setCancelled(true);
    }
    
  }
  
  //>Fireball Respawn & Disable Trapdoors
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    event.setCancelled(event.getClickedBlock() != null &&
        event.getClickedBlock().getType().name().endsWith("_TRAPDOOR"));
    
    if (event.getItem() != null &&
        event.getItem().getType() == Material.FIRE_CHARGE) {
      
      Player player = event.getPlayer();
      event.setCancelled(true);
      Location fireballLoc = player.getEyeLocation();
      player.getWorld().spawnEntity(fireballLoc, EntityType.FIREBALL);
      event.getItem().setAmount(event.getItem().getAmount()-1);
      
    }
    
  }
  
  //region Respawn positions
  
  private final HashMap<UUID, Location> respawnLocations = new HashMap<>();
  
  @EventHandler
  public void onPlayerDeathEvent(PlayerDeathEvent event) {
    
    respawnLocations.put(event.getPlayer().getUniqueId(), event.getPlayer().getLocation());
    event.getPlayer().setGameMode(GameMode.SPECTATOR);
    
  }
  
  @EventHandler
  public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
    
    Location respawnLocation = respawnLocations.get(event.getPlayer().getUniqueId());
    
    respawnLocation.setY(Math.min(0, Math.max(280, respawnLocation.getY())));
    
    event.setRespawnLocation(respawnLocation);
    
    respawnLocations.remove(event.getPlayer().getUniqueId());
    
    if (ActiveGame.getWorld() == respawnLocation.getWorld()) {
      event.getPlayer().setGameMode(GameMode.SPECTATOR);
    }
    
  }
  
  //endregion
  
}
