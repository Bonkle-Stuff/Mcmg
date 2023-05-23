package com.cak.mcsu.core.game.functions;

import com.cak.mcsu.core.game.GameFunction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class LaunchableFireballs extends GameFunction {

  public LaunchableFireballs() {
    setListener(new Listener() {
      @EventHandler
      public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
  
        Player player = event.getPlayer();
        if (event.getItem().getType() == Material.FIRE_CHARGE) {
          event.setCancelled(true);
          Location fireballLoc = player.getEyeLocation();
          player.getWorld().spawnEntity(fireballLoc, EntityType.FIREBALL);
          event.getItem().setAmount(event.getItem().getAmount()-1);
        }
      }
    });
  }

}
