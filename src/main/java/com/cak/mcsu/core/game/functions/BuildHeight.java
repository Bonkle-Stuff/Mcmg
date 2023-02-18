package com.cak.mcsu.core.game.functions;

import com.cak.mcsu.core.game.GameFunction;
import com.cak.mcsu.core.game.functionhelper.BuildLimitBypass;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuildHeight extends GameFunction  {

    double maxHeight;

    public BuildHeight(double maxHeight) {
        this(maxHeight, null);
    }

    public BuildHeight(double maxHeight, BuildLimitBypass bypass) {
        this.maxHeight = maxHeight;

        this.setListener(new Listener() {
            @EventHandler
            public void onBlockPlaceEvent(BlockPlaceEvent event) {

                if (isInside(event.getBlock().getLocation()))
                    return;

                if (bypass != null && bypass.check(event))
                    return;

                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.DARK_RED + "Block is out of range!");

            }
        });
    }

    private boolean isInside(Location location) {
        double y = location.getY();

        return y < maxHeight;
    }

}
