package Websocket;

import EiBotBoard.Connection;
import EiBotBoard.Controller;
import EiBotBoard.Ebb;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class RunWebsocket {

    public static void main(String[] args) throws InterruptedException, URISyntaxException{

        String usbDevice = Controller.getUSBDevice();
        Ebb ebb = new Ebb(usbDevice);
        Connection connection = new Connection(ebb);
        ebb.execute("SC," + 4 + "," + 30000);

        URI uri = new URI("ws://localhost:8080/board");
        WebsocketClient websocketClient = new WebsocketClient(uri, connection);
        websocketClient.connectBlocking();
        websocketClient.watchGame("a");








    }
}
