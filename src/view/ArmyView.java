package view;

import java.awt.*;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;

import engine.Game;
import units.Army;
import units.Unit;

class LaySiegeToCityButtonListener extends MouseInputAdapter {
    private Army army;
    private GameView gameView;

    LaySiegeToCityButtonListener(Army army, GameView gameView) {
        this.army = army;
        this.gameView = gameView;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gameView.laySiegeToCity(army);
    }
}

class AttackCityButtonListener extends MouseInputAdapter {
    private Army army;
    private GameView gameView;

    AttackCityButtonListener(Army army, GameView gameView) {
        this.army = army;
        this.gameView = gameView;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gameView.attackCity(army);
    }
}

class TargetCityButtonListener extends MouseInputAdapter {

    private Army army;
    private GameView gameView;

    public TargetCityButtonListener(Army army, GameView gameView) {
        this.army = army;
        this.gameView = gameView;

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gameView.startTargetingCity(army);
    }
}

class InitiateArmyButtonListener extends MouseInputAdapter {
    private Unit unit;
    private GameView gameView;

    public InitiateArmyButtonListener(Unit unit, GameView gameView) {
        this.unit = unit;
        this.gameView = gameView;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gameView.initiateArmy(unit);
    }
}

class RelocateArmyButtonListener extends MouseInputAdapter {
    private Unit unit;
    private GameView gameView;

    public RelocateArmyButtonListener(Unit unit, GameView gameView) {
        this.unit = unit;
        this.gameView = gameView;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gameView.startRelocatingUnit(unit);
    }
}

public class ArmyView extends LimitedHeightPanel implements GameInformationView {

    private Army army;
    private LimitedHeightPanel statusContainer;
    private JLabel statusTitleLabel;
    private JLabel statusLabel;
    private LimitedHeightPanel targetContainer;
    private JLabel targetTitleLabel;
    private JLabel targetLabel;
    private LimitedHeightPanel distanceToTargetContainer;
    private JLabel distanceToTargetTitleLabel;
    private JLabel distanceToTargetLabel;
    private LimitedHeightPanel locationContainer;
    private JLabel locationTitleLabel;
    private JLabel locationLabel;
    private LimitedHeightPanel armyUnitsContainer;
    private LimitedHeightPanel buttonContainer;
    private JButton targetCityButton;
    private MaxWidthLabel unitsLabel;
    private GameView gameView;
    private GridBagLayout armyGrid;
    private Game game;
    private boolean insideCityView;
    private JButton attackCityButton;
    private JButton laySiegeToCityButton;

    public ArmyView(Army army, GameView gameView, Game game) {
        this.army = army;
        this.gameView = gameView;
        this.game = game;

        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        statusContainer = new LimitedHeightPanel();
        statusContainer.setAlignmentX(LEFT_ALIGNMENT);
        statusContainer.setOpaque(false);
        statusContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(statusContainer);

        statusTitleLabel = new JLabel("Status: ");
        statusTitleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        statusContainer.add(statusTitleLabel);

        statusLabel = new JLabel(getStatusString());
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        statusContainer.add(statusLabel);

        targetContainer = new LimitedHeightPanel();
        targetContainer.setAlignmentX(LEFT_ALIGNMENT);
        targetContainer.setOpaque(false);
        targetContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(targetContainer);

        targetTitleLabel = new JLabel("Target: ");
        targetTitleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        targetContainer.add(targetTitleLabel);

        targetLabel = new JLabel(army.getTarget());
        targetLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        targetContainer.add(targetLabel);

        distanceToTargetContainer = new LimitedHeightPanel();
        distanceToTargetContainer.setAlignmentX(LEFT_ALIGNMENT);
        distanceToTargetContainer.setOpaque(false);
        distanceToTargetContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(distanceToTargetContainer);

        distanceToTargetTitleLabel = new JLabel("Distance to target: ");
        distanceToTargetTitleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        distanceToTargetContainer.add(distanceToTargetTitleLabel);

        distanceToTargetLabel = new JLabel(army.getDistancetoTarget() + "");
        distanceToTargetLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        distanceToTargetContainer.add(distanceToTargetLabel);

        locationContainer = new LimitedHeightPanel();
        locationContainer.setAlignmentX(LEFT_ALIGNMENT);
        locationContainer.setOpaque(false);
        locationContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(locationContainer);

        locationTitleLabel = new JLabel("Location:");
        locationTitleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        locationContainer.add(locationTitleLabel);

        locationLabel = new JLabel(getLocationString());
        locationLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        locationContainer.add(locationLabel);

        if (!game.isDefendingArmy(army)) {
            GridLayout grid = new GridLayout();
            grid.setColumns(1);
            grid.setRows(3);
            
            buttonContainer = new LimitedHeightPanel();
            buttonContainer.setLayout(grid);
            buttonContainer.setOpaque(false);
            buttonContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
            buttonContainer.setAlignmentX(LEFT_ALIGNMENT);
            this.add(buttonContainer);

            targetCityButton = new JButton();
            targetCityButton.setText("Target City");
            targetCityButton.setBackground(new Color(13, 202, 240));
            targetCityButton.addMouseListener(new TargetCityButtonListener(army, gameView));
            buttonContainer.add(targetCityButton);

            attackCityButton = new JButton();
            attackCityButton.setText("Attack City");
            attackCityButton.setBackground(new Color(13, 202, 240));
            attackCityButton.addMouseListener(new AttackCityButtonListener(army, gameView));
            buttonContainer.add(attackCityButton);

            laySiegeToCityButton = new JButton();
            laySiegeToCityButton.setText("Lay Siege to City");
            laySiegeToCityButton.setBackground(new Color(13, 202, 240));
            laySiegeToCityButton.addMouseListener(new LaySiegeToCityButtonListener(army, gameView));
            buttonContainer.add(laySiegeToCityButton);
        }

        unitsLabel = new MaxWidthLabel();
        unitsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        this.add(unitsLabel);

        armyGrid = new GridBagLayout();
        armyUnitsContainer = new LimitedHeightPanel();
        armyUnitsContainer.setLayout(armyGrid);
        armyUnitsContainer.setOpaque(false);
        armyUnitsContainer.setAlignmentX(LEFT_ALIGNMENT);
        armyUnitsContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.add(armyUnitsContainer);
        updateGameInformation();

    }

    public boolean isInsideCityView() {
        return insideCityView;
    }

    public void setInsideCityView(boolean insideCityView) {
        this.insideCityView = insideCityView;
        this.statusContainer.setVisible(!insideCityView);
        this.locationContainer.setVisible(!insideCityView);
        if (insideCityView) {
            this.setBorder(BorderFactory.createEtchedBorder());
        } else {
            this.setBorder(BorderFactory.createEmptyBorder());
        }
    }

    public Army getArmy() {
        return army;
    }

    public void updateGameInformation() {
        this.statusLabel.setText(getStatusString());
        this.locationLabel.setText(getLocationString());

        if (army.getTarget().equals("")) {
            this.targetContainer.setVisible(false);
            this.distanceToTargetContainer.setVisible(false);
        } else {
            this.targetContainer.setVisible(true);
            this.distanceToTargetContainer.setVisible(true);
            this.targetLabel.setText(army.getTarget());
            this.distanceToTargetLabel.setText(army.getDistancetoTarget() + "");
        }

        unitsLabel.setText(army.getUnits().size() == 0 ? "No units" : "Units");
        unitsLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, unitsLabel.getPreferredSize().height));
        armyUnitsContainer.removeAll();
        if (army.getUnits().size() == 0) {
            armyUnitsContainer.setVisible(false);
            return;
        } else {
            armyUnitsContainer.setVisible(true);
        }
        AddUnitsLabel("Type", true, 0, 0, 3);
        AddUnitsLabel("Lvl", true, 1, 0, 1);
        AddUnitsLabel("Cnt/Max", true, 2, 0, 1);
        if (game.isDefendingArmy(army)) {
            AddUnitsLabel("", true, 3, 0, 1);
        }
        AddUnitsLabel("", true, 4, 0, 4);

        int y = 1;
        for (Unit unit : army.getUnits()) {
            AddUnitsLabel(unit.getClass().getSimpleName(), false, 0, y, 3);
            AddUnitsLabel(unit.getLevel() + "", false, 1, y, 1);
            AddUnitsLabel(unit.getCurrentSoldierCount() + "/" + unit.getMaxSoldierCount(), false, 2, y, 1);
            if (game.isDefendingArmy(army)) {
                AddUnitsButton("Init", 3, y, 1, new InitiateArmyButtonListener(unit, gameView));
            }
            AddUnitsButton("Relocate", 4, y, 4, new RelocateArmyButtonListener(unit, gameView));
            y++;
        }
    }

    private void AddUnitsLabel(String text, boolean bold, int gridx, int gridy, double weightx) {
        JLabel label = new JLabel(text);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy;
        c.weightx = weightx;
        c.fill = GridBagConstraints.BOTH;
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font(Font.SANS_SERIF, bold ? Font.BOLD : Font.PLAIN, 15));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        armyUnitsContainer.add(label, c);
    }

    private void AddUnitsButton(String text, int gridx, int gridy, double weightx, MouseInputAdapter listener) {
        JButton button = new JButton();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy;
        c.weightx = weightx;
        c.fill = GridBagConstraints.BOTH;
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setText(text);
        button.setBackground(new Color(13, 202, 240));
        button.addMouseListener(listener);
        armyUnitsContainer.add(button, c);
    }

    public String getLocationString() {
        return army.getCurrentLocation().equals("onRoad") ? "On Road" : army.getCurrentLocation();
    }

    public String getStatusString() {
        switch (this.army.getCurrentStatus()) {
            case BESIEGING:
                return "Besieging";
            case IDLE:
                return "Idle";
            case MARCHING:
                return "Marching";
            default:
                return "";
        }
    }

}
