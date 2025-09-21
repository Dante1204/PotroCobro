package Vista;

import controlador.ControladorPrincipal;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import modelo.CarritoItem;
import modelo.Producto;

public class Interfaz extends JFrame {
    private JPanel panelProductos;
    private JScrollPane scrollProductos;
    public JLabel etiquetaSubtotal, etiquetaDescuento, etiquetaTotal;
    private JPanel panelItemsCarrito;
    public JButton botonFinalizarCompra, botonLogin, botonLogout, botonAnadirProducto;
    public JComboBox<String> comboCategorias;

    public Interfaz() {
        setTitle("CUU TIANGUISTENCO - Autocobro");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // --- Panel Superior (TÍTULO) ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));
        
        JLabel tituloLabel = new JLabel("PotroCobro Autoservicio");
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panelSuperior.add(tituloLabel, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // --- Panel Central (Filtros + Productos) ---
        JPanel panelCentral = new JPanel(new BorderLayout(0, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelFiltros.add(new JLabel("Categoría: "));
        comboCategorias = new JComboBox<>();
        panelFiltros.add(comboCategorias);
        
        panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));
        panelProductos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelProductos.setBackground(Color.WHITE);

        scrollProductos = new JScrollPane(panelProductos);
        scrollProductos.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        panelCentral.add(panelFiltros, BorderLayout.NORTH);
        panelCentral.add(scrollProductos, BorderLayout.CENTER);

        // --- Panel Derecho (Carrito y Acciones) ---
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        
        JPanel panelCarrito = new JPanel(new BorderLayout());
        panelCarrito.setBorder(BorderFactory.createTitledBorder("Mi Carrito"));
        
        panelItemsCarrito = new JPanel();
        panelItemsCarrito.setLayout(new BoxLayout(panelItemsCarrito, BoxLayout.Y_AXIS));
        JScrollPane scrollCarrito = new JScrollPane(panelItemsCarrito);
        panelCarrito.add(scrollCarrito);
        
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

        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 0, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        botonFinalizarCompra = new JButton("Finalizar Compra");
        botonAnadirProducto = new JButton("Añadir Producto");
        botonLogout = new JButton("Cerrar Sesión");
        
        panelBotones.add(botonFinalizarCompra);
        panelBotones.add(botonAnadirProducto);
        panelBotones.add(botonLogout);

        JPanel panelAcciones = new JPanel(new BorderLayout());
        panelAcciones.add(panelResumen, BorderLayout.NORTH);
        panelAcciones.add(panelBotones, BorderLayout.SOUTH);
        
        JSplitPane splitPaneVerticalDerecho = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelCarrito, panelAcciones);
        splitPaneVerticalDerecho.setResizeWeight(0.6);
        splitPaneVerticalDerecho.setBorder(null);

        panelDerecho.add(splitPaneVerticalDerecho, BorderLayout.CENTER);

        // --- CAMBIO PRINCIPAL: JSplitPane HORIZONTAL ---
        // Este panel divide el espacio horizontalmente entre productos y carrito
        JSplitPane splitPanePrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCentral, panelDerecho);
        splitPanePrincipal.setResizeWeight(0.65); // 65% del espacio para la lista de productos
        splitPanePrincipal.setBorder(null);
        
        add(splitPanePrincipal, BorderLayout.CENTER);

        // --- Panel Inferior (Login) ---
        JPanel panelLogin = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        botonLogin = new JButton("Iniciar Sesión / Registrarse");
        panelLogin.add(botonLogin);
        add(panelLogin, BorderLayout.SOUTH);
        
        deshabilitarTienda();
    }
    
    public void actualizarVistaCarrito(List<CarritoItem> items, ControladorPrincipal controlador) {
        panelItemsCarrito.removeAll();
        if (items.isEmpty()) {
            JLabel vacioLabel = new JLabel("El carrito está vacío.");
            vacioLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
            panelItemsCarrito.add(vacioLabel);
        } else {
            for (CarritoItem item : items) {
                panelItemsCarrito.add(new CarritoItemPanel(item, controlador));
            }
        }
        panelItemsCarrito.revalidate();
        panelItemsCarrito.repaint();
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
            panelProductos.add(Box.createRigidArea(new Dimension(0, 5)));
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