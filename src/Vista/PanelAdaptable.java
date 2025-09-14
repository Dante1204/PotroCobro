package Vista;

import javax.swing.*;
import java.awt.*;

public class PanelAdaptable extends JPanel implements Scrollable {

    public PanelAdaptable() {
        super(new FlowLayout(FlowLayout.LEFT, 10, 10));
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        // ESTE DEBE DEVOLVER 'true' PARA FORZAR EL SALTO DE LÍNEA.
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        // ¡¡ESTE ES EL IMPORTANTE, DEBE DEVOLVER 'false'!!
        // Esto permite que el panel crezca verticalmente y activa el scroll.
        return false;
    }
}