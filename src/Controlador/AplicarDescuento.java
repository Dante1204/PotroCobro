/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import javax.swing.SwingUtilities;
import modelo.CarritoCompras;
import Vista.Interfaz;

public class AplicarDescuento implements Runnable {
    private final CarritoCompras carrito;
    private final Interfaz vista;
    private volatile boolean running = true;

    public AplicarDescuento(CarritoCompras carrito, Interfaz vista) {
        this.carrito = carrito;
        this.vista = vista;
    }

    public void detener() {
        this.running = false;
    }

    @Override
    public void run() {
       while(running) {
    // ...
    double subtotal = carrito.getSubtotal();
    double descuento = 0.0;

    if (subtotal > 500.00) {
        descuento = subtotal * 0.10;
    }
    
    carrito.aplicarDescuento(descuento);
    carrito.calcularTotal();
    double total = carrito.getTotal();

    final double descuentoParaActualizar = descuento;
    final double totalParaActualizar = total;

    SwingUtilities.invokeLater(() -> {
        vista.etiquetaDescuento.setText(String.format("$%.2f", descuentoParaActualizar));
        vista.etiquetaTotal.setText(String.format("$%.2f", totalParaActualizar));
    });

    try {
        Thread.sleep(500);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
       }
    }
}