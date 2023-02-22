package com.cak.mcsu.core.scoreboard;

import com.cak.mcsu.Main;
import com.cak.mcsu.core.McsuPlayer;
import com.cak.mcsu.core.scoreboard.widget.GameCountdown;
import com.cak.mcsu.core.scoreboard.widget.TeamDisplay;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.function.Supplier;

public class PlayerScoreboard {

    static HashMap<McsuPlayer, PlayerScoreboard> playerScoreboards = new HashMap<>();

    EntryGenerator entryGenerator = new EntryGenerator();
    ScoreboardWidget[] widgets;
    Objective objective;
    Scoreboard scoreboard;
    McsuPlayer player;
    Team playerTeam;

    //Result is cached for performance (maybe?)
    static Supplier<String> teamPrefixProvider; String teamPrefixLast = "";
    @Nullable static PrefixProvider gamePrefixProvider; Component gamePrefixLast = Component.text("");

    public PlayerScoreboard(Player bukkitPlayer) {

        widgets = new ScoreboardWidget[] {
                new TeamDisplay(), new GameCountdown()
        };

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective(
                bukkitPlayer.getUniqueId().toString(), Criteria.DUMMY,
                Component.text(Main.scoreboardName)
        );
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        playerTeam = scoreboard.registerNewTeam(bukkitPlayer.getUniqueId().toString());
        player = McsuPlayer.fromBukkit(bukkitPlayer);

        playerTeam.addPlayer(bukkitPlayer);

        teamPrefixProvider = () -> player.getTeam() == null ? "[None] " :
                player.getTeam().getChatColor() + "[" + player.getTeam().getId().toUpperCase() + "] ";
        updateTeamPrefix();

        playerTeam.setColor((player.getTeam() == null ? ChatColor.GRAY :
                player.getTeam().getChatColor()));

        bukkitPlayer.setScoreboard(scoreboard);

        playerScoreboards.put(player, this);

        int score = 0;
        for (ScoreboardWidget widget : widgets) {
            score = widget.setup(player, scoreboard, objective, entryGenerator, score);
        }
        score = 0;
        for (ScoreboardWidget widget : widgets) {
            score = widget.update(player, scoreboard, objective, score);

        }

    }

    /**! Iterates by scoreboards not by players !*/
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
        playerScoreboards.values().forEach(PlayerScoreboard::updateGamePrefix);
    }

    public void updateTeamPrefix() {
        teamPrefixLast = teamPrefixProvider.get();
        playerTeam.prefix(getPrefix());
        playerTeam.setColor((player.getTeam() == null ? ChatColor.GRAY :
                player.getTeam().getChatColor()));
    }

    public void updateGamePrefix() {
        gamePrefixLast = gamePrefixProvider == null ? Component.text("") : gamePrefixProvider.get(player);
        playerTeam.prefix(getPrefix());
    }

    private Component getPrefix() {
        return gamePrefixLast.append(Component.text(teamPrefixLast));
    }

    /**Generates unique ChatColor combinations for entries*/
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
}
