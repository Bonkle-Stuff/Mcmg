package com.cak.mcmg.core.game.functions;

import com.cak.mcmg.core.Team;
import com.cak.mcmg.core.events.GamePlayerEliminatedEvent;
import com.cak.mcmg.core.game.ActiveGame;
import com.cak.mcmg.core.game.GameFunction;
import org.bukkit.event.EventHandler;

import java.util.HashMap;

public class TeamEliminationEnding extends GameFunction {
  
  public TeamEliminationEnding() {
    
    setListener(new EventListener() {
      
      @EventHandler
      void onGamePlayerEliminatedEvent(GamePlayerEliminatedEvent event) {
        HashMap<Team, Boolean> aliveTeams = new HashMap<>();
        
        ActiveGame.getAlivePlayers().forEach(player -> aliveTeams.put(player.getTeam(), true));
        
        if (aliveTeams.keySet().size() == 1) {
          ActiveGame.endWithWinner(aliveTeams.keySet().iterator().next());
        }
      }
      
    });
    
  }
}
