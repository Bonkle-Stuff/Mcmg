package com.cak.mcmg.core.game.helpers;

import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.game.GamePlayer;

public interface GamePlayerFactory<T extends GamePlayer> {
    T build(McsuPlayer player);
}
