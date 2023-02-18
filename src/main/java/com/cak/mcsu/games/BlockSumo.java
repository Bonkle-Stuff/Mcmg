package com.cak.mcsu.games;

import com.cak.mcsu.core.eventhandler.ActivityRule;
import com.cak.mcsu.core.game.ActiveGame;
import com.cak.mcsu.core.game.Game;
import com.cak.mcsu.core.game.GameState;
import com.cak.mcsu.core.game.functionhelper.BuildLimitBypass;
import com.cak.mcsu.core.game.functions.BuildHeight;
import com.cak.mcsu.core.game.functions.BuildRange;
import com.cak.mcsu.core.game.functions.EventListener;
import com.cak.mcsu.core.game.functions.HeightZone;
import org.bukkit.Material;
import org.bukkit.event.Listener;

import java.util.Arrays;

public class BlockSumo {

    static Game game = new Game("blocksumo", "Block Sumo");

    public static void register() {

        game.addGameState(new GameState("Base", true)
                .setOnEnable(()->{
                    ActivityRule.setEnabled(
                            ActivityRule.TILE_BREAKING, false,
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

                    lobbyHeightZone.setOnEnter(player -> player.teleport(
                            ActiveGame.getGameMap().getLobbySpawns()[0]
                                    .toBukkitPosition(ActiveGame.getWorld())
                    ));

                    game.getGameState("Lobby").addGameFunction(lobbyHeightZone);

                    //Setup active state for players falling off
                    HeightZone activeHeightZone = new HeightZone(killHeight);
                    activeHeightZone.setOnEnter(player -> player.setHealth(0));
                    BuildLimitBypass buildLimitBypass = placeEvent -> placeEvent.getBlock().getType() == Material.TNT;

                    game.getGameState("Active")
                            .addGameFunctions(
                                    activeHeightZone,
                                    new BuildRange(buildOrigin, buildRange, buildLimitBypass),
                                    new BuildHeight(buildMaxHeight, buildLimitBypass)
                            );
                })
        );

        game.setLobbyGameState(new GameState("Lobby")
        );
        game.setActiveGameState(new GameState("Active")
                .addGameFunctions(new EventListener(new Listener() {
                     /*@EventHandler
                     public void onPlayerMove(PlayerMoveEvent moveEvent) {
                         Debug.log("asgfgadfibhjl");
                     }*/
                }))
        );

    }

}
