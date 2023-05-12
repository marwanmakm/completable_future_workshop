package com.marwanmakm.async.composition;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class Main {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    buildGeneralWithFullArmorV2();
  }

  private static void buildGeneral() throws ExecutionException, InterruptedException {
    // Creo mi soldado
    CompletionStage<Soldier> cf = CompletableFuture.supplyAsync(Soldier::new);
    // Le seteo el rango de General
    CompletionStage<Soldier> result =
        cf.thenApply(soldier -> soldier.setRange(Soldier.Range.GENERAL));
    System.out.println("Result: " + result.toCompletableFuture().get().toString());
  }

  private static void buildGeneralWithFullArmor() throws ExecutionException, InterruptedException {

    Soldier soldier = new Soldier();
    soldier.setRange(Soldier.Range.GENERAL);

    CompletionStage<Armor> cf = CompletableFuture.supplyAsync(Armor::new);
    cf.thenApply(Armor::equipGloves) // <- Toma el objeto anterior y lo modifica
        .thenApply(Armor::equipShield)
        .thenApply(Armor::equipHelmet)
        .thenAccept(
            soldier
                ::equipArmor) // <- Toma el objeto anterior y corre una funcion que no devuelve nada
        .thenRun(
            () ->
                System.out.println(
                    "El soldado está listo")) // <- No toma ningun objeto y solo ejecuta una funcion
        // cualquiera
        .thenRun(() -> System.out.println(soldier));
  }

  private static void buildGeneralWithFullArmorV2()
      throws ExecutionException, InterruptedException {

    CompletionStage<Soldier> cGeneral = CompletableFuture.supplyAsync(Soldier::new);
    cGeneral.thenApply(soldier -> soldier.setRange(Soldier.Range.GENERAL));

    CompletionStage<Armor> cArmor = CompletableFuture.supplyAsync(Armor::new);
    cArmor
        .thenApply(Armor::equipGloves) // <- Toma el objeto anterior y lo modifica
        .thenApply(Armor::equipShield)
        .thenApply(Armor::equipHelmet)
        .thenCompose(armor -> cGeneral.thenAccept(soldier -> soldier.equipArmor(armor)))
        .thenRun(
            () ->
                System.out.println(
                    "El soldado está listo")); // <- No toma ningun objeto y solo ejecuta una
    // funcion cualquiera
  }

  //// Api para Composición
  // ThenCompose(Function) <= Retorna un CF mismo valor de lo que ejecuta la función internamente
  // ThenApply(Function) <= Retorna un CF mismo valor de lo que ejecuta la función internamente
  // ThenAccept(Consumer) <= Retorna un CF mismo valor de lo que ejecuta la función internamente
  // ThenRun(Runnable) <= Retorna un CF mismo valor de lo que ejecuta la función internamente
}
