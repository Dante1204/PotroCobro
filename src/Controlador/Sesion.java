/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

public class Sesion implements Runnable {
    private volatile boolean running = true; // 'volatile' para visibilidad entre hilos

    public void detener() {
        this.running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                System.out.println("[Hilo 2 - Sesión] La sesión sigue activa.");
                Thread.sleep(15000); // Se reporta cada 15 segundos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Hilo de sesión interrumpido.");
            }
        }
        System.out.println("[Hilo 2 - Sesión] Sesión cerrada.");
    }
}
