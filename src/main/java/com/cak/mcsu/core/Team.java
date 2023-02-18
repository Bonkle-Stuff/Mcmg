package com.cak.mcsu.core;

import com.cak.mcsu.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Team {

    public static @Nullable Team get(String teamId) {
        return teams.stream().filter(team -> team.getId().equals(teamId)).findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    public static void loadConfig() {

        Debug.log("[Team.loadConfig] Loading: teams.yml");

        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(
                Main.path + "\\teams.yml"
        ));

        List<HashMap<String, Object>> teamsList = (List<HashMap<String, Object>>) config.getList("teams");

        if (teamsList == null) {
            Main.warn("[Team.loadConfig] Null list 'teams', delete teams.yml to restore to defaults");
            return;
        }

        for (HashMap<String, Object> teamData : teamsList) {

            String teamId = (String) teamData.get("id");
            String teamName = (String) teamData.get("name");
            ChatColor chatColor = ChatColor.getByChar(((String) teamData.get("chatColour")).charAt(1));
            int points = (int) teamData.get("points");

            ConfigLoader.warnNullAny("Team.loadConfig", "for Team " + teamName,
                    "Id", teamId,
                    "Name", teamName,
                    "ChatColor", chatColor,
                    "Points", points
            );

            Team registeredTeam = new Team(teamId, teamName, chatColor, points);

            Debug.log("> Registered '" + registeredTeam.name + "'");

            List<String> playerUUIDs = (List<String>) teamData.get("playerUUIDs");

            if (playerUUIDs == null) {
                Main.warn(">> [Team.loadConfig] Null list 'playerUUIDs' for team '" + teamData.get("teamId") + "', delete teams.yml to restore to defaults");
                return;
            }

            for (String playerUUID : playerUUIDs) {

                Player player = Bukkit.getPlayer(playerUUID);

                if (player!= null) {

                    registeredTeam.addPlayer(McsuPlayer.fromBukkit(player));
                    Debug.log(">>> Added player to team: " + playerUUID);

                }

            }

        }

    }

    public static ArrayList<Team> teams = new ArrayList<>();

    ArrayList<McsuPlayer> players = new ArrayList<>();
    final String id;
    final ChatColor chatColor;

    final String name;
    int points;

    public Team(String id, String name, ChatColor chatColor, int points) {
        this.id = id;
        this.name = name;
        this.chatColor = chatColor;
        this.points=points;
        teams.add(this);
    }

    public void addPlayer(McsuPlayer player) {
        player.setTeam(this);
        players.add(player);
    }

    public void removePlayer(McsuPlayer player) {
        player.setTeam(null);
        players.remove(player);
    }

    public String getId() {
        return id;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public String getName() {
        return name;
    }

    public String getColoredName() {
        return chatColor + name;
    }

    public ArrayList<McsuPlayer> getPlayers() {
        return players;
    }

    public int getPoints() {
        return points;
    }
}
