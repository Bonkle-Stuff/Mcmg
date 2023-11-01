package com.cak.mcmg.core.lang;

import com.cak.mcmg.core.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class WinnerLang {
  
  public static Component titleWinner(Team winner) {
    return Component.text("You Won!").color(winner.getTextColor());
  }
  
  public static Component subtitleWinner(Team winner) {
    return Component.text("good for u").color(winner.getTextColor());
  }
  
  public static Component titleLooser(Team winner) {
    return winner.getNameComponent().append(Component.text(" Wins!").color(TextColor.color(156, 156, 156)));
  }
  
  public static Component subtitleLooser(Team winner) {
    return Component.text("get good or something idk").color(TextColor.color(156, 156, 156));
  }
  
}
