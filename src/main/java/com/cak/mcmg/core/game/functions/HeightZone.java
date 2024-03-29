package com.cak.mcmg.core.game.functions;

import com.cak.mcmg.core.game.helpers.ZoneFunction;

public class HeightZone extends ZoneFunction {
  
  double yLevel;
  
  public HeightZone(double yLevel) {
    super();
    this.yLevel = yLevel;
  }
  
  @Override
  public boolean isInside(double x, double y, double z) {
    return y <= yLevel;
  }
}
