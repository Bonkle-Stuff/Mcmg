package com.cak.mcmg.core.game.functions;

import com.cak.mcmg.core.game.GameFunction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockReplenishing extends GameFunction {
  
  public BlockReplenishing(BlockReplenishFilter filter) {
  
    setListener(new Listener() {
      @EventHandler
      public void onBlockPlaceEvent(BlockPlaceEvent event) {
  
        if (filter.check(event.getItemInHand()))
          event.getPlayer().getInventory().setItem(event.getHand(), event.getItemInHand());
      }
    });
    
  }
  
  public interface BlockReplenishFilter {
    boolean check(ItemStack blockStack);
  }
  
}
