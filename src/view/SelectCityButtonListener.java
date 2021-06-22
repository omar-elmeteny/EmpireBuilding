package view;

import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.event.MouseInputAdapter;

public class SelectCityButtonListener extends MouseInputAdapter{
    

    private HomeView homeView;

    public SelectCityButtonListener(HomeView homeView) {
        this.homeView = homeView;

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        JButton button = (JButton) e.getComponent();
        String cityName = button.getText();
        if(cityName.equals(homeView.getSelectedCity()))
            homeView.setSelectedCity(null);
        else
            homeView.setSelectedCity(cityName);    
    }
}
