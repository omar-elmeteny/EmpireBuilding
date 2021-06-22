package view;

import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;

public class MapView extends JPanel{
    
    private BufferedImage image;

    public MapView() throws IOException{
        image = ImageIO.read(new File("map.png"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int newWidth;
        int newHeight;

        if (panelWidth * imageHeight < panelHeight * imageWidth) {
            newWidth = panelWidth;
            newHeight = panelWidth * imageHeight / imageWidth;
        } else {
            newHeight = panelHeight;
            newWidth = panelHeight * imageWidth / imageHeight;
        }
        g.drawImage(image, 0, 0, newWidth, newHeight, this);            
    }

}
