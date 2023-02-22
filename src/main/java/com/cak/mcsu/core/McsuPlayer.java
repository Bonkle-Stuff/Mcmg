package com.cak.mcsu.core;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class McsuPlayer {

    public static McsuPlayer fromBukkit(Player player) {
        return players.stream().filter(candidate -> candidate.bukkitPlayer == player).findFirst().orElse(null);
    }

    public static ArrayList<McsuPlayer> players = new ArrayList<>();

    Team team;

    Player bukkitPlayer;

    public McsuPlayer(Player player) {
        bukkitPlayer = player;

        players.add(this);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Player toBukkit() {
        return bukkitPlayer;
    }


}
