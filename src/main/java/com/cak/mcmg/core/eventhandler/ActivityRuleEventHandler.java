package com.cak.mcmg.core.eventhandler;

import com.cak.mcmg.core.game.ActiveGame;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
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
      event.getEntity().getWorld().createExplosion(event.getLocation(),5, false,false);
    } else if (!ActivityRule.TILE_DROP.isEnabled()) {
      event.setYield(0);
    }
  
  
    Entity entity = event.getEntity();
    Location explosionOrigin = entity.getLocation();
  
    @NotNull Collection<LivingEntity> entities = entity.getWorld().getNearbyLivingEntities(entity.getLocation(), 5);
  
    entities.forEach(e -> {
      if (e.getLocation() == explosionOrigin) return;
      e.setVelocity(e.getVelocity().add(explosionOrigin.toVector().subtract(e.getLocation().toVector()).normalize()
        .multiply(Math.max(5 - explosionOrigin.toVector().distance(e.getLocation().toVector()), 0))
        .multiply(-0.1).multiply(new Vector(1, 1.1, 1))));
    });
  
  }
  
  //>Replenish blocks & Auto Ignite TNT
  @EventHandler
  public void onBlockPlaceEvent(BlockPlaceEvent event) {
    
    if (!ActivityRule.TILE_PLACING.isEnabled()) {
      event.setCancelled(true);
      return;
    }
    
    if (event.getBlockPlaced().getType() == Material.TNT) {
      
      event.getBlockPlaced().setType(Material.AIR);
      TNTPrimed tntPrimedEntity = (TNTPrimed) event.getBlockPlaced().getWorld().spawnEntity(
          event.getBlockPlaced().getLocation().add(0.5, 0, 0.5), EntityType.PRIMED_TNT
      );
      tntPrimedEntity.setFuseTicks(20);
    }
    
  }
  
  //>Cancel drops
  @EventHandler
  public void onItemDropEvent(PlayerDropItemEvent event) {
    
    event.setCancelled(!ActivityRule.DROP_ITEMS.isEnabled());
    
  }
  
  //>Entity on entity Damage
  @EventHandler
  public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
    
    if (!(event.getEntity() instanceof Player))
      return;
    
    if (!ActivityRule.ANY_DAMAGE.isEnabled()) {
      event.setDamage(0);
      return;
    }
  
    if (!ActivityRule.EXPLOSION_DAMAGE.isEnabled() && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
      event.setDamage(0);
      return;
    }
    
  }
  
  //>Non entity Damage
  @EventHandler
  public void onEntityDamageEvent(EntityDamageEvent event) {
    
    if (!(event.getEntity() instanceof Player))
      return;
    
    //Keep the event to allow for knockback to affect on explosions
    if (!ActivityRule.ANY_DAMAGE.isEnabled() &&
        !(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK ||
            event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK ||
            event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION ||
            event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION ||
            event.getCause() == EntityDamageEvent.DamageCause.CUSTOM
        )) {
      event.setCancelled(true);
      return;
    } else if (!ActivityRule.ANY_DAMAGE.isEnabled() && event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
      event.setDamage(0);
      return;
    }
    
    if (!ActivityRule.EXPLOSION_DAMAGE.isEnabled() && event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
      event.setDamage(0);
      return;
    }
    
    if (!ActivityRule.FALL_DAMAGE.isEnabled() && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
      event.setCancelled(true);
      return;
    }
    
    
  }
  
  //>Fireball Respawn & Disable Trapdoors
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (event.getPlayer().isSneaking() && event.isBlockInHand()) return; //Player is sneaking so they will place instead of interacting
  
    event.setCancelled(event.getClickedBlock() != null &&
        event.getClickedBlock().getType().name().endsWith("_TRAPDOOR") &&
        event.getAction() != Action.LEFT_CLICK_BLOCK);
    
    if (event.getItem() != null &&
        event.getItem().getType() == Material.FIRE_CHARGE) {
      
      Player player = event.getPlayer();
      event.setCancelled(true);
      Location fireballLoc = player.getEyeLocation();
      player.getWorld().spawnEntity(fireballLoc, EntityType.FIREBALL);
      event.getItem().setAmount(event.getItem().getAmount() - 1);
      
    }
    
  }

  //>Player Movement
  @EventHandler(priority = EventPriority.LOW)
  public void onPlayerMoveEvent(PlayerMoveEvent event) {
  
    if (!ActivityRule.PLAYER_MOVEMENT.isEnabled()) {
      event.setTo(event.getFrom().setDirection(event.getTo().getDirection()));
    }
    
  }
  
  //region Respawn location handling
  
  private final HashMap<UUID, Location> respawnLocations = new HashMap<>();
  
  @EventHandler
  public void onPlayerDeathEvent(PlayerDeathEvent event) {
    
    respawnLocations.put(event.getPlayer().getUniqueId(), event.getPlayer().getLocation());
    
  }
  
  @EventHandler
  public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
  
    Location respawnLocation = respawnLocations.get(event.getPlayer().getUniqueId());
    
    respawnLocation.setY(Math.min(0, Math.max(280, respawnLocation.getY())));
  
    if (ActiveGame.getWorld() == respawnLocation.getWorld()) {
      event.setRespawnLocation(respawnLocation);
      
      respawnLocations.remove(event.getPlayer().getUniqueId());
      
      event.getPlayer().setGameMode(GameMode.SPECTATOR);
    }
    
  }
  
  //endregion
  
}
