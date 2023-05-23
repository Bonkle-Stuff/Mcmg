package com.cak.mcsu.base.games;

import com.cak.mcsu.base.inventories.BlockSumoInventory;
import com.cak.mcsu.base.loottables.BlockSumoLoot;
import com.cak.mcsu.base.players.BlockSumoPlayer;
import com.cak.mcsu.core.McsuPlayer;
import com.cak.mcsu.base.texts.BlockSumoTexts;
import com.cak.mcsu.core.TimedEvent;
import com.cak.mcsu.core.eventhandler.ActivityRule;
import com.cak.mcsu.core.game.ActiveGame;
import com.cak.mcsu.core.game.Game;
import com.cak.mcsu.core.game.GameState;
import com.cak.mcsu.core.game.functions.*;
import com.cak.mcsu.core.game.helpers.BuildLimitBypass;
import com.cak.mcsu.core.scoreboard.PlayerScoreboard;
import com.cak.mcsu.core.util.BukkitRunnableLater;
import com.cak.mcsu.core.util.BukkitRunnableTimer;
import com.cak.mcsu.core.util.LootTable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockSumo {
  
  static Game game = new Game("blocksumo", "Block Sumo");
  
  static GamePlayerHelper<BlockSumoPlayer> playerHelper;
  static LootTable activeLootTable;
  
  static int maxPowerups = 5;
  
  static boolean powerupsEnabled = false;
  
  public static void register() {
    
    game.addPrefixProvider(player -> {
      BlockSumoPlayer blockSumoPlayer = playerHelper.getPlayer(player);
      return (blockSumoPlayer == null ? Component.text("") : blockSumoPlayer.getLivesTabString());
    });
    game.addGameState(new GameState("Base", true)
        .setOnEnable(() -> {
          ActivityRule.setEnabled(
              ActivityRule.TILE_BREAKING, true,
              ActivityRule.TILE_PLACING, true,
              ActivityRule.TILE_DROP, false,
              ActivityRule.DISABLE_TRAPDOORS, true,
              ActivityRule.AUTO_IGNITE_TNT, true
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
          
          game.getLobbyGameState()
              .addGameFunctions(
                  lobbyHeightZone
              );
          
          //Setup active state for players falling off
          HeightZone activeHeightZone = new HeightZone(killHeight);
          activeHeightZone.setOnInside(player -> player.damage(9999));
          
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
        })
        .addGameFunctions(
            new MapProtection(),
            new BlockReplenishing(blockStack -> blockStack.getType().name().endsWith("_WOOL"))
        )
    );
    
    game.setLobbyGameState(new GameState("Lobby")
    );
    game.setActiveGameState(new GameState("Active")
        .setOnEnable(() -> {
          playerHelper.getPlayers().forEach(blockSumoPlayer ->
              PlayerScoreboard.getScoreboard(blockSumoPlayer.getPlayer()).updateGamePrefix());
          
          powerupsEnabled = false;
          new TimedEvent("Powerups I", 60, true)
              .setOnEnd(() -> {
                activeLootTable = BlockSumoLoot.powerupLootTableI;
                powerupsEnabled = true;
              });
          
          new TimedEvent("Powerups II", 60 + 300, true)
              .setOnEnd(() -> {
                activeLootTable = BlockSumoLoot.powerupLootTableII;
              });
          
          new TimedEvent("Powerups III", 60 + 300 + 300, true)
              .setOnEnd(() -> {
                activeLootTable = BlockSumoLoot.powerupLootTableIII;
              });
          
        })
        .addGameFunctions(
            playerHelper = new GamePlayerHelper<>(BlockSumoPlayer::new),
            new GameInventoryHelper<>(BlockSumoInventory::new),
            new TaskInterval(() -> {
              for (BlockSumoPlayer player : playerHelper.getPlayers()) {
                if (powerupsEnabled && player.hasPowerupsEnabled() && player.getLives() != 0
                    && player.getLifePowerups() != maxPowerups) {
                  player.getPlayer().toBukkit().getInventory().addItem(
                      activeLootTable.generate()
                  );
                  player.incrementLifePowerups();
                  player.getPlayer().toBukkit().sendMessage(ChatColor.BLUE + "You have received a powerup! [" +
                      player.getLifePowerups() + "/" + maxPowerups + "]"
                  );
                }
              }
            }, 0L, 20L * 30),
            new EventListener() {
              @EventHandler
              public void onPlayerDeath(PlayerDeathEvent event) {
                Player player = event.getPlayer();
                BlockSumoPlayer bsPlayer = playerHelper.getPlayer(McsuPlayer.fromBukkit(event.getPlayer()));
                bsPlayer.removeLife();
                bsPlayer.resetLifePowerups();
                bsPlayer.setPowerupsEnabled(false);
                
                if (bsPlayer.getLives() == 0) return;
                ActiveGame.eliminatePlayer(player);
                
                AtomicInteger respawnTimeLeft = new AtomicInteger(5);
                
                new BukkitRunnableTimer((timer) -> {
                  
                  respawnTimeLeft.getAndDecrement();
                  if (respawnTimeLeft.get() == 0) {
                    
                    Arrays.stream(ActiveGame.getGameMap().getGameSpawns()).findFirst().orElse(null)
                        .teleportPlayerTo(player, ActiveGame.getWorld());
  
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(ChatColor.GREEN +""+ ChatColor.BOLD + "Respawned!");
                    bsPlayer.setPowerupsEnabled(true);
                    bsPlayer.setSpawnProtectionEnabled(true);
                    
                    new BukkitRunnableLater(() -> {
                      player.sendMessage(ChatColor.AQUA + "Spawn protection disabled!");
                      bsPlayer.setSpawnProtectionEnabled(false);
                    }, 20*5);
                    
                    timer.cancel();
                  } else {
                    player.sendTitlePart(
                        TitlePart.TITLE,
                        Component.text("Respawning in " + respawnTimeLeft + "!")
                    );
                    player.sendTitlePart(
                        TitlePart.SUBTITLE,
                        BlockSumoTexts.livesLeft(bsPlayer.getLives())
                    );
                  }
                  
                },20L, 20L);
                
              }
            }
        )
    );
    
  }
  
}
