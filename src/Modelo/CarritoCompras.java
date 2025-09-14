/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.List;

public class CarritoCompras {
    private final List<Producto> items = new ArrayList<>();
    private double subtotal = 0.0;
    private double descuento = 0.0;
    private double total = 0.0;

    // Sincronizado para que el Hilo 3 no interfiera con el Hilo 4
    public synchronized void agregarProducto(Producto producto) {
        items.add(producto);
        System.out.println(producto.getNombre() + " agregado al carrito.");
    }

    // Sincronizado para cálculos seguros
    public synchronized void calcularSubtotal() {
        this.subtotal = items.stream().mapToDouble(Producto::getPrecio).sum();
    }

    public synchronized void aplicarDescuento(double descuento) {
        this.descuento = descuento;
    }

    public synchronized void calcularTotal() {
        this.total = this.subtotal - this.descuento;
    }
    
    // Getters sincronizados para una lectura segura del estado actual
    public synchronized double getSubtotal() {
        return subtotal;
    }

    public synchronized double getDescuento() {
        return descuento;
    }
    
    public synchronized double getTotal() {
        return total;
    }

    public synchronized List<Producto> getItems() {
        // Devolvemos una copia para que otros hilos no puedan modificar la lista original
        return new ArrayList<>(items);
    }

    public synchronized void vaciarCarrito() {
        items.clear();
        subtotal = 0.0;
        descuento = 0.0;
        total = 0.0;
    }
}