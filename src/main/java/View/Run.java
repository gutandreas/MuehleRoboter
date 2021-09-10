package View;

import EiBotBoard.Connection;
import EiBotBoard.Ebb;

public class Run {

    public static void main(String[] args) {


        Ebb ebb = new Ebb();
        Connection connection = new Connection(ebb);
        // Ein neues Objekt der Klasse BeispielListener wird erzeugt
        // und sichtbar gemacht
        MainView mainView = new MainView(args, connection);
        //MainView mainView = new MainView(args, connection);
        mainView.setVisible(true);
    }
}
