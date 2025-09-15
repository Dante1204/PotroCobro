/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package potrocobro;

import bdd.ConexionDB;
import controlador.ControladorPrincipal;
import javax.swing.SwingUtilities;

public class PotroCobro {

    public static void main(String[] args) {
        // Es una buena práctica asegurar que la GUI se cree y se muestre
        // en el Event Dispatch Thread (EDT) de Swing.
        
        ConexionDB.getInstance().inicializarBaseDeDatos();
        
        SwingUtilities.invokeLater(() -> {
            ControladorPrincipal controlador = new ControladorPrincipal();
            controlador.iniciar();
            
        });
    }
}