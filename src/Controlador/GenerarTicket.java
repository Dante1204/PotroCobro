package controlador;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities; // Importar SwingUtilities
import modelo.CarritoCompras;
import modelo.CarritoItem;
import modelo.Producto;

public class GenerarTicket implements Runnable {
    private final CarritoCompras carrito;
    private final ControladorPrincipal controlador; // Referencia al controlador

    // Constructor actualizado para recibir el controlador
    public GenerarTicket(CarritoCompras carrito, ControladorPrincipal controlador) {
        this.carrito = carrito;
        this.controlador = controlador;
    }

    @Override
    public void run() {
        // Hilo 6: Simulación de pago y confirmación
        System.out.println("[Hilo 6 - Finalización] Procesando pago...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("[Hilo 6 - Finalización] Pago aprobado.");

        // Hilo 7: Generación de ticket
        System.out.println("[Hilo 7 - Ticket] Generando ticket...");
        StringBuilder ticket = new StringBuilder("--- RESUMEN DE COMPRA ---\n");
        
        for (CarritoItem item : carrito.getItems()) {
            Producto p = item.getProducto();
            ticket.append(String.format("%d x %-25s $%.2f\n", 
                item.getCantidad(), 
                p.getNombre(), 
                item.getSubtotal()
            ));
        }
        
        ticket.append("----------------------------------\n");
        ticket.append(String.format("SUBTOTAL:  $%.2f\n", carrito.getSubtotal()));
        ticket.append(String.format("DESCUENTO: $%.2f\n", carrito.getDescuento()));
        ticket.append(String.format("TOTAL:     $%.2f\n", carrito.getTotal()));
        ticket.append("\n¡Gracias por su compra!");

        // Mostrar el ticket en una ventana emergente
        JOptionPane.showMessageDialog(null, ticket.toString(), "Ticket de Compra", JOptionPane.INFORMATION_MESSAGE);
        
        // Limpiar el carrito
        carrito.vaciarCarrito();
        
        // --- CAMBIO CLAVE ---
        // Actualizar la interfaz gráfica DESPUÉS de limpiar el carrito.
        // Se usa SwingUtilities.invokeLater para asegurar que se ejecute en el hilo de la GUI.
        SwingUtilities.invokeLater(() -> {
            controlador.reiniciarVistaParaNuevaCompra();
        });

        System.out.println("[Hilos 6 y 7] Proceso completado. Carrito vaciado y vista reiniciada.");
    }
}