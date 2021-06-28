package view;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.MouseInputAdapter;

import buildings.Building;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import engine.City;
import engine.Game;
import exceptions.BuildingInCoolDownException;
import exceptions.FriendlyCityException;
import exceptions.FriendlyFireException;
import exceptions.MaxCapacityException;
import exceptions.MaxLevelException;
import exceptions.MaxRecruitedException;
import exceptions.NotEnoughGoldException;
import exceptions.PlayerMustAttackCityException;
import exceptions.RelocateNotAllowedException;
import exceptions.TargetNotReachedException;
import units.Army;
import units.AttackResult;
import units.Unit;

class EndTurnButtonListener extends MouseInputAdapter {
    private GameView gameView;

    public EndTurnButtonListener(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gameView.endTurn();
    }
}

public class GameView extends JPanel implements GameInformationView {

    private SummaryView summaryView;
    private MapView mapView;
    private JComponent sideView;
    private Game game;
    private JButton endTurnButton;
    private JLabel targetingLabel;
    private LimitedHeightPanel sideViewContainer;
    private JScrollPane sideViewScrollPane;
    private JPanel bottomContainer;
    private Army targetingCity;
    private Unit relocatingUnit;
    private MainWindow mainWindow;
    private BattleView battleView;

    public GameView(Game game, MainWindow mainWindow) throws IOException {
        super();

        this.game = game;
        this.mainWindow = mainWindow;

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
        endTurnButton.addMouseListener(new EndTurnButtonListener(this));
        bottomContainer.add(endTurnButton, BorderLayout.CENTER);

        targetingLabel = new JLabel();
        targetingLabel.setBackground(new Color(86, 94, 100));
        targetingLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        targetingLabel.setForeground(Color.WHITE);
        targetingLabel.setOpaque(true);
        targetingLabel.setText(" ");
        bottomContainer.add(targetingLabel, BorderLayout.NORTH);

        sideViewContainer = new LimitedHeightPanel();
        sideViewContainer.setOpaque(false);
        sideViewContainer.setLayout(new BorderLayout());

        sideViewScrollPane = new JScrollPane(sideViewContainer);
        sideViewScrollPane.setOpaque(false);
        sideViewScrollPane.getViewport().setOpaque(false);
        sideViewScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sideViewScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sideViewScrollPane.setPreferredSize(new Dimension(350, Integer.MAX_VALUE));
        add(sideViewScrollPane, BorderLayout.EAST);

    }

    public void endTurn() {
        if (this.isInBattleView()) {
            return;
        }
        try {
            game.endTurn();
            updateGameInformation();
            checkForVictoryOrDefeat();
        } catch (PlayerMustAttackCityException e) {
            selecteSiegeEndOption(e.getMessage(), e.getCity());
        }
    }

    private void selecteSiegeEndOption(String message, City city) {
        Object[] options = new Object[] { "Manually Attack City", "Auto Resolve." };
        String title = city.getName() + " siege ended";
        int choice = JOptionPane.showOptionDialog(this, message, title, JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        Army attacker = game.getSiegingArmy(city);
        if (choice == 0) {
            attackCity(attacker);
        } else {
            Army defender = city.getDefendingArmy();
            try {
                game.autoResolve(attacker, defender);
                if (attacker.getUnits().size() == 0) {
                    JOptionPane.showMessageDialog(this, "Your army lost to " + city.getName() + "'s army.",
                            "Auto resolve complete", JOptionPane.INFORMATION_MESSAGE);
                    game.getPlayer().getControlledArmies().remove(attacker);
                    if (isSideViewArmyView(attacker)) {
                        removeSideView();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Your won the battle and occupied " + city.getName() + ".",
                            "Auto resolve complete", JOptionPane.INFORMATION_MESSAGE);
                    checkForVictoryOrDefeat();
                }
            } catch (FriendlyFireException e) {
                e.printStackTrace();
            }
            updateGameInformation();
        }
    }

    public void laySiegeToCity(Army army) {
        City city = game.findCityByName(army.getCurrentLocation());
        try {
            game.getPlayer().laySiege(army, city);
            updateGameInformation();
        } catch (TargetNotReachedException | FriendlyCityException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Command failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void attackCity(Army army) {
        if (isInBattleView()) {
            return;
        }
        if (army.getCurrentLocation().equals("onRoad")) {
            JOptionPane.showMessageDialog(this, "Cannot attack a city while army has not reached its target.",
                    "Command failed", JOptionPane.WARNING_MESSAGE);
            return;
        }
        City city = game.findCityByName(army.getCurrentLocation());
        if (game.getPlayer().getControlledCities().contains(city)) {
            JOptionPane.showMessageDialog(this,
                    city.getName() + " is a friendly city. You can't lay siege to a friendly city.", "Command failed",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        removeSideView();

        battleView = new BattleView(this, army, city.getDefendingArmy());
        sideViewScrollPane.setVisible(false);
        mapView.setVisible(false);
        add(battleView, BorderLayout.CENTER);
        validate();
    }

    public void startRelocatingUnit(Unit unit) {
        relocatingUnit = unit;
        targetingCity = null;
        targetingLabel.setText("Please click on the map to select the army you want to relocate this "
                + unit.getClass().getSimpleName() + " to.");
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
            resetTargeting();
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
        } catch (MaxCapacityException | RelocateNotAllowedException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Command failed", JOptionPane.WARNING_MESSAGE);
        }
        resetTargeting();
    }

    private void showArmyView(Army army) {
        if (isInBattleView()) {
            return;
        }
        if (isSideViewArmyView(army)) {
            return;
        }
        removeSideView();
        sideView = new ArmyView(army, this, game);
        sideViewContainer.add(sideView, BorderLayout.CENTER);
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
        resetTargeting();
        updateGameInformation();
    }

    private void showCityView(String cityName) {
        if (isInBattleView()) {
            return;
        }
        for (City city : game.getPlayer().getControlledCities()) {
            if (isSideViewCityView(city)) {
                return;
            }
            if (city.getName().equals(cityName)) {
                removeSideView();
                sideView = new CityView(city, this, game);
                sideViewContainer.add(sideView, BorderLayout.CENTER);
                validate();
            }
        }
    }

    public void build(String type, City city) {
        try {
            this.game.getPlayer().build(type, city.getName());
            updateGameInformation();

        } catch (NotEnoughGoldException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Command failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void upgradeBuilding(Building building) {
        try {
            game.getPlayer().upgradeBuilding(building);
            updateGameInformation();
        } catch (BuildingInCoolDownException | MaxLevelException | NotEnoughGoldException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Command failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void recruitUnit(String unitType, City city) {
        try {
            game.getPlayer().recruitUnit(unitType, city.getName());
            updateGameInformation();
        } catch (BuildingInCoolDownException | MaxRecruitedException | NotEnoughGoldException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Command failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void updateGameInformation() {
        this.summaryView.updateGameInformation();
        this.mapView.updateGameInformation();
        if (this.sideView != null) {
            ((GameInformationView) this.sideView).updateGameInformation();
        }
        if (this.battleView != null) {
            this.battleView.updateGameInformation();
        }
    }

    private void resetTargeting() {
        targetingLabel.setText(" ");
        targetingCity = null;
        relocatingUnit = null;
    }

    private void removeSideView() {
        if (sideView != null) {
            sideViewContainer.remove(sideView);
            sideView = null;
            validate();
            repaint();
        }
    }

    private boolean isSideViewCityView(City city) {
        return sideView != null && sideView.getClass() == CityView.class && ((CityView) sideView).getCity() == city;
    }

    private boolean isSideViewArmyView(Army army) {
        return sideView != null && sideView.getClass() == ArmyView.class && ((ArmyView) sideView).getArmy() == army;
    }

    private boolean isInBattleView() {
        return battleView != null;
    }

    public ArrayList<AttackResult> attackUnit(Unit attackingUnit, Unit target) {
        Army attackingArmy = attackingUnit.getParentArmy();
        Army targetArmy = target.getParentArmy();

        ArrayList<AttackResult> results = new ArrayList<>();
        try {
            AttackResult result = attackingUnit.attack(target);
            results.add(result);
            if (targetArmy.getUnits().size() != 0) {
                result = game.performRandomAttack(targetArmy, attackingUnit.getParentArmy());
                results.add(result);
                if (attackingArmy.getUnits().size() == 0) {
                    game.getPlayer().getControlledArmies().remove(attackingArmy);
                }
            } else {
                game.occupy(attackingArmy, attackingArmy.getCurrentLocation());
                checkForVictoryOrDefeat();
            }

            updateGameInformation();
        } catch (FriendlyFireException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Command failed", JOptionPane.WARNING_MESSAGE);
        }
        return results;
    }

    public void closeBattleView() {
        this.remove(battleView);
        battleView = null;
        mapView.setVisible(true);
        sideViewScrollPane.setVisible(true);
        removeSideView();
    }

    private void checkForVictoryOrDefeat() {
        String message;
        String title;
        if (game.getCurrentTurnCount() > game.getMaxTurnCount()) {
            message = "Sorry you lost the game! Better luck next time. Do you want to start another game?";
            title = "Defeat";

        } else if (game.getPlayer().getControlledCities().size() == game.getAvailableCities().size()) {
            message = "Congratulations! You have won the game. Do you want to start another game?";
            title = "Victory";
        } else {
            return;
        }
        updateGameInformation();
        int res = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
            try {
                mainWindow.newGame();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }
}
