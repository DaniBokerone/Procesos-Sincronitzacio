package com.project;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        // Sample data
        List<Integer> numeros = Arrays.asList(74, 61, 134, 49, 24);

        // Sincronitzar tasques
        CyclicBarrier barrier = new CyclicBarrier(3, () -> System.out.println("Todos los cálculos completados.\n"));

        // ExecutorService para gestionar los hilos
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Suma
        executor.submit(() -> {
            int suma = numeros.stream().mapToInt(Integer::intValue).sum();
            System.out.println("Suma: " + suma);
            awaitBarrier(barrier);
        });

        // Mitjana
        executor.submit(() -> {
            double mitjana = numeros.stream().mapToInt(Integer::intValue).average().orElse(0);
            System.out.println("Mitjana: " + mitjana);
            awaitBarrier(barrier);
        });

        // Desviacio estandard
        executor.submit(() -> {
            double mean = numeros.stream().mapToInt(Integer::intValue).average().orElse(0);
            double variance = numeros.stream().mapToDouble(n -> Math.pow(n - mean, 2)).average().orElse(0);
            double stdDev = Math.sqrt(variance);
            System.out.println("Desviacio estandar: " + stdDev);
            awaitBarrier(barrier);
        });

        // Cerrar el executor
        executor.shutdown();
    }

    // Método auxiliar para esperar en la barrera y manejar excepciones
    private static void awaitBarrier(CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
            System.out.println("Error en la barrera: " + e.getMessage());
        }
   
    }
}