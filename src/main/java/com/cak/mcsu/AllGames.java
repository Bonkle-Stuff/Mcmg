package com.cak.mcsu;

import com.cak.mcsu.games.BlockSumo;
import com.cak.mcmg.core.game.Game;

public class AllGames {

  public static void register() {
  
     Game.registerGames(
      new BlockSumo()
    );

  }

}
