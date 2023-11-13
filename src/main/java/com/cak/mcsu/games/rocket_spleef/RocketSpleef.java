package com.cak.mcsu.games.rocket_spleef;

import com.cak.mcmg.core.config.ConfigUtils;
import com.cak.mcmg.core.eventhandler.ActivityRule;
import com.cak.mcmg.core.game.ActiveGame;
import com.cak.mcmg.core.game.Game;
import com.cak.mcmg.core.game.GameState;
import com.cak.mcmg.core.game.functions.ConfiguredFunction;
import com.cak.mcmg.core.game.functions.EventListener;
import com.cak.mcmg.core.game.functions.GameInventoryHelper;
import com.cak.mcmg.core.game.functions.TeamEliminationEnding;
import com.cak.mcmg.core.game.helpers.ZoneFunction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

public class RocketSpleef extends Game {
  
  public RocketSpleef() {
    super("rocket_spleef", "Rocket Spleef");
  }
  
  /**Gets whether the position is inside the game area
   * @param blocksZone Specifies whether to check the height as a block zone or kill zone*/
  protected boolean isOutOfBounds(boolean blocksZone, ConfigurationSection config,
                                  double x, double y, double z) {
    int yMin = config.getInt("killMinHeight");
    int yMax = blocksZone ? config.getInt("killMaxHeight") : config.getInt("gameMaxHeight");
  
    if (y < yMin || y > yMax) return y < yMin || y > yMax;
  
    int range = config.getInt("gameRange");
    double[] locationFromOrigin = ConfigUtils.getDoubleArray(
        config.getString("gameOrigin")
    );
    locationFromOrigin[0] += x;
    locationFromOrigin[1] += z;
  
    return Math.pow(locationFromOrigin[0], 2) +
        Math.pow(locationFromOrigin[1], 2) > Math.pow(range, 2);
  }
  
  GameInventoryHelper<RocketSpleefInventory> inventoryHandler;
  
  @Override
  public GameState createActiveGameState() {
    return super.createActiveGameState()
        .addGameFunctions(
            inventoryHandler = new GameInventoryHelper<>(RocketSpleefInventory::new),
            new TeamEliminationEnding(),
            new ConfiguredFunction(configurationSection -> new ZoneFunction() {
              @Override
              public boolean isInside(double x, double y, double z) {
                return isOutOfBounds(false, configurationSection, x, y, z);
              }
      
              @Override
              public void onInside(Player player) {
                player.damage(9999);
              }
            }),
            new ConfiguredFunction(configurationSection -> new EventListener() {
              @EventHandler
              public void onPlayerMoveEvent(ProjectileHitEvent event) {
    
                if (event.getEntity() instanceof Snowball) {
                  event.getHitBlock().setType(Material.AIR);
                } else if (event.getEntity() instanceof Firework) {
                  Location hitLocation;
                  if (event.getHitBlock() != null) {
                    hitLocation = event.getHitBlock().getLocation();
                  } else {
                    hitLocation = event.getEntity().getLocation();
                  }
      
                  boolean outOfBounds = isOutOfBounds(true, configurationSection,
                      hitLocation.x(), hitLocation.y(), hitLocation.z());
      
                  event.getEntity().getWorld().createExplosion(hitLocation, 2.2F, false, !outOfBounds);
                  event.getEntity().getWorld().createExplosion(hitLocation, 7f, false, false);
                }
              }
            })
        );
  }
  
  
  @Override
  public GameState createLobbyGameState() {
    return super.createLobbyGameState()
        .setOnEnable(() -> {
          ActivityRule.setEnabled(
              ActivityRule.LOCKED_INVENTORY, true,
              ActivityRule.ANY_DAMAGE, false,
              ActivityRule.PLACE_FIREWORKS, false
          );
        })
        .addGameFunctions(
            new ConfiguredFunction(configurationSection -> new ZoneFunction() {
              @Override
              public boolean isInside(double x, double y, double z) {
                return isOutOfBounds(false, configurationSection, x, y, z);
              }
  
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
