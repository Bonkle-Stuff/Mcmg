package com.cak.mcsu.core.commands;

import com.cak.mcsu.Main;
import com.cak.mcsu.core.McsuPlayer;
import com.cak.mcsu.core.Team;
import com.cak.mcsu.core.game.ActiveGame;
import com.cak.mcsu.core.game.Game;
import com.cak.mcsu.core.map.GameMap;
import com.cak.mcsu.core.scoreboard.PlayerScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GameCommands {
  
  public static void register() {
    
    Main.plugin.getCommand("startgame").setExecutor((sender, command, label, args) -> {
      
      for (McsuPlayer player : McsuPlayer.players) {
        if (player.getTeam() == null) {
          sender.sendMessage(ChatColor.RED + "Cannot start game as player '" +
              player.toBukkit().getName() + "' does not have a team");
          return true;
        }
      }
      
      ActiveGame.playGame(Game.registeredGames.get(0));
      return true;
    });
    
    
    Main.plugin.getCommand("listgames").setExecutor((sender, command, label, args) -> {
      sender.sendMessage("Registered games:");
      for (Game game : Game.registeredGames) {
        sender.sendMessage("   " + ChatColor.GRAY + "Name: " + ChatColor.WHITE + game.getName() +
            ChatColor.GRAY + " | Id: " + ChatColor.WHITE + game.getId()
        );
      }
      return true;
    });
    
    Main.plugin.getCommand("listmaps").setExecutor((sender, command, label, args) -> {
      sender.sendMessage("Registered maps:");
      for (GameMap game : GameMap.maps) {
        sender.sendMessage("   " + ChatColor.GRAY + "Name: " + ChatColor.WHITE + game.getMapName() +
            ChatColor.GRAY + " | MapId: " + ChatColor.WHITE + game.getMapId() +
            ChatColor.GRAY + " | GameId: " + game.getGameId()
        );
      }
      return true;
    });
    
    Main.plugin.getCommand("listteams").setExecutor((sender, command, label, args) -> {
      sender.sendMessage("Registered teams:");
      for (Team team : Team.teams) {
        sender.sendMessage("   " + ChatColor.GRAY + "Name: " + ChatColor.WHITE + team.getColoredName() +
            ChatColor.GRAY + " | Id: " + ChatColor.WHITE + team.getId() +
            ChatColor.GRAY + " | Player Count: " + team.getPlayers().size()
        );
      }
      return true;
    });
    
    
    Main.plugin.getCommand("team").setExecutor((sender, command, label, args) -> {
      
      if (args.length < 1) {
        sender.sendMessage(ChatColor.RED + "No option specified either set / points");
        return false;
      }
      
      switch (args[0]) {
        case "set":
          if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "No player specified");
            return false;
          } else if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "No team id specified");
            return false;
          }
          
          Player player = Bukkit.getPlayer(args[1]);
          if (player == null) {
            sender.sendMessage(ChatColor.RED + "Could not find player '" + args[1] + "'");
            return false;
          }
          
          Team team = Team.get(args[2]);
          if (team == null) {
            sender.sendMessage(ChatColor.RED + "Could not find team '" + args[2] + "' try " + ChatColor.BOLD + "/listteams");
            return false;
          }
          
          McsuPlayer mcsuPlayer = McsuPlayer.fromBukkit(player);
          
          Team oldTeam = mcsuPlayer.getTeam();
          if (oldTeam != null) {
            oldTeam.removePlayer(mcsuPlayer);
          }
          
          team.addPlayer(McsuPlayer.fromBukkit(player));
          sender.sendMessage(ChatColor.GREEN + "Added " + player.getName() + " to team " + team.getColoredName());
          
          if (oldTeam != null) {
            sender.sendMessage(ChatColor.AQUA + " > Removed " + player.getName() + " from team " + oldTeam.getColoredName());
          }
          
          PlayerScoreboard.getScoreboard(mcsuPlayer).updateTeamPrefix();
          
          Team.saveToConfig();
          
          break;
        case "points":
          break;
      }
      return true;
    });
    
  }
  
}
