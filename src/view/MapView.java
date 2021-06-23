package view;

import javax.swing.JButton;
import javax.swing.JPanel;

import engine.City;
import engine.Distance;
import engine.Game;
import units.Archer;
import units.Army;
import units.Cavalry;
import units.Infantry;
import units.Status;
import units.Unit;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.*;
import java.awt.event.*;

final class ButtonContainerKey {
    private final String from;
    private final String to;
    private final int distance;

    public ButtonContainerKey(String from, String to, int distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != ButtonContainerKey.class) {
            return false;
        }
        ButtonContainerKey other = (ButtonContainerKey) obj;
        if (other.from.equals(from) && other.to.equals(to)) {
            return other.distance == distance;
        }
        if (other.to.equals(from) && other.from.equals(to)) {
            return other.distance + distance == 10;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.from.hashCode() ^ this.to.hashCode() ^ distance ^ (10 - distance);
    }
}

class ArmyButton extends JButton {
    public static final int ButtonDefaultSize = 30;
    private final Army army;
    private MapView mapView;
    private static BufferedImage archerImage;
    private static BufferedImage cavalryImage;
    private static BufferedImage infantryImage;

    public ArmyButton(Army army, MapView mapView) {
        super();
        this.mapView = mapView;
        setOpaque(false);
        setContentAreaFilled(false);
        setText(null);
        this.army = army;

        try {
            if (archerImage == null) {
                archerImage = ImageIO.read(new File("archer.png"));
            }
            if (cavalryImage == null) {
                cavalryImage = ImageIO.read(new File("cavalry.png"));
            }
            if (infantryImage == null) {
                infantryImage = ImageIO.read(new File("infantry.png"));
            }
        } catch (IOException ex) {

        }
    }

    public Army getArmy() {
        return army;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 0;
        int y = 0;
        int unitTypes = getUnitTypeCount();
        if (unitTypes == 0) {
            return;
        }
        int width = this.getWidth() / unitTypes;
        int height = this.getHeight();

        if (getArmyHasUnitOfType(Archer.class)) {
            g.drawImage(archerImage, x, y, width, height, this);
            x += width;
        }
        if (getArmyHasUnitOfType(Cavalry.class)) {
            g.drawImage(cavalryImage, x, y, width, height, this);
            x += width;
        }
        if (getArmyHasUnitOfType(Infantry.class)) {
            g.drawImage(infantryImage, x, y, width, height, this);
        }
    }

    public int getUnitTypeCount() {
        int c = 0;
        if (getArmyHasUnitOfType(Archer.class)) {
            c++;
        }
        if (getArmyHasUnitOfType(Cavalry.class)) {
            c++;
        }
        if (getArmyHasUnitOfType(Infantry.class)) {
            c++;
        }
        return c;
    }

    private boolean getArmyHasUnitOfType(Class<?> cls) {
        for (Unit u : army.getUnits()) {
            if (cls == u.getClass()) {
                return true;
            }
        }
        return false;
    }

    public void updateArmyButton() {
        Dimension mapDimensions = mapView.getMapDimensions();
        Dimension imageDimension = mapView.getImageDimensions();
        int size = ButtonDefaultSize * mapDimensions.width / imageDimension.width;
        this.setPreferredSize(new Dimension(getUnitTypeCount() * size, size));
    }
}

class ArmyButtonContainerContainerListener extends ContainerAdapter {

    private MapView mapView;
    private ArmyButtonContainer container;

    public ArmyButtonContainerContainerListener(MapView mapView, ArmyButtonContainer container) {
        this.mapView = mapView;
        this.container = container;
    }

    @Override
    public void componentRemoved(ContainerEvent e) {
        if (container.getComponentCount() == 0) {
            mapView.removeButtonContainer(container);
        } else {
            container.updateBounds();
        }
    }

    @Override
    public void componentAdded(ContainerEvent e) {
        container.updateBounds();
    }

}

class ArmyButtonContainer extends JPanel {
    private ButtonContainerKey key;
    private MapView mapView;

    public ArmyButtonContainer(MapView mapView, ButtonContainerKey key) {
        this.mapView = mapView;
        this.key = key;
        this.addContainerListener(new ArmyButtonContainerContainerListener(mapView, this));
        this.setOpaque(false);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    }

    public int getIconCount() {
        int c = 0;
        for (int i = 0; i < this.getComponentCount(); i++) {
            ArmyButton button = (ArmyButton) getComponent(i);
            c += button.getUnitTypeCount();
        }
        return c;
    }

    public void updateBounds() {
        Dimension mapDimensions = mapView.getMapDimensions();
        Dimension imageDimension = mapView.getImageDimensions();
        int buttonSize = ArmyButton.ButtonDefaultSize * mapDimensions.width / imageDimension.width;
        int width = this.getIconCount() * buttonSize;
        int height = buttonSize;
        int x;
        int y;

        String fromCity = key.getFrom();
        String toCity = key.getTo();
        Point fromPoint = mapView.getCityCoordinates(fromCity);
        int xCity = fromPoint.x * mapDimensions.width / imageDimension.width;
        int yCity = fromPoint.y * mapDimensions.height / imageDimension.height;
        if (fromCity.equals(toCity)) {
            if (fromCity.equals("Cairo")) {
                x = xCity - width;
                y = yCity;
            } else if (fromCity.equals("Sparta")) {
                x = xCity;
                y = yCity - height;
            } else {
                x = xCity;
                y = yCity - height;
            }
        } else {
            Point toPoint = mapView.getCityCoordinates(toCity);
            int xCity2 = toPoint.x * mapDimensions.width / imageDimension.width;
            int yCity2 = toPoint.y * mapDimensions.height / imageDimension.height;

            x = xCity + (xCity2 - xCity) * (key.getDistance() + 2) / 12;
            y = yCity + (yCity2 - yCity) * (key.getDistance() + 2) / 12;
        }
        setBounds(x, y, width, height);
    }

    public ButtonContainerKey getKey() {
        return this.key;
    }
}

class MapComponentListener extends ComponentAdapter {

    private MapView mapView;

    public MapComponentListener(MapView mapView) {
        super();
        this.mapView = mapView;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        mapView.updateCityButtons();
        mapView.updateArmyButtons();
    }
}

public class MapView extends JPanel {

    private BufferedImage image;
    private Game game;
    private JButton[] cityButtons;
    private Hashtable<Army, ArmyButton> armyButtons;
    private Hashtable<ButtonContainerKey, ArmyButtonContainer> armyButtonContainers;

    public MapView(Game game) throws IOException {
        this.game = game;
        this.setLayout(null);
        image = ImageIO.read(new File("map.png"));

        cityButtons = new JButton[game.getAvailableCities().size()];
        for (int i = 0; i < game.getAvailableCities().size(); i++) {
            City city = game.getAvailableCities().get(i);
            cityButtons[i] = new JButton();
            cityButtons[i].setText(city.getName());
            cityButtons[i].setOpaque(false);
            cityButtons[i].setContentAreaFilled(false);
            this.add(cityButtons[i]);
            updateCityButton(cityButtons[i]);
        }

        this.addComponentListener(new MapComponentListener(this));
        armyButtons = new Hashtable<>();
        armyButtonContainers = new Hashtable<>();
        updateArmyButtons();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMap(g);
    }

    public void updateCityButtons() {
        for (JButton button : cityButtons) {
            updateCityButton(button);
        }
    }

    public void updateArmyButtons() {
        for (Army army : this.game.getPlayer().getControlledArmies()) {

            ArmyButton armyButton = armyButtons.get(army);
            if (armyButton == null) {
                armyButton = new ArmyButton(army, this);
                armyButtons.put(army, armyButton);
            }
            ButtonContainerKey key;

            if (army.getCurrentStatus() == Status.MARCHING) {
                int totalDistance = getDistanceBetweenCities(army.getStartingCity(), army.getTarget());
                key = new ButtonContainerKey(army.getStartingCity(), army.getTarget(),
                        army.getDistancetoTarget() * 10 / totalDistance);
            } else {
                key = new ButtonContainerKey(army.getCurrentLocation(), army.getCurrentLocation(), 0);
            }

            ArmyButtonContainer container = armyButtonContainers.get(key);
            if (container == null) {
                container = new ArmyButtonContainer(this, key);
                armyButtonContainers.put(key, container);
                this.add(container);
            }
            ArmyButtonContainer oldContainer = (ArmyButtonContainer) armyButton.getParent();
            if (oldContainer != container) {
                if (oldContainer != null) {
                    oldContainer.remove(armyButton);
                }
                container.add(armyButton);
            }
        }

        ArmyButton[] currentButtons = new ArmyButton[armyButtons.size()];
        currentButtons = armyButtons.values().toArray(currentButtons);
        for (ArmyButton armyButton : currentButtons) {
            Army army = armyButton.getArmy();
            boolean found = this.game.getPlayer().getControlledArmies().contains(army);
            if (!found) {
                armyButtons.remove(army);
                ArmyButtonContainer container = (ArmyButtonContainer) armyButton.getParent();
                if (container != null) {
                    container.remove(armyButton);
                }
            } else {
                armyButton.updateArmyButton();
            }
        }

        ArmyButtonContainer[] currentContainers = new ArmyButtonContainer[armyButtonContainers.size()];
        currentContainers = armyButtonContainers.values().toArray(currentContainers);
        for (ArmyButtonContainer container : currentContainers) {
            container.updateBounds();
        }
    }

    public int getDistanceBetweenCities(String fromCity, String toCity) {
        for (Distance d : this.game.getDistances()) {
            if (d.getFrom().equals(fromCity) && d.getTo().equals(toCity)
                    || d.getFrom().equals(toCity) && d.getTo().equals(fromCity)) {
                return d.getDistance();
            }
        }
        return 1;
    }

    public void removeButtonContainer(ArmyButtonContainer container) {
        this.armyButtonContainers.remove(container.getKey());
        this.remove(container);
    }

    private void updateCityButton(JButton button) {
        String cityName = button.getText();
        Color c = isControlledCity(cityName) ? Color.BLUE : Color.RED;
        button.setForeground(c);
        Point p = getCityCoordinates(cityName);
        Dimension d = getMapDimensions();
        int x = p.x * d.width / image.getWidth();
        int y = p.y * d.height / image.getHeight();

        int width = 180 * d.width / image.getWidth();
        int height = 40 * d.height / image.getHeight();
        button.setBounds(x, y, width, height);

        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25 * d.width / image.getWidth()));
    }

    private boolean isControlledCity(String cityName) {
        for (City c : game.getPlayer().getControlledCities()) {
            if (c.getName().equals(cityName))
                return true;
        }
        return false;
    }

    public Point getCityCoordinates(String cityName) {
        if (cityName.equals("Sparta")) {
            return new Point(590, 410);
        } else if (cityName.equals("Cairo")) {
            return new Point(810, 745);
        } else {
            return new Point(270, 200);
        }
    }

    private void drawMap(Graphics g) {
        Dimension d = getMapDimensions();
        g.drawImage(image, 0, 0, d.width, d.height, this);
    }

    public Dimension getImageDimensions() {
        return new Dimension(image.getWidth(), image.getHeight());
    }

    public Dimension getMapDimensions() {
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
        Dimension d = new Dimension(mapWidth, mapHeight);
        return d;
    }

}
