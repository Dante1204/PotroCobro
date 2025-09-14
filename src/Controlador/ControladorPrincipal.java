package controlador;

import modelo.CarritoCompras;
import modelo.Producto;
import bdd.ConexionDB;
import Vista.Interfaz;
import javax.swing.*;
import java.awt.*;
import java.util.List;

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
        configurarEventosLogin();
        vista.setVisible(true);
    }

    private void configurarEventosLogin() {
        vista.botonLogin.addActionListener(e -> mostrarDialogoLogin());
        vista.botonLogout.addActionListener(e -> gestionarLogout());
        vista.botonAnadirProducto.addActionListener(e -> anadirProducto());
    }

    private void anadirProducto() {
        JTextField nombreField = new JTextField(15);
        JTextField precioField = new JTextField(15);
        JPanel panelAnadir = new JPanel(new GridLayout(2, 2, 5, 5));
        panelAnadir.add(new JLabel("Nombre del Producto:"));
        panelAnadir.add(nombreField);
        panelAnadir.add(new JLabel("Precio del Producto:"));
        panelAnadir.add(precioField);

        int result = JOptionPane.showConfirmDialog(vista, panelAnadir, "Añadir Nuevo Producto", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText();
            Double precio = Double.parseDouble(precioField.getText());
            
            db.registrarProducto(nombre, precio);

        }
        
    }
    
    private void mostrarDialogoLogin() {
        JTextField usuarioField = new JTextField(15);
        JPasswordField contrasenaField = new JPasswordField(15);
        JPanel panelLogin = new JPanel(new GridLayout(2, 2, 5, 5));
        panelLogin.add(new JLabel("Usuario:"));
        panelLogin.add(usuarioField);
        panelLogin.add(new JLabel("Contraseña:"));
        panelLogin.add(contrasenaField);
        int result = JOptionPane.showConfirmDialog(vista, panelLogin, "Iniciar Sesión / Registrarse", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String usuario = usuarioField.getText();
            String contrasena = new String(contrasenaField.getPassword());
            if (usuario.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "El usuario y la contraseña no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (db.validarUsuario(usuario, contrasena)) {
                gestionarLoginExitoso();
            } else {
                int registrar = JOptionPane.showConfirmDialog(vista, "Usuario no encontrado o contraseña incorrecta.\n¿Desea registrar este nuevo usuario?", "Registrar", JOptionPane.YES_NO_OPTION);
                if (registrar == JOptionPane.YES_OPTION) {
                    if (db.registrarUsuario(usuario, contrasena)) {
                        JOptionPane.showMessageDialog(vista, "Usuario registrado exitosamente. Por favor, inicie sesión de nuevo.");
                    } else {
                        JOptionPane.showMessageDialog(vista, "No se pudo registrar (es posible que el nombre de usuario ya exista).", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private void gestionarLoginExitoso() {
        this.catalogo = db.obtenerTodosLosProductos();
        // Le pasamos 'this' (el controlador) a la vista para que pueda llamarnos
        vista.cargarProductos(catalogo, this);
        
        vista.habilitarTienda();
        iniciarHilosDeCompra();
        
        // El ActionListener para finalizar compra se configura aquí
        if (vista.botonFinalizarCompra.getActionListeners().length == 0) {
            vista.botonFinalizarCompra.addActionListener(ev -> gestionarFinalizacionCompra());
        }
    }

    /**
     * Método público para que la Vista nos ordene agregar un producto al carrito.
     * @param producto El producto en el que se hizo clic.
     */
    public void agregarProductoAlCarrito(Producto producto) {
        carrito.agregarProducto(producto);
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
        Thread hiloFinalizar = new Thread(new GenerarTicket(carrito), "HILO_FINALIZAR_TICKET");
        hiloFinalizar.start();
        reiniciarVistaParaNuevaCompra();
    }

    private void detenerHilosActivos() {
        if (calculoRunnable != null) calculoRunnable.detener();
        if (descuentosRunnable != null) descuentosRunnable.detener();
        if (sesionRunnable != null) sesionRunnable.detener();
    }

    private void actualizarVistaCarrito() {
        StringBuilder textoCarrito = new StringBuilder();
        for (Producto p : carrito.getItems()) {
            textoCarrito.append(p.getNombre()).append("\n");
        }
        vista.areaCarrito.setText(textoCarrito.toString());
    }
    
    private void reiniciarVistaParaNuevaCompra() {
        vista.areaCarrito.setText("");
        vista.etiquetaSubtotal.setText("$0.00");
        vista.etiquetaDescuento.setText("$0.00");
        vista.etiquetaTotal.setText("$0.00");
    }
}