package view;

import java.io.IOException;

import javax.swing.JFrame;

public class MainWindow extends JFrame{

    private HomeView homeView;
    public MainWindow() throws IOException{
        super();
        this.setTitle("Empire Building");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 400);

        homeView = new HomeView();
        this.add(homeView);
    }

}
