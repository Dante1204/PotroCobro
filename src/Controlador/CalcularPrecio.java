/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import javax.swing.SwingUtilities;
import modelo.CarritoCompras;
import Vista.Interfaz;

public class CalcularPrecio implements Runnable {
    private final CarritoCompras carrito;
    private final Interfaz vista;
    private volatile boolean running = true;

    public CalcularPrecio(CarritoCompras carrito, Interfaz vista) {
        this.carrito = carrito;
        this.vista = vista;
    }
    
    public void detener() {
        this.running = false;
    }

    @Override
    public void run() {
        while(running) {
    carrito.calcularSubtotal();
    double subtotal = carrito.getSubtotal();
    
    final double subtotalParaActualizar = subtotal; // Copia final
    
    SwingUtilities.invokeLater(() -> {
        vista.etiquetaSubtotal.setText(String.format("$%.2f", subtotalParaActualizar));
        });

            try {
                Thread.sleep(500); // Recalcula cada medio segundo
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}