package com.marwanmakm.async.composition;

public class Soldier {
  private Range range;
  private Armor armor;

  @Override
  public String toString() {
    return "Soldier{" +
        "range=" + range +
        ", armor=" + armor +
        '}';
  }

  public Soldier() {
    this.range = Range.MARINE;
  }

  public void equipArmor(Armor armor) {
    this.armor = armor;
    System.out.println("Se equipo correctamente la armadura de su soldado");
  }

  public Soldier setRange(Range range) {
    this.range = range;
    System.out.println("El nuevo rango es: " + this.range.getName());
    return this;
  }


  public enum Range {
    CAPTAIN("captain"),
    GENERAL("general"),
    MARINE("marine");

    private final String name;

    Range(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }
  }
}
