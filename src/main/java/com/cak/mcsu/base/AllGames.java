package com.cak.mcsu;

import com.cak.mcsu.core.game.Game;

public class AllGames {

  public static void register() {
  
     Game.registerGames(
      new BlockSumo()
    );

  }

}
