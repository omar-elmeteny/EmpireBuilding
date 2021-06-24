package view;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import buildings.Building;

import java.awt.*;
import java.io.IOException;

import engine.City;
import engine.Game;
import exceptions.BuildingInCoolDownException;
import exceptions.MaxLevelException;
import exceptions.MaxRecruitedException;
import exceptions.NotEnoughGoldException;

public class GameView extends JPanel {

    private SummaryView summaryView;
    private MapView mapView;
    private JComponent sideView;
    private Game game;

    public GameView(Game game) throws IOException {
        super();

        this.game = game;
        setLayout(new BorderLayout());
        this.setBackground(new Color(255, 228, 196));
        summaryView = new SummaryView(game);
        this.add(summaryView, BorderLayout.NORTH);

        mapView = new MapView(game, this);
        this.add(mapView, BorderLayout.CENTER);
    }

    public void showCityView(String cityName) {
        for(City city : game.getPlayer().getControlledCities()){
            if(city.getName().equals(cityName)){
                if(sideView != null && sideView.getClass() == CityView.class && ((CityView) sideView).getCity() == city){
                    return;
                }
                if(sideView != null){
                    remove(sideView);
                }
                sideView = new CityView(city, this.game.getPlayer(), this);
                add(sideView, BorderLayout.EAST);
                validate(); 
            }
        }
    }

    public void build(String type, City city) {
        try {
            this.game.getPlayer().build(type, city.getName());
            updateAllViews();

        } catch(NotEnoughGoldException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Command faild", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void upgradeBuilding(Building building) {
        try {
            building.upgrade();
            updateAllViews();
        } catch (BuildingInCoolDownException | MaxLevelException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Command faild", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void recruitUnit(String unitType, City city) {
        try {
            game.getPlayer().recruitUnit(unitType, city.getName());
            updateAllViews();
        } catch (BuildingInCoolDownException | MaxRecruitedException | NotEnoughGoldException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Command faild", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateAllViews() {
        this.summaryView.updateGameInformation();
        this.mapView.updateGameInformation();
        ((GameInformationView)this.sideView).updateGameInformation();
    }
}
