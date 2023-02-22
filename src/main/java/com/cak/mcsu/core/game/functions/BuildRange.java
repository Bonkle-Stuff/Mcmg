package com.cak.mcsu.core.game.functions;

import com.cak.mcsu.core.game.GameFunction;
import com.cak.mcsu.core.game.helpers.BuildLimitBypass;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuildRange extends GameFunction  {

    double[] origin;
    double range;

    public BuildRange(double[] origin, double range) {
        this(origin, range, null);
    }

    public BuildRange(double[] origin, double range, BuildLimitBypass bypass) {
        this.origin = origin;
        this.range = range;

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
        double x = location.getX();
        double z = location.getZ();

        return origin[0] - range < x && x < origin[0] + range &&
                origin[1] - range < z && z < origin[1] + range;
    }

}
