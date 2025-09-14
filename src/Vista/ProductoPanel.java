package Vista;

import modelo.Producto;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class ProductoPanel extends JPanel {

    public ProductoPanel(Producto producto) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new CompoundBorder(
            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
            new EmptyBorder(10, 10, 10, 10)
        ));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        JLabel nombreLabel = new JLabel(producto.getNombre());
        nombreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel precioLabel = new JLabel("$" + String.format("%.2f", producto.getPrecio()));
        precioLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        add(nombreLabel);
        add(Box.createHorizontalGlue());
        add(precioLabel);
    }
}