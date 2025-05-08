package com.project;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) {
            final int maxConnexions = 3; // Conexiones simultanias
            final int totalConnexions = 10; // Maximo conexiones 
    
            WebPage pagina = new WebPage(maxConnexions);
            ExecutorService executor = Executors.newFixedThreadPool(5);
    
            for (int i = 1; i <= totalConnexions; i++) {
                final int idUsuari = i;
                executor.execute(() -> pagina.connectarUsuari(idUsuari));
            }
    
            executor.shutdown();
        }
    
        static class WebPage {
            private final Semaphore semafor;
    
            public WebPage(int maxConnexions) {
                this.semafor = new Semaphore(maxConnexions);
            }
    
            public void connectarUsuari(int idUsuari) {
                try {
                    System.out.println("Usuari " + idUsuari + " intenta connectar-se.");
                    if (!semafor.tryAcquire()) {
                        System.out.println("Usuari " + idUsuari + " espera: connexions màximes superades.");
                        semafor.acquire();
                    }
                    System.out.println("✅ Usuari " + idUsuari + " s'ha connectat.");
    
                    // Simulacio temps connexio
                    Thread.sleep((long) (Math.random() * 4000 + 1000));
    
                    System.out.println("Usuari " + idUsuari + " s'ha desconnectat.");
                    semafor.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Usuari " + idUsuari + " ha estat interromput.");
                }
            }
        }
}
