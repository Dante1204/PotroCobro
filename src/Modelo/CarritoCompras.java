package modelo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CarritoCompras {
    private final Map<String, CarritoItem> items = new LinkedHashMap<>();
    private double subtotal = 0.0;
    private double descuento = 0.0;
    private double total = 0.0;

    public synchronized void agregarProducto(Producto producto) {
        String nombreProducto = producto.getNombre();
        CarritoItem itemExistente = items.get(nombreProducto);

        if (itemExistente != null) {
            itemExistente.incrementarCantidad();
        } else {
            items.put(nombreProducto, new CarritoItem(producto, 1));
        }
        System.out.println(producto.getNombre() + " agregado al carrito.");
    }
    
    public synchronized void incrementarCantidad(String nombreProducto) {
        CarritoItem item = items.get(nombreProducto);
        if (item != null) {
            item.incrementarCantidad();
        }
    }

    public synchronized void decrementarCantidad(String nombreProducto) {
        CarritoItem item = items.get(nombreProducto);
        if (item != null) {
            item.decrementarCantidad();
            if (item.getCantidad() == 0) {
                items.remove(nombreProducto);
            }
        }
    }

    public synchronized void eliminarProducto(String nombreProducto) {
        items.remove(nombreProducto);
    }

    public synchronized void calcularSubtotal() {
        this.subtotal = items.values().stream()
                .mapToDouble(CarritoItem::getSubtotal)
                .sum();
    }

    public synchronized void aplicarDescuento(double descuento) {
        this.descuento = descuento;
    }

    public synchronized void calcularTotal() {
        this.total = this.subtotal - this.descuento;
    }
    
    public synchronized double getSubtotal() {
        return subtotal;
    }

    public synchronized double getDescuento() {
        return descuento;
    }
    
    public synchronized double getTotal() {
        return total;
    }

    public synchronized List<CarritoItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public synchronized void vaciarCarrito() {
        items.clear();
        subtotal = 0.0;
        descuento = 0.0;
        total = 0.0;
    }
}