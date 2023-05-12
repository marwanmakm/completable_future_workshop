package com.marwanmakm.async.creation;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Main {

  public static void main(String[] args) {
    //Simple Creation
    //La diferencia a nivel de código es que en Completable Future tengo acceso más métodos, pruebalo tu mismo ;)
    CompletionStage<String> cs = new CompletableFuture<>();
    CompletableFuture<String> cf = new CompletableFuture<>();

    //Precharged Process
    //En este caso, varía la ejecución de la tarea.
    // Una me podría servir para encadenar el resultado de una tarea con la siguiente
    // Mientras que en otra, solo ejecuto funciones sobre algun objeto
    CompletionStage<String> supplyCs = CompletableFuture.supplyAsync(() -> "Devuelvo este String cuando me completo");
    CompletionStage<Void> runCs = CompletableFuture.runAsync(() -> System.out.println("Solo ejecuto funciones sin devolver nada"));

  }
}
