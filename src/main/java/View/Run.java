package View;

import EiBotBoard.Connection;
import EiBotBoard.Ebb;

public class Run {

    public static void main(String[] args) {


        Ebb ebb = new Ebb();
        Connection connection = new Connection(ebb);
        // Ein neues Objekt der Klasse BeispielListener wird erzeugt
        // und sichtbar gemacht
        MenuView menuView = new MenuView(args, connection);
        //MenuView menuView = new MenuView(args, connection);
        menuView.setVisible(true);
    }
}
