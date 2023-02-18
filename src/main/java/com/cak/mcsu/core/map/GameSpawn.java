package com.cak.mcsu.core.map;

import com.cak.mcsu.core.Team;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nullable;

public class GameSpawn {

    final double[] location;
    final double[] direction;
    final Team team;

    public GameSpawn(double[] location, double[] direction, @Nullable Team team) {
        this.location = location;
        this.direction = direction;
        this.team = team;
    }

    public double[] getLocation() {
        return location;
    }

    public double[] getDirection() {
        return direction;
    }

    public Team getTeam() {
        return team;
    }

    public Location toBukkitPosition(World world) {
        return new Location(world, location[0], location[1], location[2]);
    }

}
