package view;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import buildings.Building;

import java.awt.*;
import java.io.IOException;

import engine.City;
import engine.Game;
import exceptions.BuildingInCoolDownException;
import exceptions.MaxCapacityException;
import exceptions.MaxLevelException;
import exceptions.MaxRecruitedException;
import exceptions.NotEnoughGoldException;
import units.Army;
import units.Unit;

public class GameView extends JPanel implements GameInformationView {

    private SummaryView summaryView;
    private MapView mapView;
    private JComponent sideView;
    private Game game;
    private JButton endTurnButton;
    private JLabel targetingLabel;
    private JPanel bottomContainer;
    private Army targetingCity;
    private Unit relocatingUnit;

    public GameView(Game game) throws IOException {
        super();

        this.game = game;
        setLayout(new BorderLayout());
        this.setBackground(new Color(255, 228, 196));
        summaryView = new SummaryView(game);
        this.add(summaryView, BorderLayout.NORTH);

        mapView = new MapView(game, this);
        this.add(mapView, BorderLayout.CENTER);

        bottomContainer = new JPanel();
        bottomContainer.setLayout(new BorderLayout());
        this.add(bottomContainer, BorderLayout.SOUTH);

        endTurnButton = new JButton();
        endTurnButton.setText("End Turn");
        endTurnButton.setBackground(new Color(20, 108, 76));
        endTurnButton.setForeground(Color.WHITE);
        endTurnButton.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        bottomContainer.add(endTurnButton, BorderLayout.CENTER);

        targetingLabel = new JLabel();
        targetingLabel.setBackground(new Color(86, 94, 100));
        targetingLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        targetingLabel.setForeground(Color.WHITE);
        targetingLabel.setOpaque(true);
        targetingLabel.setVisible(false);
        bottomContainer.add(targetingLabel, BorderLayout.NORTH);
    }

    public void startRelocatingUnit(Unit unit) {
        relocatingUnit = unit;
        targetingCity = null;
        targetingLabel.setText("Please click on the map to select the army you want to relocate this " + unit.getClass().getSimpleName() + " to.");
        targetingLabel.setVisible(true);
    }

    public void armyClicked(Army army) {
        if (relocatingUnit != null) {
            endRelocatingUnit(army);
        } else {
            showArmyView(army);
        }
    }

    private void endRelocatingUnit(Army army) {
        Army originalArmy = relocatingUnit.getParentArmy();
        if (originalArmy == army) {
            return;
        } 
        try {
            army.relocateUnit(relocatingUnit);
            if (originalArmy.getUnits().size() == 0) {
                game.getPlayer().getControlledArmies().remove(originalArmy);
                if (isSideViewArmyView(originalArmy)) {
                    removeSideView();
                }
            }
            updateGameInformation();
        } catch (MaxCapacityException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Command failed", JOptionPane.WARNING_MESSAGE);
            
        }
        relocatingUnit = null;
        targetingCity = null;
        targetingLabel.setVisible(false);
    }

    private void showArmyView(Army army) {
        if (isSideViewArmyView(army)) {
            return;
        }
        removeSideView();
        sideView = new ArmyView(army, this, game);
        add(sideView, BorderLayout.EAST);
        validate();
    }

    public void initiateArmy(Unit unit) {
        Army army = unit.getParentArmy();
        String cityName = army.getCurrentLocation();
        City city = game.findCityByName(cityName);
        if (city == null) {
            return;
        }
        if (city.getDefendingArmy() != army) {
            return;
        }

        game.getPlayer().initiateArmy(city, unit);
        updateGameInformation();
    }

    public void startTargetingCity(Army army) {
        targetingCity = army;
        relocatingUnit = null;
        targetingLabel.setText("Please click on the map to select the city you want this army to target.");
        targetingLabel.setVisible(true);
    }

    public void cityClicked(String cityName) {
        if (relocatingUnit != null) {
            return;
        }
        if (targetingCity != null) {
            endTargetCity(cityName);
        } else {
            showCityView(cityName);
        }
    }

    private void endTargetCity(String cityName) {
        game.targetCity(targetingCity, cityName);
        targetingLabel.setVisible(false);
        targetingCity = null;
        relocatingUnit = null;
        updateGameInformation();
    }

    private void showCityView(String cityName) {
        
        for (City city : game.getPlayer().getControlledCities()) {
            if (isSideViewCityView(city)) {
                    return;
            }
            removeSideView();
            sideView = new CityView(city, this, game);
            add(sideView, BorderLayout.EAST);
            validate();
        }
    }

    public void build(String type, City city) {
        try {
            this.game.getPlayer().build(type, city.getName());
            updateGameInformation();

        } catch (NotEnoughGoldException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Command failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void upgradeBuilding(Building building) {
        try {
            building.upgrade();
            updateGameInformation();
        } catch (BuildingInCoolDownException | MaxLevelException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Command failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void recruitUnit(String unitType, City city) {
        try {
            game.getPlayer().recruitUnit(unitType, city.getName());
            updateGameInformation();
        } catch (BuildingInCoolDownException | MaxRecruitedException | NotEnoughGoldException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Command failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void updateGameInformation() {
        this.summaryView.updateGameInformation();
        this.mapView.updateGameInformation();
        ((GameInformationView) this.sideView).updateGameInformation();
    }

    private void removeSideView() {
        if (sideView != null) {
            remove(sideView);
        }
    }

    private boolean isSideViewCityView(City city) {
        return sideView != null
            && sideView.getClass() == CityView.class
            && ((CityView)sideView).getCity() == city;
    }

    private boolean isSideViewArmyView(Army army) {
        return sideView != null
            && sideView.getClass() == ArmyView.class
            && ((ArmyView)sideView).getArmy() == army;
    }
}
