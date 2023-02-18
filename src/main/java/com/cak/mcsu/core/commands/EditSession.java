package com.cak.mcsu.core.commands;

import com.cak.mcsu.Main;
import com.cak.mcsu.core.Debug;
import com.cak.mcsu.core.map.GameMap;
import com.cak.mcsu.core.map.GameSpawn;
import com.cak.mcsu.core.util.ColorConverter;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class EditSession {

    public static @Nullable EditSession getEditSession(Player player) {
        return editSessions.stream().filter(editSession -> editSession.editor.getUniqueId().equals(player.getUniqueId())).findFirst().orElse(null);
    }

    public static ArrayList<EditSession> editSessions = new ArrayList<>();

    static Particle.DustOptions gameSpawnOptions = new Particle.DustOptions(Color.fromRGB(25, 50, 55), 2);
    static Particle.DustOptions lobbySpawnColor = new Particle.DustOptions(Color.fromRGB(100, 255, 175), 2);

    GameMap map;
    World editWorld;
    Player editor;
    boolean isDevWorld;
    String worldName;

    BukkitRunnable spawnDisplayer = null;

    public EditSession(GameMap map, World editWorld, Player editor, boolean isDevWorld, String worldName) {
        editSessions.add(this);
        this.map = map;
        this.editWorld = editWorld;
        this.editor = editor;
        this.isDevWorld = isDevWorld;
        this.worldName = worldName;
    }

    public void showParticles() {
        if (spawnDisplayer != null && !spawnDisplayer.isCancelled())
            spawnDisplayer.cancel();

        spawnDisplayer = new BukkitRunnable() {
            @Override
            public void run() {
                for (GameSpawn spawn : map.getGameSpawns()) {
                    double[] location = spawn.getLocation();

                    Particle.DustOptions dustOptions = gameSpawnOptions;

                    if (spawn.getTeam() != null) {
                        ColorConverter converter = ColorConverter.fromCode(spawn.getTeam().getChatColor().getChar());

                        Debug.log(String.valueOf(converter.g));

                        dustOptions = new Particle.DustOptions(
                                Color.fromRGB(converter.r, converter.g, converter.b),
                                2
                        );
                    }

                    editWorld.spawnParticle(
                            Particle.REDSTONE,
                            location[0],
                            location[1],
                            location[2],
                            0,
                            0,
                            0,
                            0,
                            dustOptions
                    );
                }
            }
        };
        spawnDisplayer.runTaskTimer(Main.plugin, 0L, 5L);
    }

    public void hideParticles() {
        if (spawnDisplayer != null && !spawnDisplayer.isCancelled() && !isDevWorld)
            spawnDisplayer.cancel();
    }

    public void end() {
        if (spawnDisplayer != null && !spawnDisplayer.isCancelled() && !isDevWorld)
            spawnDisplayer.cancel();
    }

    public Player getEditor() {
        return editor;
    }

    public boolean getIsDevWorld() {
        return isDevWorld;
    }

    public String getWorldName() {
        return worldName;
    }


}
