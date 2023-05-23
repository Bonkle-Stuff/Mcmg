package com.cak.mcsu.core.commands;

import com.cak.mcsu.Main;
import com.cak.mcsu.core.map.GameMap;
import com.cak.mcsu.core.map.MapLoader;
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
          if (args.length < 2 || (!args[1].equals("dev") && !args[1].equals("map"))) {
            sender.sendMessage("Not specified map or dev world");
            return false;
          }
          
          if (args.length < 3) {
            sender.sendMessage("No world specified");
            return false;
          }
          
          boolean isDevWorld = args[1].equals("dev");
          
          String worldName;
          GameMap map = null;
          
          if (isDevWorld) {
            File devWorld = new File(Main.path + "\\DevWorlds\\" + args[2]);
            
            if (!devWorld.exists()) {
              sender.sendMessage(ChatColor.RED + "No such world '" + args[2] + "' " +
                  ChatColor.DARK_RED + "(World should be in folder DevWorlds)");
              return false;
            }
            
            worldName = args[2];
          } else {
            map = GameMap.getMap(args[2]);
            
            if (map == null) {
              sender.sendMessage(ChatColor.RED + "No map found for '" + args[2] + "', try /listmaps " +
                  ChatColor.DARK_RED + "(Filename should be '<game name>-" + args[2] + "')");
              return false;
            }
            
            worldName = map.getWorldId();
          }
          
          sender.sendMessage(ChatColor.AQUA + "Opening map editor for world '" + args[2] + "'");
          
          World world = MapLoader.loadWorld(worldName, isDevWorld);
          
          Player editor = ((Player) sender);
          
          editor.teleport(new Location(world, 0, 100, 0));
          
          new EditSession(map, world, editor, isDevWorld, worldName);
          break;
        case "new":
          if (args.length < 2) {
            sender.sendMessage("No map name specified");
            return false;
          }
          
          world = MapLoader.createWorld(args[1], true);
          sender.sendMessage(ChatColor.AQUA + "Created world!");
          
          editor = ((Player) sender);
          
          editor.teleport(new Location(world, 0, 100, 0));
          new EditSession(null, world, editor, true, args[1]);
          
          break;
        case "save":
          EditSession editSession = EditSession.getEditSession((Player) sender);
          
          if (editSession == null) {
            sender.sendMessage("You are not editing!");
            return false;
          }
          
          if (!editSession.getIsDevWorld()) {
            sender.sendMessage("You are not editing a dev world!");
            return false;
          }
          
          
          MapLoader.saveWorld(editSession.getWorldName(), true);
          sender.sendMessage(ChatColor.AQUA + "Saved world!");
          break;
        
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
