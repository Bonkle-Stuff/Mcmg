package com.cak.mcmg.core.config;

import com.cak.mcmg.Main;

public class ConfigValidator {
  
  public static boolean warnNullAny(String source, String suffix, Object... args) {
    boolean anyNull = false;
    
    for (int i = 0; i < args.length; i += 2) {
      if (args[i + 1] == null) {
        Main.warn("[" + source + "] " + args[i] + " is null " + suffix);
        anyNull = true;
      }
    }
    
    return anyNull;
    
  }
  
}
