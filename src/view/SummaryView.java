package view;

import javax.swing.JLabel;
import javax.swing.JPanel;

import engine.Game;

import java.awt.*;

public class SummaryView extends JPanel{
    
    private JLabel nameTitleLabel;
    private JLabel nameLabel;
    private JLabel treasuryTitleLabel;
    private JLabel treasuryLabel;
    private JLabel foodTitleLabel;
    private JLabel foodLabel;
    private JLabel citiesTitleLabel;
    private JLabel citiesLabel;
    private JLabel armiesTitleLabel;
    private JLabel armiesLabel;
    private JLabel turnsTitleLabel;
    private JLabel turnsLabel;
    private Game game;

    public SummaryView(Game game) {
        super();
        this.game = game;

        setLayout(new FlowLayout());
        setBackground(new Color(108, 117, 125));
        Font titleFont = new Font(Font.SANS_SERIF, Font.BOLD, 15);
        Color titleColor = Color.WHITE;
        Font infoFont = new Font(Font.SANS_SERIF, Font.PLAIN, 15);
        Color infoColor = Color.WHITE;
        
        nameTitleLabel = new JLabel("Name:");
        nameTitleLabel.setFont(titleFont);
        nameTitleLabel.setForeground(titleColor);
        this.add(nameTitleLabel);

        nameLabel = new JLabel(game.getPlayer().getName());
        nameLabel.setFont(infoFont);
        nameLabel.setForeground(infoColor);
        this.add(nameLabel);

        treasuryTitleLabel = new JLabel("Treasury:");
        treasuryTitleLabel.setFont(titleFont);
        treasuryTitleLabel.setForeground(titleColor);
        this.add(treasuryTitleLabel);

        treasuryLabel = new JLabel(game.getPlayer().getTreasury() + "");
        treasuryLabel.setFont(infoFont);
        treasuryLabel.setForeground(infoColor);
        this.add(treasuryLabel);

        foodTitleLabel = new JLabel("Food:");
        foodTitleLabel.setFont(titleFont);
        foodTitleLabel.setForeground(titleColor);
        this.add(foodTitleLabel);

        foodLabel = new JLabel(game.getPlayer().getFood() + "");
        foodLabel.setFont(infoFont);
        foodLabel.setForeground(infoColor);
        this.add(foodLabel);

        citiesTitleLabel = new JLabel("Controlled cities:");
        citiesTitleLabel.setFont(titleFont);
        citiesTitleLabel.setForeground(titleColor);
        this.add(citiesTitleLabel);

        citiesLabel = new JLabel(game.getPlayer().getControlledCities().size() + "");
        citiesLabel.setFont(infoFont);
        citiesLabel.setForeground(infoColor);
        this.add(citiesLabel);

        armiesTitleLabel = new JLabel("Controlled armies:");
        armiesTitleLabel.setFont(titleFont);
        armiesTitleLabel.setForeground(titleColor);
        this.add(armiesTitleLabel);

        armiesLabel = new JLabel(game.getPlayer().getControlledArmies().size() + "");
        armiesLabel.setFont(infoFont);
        armiesLabel.setForeground(infoColor);
        this.add(armiesLabel);

        turnsTitleLabel = new JLabel("Turn:");
        turnsTitleLabel.setFont(titleFont);
        turnsTitleLabel.setForeground(titleColor);
        this.add(turnsTitleLabel);

        turnsLabel = new JLabel(game.getCurrentTurnCount() + "/" + game.getMaxTurnCount());
        turnsLabel.setFont(infoFont);
        turnsLabel.setForeground(infoColor);
        this.add(turnsLabel);

    }

    public void updateSummary() {
        treasuryLabel.setText(game.getPlayer().getTreasury() + "");
        foodLabel.setText(game.getPlayer().getFood() + "");
        citiesLabel.setText(game.getPlayer().getControlledCities().size() + "");
        armiesLabel.setText(game.getPlayer().getControlledArmies().size() + "");
        turnsLabel.setText(game.getCurrentTurnCount() + "/" + game.getMaxTurnCount());
    }

}
