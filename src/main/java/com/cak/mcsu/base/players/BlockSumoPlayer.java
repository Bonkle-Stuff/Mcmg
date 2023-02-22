package com.cak.mcsu.base.players;

import com.cak.mcsu.core.McsuPlayer;
import com.cak.mcsu.core.Texts;
import com.cak.mcsu.core.game.GamePlayer;
import net.kyori.adventure.text.Component;

public class BlockSumoPlayer extends GamePlayer {

    static int maxLives = 5;
    int lives = maxLives;

    public BlockSumoPlayer(McsuPlayer player) {
        super(player);
    }

    public Component getLivesPrefix() {
        return Texts.livesBar(maxLives, lives);
    }

    public Component getLivesTabString() {
        return Texts.lives(lives);
    }


    public void setLives(int lives) {
        this.lives = lives;
    }

    public void removeLife() {
        this.lives--;
    }

}
