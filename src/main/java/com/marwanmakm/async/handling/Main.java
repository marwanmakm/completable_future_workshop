package com.marwanmakm.async.handling;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {

  /**
   * PENDING: The CompletableFuture is created but the task has not started yet.
   * COMPLETED: The task has completed successfully with a result.
   * EXCEPTIONALLY_COMPLETED: The task has completed exceptionally with an exception.
   * CANCELLED: The task has been cancelled before it could complete.
   * INTERRUPTED: The task has been interrupted while executing.
   */
  public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
    parallelExecution();
  }

  private static void completedCF() throws ExecutionException, InterruptedException, TimeoutException {
    //Pending
    CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> System.out.println("Me completé"));

    //Espera que lo de adentro se termine de ejecutar. Una vez termina, se marca como completada
    cf.get(); // Se completa
  }

  private static void completedCFWithTO() throws ExecutionException, InterruptedException, TimeoutException {
    //Pending
    CompletableFuture<Void> cf = CompletableFuture.runAsync(() ->
    {
      System.out.println("Empezó mi tarea a las " + System.currentTimeMillis());
      try {
        Thread.sleep(2000); // pause for 1 second
      } catch (InterruptedException ignored) {
      }
      System.out.println("Terminé mi tarea a las " + System.currentTimeMillis());
    });

    cf.get(1, TimeUnit.SECONDS);
  }

  private static void exceptionallyCompletedCF() throws ExecutionException, InterruptedException {
    //Pending
    //EXCEPTIONALLY_COMPLETED
    CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> 1 / 0);

    //cf.get(); //<- Esto rompe

    //Asi se arregla
    cf.exceptionally(ex -> {
      System.out.println("Ocurrió un error: " + ex);
      return 0;
    }).get();

    System.out.println("Result: " + cf.get());
  }

  private static void interruptedCF() throws ExecutionException, InterruptedException {
    CompletableFuture<String> cf = new CompletableFuture<>();

    // Start a new thread to complete the CompletableFuture after 1 second
    Thread thread = new Thread(() -> {
      try {
        Thread.sleep(1000);
        cf.complete("Hello, World!");
      } catch (InterruptedException e) {
        cf.completeExceptionally(e);
      }
    });

    thread.start();

    // Interrupt the thread after 500 milliseconds
    try {
      Thread.sleep(500);
      thread.interrupt();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Use exceptionally() to handle the InterruptedException
    cf.exceptionally(ex -> {
      if (ex instanceof InterruptedException) {
        System.out.println("Computation was interrupted!");
      } else {
        System.out.println("Exception occurred: " + ex);
      }
      return null;
    });

    System.out.println("Result: " + cf.get());
  }

  private static void cancelledCF() throws ExecutionException, InterruptedException {
    CompletableFuture<String> cf = new CompletableFuture<>();

    // Start a new thread to complete the CompletableFuture after 1 second
    Thread thread = new Thread(() -> {
      try {
        Thread.sleep(1000);
        cf.complete("Hello, World!");
      } catch (InterruptedException e) {
        cf.cancel(true); // Cancel the CompletableFuture
      }
    });

    thread.start();

    // Cancel the CompletableFuture after 500 milliseconds
    try {
      Thread.sleep(500);
      cf.cancel(true);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Use exceptionally() to handle the CancellationException
    cf.exceptionally(ex -> {
      if (ex instanceof CancellationException) {
        System.out.println("Computation was cancelled!");
      } else {
        System.out.println("Exception occurred: " + ex);
      }
      return null;
    });

    System.out.println("Result: " + cf.get());

  }

  private static void parallelExecution() throws ExecutionException, InterruptedException {
    CompletableFuture<Void> future1
        = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(3000);
        System.out.println("Hello, ID:" + Thread.currentThread().getId());
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return null;
    });
    CompletableFuture<String> future2
        = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(2000);
        System.out.println("Beautiful, ID:" + Thread.currentThread().getId());
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return null;
    });
    CompletableFuture<String> future3
        = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(6000);
        System.out.println("World, ID:" + Thread.currentThread().getId());
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return null;
    });
    // como para un modelo que necesita api calls para poder avanzar
    CompletableFuture<Void> combinedFuture
        = CompletableFuture.allOf(future1, future2, future3);

    // el que se "finalice primero", va a parar ahi y retornara ese resultado. Los otros tambien se ejecutan, pero el va a retomar con el primero q finalice.
    CompletableFuture<Object> combinedFutureAny
        = CompletableFuture.anyOf(future1, future2, future3);

    //combinedFuture.get();
    combinedFutureAny.get();

    System.out.println("Done future1: " + future1.isDone());
    System.out.println("Done future2: " + future2.isDone());
    System.out.println("Done future3: " + future3.isDone());

  }

}
