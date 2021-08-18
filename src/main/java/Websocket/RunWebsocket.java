package Websocket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class RunWebsocket {

    public static void main(String[] args) throws InterruptedException, URISyntaxException{

        URI uri = new URI("ws://localhost:8080/board");
        WebsocketClient websocketClient = new WebsocketClient(uri);
        websocketClient.connectBlocking();






    }
}
