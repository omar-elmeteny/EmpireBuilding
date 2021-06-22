package view;

import javax.swing.JPanel;

import engine.City;
import engine.Game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.*;

public class MapView extends JPanel{
    
    private BufferedImage image;
    private Game game;

    public MapView(Game game) throws IOException{
        this.game = game;
        image = ImageIO.read(new File("map.png"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension mapSize = drawMap(g);            
        for (int i = 0; i < game.getAvailableCities().size(); i++) {
            City city = game.getAvailableCities().get(i);
            drawCity(city, mapSize, g);
        }
    }

    private void drawCity(City city, Dimension mapSize, Graphics g) {
        Point p = getCityCoordinates(city);
        int x = (int)(p.getX() * mapSize.getWidth() / image.getWidth());
        int y = (int)(p.getY() * mapSize.getHeight() / image.getHeight());
        Color c = isControlledCity(city) ? Color.BLUE : Color.RED;
        g.setColor(c);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, (int)(30 * mapSize.getWidth() / image.getWidth())));
        g.drawString(city.getName(), x, y);
    }

    private boolean isControlledCity(City city) {
        for (City c: game.getPlayer().getControlledCities()) {
            if (city == c) return true;
        }
        return false;
    }

    private Point getCityCoordinates(City city) {
        if (city.getName().equals("Sparta")) {
            return new Point(650, 420);
        } else if (city.getName().equals("Cairo")) {
            return new Point(900, 770);
        } else {
            return new Point(300, 208);
        }
    }

    private Dimension drawMap(Graphics g) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int mapWidth;
        int mapHeight;

        if (panelWidth * imageHeight < panelHeight * imageWidth) {
            mapWidth = panelWidth;
            mapHeight = panelWidth * imageHeight / imageWidth;
        } else {
            mapHeight = panelHeight;
            mapWidth = panelHeight * imageWidth / imageHeight;
        }
        g.drawImage(image, 0, 0, mapWidth, mapHeight, this);
        return new Dimension(mapWidth, mapHeight);
    }

}
