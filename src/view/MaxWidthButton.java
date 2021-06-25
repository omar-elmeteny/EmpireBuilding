package view;

import java.awt.Dimension;
import javax.swing.JButton;

public class MaxWidthButton extends JButton {
    
    @Override
    public Dimension getMaximumSize() {
        Dimension d = getPreferredSize();
        return new Dimension(Integer.MAX_VALUE, d.height);
    }
}
