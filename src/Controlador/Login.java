package controlador;

import bdd.ConexionDB;
import Vista.Interfaz;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Login implements Runnable {
    private final Interfaz vista;
    private final ConexionDB db;
    private final ControladorPrincipal controlador;

    public Login(Interfaz vista, ConexionDB db, ControladorPrincipal controlador) {
        this.vista = vista;
        this.db = db;
        this.controlador = controlador;
    }

    @Override
    public void run() {
        System.out.println("[Hilo 1 - Login] Iniciando proceso de autenticación.");
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
                // Invocamos el método en el hilo de la GUI
                SwingUtilities.invokeLater(() -> controlador.gestionarLoginExitoso());
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
        System.out.println("[Hilo 1 - Login] Proceso de autenticación finalizado.");
    }
}