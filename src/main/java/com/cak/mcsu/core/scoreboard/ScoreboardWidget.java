package com.cak.mcsu.core.scoreboard;

import com.cak.mcsu.core.McsuPlayer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public interface ScoreboardWidget {
  
  /**
   * Called to a widget to register teams and set up any other variables
   *
   * @return score incremented by however many elements are displayed
   */
  int setup(McsuPlayer player, Scoreboard scoreboard, Objective objective, PlayerScoreboard.EntryGenerator entryGenerator, int score);
  
  /**
   * Called to change / update the contents of a player scoreboard
   *
   * @return score incremented by however many elements are displayed
   */
  int update(McsuPlayer player, Scoreboard scoreboard, Objective objective, int score);
  
}
