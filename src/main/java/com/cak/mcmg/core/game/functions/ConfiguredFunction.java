package com.cak.mcmg.core.game.functions;

import com.cak.mcmg.core.game.ActiveGame;
import com.cak.mcmg.core.game.GameFunction;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Function;

public class ConfiguredFunction extends GameFunction {
  
  GameFunction configuredGameFunction;
  Function<ConfigurationSection, GameFunction> functionSupplier;
  
  public ConfiguredFunction(Function<ConfigurationSection, GameFunction> functionSupplier) {
    this.functionSupplier = functionSupplier;
  }
  
  @Override
  public void onEnable() {
    super.onEnable();
    configuredGameFunction = functionSupplier.apply(ActiveGame.getMapGameData());
    
    configuredGameFunction.setEnabled(true);
  }
  
  @Override
  public void onDisable() {
    configuredGameFunction.setEnabled(false);
  }
}
