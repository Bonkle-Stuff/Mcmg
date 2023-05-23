package com.cak.mcsu.core.game.functions;

import com.cak.mcsu.core.game.helpers.ZoneFunction;

public class HeightZone extends ZoneFunction {
  
  double yLevel;
  
  public HeightZone(double yLevel) {
    this.yLevel = yLevel;
  }
  
  @Override
  public boolean isInside(double x, double y, double z) {
    return y <= yLevel;
  }
}
