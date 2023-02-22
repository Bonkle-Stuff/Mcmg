package com.cak.mcsu.core.scoreboard;

import com.cak.mcsu.core.McsuPlayer;
import net.kyori.adventure.text.Component;

public interface PrefixProvider {
    Component get(McsuPlayer player);
}
