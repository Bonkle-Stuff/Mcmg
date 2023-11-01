package com.cak.mcsu.games;

import com.cak.mcmg.Main;
import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.config.ConfigUtils;
import com.cak.mcmg.core.eventhandler.ActivityRule;
import com.cak.mcmg.core.game.ActiveGame;
import com.cak.mcmg.core.game.Game;
import com.cak.mcmg.core.game.GameState;
import com.cak.mcmg.core.game.functions.*;
import com.cak.mcmg.core.game.helpers.ZoneFunction;
import com.cak.mcmg.core.scoreboard.PlayerScoreboard;
import com.cak.mcmg.core.scoreboard.PrefixProvider;
import com.cak.mcmg.core.util.Text;
import com.cak.mcsu.inventories.KingOfTheHillInventory;
import com.cak.mcsu.lang.KingOfTheHillLang;
import com.cak.mcsu.players.KingOfTheHillPlayer;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class KingOfTheHill extends Game {
  
  public KingOfTheHill() {
    super("koth", "King Of The Hill");
  }
  
  static final float winPercentagePerTick = 1f/1200;
  public static int zoneRadiusSquared = (int) Math.pow(5, 2);
  
  static GamePlayerHelper<KingOfTheHillPlayer> playerHelper;
  static GameInventoryHelper<KingOfTheHillInventory> inventoryHelper;
  
  static net.kyori.adventure.bossbar.BossBar bossbar = net.kyori.adventure.bossbar.BossBar.bossBar(
      Text.raw(""), 0f,
      net.kyori.adventure.bossbar.BossBar.Color.BLUE, net.kyori.adventure.bossbar.BossBar.Overlay.PROGRESS
  );
  
  static ZoneFunction hillZoneFunction;
  
  /*Thread based malarkey*/
  public static ZoneFunction getHillZoneFunction() {
    return hillZoneFunction;
  }
  
  static BukkitRunnable hillZoneTicker;
  
  @Override
  public PrefixProvider createPrefixProvider() {
    return player -> {
      KingOfTheHillPlayer kingOfTheHillPlayer = playerHelper.getPlayer(player);
      return (
          kingOfTheHillPlayer == null ? Component.text("")
              : KingOfTheHillLang.winProgressShorthand(kingOfTheHillPlayer.getWinPercentage())
      );
    };
  }
  
  @Override
  public GameState createActiveGameState() {
    return super.createActiveGameState()
        .setOnEnable(() -> {
          KingOfTheHillInventory.stickLevel = 2;
          bossbar.name(Text.raw(""));
          bossbar.progress(0);
          ActiveGame.getWorld().getPlayers().forEach(gamePlayer -> gamePlayer.showBossBar(bossbar));
          hillZoneTicker = new BukkitRunnable() {
            @Override
            public void run() {
              List<Player> playersInZone = getHillZoneFunction().getPlayersInside();
    
              if (playersInZone.size() == 0) {
                bossbar.color(BossBar.Color.PURPLE);
                bossbar.name(Text.raw(""));
                bossbar.progress(0);
              } else if (playersInZone.size() == 1) {
                bossbar.color(BossBar.Color.BLUE);
                Player player = playersInZone.get(0);
                McsuPlayer mcsuPlayer = McsuPlayer.fromBukkit(player);
                bossbar.name(KingOfTheHillLang.playerOnHill(playerHelper.getPlayer(mcsuPlayer)));
      
                KingOfTheHillPlayer kothPlayer = playerHelper.getPlayer(mcsuPlayer);
      
                if (kothPlayer.getWinPercentage() > 1f) {
                  ActiveGame.endWithWinner(mcsuPlayer.getTeam());
                } else {
                  kothPlayer.addWinPercentage(winPercentagePerTick);
                }
      
                bossbar.progress(Math.min(kothPlayer.getWinPercentage(), 1));
                PlayerScoreboard.playerScoreboards.get(mcsuPlayer).updatePlayersForScoreboard();
              } else {
                bossbar.color(BossBar.Color.WHITE);
                bossbar.name(Text.bold("Point contested!", NamedTextColor.GRAY));
              }
            }
          };
          hillZoneTicker.runTaskTimer(Main.plugin, 1, 1);
        })
        .addGameFunctions(
            //>Generic functions
            playerHelper = new GamePlayerHelper<>(KingOfTheHillPlayer::new),
            inventoryHelper = new GameInventoryHelper<>(KingOfTheHillInventory::new),
    
            new ConfiguredFunction(configurationSection -> (hillZoneFunction = new RadiusZone(5,
                ConfigUtils.getLocationInActiveGame(configurationSection.getString("centerHillPosition"))))),
            
            new Respawns<>(playerHelper) {
              @Override
              protected void onPlayerRespawn(KingOfTheHillPlayer player) {
                inventoryHelper.getPlayer(player.getPlayer()).apply();
              }
            },
            new ConfiguredFunction(config -> new HeightZone(config.getInt("killHeight")) {
              @Override
              public void onInside(Player player) {
                player.damage(9999);
              }
            })
        )
        .setOnDisable(() -> {
          Main.info("disposen");
          hillZoneTicker.cancel();
          Main.info(String.valueOf(hillZoneTicker.isCancelled()));
          ActiveGame.getWorld().getPlayers().forEach(gamePlayer -> gamePlayer.hideBossBar(bossbar));
        });
  }
  
  @Override
  public GameState createLobbyGameState() {
    return super.createLobbyGameState()
        .setOnEnable(() -> {
          ActivityRule.setEnabled(
              ActivityRule.ANY_DAMAGE, false
          );
        })
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
  
}
