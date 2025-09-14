/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import javax.swing.JOptionPane;
import modelo.CarritoCompras;
import modelo.Producto;

public class GenerarTicket implements Runnable {
    private final CarritoCompras carrito;

    public GenerarTicket(CarritoCompras carrito) {
        this.carrito = carrito;
    }

    @Override
    public void run() {
        // Hilo 6: Simulación de pago y confirmación
        System.out.println("[Hilo 6 - Finalización] Procesando pago...");
        try {
            Thread.sleep(2000); // Simula el tiempo de procesamiento
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("[Hilo 6 - Finalización] Pago aprobado.");

        // Hilo 7: Generación de ticket
        System.out.println("[Hilo 7 - Ticket] Generando ticket...");
        StringBuilder ticket = new StringBuilder("--- RESUMEN DE COMPRA ---\n");
        for (Producto p : carrito.getItems()) {
            ticket.append(String.format("%-30s $%.2f\n", p.getNombre(), p.getPrecio()));
        }
        ticket.append("--------------------------\n");
        ticket.append(String.format("SUBTOTAL: $%.2f\n", carrito.getSubtotal()));
        ticket.append(String.format("DESCUENTO: $%.2f\n", carrito.getDescuento()));
        ticket.append(String.format("TOTAL: $%.2f\n", carrito.getTotal()));
        ticket.append("\n¡Gracias por su compra!");

        // Mostrar el ticket en una ventana emergente
        JOptionPane.showMessageDialog(null, ticket.toString(), "Ticket de Compra", JOptionPane.INFORMATION_MESSAGE);
        
        // Limpiar el carrito para una nueva compra
        carrito.vaciarCarrito();
        System.out.println("[Hilos 6 y 7] Proceso completado. Carrito vaciado.");
    }
}