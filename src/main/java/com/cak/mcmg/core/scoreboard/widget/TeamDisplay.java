package com.cak.mcmg.core.scoreboard.widget;

import com.cak.mcmg.core.McsuPlayer;
import com.cak.mcmg.core.Team;
import com.cak.mcmg.core.scoreboard.PlayerScoreboard;
import com.cak.mcmg.core.scoreboard.ScoreboardWidget;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public class TeamDisplay implements ScoreboardWidget {
  
  static String teamPrefix = "teamdisplay-";//Avoid conflict with other widget team names
  String paddingEntry;
  
  HashMap<Team, ScoreboardItem> teamEntries = new HashMap<>();
  
  public int setup(McsuPlayer player, Scoreboard scoreboard, Objective objective, PlayerScoreboard.EntryGenerator entryGenerator, int score) {
    
    for (Team team : Team.teams) {
      ScoreboardItem item = new ScoreboardItem(
          scoreboard.registerNewTeam(teamPrefix + "-" + team.getId()),
          entryGenerator.nextScore()
      );
      teamEntries.put(team, item);
      
      item.team.addEntry(item.entry);
    }
    
    paddingEntry = entryGenerator.nextScore();
    scoreboard.registerNewTeam(teamPrefix + "padding").addEntry(paddingEntry);
    
    return score;
  }
  
  public int update(McsuPlayer player, Scoreboard scoreboard, Objective objective, int score) {
    
    for (Team team : Team.teams) {
      ScoreboardItem item = teamEntries.get(team);
      item.team.setPrefix((team.getPlayers().contains(player) ? ChatColor.GREEN + ">" : "") +
          team.getColoredName() + " : " + team.getPoints());
      score++;
      objective.getScore(item.entry).setScore(score);
    }
    
    score++;
    objective.getScore(paddingEntry).setScore(score);
    
    return score;
  }
  
  public record ScoreboardItem(org.bukkit.scoreboard.Team team, String entry) {
  }
}
