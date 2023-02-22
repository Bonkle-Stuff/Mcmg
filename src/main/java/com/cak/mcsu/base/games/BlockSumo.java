package com.cak.mcsu.base.games;

import com.cak.mcsu.base.players.BlockSumoPlayer;
import com.cak.mcsu.core.eventhandler.ActivityRule;
import com.cak.mcsu.core.game.ActiveGame;
import com.cak.mcsu.core.game.Game;
import com.cak.mcsu.core.game.GameState;
import com.cak.mcsu.core.game.functions.*;
import com.cak.mcsu.core.game.helpers.BuildLimitBypass;
import com.cak.mcsu.core.scoreboard.PlayerScoreboard;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Arrays;

public class BlockSumo {

    static Game game = new Game("blocksumo", "Block Sumo");

    static GamePlayerHelper<BlockSumoPlayer> playerHelper;

    public static void register() {

        game.addPrefixProvider(player -> {
            BlockSumoPlayer blockSumoPlayer = playerHelper.getPlayer(player);
            return (blockSumoPlayer == null ? Component.text("") : blockSumoPlayer.getLivesTabString());
        });
        game.addGameState(new GameState("Base", true)
                .setOnEnable(()->{
                    ActivityRule.setEnabled(
                            ActivityRule.TILE_BREAKING, true,
                            ActivityRule.TILE_PLACING, true,
                            ActivityRule.REPLENISH_BLOCKS, true,
                            ActivityRule.TILE_DROP, false
                    );

                    //Map GameData
                    double buildRange = ActiveGame.getMapGameData().getDouble("buildRange");
                    double[] buildOrigin = Arrays.stream(
                            ActiveGame.getMapGameData().getString("buildOrigin")
                                    .split(" ")
                    ).mapToDouble(Double::valueOf).toArray();
                    double buildMaxHeight = ActiveGame.getMapGameData().getDouble("buildMaxHeight");
                    double killHeight = ActiveGame.getMapGameData().getDouble("killHeight");

                    //Tp players falling off to platform
                    HeightZone lobbyHeightZone = new HeightZone(killHeight);

                    lobbyHeightZone.setOnInside(player -> player.teleport(
                            ActiveGame.getGameMap().getLobbySpawns()[0]
                                    .toBukkitPosition(ActiveGame.getWorld())
                    ));

                    game.getLobbyGameState().addGameFunction(lobbyHeightZone);

                    //Setup active state for players falling off
                    HeightZone activeHeightZone = new HeightZone(killHeight);
                    activeHeightZone.setOnInside(player -> player.setHealth(0));

                    BuildLimitBypass buildLimitBypass = placeEvent -> placeEvent.getBlock().getType() == Material.TNT;

                    game.getActiveGameState()
                            .addGameFunctions(
                                    activeHeightZone,
                                    new BuildRange(buildOrigin, buildRange, buildLimitBypass),
                                    new BuildHeight(buildMaxHeight, buildLimitBypass),
                                    new TaskInterval(() -> {
                                        for (BlockSumoPlayer player : playerHelper.getPlayers()) {
                                            player.getPlayer().toBukkit().sendActionBar(player.getLivesPrefix());
                                        }
                                    }, 20L, 20L)
                            );
                    Bukkit.getLogger().info(String.valueOf(killHeight));
                })
                .addGameFunctions(
                        new MapProtection()
                )
        );

        game.setLobbyGameState(new GameState("Lobby")
        );
        game.setActiveGameState(new GameState("Active")
                .setOnEnable(() -> playerHelper.getPlayers().forEach(blockSumoPlayer ->
                        PlayerScoreboard.getScoreboard(blockSumoPlayer.getPlayer()).updateGamePrefix()))
                .addGameFunctions(
                        playerHelper = new GamePlayerHelper<>(BlockSumoPlayer::new)
                )
        );

    }

}
