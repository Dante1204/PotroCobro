package controlador;

import javax.swing.SwingUtilities;
import modelo.CarritoCompras;
import modelo.Producto;

public class SeleccionProducto implements Runnable {
    private final CarritoCompras carrito;
    private final Producto producto;
    private final ControladorPrincipal controlador;

    public SeleccionProducto(CarritoCompras carrito, Producto producto, ControladorPrincipal controlador) {
        this.carrito = carrito;
        this.producto = producto;
        this.controlador = controlador;
    }

    @Override
    public void run() {
        System.out.println("[Hilo 3 - Selección] Agregando producto: " + producto.getNombre());
        carrito.agregarProducto(producto);
        
        // La actualización de la vista debe hacerse en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            controlador.actualizarVistaCarrito();
        });
        System.out.println("[Hilo 3 - Selección] Producto agregado y vista actualizada.");
    }
}