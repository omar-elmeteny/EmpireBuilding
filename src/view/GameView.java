package view;

import javax.swing.JPanel;
import java.awt.*;
import java.io.IOException;

import engine.Game;

public class GameView extends JPanel {

    private SummaryView summaryView;
    private MapView mapView;
    // private SideView sideView;

    public GameView(Game game) throws IOException {
        super();
        setLayout(new BorderLayout());
        summaryView = new SummaryView(game);
        this.add(summaryView, BorderLayout.NORTH);

        mapView = new MapView(game);
        this.add(mapView, BorderLayout.CENTER);
    }

}
