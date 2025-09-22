package controlador;

import modelo.CarritoCompras;
import modelo.Producto;
import bdd.ConexionDB;
import Vista.Interfaz;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import modelo.CarritoItem;

public class ControladorPrincipal {
    private final CarritoCompras carrito;
    private final Interfaz vista;
    private List<Producto> catalogo;
    private final ConexionDB db;

    private Sesion sesionRunnable;
    private CalcularPrecio calculoRunnable;
    private AplicarDescuento descuentosRunnable;
    private Thread hiloSesion, hiloCalculo, hiloDescuentos;

    public ControladorPrincipal() {
        this.db = ConexionDB.getInstance();
        this.carrito = new CarritoCompras();
        this.vista = new Interfaz();
    }

    public void iniciar() {
        vista.deshabilitarTienda();
        configurarEventos();
        vista.setVisible(true);
    }

    private void configurarEventos() {
        vista.botonLogin.addActionListener(e -> {
            Thread hiloLogin = new Thread(new Login(vista, db, this), "HILO_LOGIN");
            hiloLogin.start();
        });
        vista.botonLogout.addActionListener(e -> gestionarLogout());
        vista.botonAnadirProducto.addActionListener(e -> anadirProducto());
        vista.comboCategorias.addActionListener(e -> filtrarProductosPorCategoria());
    }
    
    private void filtrarProductosPorCategoria() {
        String categoriaSeleccionada = (String) vista.comboCategorias.getSelectedItem();
        if (categoriaSeleccionada == null) return;
        
        if (categoriaSeleccionada.equals("Todas")) {
            this.catalogo = db.obtenerTodosLosProductos();
        } else {
            this.catalogo = db.obtenerProductosPorCategoria(categoriaSeleccionada);
        }
        vista.cargarProductos(catalogo, this);
    }

    private void anadirProducto() {
        JTextField nombreField = new JTextField(15);
        JTextField precioField = new JTextField(15);
        JComboBox<String> categoriaCombo = new JComboBox<>(new String[]{"Lácteos", "Snacks", "Limpieza", "Bebidas", "General"});
        
        JPanel panelAnadir = new JPanel(new GridLayout(3, 2, 5, 5));
        panelAnadir.add(new JLabel("Nombre del Producto:"));
        panelAnadir.add(nombreField);
        panelAnadir.add(new JLabel("Precio del Producto:"));
        panelAnadir.add(precioField);
        panelAnadir.add(new JLabel("Categoría:"));
        panelAnadir.add(categoriaCombo);

        int result = JOptionPane.showConfirmDialog(vista, panelAnadir, "Añadir Nuevo Producto", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText();
            String categoria = (String) categoriaCombo.getSelectedItem();
            try {
                double precio = Double.parseDouble(precioField.getText());

                if (db.registrarProducto(nombre, precio, categoria)) {
                    JOptionPane.showMessageDialog(vista, "Producto añadido exitosamente.");
                    filtrarProductosPorCategoria();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al añadir el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, "El precio debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void gestionarLoginExitoso() {
        List<String> categorias = db.obtenerCategorias();
        vista.cargarCategorias(categorias);
    
        this.catalogo = db.obtenerTodosLosProductos();    
        vista.cargarProductos(catalogo, this);
        vista.habilitarTienda();
        
        vista.revalidate();
        vista.repaint();
        
        actualizarVistaCarrito();

        iniciarHilosDeCompra();
        if (vista.botonFinalizarCompra.getActionListeners().length == 0) {
            vista.botonFinalizarCompra.addActionListener(ev -> gestionarFinalizacionCompra());
        }
    }

    public void agregarProductoAlCarrito(Producto producto) {
        Thread hiloSeleccion = new Thread(new SeleccionProducto(carrito, producto, this), "HILO_SELECCION_PRODUCTO");
        hiloSeleccion.start();
    }
    
    public void incrementarCantidad(String nombreProducto) {
        carrito.incrementarCantidad(nombreProducto);
        actualizarVistaCarrito();
    }
    
    public void decrementarCantidad(String nombreProducto) {
        carrito.decrementarCantidad(nombreProducto);
        actualizarVistaCarrito();
    }
    
    public void eliminarDelCarrito(String nombreProducto) {
        carrito.eliminarProducto(nombreProducto);
        actualizarVistaCarrito();
    }

    private void iniciarHilosDeCompra() {
        sesionRunnable = new Sesion();
        hiloSesion = new Thread(sesionRunnable, "HILO_SESION");
        hiloSesion.start();
        calculoRunnable = new CalcularPrecio(carrito, vista);
        hiloCalculo = new Thread(calculoRunnable, "HILO_CALCULO_PRECIO");
        hiloCalculo.start();
        descuentosRunnable = new AplicarDescuento(carrito, vista);
        hiloDescuentos = new Thread(descuentosRunnable, "HILO_DESCUENTOS");
        hiloDescuentos.start();
    }
    
    private void gestionarLogout() {
        detenerHilosActivos();
        carrito.vaciarCarrito();
        reiniciarVistaParaNuevaCompra();
        vista.limpiarPanelProductos();
        vista.deshabilitarTienda();
        System.out.println("Sesión cerrada.");
    }

    private void gestionarFinalizacionCompra() {
        detenerHilosActivos();
        Thread hiloFinalizar = new Thread(new FinalizarCompra(carrito, this), "HILO_FINALIZAR_COMPRA");
        hiloFinalizar.start();
    }

    private void detenerHilosActivos() {
        if (calculoRunnable != null) calculoRunnable.detener();
        if (descuentosRunnable != null) descuentosRunnable.detener();
        if (sesionRunnable != null) sesionRunnable.detener();
    }

    public void actualizarVistaCarrito() {
        List<CarritoItem> items = carrito.getItems();
        vista.actualizarVistaCarrito(items, this);
    }
    
    public void reiniciarVistaParaNuevaCompra() {
        actualizarVistaCarrito();
        vista.etiquetaSubtotal.setText("$0.00");
        vista.etiquetaDescuento.setText("$0.00");
        vista.etiquetaTotal.setText("$0.00");
    }

    public CarritoCompras getCarrito() {
        return carrito;
    }
}