package com.cak.mcmg.core.eventhandler;

public enum ActivityRule {
  TILE_DROP(false),
  TILE_BREAKING(false),
  TILE_PLACING(false),
  DROP_ITEMS(false),
  PLAYER_MOVEMENT(true),
  LOCKED_INVENTORY(false),
  EXPLOSION_DAMAGE(false),
  FALL_DAMAGE(false),
  ANY_DAMAGE(true),
  HUNGER(false),
  SUFFOCATION(false),
  EXPLOSION_GRIEFING(false),
  AUTO_IGNITE_TNT(true),
  DISABLE_TRAPDOORS(true),
  PLACE_FIREWORKS(false),
  ;
  
  public static void clearAll() {
    for (ActivityRule rule : ActivityRule.values()) {
      rule.reset();
    }
  }
  
  /**
   * Sets a list of activity rules, should be called like:
   * <pre>{@code
   *     ActivityRule.setEnabled(
   *        ActivityRule.TILE_DROP, false,
   *        ActivityRule.EXPLOSION_DAMAGE, false,
   *        ActivityRule.EXPLOSION_GRIEFING, true,
   *        ActivityRule.TILE_BREAKING, false,
   *     )
   * }</pre>
   *
   * @param args ActivityRule followed by enabled value
   */
  public static void setEnabled(Object... args) {
    for (int i = 0; i < args.length; i += 2) {
      ((ActivityRule) args[i]).setEnabled((boolean) args[i + 1]);
    }
  }
  
  final private boolean enabledDefault;
  private boolean enabled;
  
  ActivityRule(boolean enabledDefault) {
    this.enabledDefault = enabledDefault;
    this.enabled = enabledDefault;
  }
  
  public void reset() {
    enabled = enabledDefault;
  }
  
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
  
  public boolean isEnabled() {
    return enabled;
  }
  
}
