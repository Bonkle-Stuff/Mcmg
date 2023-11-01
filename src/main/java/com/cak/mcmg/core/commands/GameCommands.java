package com.cak.mcmg.core.commands;

import com.cak.mcmg.Main;
import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.Team;
import com.cak.mcmg.core.game.ActiveGame;
import com.cak.mcmg.core.game.Game;
import com.cak.mcmg.core.map.GameMap;
import com.cak.mcmg.core.scoreboard.PlayerScoreboard;
import com.cak.mcmg.core.util.Text;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class GameCommands {
  
  public static void register() {
    
    Main.plugin.getCommand("startgame").setExecutor((sender, command, label, args) -> {
  
      if (args.length != 1) {
        return false;
      }
      
      for (McsuPlayer player : McsuPlayer.players) {
        if (player.getTeam() == null) {
          sender.sendMessage(ChatColor.RED + "Cannot start game as player '" +
              player.toBukkit().getName() + "' does not have a team");
          return true;
        }
      }
  
      Game game = Game.registeredGames.stream().filter(candidate ->
          Objects.equals(candidate.getId(), args[0])).findFirst().orElse(null);
      
      if (game == null) {
        sender.sendMessage(Text.formatted("Could not find game '%s'", Text.red, args[0]));
        return false;
      }
      
      ActiveGame.playGame(game);
      return true;
    });
    
    
    Main.plugin.getCommand("listgames").setExecutor((sender, command, label, args) -> {
      sender.sendMessage("Registered games:");
      for (Game game : Game.registeredGames) {
        Component spacing = Text.raw(" | ").color(Text.darkGrey);
        sender.sendMessage(Text.raw("   ")
            .append(spacing).append(Text.raw("Name: ").color(Text.darkGrey).append(Text.raw(game.getName())))
            .append(spacing).append(Text.raw("Id: ").color(Text.darkGrey).append(Text.raw(game.getId())))
        );
      }
      return true;
    });
    
    Main.plugin.getCommand("listmaps").setExecutor((sender, command, label, args) -> {
      sender.sendMessage("Registered maps:");
      for (GameMap map : GameMap.maps) {
        Component spacing = Text.raw(" | ").color(Text.darkGrey);
        sender.sendMessage(
            Text.indent(1).append(Text.raw("Name: ", Text.darkGrey).append(Text.raw(map.getMapName(), Text.white)))
            .append(spacing).append(Text.raw("MapId: ", Text.darkGrey).append(Text.raw(map.getMapId(), Text.white)))
            .append(spacing).append(Text.raw("GameId: ", Text.darkGrey).append(Text.raw(map.getGameId(), Text.white)))
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
        sender.sendMessage(ChatColor.RED + "No option specified either set or points");
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
  
          PlayerScoreboard.updateAllPrefixes();
          
          Team.saveToConfig();
          
          break;
        case "points":
          break;
        default:
          return false;
      }
      return true;
    });
    
  }
  
}
