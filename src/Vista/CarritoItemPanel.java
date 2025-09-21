package Vista;

import controlador.ControladorPrincipal;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import modelo.CarritoItem;
import modelo.Producto;

public class CarritoItemPanel extends JPanel {

    public CarritoItemPanel(CarritoItem item, ControladorPrincipal controlador) {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(5, 10, 5, 10));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        Producto producto = item.getProducto();
        
        // Nombre del producto
        JLabel nombreLabel = new JLabel(producto.getNombre());
        nombreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Panel de controles (cantidad y botones)
        JPanel controlesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton botonMenos = new JButton("-");
        JLabel cantidadLabel = new JLabel(String.valueOf(item.getCantidad()));
        cantidadLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JButton botonMas = new JButton("+");
        JButton botonEliminar = new JButton("X");
        
        // Estilo de los botones
        Dimension buttonSize = new Dimension(45, 25);
        botonMenos.setPreferredSize(buttonSize);
        botonMas.setPreferredSize(buttonSize);
        botonEliminar.setPreferredSize(buttonSize);

        // Acciones de los botones
        botonMas.addActionListener(e -> controlador.incrementarCantidad(producto.getNombre()));
        botonMenos.addActionListener(e -> controlador.decrementarCantidad(producto.getNombre()));
        botonEliminar.addActionListener(e -> controlador.eliminarDelCarrito(producto.getNombre()));

        controlesPanel.add(botonMenos);
        controlesPanel.add(cantidadLabel);
        controlesPanel.add(botonMas);
        controlesPanel.add(new JLabel(String.format("$%.2f", item.getSubtotal())));
        controlesPanel.add(botonEliminar);

        add(nombreLabel, BorderLayout.CENTER);
        add(controlesPanel, BorderLayout.EAST);
    }
}