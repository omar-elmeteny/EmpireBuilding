package view;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import engine.Game;
import units.Archer;
import units.Army;
import units.Cavalry;
import units.Infantry;
import units.Status;

public class MainWindow extends JFrame {

    private HomeView homeView;
    private GameView gameView;
    private Game game;

    public MainWindow() throws IOException {
        super();
        this.setTitle("Empire Building");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1500, 800);

        homeView = new HomeView(this);
        this.add(homeView);
    }

    public void startGame() throws IOException {
        if (homeView.getSelectedCity() == null) {
            JOptionPane.showMessageDialog(this, "Please select a city.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (homeView.getPlayerName().length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter your name.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        game = new Game(homeView.getPlayerName(), homeView.getSelectedCity());
        homeView.setVisible(false);

        // THIS IS FOR TESTING ONLY DELETE LATER
        Archer archer = new Archer(2, 10, 4, 3, 1);
        game.getPlayer().initiateArmy(game.getPlayer().getControlledCities().get(0), archer);
        
        Archer archer2 = new Archer(2, 10, 4, 3, 1);
        game.getPlayer().initiateArmy(game.getPlayer().getControlledCities().get(0), archer2);
        Army army = game.getPlayer().getControlledArmies().get(1);
        Infantry infantry = new Infantry(2, 10, 3, 1, 1);
        army.getUnits().add(infantry);
        infantry.setParentArmy(army);
        Cavalry cavalry = new Cavalry(2, 10, 3, 1, 1);
        army.getUnits().add(cavalry);
        cavalry.setParentArmy(army);
        army.setStartingCity(army.getCurrentLocation());
        game.targetCity(army, "Rome");
        army.setDistancetoTarget(army.getDistancetoTarget() / 2);
        army.setCurrentLocation("onRoad");
        army.setCurrentStatus(Status.MARCHING);
        // DELETE ABOVE THIS

        gameView = new GameView(game);
        this.add(gameView);
    }

}
