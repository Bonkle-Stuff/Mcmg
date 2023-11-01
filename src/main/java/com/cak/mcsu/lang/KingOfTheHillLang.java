package com.cak.mcsu.lang;

import com.cak.mcmg.core.util.Text;
import com.cak.mcsu.players.KingOfTheHillPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class KingOfTheHillLang {
  
  static int[] winGradientStart = new int[] {27, 126, 130};
  static int[] winGradientEnd = new int[] {60, 128, 27};
  
  public static TextColor getWinGradient(float percentage) {
    return TextColor.color(
        (int) Math.ceil((winGradientStart[0] * percentage) + (winGradientEnd[0] * (1-percentage))),
        (int) Math.ceil((winGradientStart[1] * percentage) + (winGradientEnd[1] * (1-percentage))),
        (int) Math.ceil((winGradientStart[2] * percentage) + (winGradientEnd[2] * (1-percentage)))
    );
  }
  
  public static Component winProgressShorthand(float percentage) {
    TextColor color = getWinGradient(percentage);
    
    return Text.formatted("[%s%%]", color,
        String.valueOf((int) Math.ceil(percentage * 100))
    );
  }
  
  public static Component playerOnHill(KingOfTheHillPlayer player) {
    float winPercentage = player.getWinPercentage();
    return winProgressShorthand(winPercentage)
        .append(Text.formatted(" %s is on the hill! ", player.getPlayer().toBukkit().getName()))
        .append(winProgressShorthand(winPercentage));
  }
}
