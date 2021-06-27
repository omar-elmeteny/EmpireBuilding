package view;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;

import buildings.ArcheryRange;
import buildings.Barracks;
import buildings.Building;
import buildings.Farm;
import buildings.Market;
import buildings.MilitaryBuilding;
import buildings.Stable;
import engine.City;
import engine.Game;
import units.Army;

class BuildButtonListener extends MouseInputAdapter {
    private String type;
    private City city;
    private GameView gameView;

    public BuildButtonListener(String type, City city, GameView gameView) {
        this.type = type;
        this.city = city;
        this.gameView = gameView;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gameView.build(type, city);
    }
}

class UpgradeButtonListener extends MouseInputAdapter {
    private GameView gameView;
    private BuildingPanel buildingPanel;

    public UpgradeButtonListener(BuildingPanel buildingPanel, GameView gameView) {
        this.buildingPanel = buildingPanel;
        this.gameView = gameView;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gameView.upgradeBuilding(buildingPanel.getBuilding());
    }
}

class RecruitButtonListener extends MouseInputAdapter {
    private GameView gameView;
    private BuildingPanel buildingPanel;

    public RecruitButtonListener(BuildingPanel buildingPanel, GameView gameView) {
        this.buildingPanel = buildingPanel;
        this.gameView = gameView;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        MilitaryBuilding building = (MilitaryBuilding) buildingPanel.getBuilding();
        gameView.recruitUnit(building.getUnitType().getSimpleName(), buildingPanel.getCity());
    }
}

abstract class BuildingPanel extends LimitedHeightPanel implements GameInformationView {

    private City city;
    private String type;
    private Class<?> buildingClass;

    private LimitedHeightPanel buildingInfo;
    private JLabel buildingNameLabel;
    private JLabel buildingLevelLabel;
    private JLabel buildingCooldownLabel;

    private LimitedHeightPanel buttonsContainer;
    private JButton upgradeButton;
    private JButton buildButton;

    public BuildingPanel(City city, Class<?> buildingClass, GameView gameView) {
        super();
        this.city = city;
        this.type = buildingClass.getSimpleName();
        this.setOpaque(false);
        this.setBorder(BorderFactory.createEtchedBorder());
        this.buildingClass = buildingClass;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.buildingInfo = new LimitedHeightPanel();
        this.buildingInfo.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 4));
        this.buildingInfo.setOpaque(false);
        this.buildingInfo.setAlignmentX(LEFT_ALIGNMENT);
        this.add(buildingInfo);

        this.buildingNameLabel = new JLabel();
        this.buildingNameLabel.setText(getBuildingName());
        this.buildingNameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        buildingInfo.add(this.buildingNameLabel);

        this.buildingLevelLabel = new JLabel();
        this.buildingLevelLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        buildingInfo.add(this.buildingLevelLabel);

        this.buildingCooldownLabel = new JLabel();
        this.buildingCooldownLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        buildingInfo.add(this.buildingCooldownLabel);

        this.buttonsContainer = new LimitedHeightPanel();
        this.buttonsContainer.setOpaque(false);
        this.buttonsContainer.setAlignmentX(LEFT_ALIGNMENT);
        this.buttonsContainer.setLayout(new BoxLayout(this.buttonsContainer, BoxLayout.Y_AXIS));
        this.add(buttonsContainer);

        this.upgradeButton = new MaxWidthButton();
        this.upgradeButton.setBackground(new Color(13, 202, 240));
        this.upgradeButton.addMouseListener(new UpgradeButtonListener(this, gameView));
        this.upgradeButton.setAlignmentX(LEFT_ALIGNMENT);
        buttonsContainer.add(this.upgradeButton);

        this.buildButton = new MaxWidthButton();
        this.buildButton.setBackground(new Color(13, 202, 240));
        this.buildButton.addMouseListener(new BuildButtonListener(type, city, gameView));
        this.buildButton.setAlignmentX(LEFT_ALIGNMENT);
        buttonsContainer.add(this.buildButton);
    }

    public void updateGameInformation() {

        Building building = this.getBuilding();
        if (building == null) {
            this.buildButton.setVisible(true);
            this.buildingInfo.setVisible(false);
            this.upgradeButton.setVisible(false);

            int cost = getBuildingCost();
            this.buildButton.setText("Build " + this.getBuildingName() + " (" + cost + " gold)");
        } else {
            this.buildButton.setVisible(false);
            this.buildingInfo.setVisible(true);
            this.buildingLevelLabel.setText("Level " + building.getLevel());
            this.buildingCooldownLabel.setText(building.isCoolDown() ? "On Cooldown" : "");
            this.upgradeButton.setVisible(true);
            this.upgradeButton.setText("Upgrade (" + building.getUpgradeCost() + " gold)");
        }
    }

    public String getBuildingName() {
        try {
            Building b = (Building) this.buildingClass.getConstructor().newInstance();
            return b.getName();
        } catch (Exception ex) {
            return this.type;
        }
    }

    public int getBuildingCost() {
        try {
            Building b = (Building) this.buildingClass.getConstructor().newInstance();
            int cost = b.getCost();
            return cost;
        } catch (Exception ex) {
            return 0;
        }
    }

    public Building getBuilding() {
        for (Building b : city.getMilitaryBuildings()) {
            if (b.getClass() == this.buildingClass) {
                return b;
            }
        }

        for (Building b : city.getEconomicalBuildings()) {
            if (b.getClass() == this.buildingClass) {
                return b;
            }
        }
        return null;
    }

    public City getCity() {
        return city;
    }

    public Class<?> getBuildingClass() {
        return buildingClass;
    }

    public String getType() {
        return type;
    }

    protected JPanel getButtonsContainer() {
        return buttonsContainer;
    }
}

class MilitaryBuildingPanel extends BuildingPanel {
    private JButton recruitButton;

    MilitaryBuildingPanel(City city, Class<?> buildingClass, GameView gameView) {
        super(city, buildingClass, gameView);

        this.recruitButton = new MaxWidthButton();
        this.recruitButton.setBackground(new Color(13, 202, 240));
        this.recruitButton.setAlignmentX(LEFT_ALIGNMENT);
        recruitButton.addMouseListener(new RecruitButtonListener(this, gameView));

        getButtonsContainer().add(this.recruitButton);
    }

    public void updateGameInformation() {
        super.updateGameInformation();
        MilitaryBuilding building = (MilitaryBuilding) this.getBuilding();
        if (building == null) {
            this.recruitButton.setVisible(false);
        } else {
            this.recruitButton.setVisible(true);
            Class<?> unitType = building.getUnitType();
            this.recruitButton
                    .setText("Recruit " + unitType.getSimpleName() + " (" + building.getRecruitmentCost() + " gold)");
        }

    }
}

class EconomicalBuildingPanel extends BuildingPanel {
    EconomicalBuildingPanel(City city, Class<?> buildingClass, GameView gameView) {
        super(city, buildingClass, gameView);
    }
}

public class CityView extends LimitedHeightPanel implements GameInformationView {

    final private City city;
    private JLabel cityNameLabel;
    private JLabel militaryBuildingsLabel;
    private JLabel economicalBuildingsLabel;
    private ArrayList<BuildingPanel> buildingPanels;
    private JLabel defendingArmyLabel;
    private JLabel stationedArmiesLabel;
    private LimitedHeightPanel stationedArmiesContainer;
    private ArmyView defendingArmyView;
    private Game game;
    private GameView gameView;

    public CityView(City city, GameView gameView, Game game) {
        this.city = city;
        this.gameView = gameView;
        this.game = game;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        this.setBorder(new EmptyBorder(0, 5, 5, 5));

        cityNameLabel = new JLabel(city.getName());
        cityNameLabel.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        cityNameLabel.setAlignmentX(LEFT_ALIGNMENT);
        add(cityNameLabel);

        this.buildingPanels = new ArrayList<>();

        militaryBuildingsLabel = new JLabel("Military Buildings");
        militaryBuildingsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        militaryBuildingsLabel.setAlignmentX(LEFT_ALIGNMENT);
        add(militaryBuildingsLabel);

        addBuildingPanel(new MilitaryBuildingPanel(city, Barracks.class, gameView));
        addBuildingPanel(new MilitaryBuildingPanel(city, ArcheryRange.class, gameView));
        addBuildingPanel(new MilitaryBuildingPanel(city, Stable.class, gameView));

        add(Box.createRigidArea(new Dimension(0, 5)));
        economicalBuildingsLabel = new JLabel("Economical Buildings");
        economicalBuildingsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        economicalBuildingsLabel.setAlignmentX(LEFT_ALIGNMENT);
        add(economicalBuildingsLabel);

        addBuildingPanel(new EconomicalBuildingPanel(city, Farm.class, gameView));
        addBuildingPanel(new EconomicalBuildingPanel(city, Market.class, gameView));

        defendingArmyLabel = new JLabel("Defending Army");
        defendingArmyLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        defendingArmyLabel.setAlignmentX(LEFT_ALIGNMENT);
        add(defendingArmyLabel);

        defendingArmyView = new ArmyView(city.getDefendingArmy(), gameView, game);
        defendingArmyView.setInsideCityView(true);
        defendingArmyView.setAlignmentX(LEFT_ALIGNMENT);
        add(defendingArmyView);

        stationedArmiesLabel = new JLabel("Stationed Armies");
        stationedArmiesLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        stationedArmiesLabel.setAlignmentX(LEFT_ALIGNMENT);
        add(stationedArmiesLabel);

        stationedArmiesContainer = new LimitedHeightPanel();
        stationedArmiesContainer.setLayout(new BoxLayout(stationedArmiesContainer, BoxLayout.Y_AXIS));
        stationedArmiesContainer.setOpaque(false);
        stationedArmiesContainer.setAlignmentX(LEFT_ALIGNMENT);

        add(stationedArmiesContainer);

        updateStationedArmies();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(330, d.height);
    }

    private void addBuildingPanel(BuildingPanel buildingPanel) {
        buildingPanel.setAlignmentX(LEFT_ALIGNMENT);
        add(buildingPanel);
        buildingPanels.add(buildingPanel);
        buildingPanel.updateGameInformation();
        add(Box.createRigidArea(new Dimension(0, 5)));
    }

    public City getCity() {
        return city;
    }

    public void updateGameInformation() {
        for (BuildingPanel buildingPanel : buildingPanels) {
            buildingPanel.updateGameInformation();
        }
        defendingArmyView.updateGameInformation();
        updateStationedArmies();
    }

    private void updateStationedArmies() {
        stationedArmiesContainer.removeAll();
        for (Army army : game.getPlayer().getControlledArmies()) {
            if (!army.getCurrentLocation().equals(city.getName()))
                continue;

            ArmyView armyView = new ArmyView(army, gameView, game);
            armyView.setInsideCityView(true);
            stationedArmiesContainer.add(armyView);
        }
        this.stationedArmiesLabel.setVisible(this.stationedArmiesContainer.getComponentCount() != 0);
        stationedArmiesContainer.setVisible(this.stationedArmiesContainer.getComponentCount() != 0);
    }
}
