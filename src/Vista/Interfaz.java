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
    public JComboBox<String> comboCategorias;

    public Interfaz() {
        setTitle("CUU TIANGUISTENCO - Autocobro");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // --- Panel Superior (SOLO TÍTULO) ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15)); // Ajuste de borde
        
        JLabel tituloLabel = new JLabel("PotroCobro Autoservicio");
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panelSuperior.add(tituloLabel, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // --- Panel Central (Filtros + Productos) ---
        JPanel panelCentral = new JPanel(new BorderLayout(0, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        // Panel de Filtros (Movido aquí)
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelFiltros.add(new JLabel("Categoría: "));
        comboCategorias = new JComboBox<>();
        panelFiltros.add(comboCategorias);
        
        // Panel de Productos
        panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));
        panelProductos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelProductos.setBackground(Color.WHITE);

        scrollProductos = new JScrollPane(panelProductos);
        scrollProductos.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        // Añadir filtros y lista de productos al panel central
        panelCentral.add(panelFiltros, BorderLayout.NORTH);
        panelCentral.add(scrollProductos, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);


        // --- Panel Derecho (Carrito y Acciones) ---
        JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 15));
        
        // Panel del Carrito
        JPanel panelCarrito = new JPanel(new BorderLayout());
        panelCarrito.setBorder(BorderFactory.createTitledBorder("Mi Carrito"));
        areaCarrito = new JTextArea(15, 35);
        areaCarrito.setEditable(false);
        areaCarrito.setFont(new Font("Consolas", Font.PLAIN, 14));
        panelCarrito.add(new JScrollPane(areaCarrito));
        
        // Panel de Resumen de Compra
        JPanel panelResumen = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panelResumen.setBorder(BorderFactory.createTitledBorder("Resumen de Compra"));
        
        etiquetaSubtotal = new JLabel("$0.00");
        etiquetaDescuento = new JLabel("$0.00");
        etiquetaTotal = new JLabel("$0.00");
        
        etiquetaSubtotal.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        etiquetaDescuento.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        etiquetaTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; panelResumen.add(new JLabel("Subtotal:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; panelResumen.add(etiquetaSubtotal, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; panelResumen.add(new JLabel("Descuento:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST; panelResumen.add(etiquetaDescuento, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; panelResumen.add(new JLabel("Total:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST; panelResumen.add(etiquetaTotal, gbc);

        // Panel de Botones
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 0, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        botonFinalizarCompra = new JButton("Finalizar Compra");
        botonAnadirProducto = new JButton("Añadir Producto");
        botonLogout = new JButton("Cerrar Sesión");
        
        panelBotones.add(botonFinalizarCompra);
        panelBotones.add(botonAnadirProducto);
        panelBotones.add(botonLogout);

        // Ensamblaje del panel derecho
        JPanel panelAcciones = new JPanel(new BorderLayout());
        panelAcciones.add(panelResumen, BorderLayout.NORTH);
        panelAcciones.add(panelBotones, BorderLayout.SOUTH);
        
        panelDerecho.add(panelCarrito, BorderLayout.CENTER);
        panelDerecho.add(panelAcciones, BorderLayout.SOUTH);
        add(panelDerecho, BorderLayout.EAST);

        // --- Panel Inferior (Login) ---
        JPanel panelLogin = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
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
            panelProductos.add(Box.createRigidArea(new Dimension(0, 5))); // Espacio entre productos
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
        this.botonLogin.setVisible(false);
        this.botonFinalizarCompra.setEnabled(true);
        this.botonLogout.setEnabled(true);
        this.scrollProductos.setVisible(true);
        this.comboCategorias.setEnabled(true);
    }

    public void deshabilitarTienda() {
        this.botonLogin.setVisible(true);
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