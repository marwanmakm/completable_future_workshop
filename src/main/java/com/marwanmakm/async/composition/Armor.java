package com.marwanmakm.async.composition;

public class Armor {
  private boolean hasHelmet;
  private boolean hasGloves;
  private boolean hasShield;

  @Override
  public String toString() {
    return "Armor{"
        + "hasHelmet="
        + hasHelmet
        + ", hasGloves="
        + hasGloves
        + ", hasShield="
        + hasShield
        + '}';
  }

  public Armor() {}

  public Armor equipHelmet() {
    buildPart("helmet", 3);
    this.hasHelmet = true;
    return this;
  }

  public Armor equipGloves() {
    buildPart("gloves", 1);
    this.hasGloves = true;
    return this;
  }

  public Armor equipShield() {
    buildPart("shield", 5);
    this.hasShield = true;
    return this;
  }

  private void buildPart(String partName, Integer seconds) {
    System.out.println("Building part: " + partName + " at " + System.currentTimeMillis());
    try {
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException ignored) {
    }
    System.out.println("Finished part: " + partName + " at " + System.currentTimeMillis());
  }
}
