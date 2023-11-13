package com.cak.mcsu;

import com.cak.mcmg.core.game.Game;
import com.cak.mcsu.games.blocksumo.BlockSumo;
import com.cak.mcsu.games.koth.KingOfTheHill;
import com.cak.mcsu.games.rocket_spleef.RocketSpleef;

public class AllGames {

  public static void register() {
    Game.registerGames(
        new BlockSumo(),
        new KingOfTheHill(),
        new RocketSpleef()
    );
  }

}
