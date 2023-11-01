package com.cak.mcmg.core.commands;

import com.cak.mcmg.Main;
import com.cak.mcmg.core.eventhandler.ActivityRule;
import org.bukkit.ChatColor;

import java.util.List;

public class MiscCommands {
  
  public static void register() {
  
    Main.plugin.getCommand("activityrule").setExecutor((sender, command, label, args) -> {
  
      if (args.length < 1) {
        sender.sendMessage(ChatColor.RED + "No arguments specified");
        return false;
      }
      ActivityRule rule;
      try {
        rule = ActivityRule.valueOf(ActivityRule.class, args[0].toUpperCase());
      } catch (IllegalArgumentException exception) {
        sender.sendMessage(String.format(ChatColor.RED + "ActivityRule '%s' does not exist", args[0]));
        return false;
      }
      
      if (args.length < 2 || !List.of("true", "false").contains(args[1].toLowerCase())) {
        sender.sendMessage(String.format(ChatColor.RED + "You must specify if the rule should be true or false"));
        return false;
      }
      
      boolean enabled = "true".equalsIgnoreCase(args[1]);
      
      rule.setEnabled(enabled);
  
      sender.sendMessage(ChatColor.GREEN + "Set ActivityRule '" + rule.name() +
          "' to " + (enabled ? ChatColor.GREEN : ChatColor.RED) + args[1]);
      return true;
  
    });
  
    Main.plugin.getCommand("enableblocks").setExecutor((sender, command, label, args) -> {
    
      ActivityRule.setEnabled(
          ActivityRule.TILE_BREAKING, true,
          ActivityRule.TILE_PLACING, true
      );
      
      sender.sendMessage(ChatColor.GREEN + "Enabled block placement");
      return true;
    
    });
    
  }
  
  
  
}
