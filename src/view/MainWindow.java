package view;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import engine.Game;


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
        this.remove(homeView);
        this.homeView = null;

        gameView = new GameView(game, this);
        this.add(gameView);
    }

    public void newGame() throws IOException {
        if (gameView != null) {
            this.gameView.setVisible(false);
            remove(gameView);
            this.gameView = null;
        }
        homeView = new HomeView(this);
        this.add(homeView);
    }

}
