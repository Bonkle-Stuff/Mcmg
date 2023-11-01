package com.cak.mcmg.core.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Text {
  
  public static TextColor darkGrey = TextColor.color(58, 58, 58);
  public static TextColor white = TextColor.color(230, 230, 230);
  public static TextColor red = TextColor.color(230, 58, 68);
  
  public static Component indent(int i) {
    return Component.text("   ".repeat(i));
  }
  
  public static Component raw(String string) {
    return Component.text(string);
  }
  
  public static Component raw(String string, TextColor color) {
    return Component.text(string).color(color);
  }
  
  
  public static Component bold(String string) {
    return Component.text(string).decorate(TextDecoration.BOLD);
  }
  
  public static Component bold(String string, TextColor color) {
    return Component.text(string).color(color).decorate(TextDecoration.BOLD);
  }
  
  public static Component formatted(String string, String... formatting) {
    return Component.text(String.format(string, (Object[]) formatting));
  }
  
  public static Component formatted(String string, TextColor color, String... formatting) {
    return Component.text(String.format(string, (Object[]) formatting)).color(color);
  }
  
}
