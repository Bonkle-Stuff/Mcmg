package com.cak.mcmg.core.config;


import com.cak.mcmg.Main;
import com.cak.mcmg.core.Debug;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * A representation of a config file that has direct getter functions for values
 * */
public class AttatchedConfig {
  
  protected static YamlConfiguration config;
  protected static File configFile;
  
  protected static void loadConfigFile(String path) {
    configFile = new File(
        Main.path + File.separator + path
    );
    Debug.log("Loading Config '%s' ['%s']".formatted(path, configFile.getAbsolutePath()));
    if (!configFile.exists())
      Main.plugin.saveResource(path, false);
    config = YamlConfiguration.loadConfiguration(configFile);
  }
  
}
