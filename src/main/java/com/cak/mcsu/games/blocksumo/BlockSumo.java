package com.cak.mcsu.games.blocksumo;

import com.cak.mcmg.core.TimedEvent;
import com.cak.mcmg.core.config.ConfigUtils;
import com.cak.mcmg.core.eventhandler.ActivityRule;
import com.cak.mcmg.core.game.ActiveGame;
import com.cak.mcmg.core.game.Game;
import com.cak.mcmg.core.game.GameState;
import com.cak.mcmg.core.game.functions.*;
import com.cak.mcmg.core.game.helpers.BuildLimitBypass;
import com.cak.mcmg.core.scoreboard.PlayerScoreboard;
import com.cak.mcmg.core.scoreboard.PrefixProvider;
import com.cak.mcmg.core.util.LootTable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public class BlockSumo extends Game {

  public BlockSumo() {
    super("blocksumo", "Block Sumo");
  }

  static int maxPowerups = 5;

  static boolean powerupsEnabled = false;
  static GamePlayerHelper<BlockSumoPlayer> playerHelper;
  static GameInventoryHelper<BlockSumoInventory> inventoryHelper;
  static LootTable activeLootTable;

  @Override
  public PrefixProvider createPrefixProvider() {
    return player -> {
      BlockSumoPlayer blockSumoPlayer = playerHelper.getPlayer(player);
      return (
        blockSumoPlayer == null ? Component.text("")
          : blockSumoPlayer.getLivesTabString()
      );
    };
  }

  @Override
  public GameState createActiveGameState() {
    return super.createActiveGameState()
      .setOnEnable(() -> {
        PlayerScoreboard.updateAllPrefixes();

        powerupsEnabled = false;
        new TimedEvent("Powerups I", 60, true)
          .setOnEnd(() -> {
            activeLootTable = BlockSumoLoot.powerupLootTableI;
            powerupsEnabled = true;
          });

        new TimedEvent("Powerups II", 60 + 60, true)
          .setOnEnd(() -> {
            activeLootTable = BlockSumoLoot.powerupLootTableII;
          });

        new TimedEvent("Powerups III", 60 + 60 + 60, true)
          .setOnEnd(() -> {
            activeLootTable = BlockSumoLoot.powerupLootTableIII;
          });
        new TimedEvent("Just win already", 60 + 60 + 60 + 60, true)
          .setOnEnd(() -> {
            for (BlockSumoPlayer player : playerHelper.getPlayers()) {
              if (player.getLives() != 0) {
                player.setLives(1);
              }
              player.getPlayer().toBukkit().sendTitlePart(
                  TitlePart.TITLE, BlockSumoLang.finalDeath()
              );
            }
          });
      })
      .addGameFunctions(
          playerHelper = new GamePlayerHelper<>(BlockSumoPlayer::new),
          inventoryHelper = new GameInventoryHelper<>(BlockSumoInventory::new),
          new MultipleLives<>(playerHelper) {
            @Override
            protected void onPlayerRespawn(BlockSumoPlayer player) {
              inventoryHelper.getPlayer(player.getPlayer()).apply();
              if (activeLootTable != null)
                player.getPlayer().toBukkit().getInventory().addItem(activeLootTable.generate());
            }
          },
          new ConfiguredFunction(config -> new HeightZone(config.getInt("killHeight")) {
            @Override
            public void onInside(Player player) {
              player.damage(9999);
            }
          }),
          
          //>Give out powerups
          new TaskInterval(
              () -> {
                for (BlockSumoPlayer player : playerHelper.getPlayers()) {
                  if (
                      powerupsEnabled &&
                          player.hasPowerupsEnabled() &&
                          player.getLives() != 0 &&
                          player.getLifePowerups() != maxPowerups
                  ) {
                    player.getPlayer().toBukkit().getInventory().addItem(activeLootTable.generate());
                    player.addLifePowerup();
                    player.getPlayer().toBukkit().sendMessage(BlockSumoLang.receivedPowerup(player.getLifePowerups(), maxPowerups));
                  }
                }
              },
              20L * 60,
              20L * 30
          ),
          
          //>Center loot drops
          new ConfiguredFunction(config -> new TaskInterval(
              () -> {
                ActiveGame.getWorld().dropItem(
                    ConfigUtils.getLocationInActiveGame(config.getString("centerLootPosition")),
                    BlockSumoLoot.centerLootTable.generate()
                );
              },
              20L * 60,
              20L * 30
          ))
      );
  }
  
  @Override
  public GameState createLobbyGameState() {
    return super.createLobbyGameState()
        .addGameFunctions(
            new ConfiguredFunction(config -> new HeightZone(config.getInt("killHeight")) {
              @Override
              public void onInside(Player player) {
                player.teleport(
                    ActiveGame.getGameMap().getLobbySpawns()[0]
                        .toBukkitPosition(ActiveGame.getWorld())
                );
              }
            })
        );
  }
  
  @Override
  public List<GameState> createGameStates() {
    return List.of(
      new GameState("Base", true)
        .setOnEnable(() -> {
          ActivityRule.setEnabled(
              ActivityRule.TILE_BREAKING, true,
              ActivityRule.TILE_PLACING, true,
              ActivityRule.TILE_DROP, false,
              ActivityRule.DISABLE_TRAPDOORS, true,
              ActivityRule.AUTO_IGNITE_TNT, true,
              ActivityRule.ANY_DAMAGE, false
          );

          // Map GameData
          ConfigurationSection mapData = ActiveGame.getMapGameData();
          
          double buildRange = mapData.getDouble("buildRange");
          double buildMaxHeight = mapData.getDouble("buildMaxHeight");
          
          double[] buildOrigin = ConfigUtils.getDoubleArray(mapData.getString("buildOrigin"));

          BuildLimitBypass allowTntBypass = placeEvent -> placeEvent.getBlock().getType() == Material.TNT;

          getActiveGameState()
            .addGameFunctions(
              new BuildRange(buildOrigin, buildRange, allowTntBypass),
              new BuildHeight(buildMaxHeight, allowTntBypass),
              new TaskInterval(
                () -> {
                  for (BlockSumoPlayer player : playerHelper.getPlayers()) {
                    player.getPlayer().toBukkit().sendActionBar(player.getLivesPrefix());
                  }
                }, 20L, 20L
              ),
              new TeamEliminationEnding()
            );
        })
        .addGameFunctions(
          new MapProtection(),
          new BlockReplenishing(blockStack ->
            blockStack.getType().name().endsWith("_WOOL")
          )
        )
    );
  }
  
}
