package com.cak.mcmg.core.scoreboard;

import com.cak.mcmg.core.McsuPlayer;
import net.kyori.adventure.text.Component;

public interface PrefixProvider {
  Component get(McsuPlayer player);
}
