package Vista;

import modelo.Producto;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProductoPanel extends JPanel {

    public ProductoPanel(Producto producto) {
        setLayout(new BorderLayout(15, 0));
        
        // Borde con padding
        Border padding = BorderFactory.createEmptyBorder(15, 15, 15, 15);
        Border line = BorderFactory.createLineBorder(new Color(220, 220, 220));
        setBorder(BorderFactory.createCompoundBorder(line, padding));
        
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));
        setBackground(Color.WHITE);

        // Nombre del producto
        JLabel nombreLabel = new JLabel(producto.getNombre());
        nombreLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        // Precio del producto
        JLabel precioLabel = new JLabel(String.format("$%.2f", producto.getPrecio()));
        precioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        precioLabel.setForeground(new Color(0, 120, 0)); // Color verde

        add(nombreLabel, BorderLayout.CENTER);
        add(precioLabel, BorderLayout.EAST);

        // --- Efecto Hover ---
        Color originalColor = getBackground();
        Color hoverColor = new Color(240, 248, 255); // AliceBlue
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(originalColor);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
}