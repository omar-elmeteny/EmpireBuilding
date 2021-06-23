package view;

import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import engine.City;

public class CityView extends JPanel{
    
    final private City city;
    private JLabel cityNameLabel;
    private JLabel militaryBuildingsLabel;
    private JLabel economicalBuildingsLabel;



    public CityView(City city) {
        this.city = city;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        this.setBorder(new EmptyBorder(0, 5, 5, 5));

        cityNameLabel = new JLabel(city.getName());
        cityNameLabel.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        add(cityNameLabel);

        militaryBuildingsLabel = new JLabel("Military Buildings");
        militaryBuildingsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        add(militaryBuildingsLabel);

        economicalBuildingsLabel = new JLabel("Economical Buildings");
        economicalBuildingsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        add(economicalBuildingsLabel);        
    }

    public City getCity() {
        return city;
    }

}
