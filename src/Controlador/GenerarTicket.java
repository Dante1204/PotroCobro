package controlador;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import modelo.CarritoCompras;
import modelo.CarritoItem;
import modelo.Producto;

public class GenerarTicket implements Runnable {
    private final CarritoCompras carrito;
    private final ControladorPrincipal controlador;

    public GenerarTicket(CarritoCompras carrito, ControladorPrincipal controlador) {
        this.carrito = carrito;
        this.controlador = controlador;
    }

    @Override
    public void run() {
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

        JOptionPane.showMessageDialog(null, ticket.toString(), "Ticket de Compra", JOptionPane.INFORMATION_MESSAGE);
        
        carrito.vaciarCarrito();
        
        SwingUtilities.invokeLater(() -> {
            controlador.reiniciarVistaParaNuevaCompra();
        });

        System.out.println("[Hilos 6 y 7] Proceso completado. Carrito vaciado y vista reiniciada.");
    }
}