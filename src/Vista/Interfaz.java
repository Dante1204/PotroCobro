package Vista;

import controlador.ControladorPrincipal;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import modelo.Producto;

public class Interfaz extends JFrame {
    private JPanel panelProductos;
    private JScrollPane scrollProductos;
    public JLabel etiquetaSubtotal, etiquetaDescuento, etiquetaTotal;
    public JTextArea areaCarrito;
    public JButton botonFinalizarCompra, botonLogin, botonLogout, botonAnadirProducto;

    public Interfaz() {
        setTitle("CUU TIANGUISTENCO - Autocobro");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Panel de Productos (CENTRO) ---
        panelProductos = new JPanel(new GridLayout(0, 1, 10, 10));
        panelProductos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollProductos = new JScrollPane(panelProductos);
        add(scrollProductos, BorderLayout.CENTER);

        // --- Panel Derecho (Carrito y Totales) ---
        JPanel panelDerecho = new JPanel(new BorderLayout());
        JPanel panelCarrito = new JPanel(new BorderLayout());
        panelCarrito.setBorder(BorderFactory.createTitledBorder("Carrito"));
        areaCarrito = new JTextArea(15, 30);
        areaCarrito.setEditable(false);
        panelCarrito.add(new JScrollPane(areaCarrito));
        
        JPanel panelTotal = new JPanel(new GridLayout(5, 2, 5, 5));
        panelTotal.setBorder(BorderFactory.createTitledBorder("Resumen de Compra"));
        etiquetaSubtotal = new JLabel("$0.00");
        etiquetaDescuento = new JLabel("$0.00");
        etiquetaTotal = new JLabel("$0.00");
        botonFinalizarCompra = new JButton("Finalizar Compra");
        botonLogout = new JButton("Cerrar Sesión");
        botonAnadirProducto = new JButton("Añadir producto");
        
        panelTotal.add(new JLabel("Subtotal:"));
        panelTotal.add(etiquetaSubtotal);
        panelTotal.add(new JLabel("Descuento:"));
        panelTotal.add(etiquetaDescuento);
        panelTotal.add(new JLabel("Total:"));
        panelTotal.add(etiquetaTotal);
        panelTotal.add(new JLabel("")); 
        panelTotal.add(botonFinalizarCompra);
        panelTotal.add(new JLabel("")); 
        panelTotal.add(botonLogout);
        panelTotal.add(new JLabel(""));
        panelTotal.add(botonAnadirProducto);
        
        panelDerecho.add(panelCarrito, BorderLayout.CENTER);
        panelDerecho.add(panelTotal, BorderLayout.SOUTH);
        add(panelDerecho, BorderLayout.EAST);

        // --- Panel Inferior (Login) ---
        JPanel panelLogin = new JPanel();
        botonLogin = new JButton("Iniciar Sesión / Registrarse");
        panelLogin.add(botonLogin);
        add(panelLogin, BorderLayout.SOUTH);
        
        deshabilitarTienda();
    }

    public void cargarProductos(List<Producto> productos, ControladorPrincipal controlador) {
        limpiarPanelProductos();
        for (Producto producto : productos) {
            ProductoPanel panelProducto = new ProductoPanel(producto);
            panelProducto.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    controlador.agregarProductoAlCarrito(producto);
                }
            });

            panelProductos.add(panelProducto);
        }
        panelProductos.revalidate();
        panelProductos.repaint();
        scrollProductos.revalidate();
        scrollProductos.repaint();
    }

    public void limpiarPanelProductos() {
        panelProductos.removeAll();
        panelProductos.revalidate();
        panelProductos.repaint();
    }

    public void habilitarTienda() {
        this.botonLogin.setEnabled(false);
        this.botonFinalizarCompra.setEnabled(true);
        this.botonLogout.setEnabled(true);
        this.scrollProductos.setVisible(true);
        
    }

    public void deshabilitarTienda() {
        this.botonLogin.setEnabled(true);
        this.botonFinalizarCompra.setEnabled(false);
        this.botonLogout.setEnabled(false);
        if (this.scrollProductos != null) {
            this.scrollProductos.setVisible(false);
        }
    }
}