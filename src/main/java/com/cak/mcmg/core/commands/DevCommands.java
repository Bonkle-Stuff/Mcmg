package com.cak.mcmg.core.commands;

import com.cak.mcmg.Main;
import com.cak.mcmg.core.editor.EditSession;
import com.cak.mcmg.core.map.GameMap;
import com.cak.mcmg.core.map.MapLoader;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;

public class DevCommands {
  
  public static void register() {
    
    Main.plugin.getCommand("editor").setExecutor((sender, command, label, args) -> {
      
      if (args.length < 1) {
        sender.sendMessage("No option specified");
        return false;
      }
      
      switch (args[0]) {
        case "open":
          
          if (args.length < 2) {
            sender.sendMessage("No world specified");
            return false;
          }
          
          String worldName;
  
          GameMap map = GameMap.getMap(args[0]);
  
          if (map == null) {
            sender.sendMessage(ChatColor.RED + "No map found for '" + args[0] + "', try /listmaps " +
                ChatColor.DARK_RED + "(Filename should be <Map Name>)");
            return false;
          }
  
          worldName = map.getWorldId();
          
          sender.sendMessage(ChatColor.AQUA + "Opening map editor for world '" + args[0] + "'");
          
          World world = MapLoader.loadWorld(worldName);
          
          Player editor = ((Player) sender);
          
          editor.teleport(new Location(world, 0, 100, 0));
          
          new EditSession(map, world, worldName);
          break;
        default:
          return false;
      }
      
      return true;
    });
    
    Main.plugin.getCommand("showpoints").setExecutor((sender, command, label, args) -> {
      
      EditSession editSession = EditSession.getEditSession((Player) sender);
      
      if (editSession == null) {
        sender.sendMessage("You are not editing!");
        return false;
      }
      
      editSession.showParticles();
      
      return true;
    });
    
    Main.plugin.getCommand("hidepoints").setExecutor((sender, command, label, args) -> {
      
      EditSession editSession = EditSession.getEditSession((Player) sender);
      
      if (editSession == null) {
        sender.sendMessage("You are not editing!");
        return false;
      }
      
      editSession.hideParticles();
      
      return true;
    });
    
    Main.plugin.getCommand("listworlds").setExecutor((sender, command, label, args) -> {
      
      File folder = new File(Main.path + "\\DevWorlds");
      
      if (!folder.exists()) {
        sender.sendMessage(ChatColor.RED + "No directory /DevWorlds");
        return false;
      }
      
      sender.sendMessage("Worlds in /DevWorlds:");
      
      for (File file : folder.listFiles()) {
        sender.sendMessage("   " + ChatColor.GRAY + "Name: " + ChatColor.WHITE + file.getName());
      }
      return true;
    });
    
  }
  
}
