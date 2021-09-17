package View;

import EiBotBoard.Connection;
import EiBotBoard.Ebb;

public class Run {

    public static void main(String[] args) {


        Ebb ebb = new Ebb();
        Connection connection = new Connection(ebb);
        // Ein neues Objekt der Klasse BeispielListener wird erzeugt
        // und sichtbar gemacht

        ViewManager viewManager = new ViewManager();
        MenuView menuView = new MenuView(viewManager, args, connection);

        viewManager.setCurrentView(menuView);
        viewManager.getCurrentView().setVisible(true);

    }
}
