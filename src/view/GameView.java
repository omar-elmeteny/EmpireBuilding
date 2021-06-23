package view;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.*;
import java.io.IOException;

import engine.City;
import engine.Game;

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
                sideView = new CityView(city);
                add(sideView, BorderLayout.EAST);
                doLayout(); 
            }
        }
    }

}
