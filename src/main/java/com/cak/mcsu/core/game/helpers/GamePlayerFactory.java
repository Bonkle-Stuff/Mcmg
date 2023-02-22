package com.cak.mcsu.core.game.helpers;

import com.cak.mcsu.core.McsuPlayer;
import com.cak.mcsu.core.game.GamePlayer;

public interface GamePlayerFactory<T extends GamePlayer> {
    T build(McsuPlayer player);
}
