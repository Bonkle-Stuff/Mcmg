package com.cak.mcmg.core.config;

import com.cak.mcmg.Main;

public class ConfigValidator {
  
  /**Prints a warning message if any of the specified arguments are null (paired in name and the variable)
   * @return true if any were null*/
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
