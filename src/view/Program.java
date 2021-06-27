package view;

import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Program {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
    	UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
    	MainWindow mainWindow = new MainWindow();
        mainWindow.setSize(1300, 800);
        mainWindow.setVisible(true);
    }
}
