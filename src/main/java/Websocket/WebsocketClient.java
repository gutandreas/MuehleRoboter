package Websocket;

import EiBotBoard.Connection;
import EiBotBoard.Controller;
import EiBotBoard.Ebb;
import EiBotBoard.RingAndFieldCoordsCm;
import game.Position;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;

public class WebsocketClient extends WebSocketClient {

    Connection connection;

    public WebsocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Verbunden mit Server");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "watch");
        jsonObject.put("gameCode", "aaa");
        send(jsonObject.toString());
        String usbDevice = Controller.getUSBDevice();
        Ebb ebb = new Ebb(usbDevice);
        connection = new Connection(ebb);
        //Ebb ebb = new Ebb("/dev/cu.usbmodem142101");
        //Ebb ebb = new Ebb("/dev/ttyACM0");
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
        JSONObject jsonObject = new JSONObject(message);
        String command = jsonObject.getString("command");

        switch (command){
            case "join":
                System.out.println("Spiel beigetreten");
            case "update":
                if (jsonObject.getString("action").equals("put")){

                    int ring = jsonObject.getInt("ring");
                    int field = jsonObject.getInt("field");


                    RingAndFieldCoordsCm ringAndFieldCoordsCm = new RingAndFieldCoordsCm();
                    //ebb.execute("SC," + 4 + "," + 30000);



                    connection.put(new Position(ring,field), 1);
                }

        }

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }
}
