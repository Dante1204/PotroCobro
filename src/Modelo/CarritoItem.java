package modelo;

public class CarritoItem {
    private final Producto producto;
    private int cantidad;

    public CarritoItem(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void incrementarCantidad() {
        this.cantidad++;
    }

    public void decrementarCantidad() {
        if (this.cantidad > 0) {
            this.cantidad--;
        }
    }

    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }
}