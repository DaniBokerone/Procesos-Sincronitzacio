package com.project;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        
        List<String> partialResults = new ArrayList<>();
        
        // CyclicBarrier per a 3 fils, amb una acció final per combinar els resultats
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            String combinedResult = String.join(" ", partialResults);
            System.out.println("Resultat final: " + combinedResult);
        });

        // ExecutorService per gestionar les tasques en paral·lel
        ExecutorService executor = Executors.newFixedThreadPool(3);

        Runnable microservice1 = () -> {
            try {
                System.out.println("Microservei 1 processant dades...");
                Thread.sleep(1000);
                String result = "Aquest es";
                synchronized (partialResults) {
                    partialResults.add(result);
                }
                System.out.println("Microservei 1 completat.");
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        Runnable microservice2 = () -> {
            try {
                System.out.println("Microservei 2 processant dades...");
                Thread.sleep(1500);
                String result = "el resultat final";
                synchronized (partialResults) {
                    partialResults.add(result);
                }
                System.out.println("Microservei 2 completat.");
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        Runnable microservice3 = () -> {
            try {
                System.out.println("Microservei 3 processant dades...");
                Thread.sleep(2000);
                String result = "del microservei.";
                synchronized (partialResults) {
                    partialResults.add(result);
                }
                System.out.println("Microservei 3 completat.");
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        // Executar les tasques en paral·lel
        executor.submit(microservice1);
        executor.submit(microservice2);
        executor.submit(microservice3);

        // Tancar l'executor després de completar totes les tasques
        executor.shutdown();
    }
}
