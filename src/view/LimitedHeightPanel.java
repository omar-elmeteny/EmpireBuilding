package view;

import java.awt.Dimension;

import javax.swing.JPanel;

public class LimitedHeightPanel extends JPanel {

    @Override
    public Dimension getMaximumSize() {
        Dimension d = getPreferredSize();
        return new Dimension(Integer.MAX_VALUE, d.height);
    }
}
