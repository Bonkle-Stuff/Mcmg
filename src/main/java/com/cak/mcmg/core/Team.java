package com.cak.mcmg.core;

import com.cak.mcmg.Main;
import com.cak.mcmg.core.config.ConfigValidator;
import com.cak.mcmg.core.config.FriendlyConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Team extends FriendlyConfigFile {
  
  public static @Nullable Team get(String teamId) {
    return teams.stream().filter(team -> team.getId().equals(teamId)).findFirst().orElse(null);
  }
  
  static List<HashMap<String, Object>> teamsList;
  
  @SuppressWarnings("unchecked")
  public static void loadConfig() {
    loadConfigFile("teams.yml");
    
    teamsList = (List<HashMap<String, Object>>) config.getList("teams");
    
    if (teamsList == null) {
      Main.warn("[Team.loadConfig] Null list 'teams', delete teams.yml to restore to defaults");
      return;
    }
    
    for (HashMap<String, Object> teamData : teamsList) {
      
      String teamId = (String) teamData.get("id");
      String teamName = (String) teamData.get("name");
      ChatColor chatColor = ChatColor.getByChar(((String) teamData.get("chatColor")).charAt(1));
      int points = (int) teamData.get("points");
      
      //>Property Validation
      if (ConfigValidator.warnNullAny("Team.loadConfig", "for Team " + teamName,
          "Id", teamId,
          "Name", teamName,
          "ChatColor", chatColor,
          "Points", points
      )) {
        Main.warn(">> [Team.loadConfig] It is recommended that you delete teams.yml to restore to defaults");
        return;
      }
      
      Team registeredTeam = new Team(teamId, teamName, chatColor, points);
      
      Debug.log("> Registered '" + registeredTeam.name + "'");
      
      List<String> playerUUIDs = (List<String>) teamData.get("playerUUIDs");
  
      //>PlayerUUIDs Validation
      if (playerUUIDs == null) {
        Main.warn(">> [Team.loadConfig] Null property 'playerUUIDs' for team '" + teamData.get("teamId") + "', delete teams.yml to restore to defaults");
        continue;
      }
      
      for (String playerUUID : playerUUIDs) {
        
        Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
        
        if (player != null) {
          
          registeredTeam.addPlayer(McsuPlayer.fromBukkit(player));
          Debug.log(">>> Added player to team: " + playerUUID);
          
        }
        
      }
      
    }
    
  }
  
  public static void saveToConfig() {
    config.set("teams", teamsList);
    try {
      config.save(new File(
          Main.path + "\\teams.yml"
      ));
    } catch (IOException e) {
      throw new RuntimeException(e);
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
    this.points = points;
    teams.add(this);
  }
  
  public void saveData() {
    
    for (HashMap<String, Object> item : teamsList) {
      if (item.get("name") == this.name) {
        item.put("points", points);
        
        List<String> playerList = new ArrayList<>();
        players.forEach(player -> playerList.add(
            player.toBukkit().getUniqueId().toString()
        ));
        item.put("playerUUIDs", playerList);
        return;
      }
    }
    
  }
  
  public void addPlayer(McsuPlayer player) {
    player.setTeam(this);
    players.add(player);
    saveData();
  }
  
  public void removePlayer(McsuPlayer player) {
    player.setTeam(null);
    players.remove(player);
    saveData();
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
  
  public Material getWool() {
    return Material.getMaterial(chatColor.name() + "_WOOL");
  }
  
}
