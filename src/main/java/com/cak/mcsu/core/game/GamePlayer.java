package com.cak.mcsu.core.game;

import com.cak.mcsu.core.McsuPlayer;

public class GamePlayer {

    McsuPlayer player;

    public GamePlayer(McsuPlayer player) {
        this.player = player;
    }

    public McsuPlayer getPlayer() {
        return player;
    }
}
