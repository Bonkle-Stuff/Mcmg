package com.cak.mcsu;

import com.cak.mcmg.core.game.Game;
import com.cak.mcsu.games.BlockSumo;
import com.cak.mcsu.games.KingOfTheHill;

public class AllGames {

  public static void register() {
    Game.registerGames(
        new BlockSumo(),
        new KingOfTheHill()
    );
  }

}
