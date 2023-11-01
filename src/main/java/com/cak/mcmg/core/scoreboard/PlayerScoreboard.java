package com.cak.mcmg.core.scoreboard;

import com.cak.mcmg.Main;
import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.scoreboard.widget.GameCountdown;
import com.cak.mcmg.core.scoreboard.widget.TeamDisplay;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import javax.annotation.Nullable;
import java.util.HashMap;

public class PlayerScoreboard {
  
  public static HashMap<McsuPlayer, PlayerScoreboard> playerScoreboards = new HashMap<>();
  
  //Scoreboard
  static PrefixProvider teamPrefixProvider = player -> Component.text(player.getTeam() == null ? ChatColor.GRAY + "[None] " :
      player.getTeam().getChatColor() + "[" + player.getTeam().getShortName() + "] ");
  @Nullable static PrefixProvider gamePrefixProvider;
  
  EntryGenerator entryGenerator = new EntryGenerator();
  ScoreboardWidget[] widgets;
  Objective objective;
  Scoreboard scoreboard;
  McsuPlayer player;
  
  public PlayerScoreboard(McsuPlayer player) {
    
    Player bukkitPlayer = player.toBukkit();
    
    widgets = new ScoreboardWidget[]{
        new TeamDisplay(), new GameCountdown()
    };
    
    scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    objective = scoreboard.registerNewObjective(
        bukkitPlayer.getUniqueId().toString(), Criteria.DUMMY,
        Component.text(Main.scoreboardName)
    );
    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    
    bukkitPlayer.setScoreboard(scoreboard);
    
    playerScoreboards.put(player, this);
  
    updateAllPrefixes();
    
    int score = 0;
    for (ScoreboardWidget widget : widgets) {
      score = widget.setup(player, scoreboard, objective, entryGenerator, score);
    }
    score = 0;
    for (ScoreboardWidget widget : widgets) {
      score = widget.update(player, scoreboard, objective, score);
      
    }
    
  }
  
  public static void updateAllPrefixes() {
    playerScoreboards.keySet().forEach(player -> playerScoreboards.get(player).updatePlayersForScoreboard());
  }
  
  public void updatePlayersForScoreboard() {
  
    playerScoreboards.keySet().forEach(player -> {
      Player bukkitPlayer = player.toBukkit();
      Team playerTeam = scoreboard.getTeam(bukkitPlayer.getUniqueId().toString());
      if (playerTeam == null) playerTeam = scoreboard.registerNewTeam(bukkitPlayer.getUniqueId().toString());
  
      playerTeam.setColor((player.getTeam() == null ? ChatColor.GRAY :
          player.getTeam().getChatColor()));
  
      updatePrefix(playerTeam, player);
      playerTeam.addPlayer(player.toBukkit());
      
    });
    
  }
  
  /**
   * ! Iterates by scoreboards not by players !
   */
  public static void updateAll() {
    playerScoreboards.forEach(((player, scoreboard) -> scoreboard.updateDisplay()));
  }
  
  public static void updateDisplay(McsuPlayer player) {
    playerScoreboards.get(player).updateDisplay();
  }
  
  public void updateDisplay() {
    int score = 0;
    for (ScoreboardWidget widget : widgets) {
      score = widget.update(player, scoreboard, objective, score);
    }
  }
  
  public static PlayerScoreboard getScoreboard(McsuPlayer player) {
    return playerScoreboards.get(player);
  }
  
  public static void setGamePrefixProvider(@Nullable PrefixProvider gamePrefixProvider) {
    PlayerScoreboard.gamePrefixProvider = gamePrefixProvider;
    PlayerScoreboard.updateAllPrefixes();
  }
  
  public static void updatePrefix(Team team, McsuPlayer player) {
    team.prefix(getPrefix(player));
    team.setColor((player.getTeam() == null ? ChatColor.GRAY :
        player.getTeam().getChatColor()));
  }
  
  private static Component getPrefix(McsuPlayer player) {
    return getGamePrefixProvider().get(player)
        .append(getTeamPrefixProvider().get(player));
  }
  
  /**
   * Generates unique ChatColor combinations for entries
   */
  public static class EntryGenerator {
    int i = 0;
    
    public String nextScore() {
      StringBuilder result = new StringBuilder();
      String hex = Integer.toHexString(i);
      for (int i = 0; i < hex.length(); i++) {
        result.append(ChatColor.values()[Integer.parseInt(hex.charAt(i) + "", 16)]);
      }
      i++;
      return result.toString();
    }
  }
  
  public static PrefixProvider getTeamPrefixProvider() {
    return teamPrefixProvider;
  }
  
  public static PrefixProvider getGamePrefixProvider() {
    return gamePrefixProvider == null ? p -> Component.text("") : gamePrefixProvider;
  }
  
}
