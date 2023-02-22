package com.cak.mcsu.core.game.functions;

import com.cak.mcsu.core.McsuPlayer;
import com.cak.mcsu.core.game.ActiveGame;
import com.cak.mcsu.core.game.GameFunction;
import com.cak.mcsu.core.game.GamePlayer;
import com.cak.mcsu.core.game.helpers.GamePlayerFactory;

import java.util.ArrayList;

public class GamePlayerHelper<T extends GamePlayer> extends GameFunction {

    ArrayList<T> players = new ArrayList<>();
    GamePlayerFactory<T> playerFactory;

    public GamePlayerHelper(GamePlayerFactory<T> playerFactory) {
        this.playerFactory = playerFactory;
    }

    @Override
    public void onEnable() {
        players = new ArrayList<>();

        for (McsuPlayer player : ActiveGame.getPlayers()) {
            players.add(playerFactory.build(player));
        }
    }

    @Override
    public void onDisable() {
        players = new ArrayList<>();
    }

    public T getPlayer(McsuPlayer bukkitPlayer) {
        return players.stream().filter(player -> player.getPlayer().equals(bukkitPlayer)).findFirst().orElse(null);
    }

    public ArrayList<T> getPlayers() {
        return players;
    }
}
