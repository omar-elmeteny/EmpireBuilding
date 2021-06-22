package view;

import javax.swing.JPanel;
import java.awt.*;

import engine.Game;

public class GameView extends JPanel{
    
    private SummaryView summaryView;
    private MapView mapView;
    private SideView sideView;

    public GameView(Game game) {
        super();
        setLayout(new BorderLayout());
        summaryView = new SummaryView(game);
        this.add(summaryView, BorderLayout.NORTH);


    }

}
