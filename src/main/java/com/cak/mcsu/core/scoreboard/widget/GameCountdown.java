package com.cak.mcsu.core.scoreboard.widget;

import com.cak.mcsu.core.McsuPlayer;
import com.cak.mcsu.core.game.ActiveGame;
import com.cak.mcsu.core.scoreboard.PlayerScoreboard;
import com.cak.mcsu.core.scoreboard.ScoreboardWidget;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class GameCountdown implements ScoreboardWidget {

    static String teamPrefix = "gameCountdown-";//Avoid conflict with other widget team names
    //Entries for changing score number
    String paddingEntry;
    String scoreboardEntry;
    //Teams to change text content
    Team scoreboardTeam;

    public int setup(McsuPlayer player, Scoreboard scoreboard, Objective objective, PlayerScoreboard.EntryGenerator entryGenerator, int score) {

        scoreboardEntry = entryGenerator.nextScore();
        scoreboardTeam = scoreboard.registerNewTeam(teamPrefix + "scoreboard");
        scoreboardTeam.addEntry(scoreboardEntry);

        paddingEntry = entryGenerator.nextScore();
        scoreboard.registerNewTeam(teamPrefix + "padding").addEntry(paddingEntry);

        return score;
    }

    public int update(McsuPlayer player, Scoreboard scoreboard, Objective objective, int score) {

        String countDown = ActiveGame.getCurrentCountDown();
        if (countDown == null) {
            objective.getScore(scoreboardEntry).resetScore();
            objective.getScore(paddingEntry).resetScore();
        } else {
            score++;objective.getScore(scoreboardEntry).setScore(score);
            score++;objective.getScore(paddingEntry).setScore(score);
            scoreboardTeam.setPrefix(countDown);
        }

        return score;
    }
}
