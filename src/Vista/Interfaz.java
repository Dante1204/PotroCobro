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
    public JComboBox<String> comboCategorias; // <-- JComboBox declarado

    public Interfaz() {
        setTitle("CUU TIANGUISTENCO - Autocobro");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10)); // Añadido espaciado general

        // --- Panel Superior (Filtros de Categoría) ---
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Espaciado
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));
        panelFiltros.add(new JLabel("Categoría:"));
        comboCategorias = new JComboBox<>();
        panelFiltros.add(comboCategorias);
        add(panelFiltros, BorderLayout.NORTH);

        // --- Panel de Productos (CENTRO) ---
        panelProductos = new JPanel(new GridLayout(0, 1, 10, 10));
        panelProductos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollProductos = new JScrollPane(panelProductos);
        add(scrollProductos, BorderLayout.CENTER);

        // --- Panel Derecho (Carrito y Totales) ---
        JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10)); // Margen
        JPanel panelCarrito = new JPanel(new BorderLayout());
        panelCarrito.setBorder(BorderFactory.createTitledBorder("Carrito"));
        areaCarrito = new JTextArea(15, 30);
        areaCarrito.setEditable(false);
        panelCarrito.add(new JScrollPane(areaCarrito));

        // --- Panel de Totales REORGANIZADO con GridBagLayout ---
        JPanel panelTotal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panelTotal.setBorder(BorderFactory.createTitledBorder("Resumen de Compra"));
        
        etiquetaSubtotal = new JLabel("$0.00");
        etiquetaDescuento = new JLabel("$0.00");
        etiquetaTotal = new JLabel("$0.00");
        botonFinalizarCompra = new JButton("Finalizar Compra");
        botonLogout = new JButton("Cerrar Sesión");
        botonAnadirProducto = new JButton("Añadir producto");
        
        // Configuración de Constraints
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: Subtotal
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; panelTotal.add(new JLabel("Subtotal:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; panelTotal.add(etiquetaSubtotal, gbc);

        // Fila 1: Descuento
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; panelTotal.add(new JLabel("Descuento:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST; panelTotal.add(etiquetaDescuento, gbc);

        // Fila 2: Total
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; panelTotal.add(new JLabel("Total:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST; panelTotal.add(etiquetaTotal, gbc);

        // Fila 3: Botones
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; panelTotal.add(botonFinalizarCompra, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; panelTotal.add(botonAnadirProducto, gbc);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; panelTotal.add(botonLogout, gbc);

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

    public void cargarCategorias(List<String> categorias) {
        comboCategorias.removeAllItems();
        comboCategorias.addItem("Todas");
        for (String categoria : categorias) {
            comboCategorias.addItem(categoria);
        }
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
        this.comboCategorias.setEnabled(true); // <-- Habilitar ComboBox
    }

    public void deshabilitarTienda() {
        this.botonLogin.setEnabled(true);
        this.botonFinalizarCompra.setEnabled(false);
        this.botonLogout.setEnabled(false);
        if (this.comboCategorias != null) {
            this.comboCategorias.setEnabled(false);
        }
        if (this.scrollProductos != null) {
            this.scrollProductos.setVisible(false);
        }
    }
}