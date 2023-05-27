package com.cak.mcmg.core.config;


import com.cak.mcmg.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * A representation of a config file that has direct getter functions for values
 * */
public class FriendlyConfigFile {
  
  protected static YamlConfiguration config;
  protected static File configFile;
  
  protected static void loadConfigFile(String path) {
    configFile = new File(
        Main.path + "\\" + path
    );
    Main.plugin.saveResource(path, false);
    config = YamlConfiguration.loadConfiguration(configFile);
  }
  
}
