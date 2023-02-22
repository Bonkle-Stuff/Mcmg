package com.cak.mcsu.core.eventhandler;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.UUID;

public class ActivityRuleEventHandler implements Listener {

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {

        event.setCancelled(!ActivityRule.TILE_BREAKING.isEnabled());
        event.setDropItems(!ActivityRule.TILE_DROP.isEnabled());

    }

    @EventHandler
    public void onBlockBreakEvent(BlockExplodeEvent event) {

        if (!ActivityRule.EXPLOSION_GRIEFING.isEnabled() || !ActivityRule.TILE_BREAKING.isEnabled()) {
            event.setCancelled(true);
        } else if (!ActivityRule.TILE_DROP.isEnabled()) {
            event.setYield(0);
        }

    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {

        event.setCancelled(!ActivityRule.TILE_PLACING.isEnabled());
        if (ActivityRule.REPLENISH_BLOCKS.isEnabled())
            event.getItemInHand().setAmount(event.getItemInHand().getAmount() + 1);

    }

    @EventHandler
    public void onItemDropEvent(PlayerDropItemEvent event) {

        event.setCancelled(!ActivityRule.DROP_ITEMS.isEnabled());

    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player))
            return;

        if (!ActivityRule.EXPLOSION_DAMAGE.isEnabled() && event.getDamager() instanceof TNTPrimed) {
            event.setCancelled(true);
        }

    }

    //Respawns

    private final HashMap<UUID, Location> respawnLocations = new HashMap<>();

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {

        respawnLocations.put(event.getPlayer().getUniqueId(), event.getPlayer().getLocation());

    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {

        Location respawnLocation = respawnLocations.get(event.getPlayer().getUniqueId());

        respawnLocation.setY(Math.min(0, Math.max(280, respawnLocation.getY())));

        event.setRespawnLocation(respawnLocation);

        respawnLocations.remove(event.getPlayer().getUniqueId());

    }

}
