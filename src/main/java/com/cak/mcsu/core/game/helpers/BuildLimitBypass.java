package com.cak.mcsu.core.game.helpers;

import org.bukkit.event.block.BlockPlaceEvent;

//region Interfaces
public interface BuildLimitBypass {
  boolean check(BlockPlaceEvent event);
}
