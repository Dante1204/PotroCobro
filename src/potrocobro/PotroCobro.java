package potrocobro;

import bdd.ConexionDB;
import com.formdev.flatlaf.FlatLightLaf; // Importa FlatLaf
import controlador.ControladorPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class PotroCobro {

    public static void main(String[] args) {
        // --- CONFIGURACIÓN DEL LOOK AND FEEL ---
        try {
            // Usa el Look and Feel FlatLaf Light
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Falló la inicialización de FlatLaf. Usando el L&F por defecto.");
        }
        
        ConexionDB.getInstance().inicializarBaseDeDatos();
        
        SwingUtilities.invokeLater(() -> {
            ControladorPrincipal controlador = new ControladorPrincipal();
            controlador.iniciar();
        });
    }
}