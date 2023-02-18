package com.cak.mcsu.core.game.functionhelper;

import org.bukkit.event.block.BlockPlaceEvent;

public interface BuildLimitBypass {
    boolean check(BlockPlaceEvent event);
}
