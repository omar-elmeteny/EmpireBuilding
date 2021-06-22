package view;

import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.event.MouseInputAdapter;

public class StartGameButtonListener extends MouseInputAdapter{
    

    private MainWindow mainWindow;

    public StartGameButtonListener(MainWindow mainWindow) {
        super();
        this.mainWindow = mainWindow;
    }

    @Override
    public void mouseClicked(MouseEvent e){
        try{
            mainWindow.startGame();
        }
        catch(IOException ex){
            JOptionPane.showMessageDialog(mainWindow, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
