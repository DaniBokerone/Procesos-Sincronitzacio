package com.project;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) {
        final int capacitat = 3; // Capacitat maxima parking
        final int nombreCotxes = 10; // Num coches que intentan aparcar

        ParkingLot parkingLot = new ParkingLot(capacitat);
        ExecutorService executor = Executors.newFixedThreadPool(5); 

        for (int i = 1; i <= nombreCotxes; i++) {
            final int idCotxe = i;
            executor.execute(() -> parkingLot.aparcarCotxe(idCotxe));
        }

        executor.shutdown();
    }

    static class ParkingLot {
        private final Semaphore semafor;

        public ParkingLot(int capacitat) {
            this.semafor = new Semaphore(capacitat);
        }

        public void aparcarCotxe(int idCotxe) {
            try {
                System.out.println("Cotxe " + idCotxe + " intenta entrar a l'aparcament.");
                if (!semafor.tryAcquire()) {
                    System.out.println("Aparcament ple. Cotxe " + idCotxe + " està esperant...");
                    semafor.acquire(); // esperar a espacio libre
                }
                System.out.println("Cotxe " + idCotxe + " ha entrat a l'aparcament.");
                
                // Tiempo simulat aparcar
                Thread.sleep((long) (Math.random() * 5000 + 1000));
                
                System.out.println("Cotxe " + idCotxe + " surt de l'aparcament.");
                semafor.release(); // salir parking

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Cotxe " + idCotxe + " ha estat interromput.");
            }
        }
    }
}
