package view;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;

import engine.Game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class StartGameButtonListener extends MouseInputAdapter {

    private MainWindow mainWindow;

    public StartGameButtonListener(MainWindow mainWindow) {
        super();
        this.mainWindow = mainWindow;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            mainWindow.startGame();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(mainWindow, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}

class SelectCityButtonListener extends MouseInputAdapter {

    private HomeView homeView;

    public SelectCityButtonListener(HomeView homeView) {
        this.homeView = homeView;

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        JButton button = (JButton) e.getComponent();
        String cityName = button.getText();
        if (cityName.equals(homeView.getSelectedCity()))
            homeView.setSelectedCity(null);
        else
            homeView.setSelectedCity(cityName);
    }
}

public class HomeView extends JPanel {

    private JLabel titleLabel;
    private JLabel teamLabel;
    private JLabel messageLabel;
    private LimitedHeightPanel nameContainer;
    private JLabel nameLabel;
    private JTextField nameField;
    private JPanel citiesContainer;
    private JButton[] cityButtons;
    private JButton startGameButton;
    private JPanel startButtonContainer;
    private String selectedCity;

    public HomeView(MainWindow mainWindow) throws IOException {
        super();
        this.setBackground(new Color(255, 228, 196));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(0, 5, 5, 5));

        titleLabel = new JLabel("Empire Building");
        titleLabel.setFont(new Font(Font.SERIF, Font.BOLD, 40));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(titleLabel);

        teamLabel = new JLabel("Team 100");
        teamLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        teamLabel.setHorizontalAlignment(SwingConstants.CENTER);
        teamLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(teamLabel);

        this.add(Box.createRigidArea(new Dimension(0, 5)));

        messageLabel = new JLabel(
                "Please enter your name and select a city then click \"Start Game\" button to begin.");
        messageLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(messageLabel);

        this.add(Box.createRigidArea(new Dimension(0, 5)));

        nameContainer = new LimitedHeightPanel();
        nameContainer.setLayout(new BorderLayout());
        nameContainer.setOpaque(false);
        this.add(nameContainer);

        nameLabel = new JLabel("Your Name: ");
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        nameContainer.add(nameLabel, BorderLayout.WEST);

        nameField = new JTextField();
        nameField.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        nameContainer.add(nameField, BorderLayout.CENTER);

        this.add(Box.createRigidArea(new Dimension(0, 5)));

        ArrayList<String> cityNames = Game.getCityNames();
        citiesContainer = new JPanel();
        GridLayout citiesGrid = new GridLayout();
        citiesGrid.setRows(1);
        citiesGrid.setColumns(cityNames.size());
        citiesGrid.setHgap(2);
        citiesContainer.setLayout(citiesGrid);
        cityButtons = new JButton[cityNames.size()];
        for (int i = 0; i < cityNames.size(); i++) {
            cityButtons[i] = new JButton();
            JButton button = cityButtons[i];
            button.setBackground(new Color(86, 94, 100));
            button.setForeground(Color.WHITE);
            button.setFont(new Font(Font.SERIF, Font.BOLD, 40));
            button.setText(cityNames.get(i));
            button.addMouseListener(new SelectCityButtonListener(this));
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setHorizontalTextPosition(SwingConstants.CENTER);

            BufferedImage image = ImageIO.read(new File(cityNames.get(i).toLowerCase() + ".png"));
            Image scaledImage = image.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImage);

            button.setIcon(icon);
            citiesContainer.add(button);
        }
        this.add(citiesContainer);

        this.add(Box.createRigidArea(new Dimension(0, 5)));

        startButtonContainer = new LimitedHeightPanel();
        startButtonContainer.setLayout(new BorderLayout());
        this.add(startButtonContainer);
        startGameButton = new JButton();
        startGameButton.setBackground(new Color(20, 108, 76));
        startGameButton.setForeground(Color.WHITE);
        startGameButton.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        startGameButton.setText("Start Game");
        startGameButton.addMouseListener(new StartGameButtonListener(mainWindow));
        startButtonContainer.add(startGameButton, BorderLayout.CENTER);

    }

    public String getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(String selectedCity) {
        this.selectedCity = selectedCity;
        for (int i = 0; i < cityButtons.length; i++) {
            JButton button = cityButtons[i];
            String cityName = button.getText();
            if (cityName.equals(selectedCity))
                button.setBackground(new Color(20, 108, 76));
            else
                button.setBackground(new Color(86, 94, 100));
        }
    }

    public String getPlayerName() {
        String name = this.nameField.getText();
        if (name == null)
            return "";
        return name.trim();
    }

}
