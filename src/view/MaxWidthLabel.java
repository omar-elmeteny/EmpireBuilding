package view;

import java.awt.Dimension;

import javax.swing.JLabel;

public class MaxWidthLabel extends JLabel {
    
    @Override
    public Dimension getMaximumSize() {
        Dimension d = getPreferredSize();
        return new Dimension(Integer.MAX_VALUE, d.height);
    }
}
