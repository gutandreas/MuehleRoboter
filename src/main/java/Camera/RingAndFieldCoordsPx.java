package Camera;

import game.Position;

import java.util.HashMap;

public class RingAndFieldCoordsPx {

    private static HashMap<Position, XyCoordsPx> positionMap = new HashMap<>();

    public RingAndFieldCoordsPx() {

        int xBorder = 0;
        int yBorder = 0;

        //Feldpositionen
        positionMap.put(new Position(0,0), new XyCoordsPx(427+xBorder, 2312+yBorder));
        positionMap.put(new Position(0,1), new XyCoordsPx(1562+xBorder, 2317+yBorder));
        positionMap.put(new Position(0,2), new XyCoordsPx(2718+xBorder, 2373+yBorder));
        positionMap.put(new Position(0,3), new XyCoordsPx(2710+xBorder, 1227+yBorder));
        positionMap.put(new Position(0,4), new XyCoordsPx(2703+xBorder, 85+yBorder));
        positionMap.put(new Position(0,5), new XyCoordsPx(1560+xBorder, 94+yBorder));
        positionMap.put(new Position(0,6), new XyCoordsPx(475+xBorder, 112+yBorder));
        positionMap.put(new Position(0,7), new XyCoordsPx(448+xBorder, 1209+yBorder));

        positionMap.put(new Position(1,0), new XyCoordsPx(800+xBorder, 1963+yBorder));
        positionMap.put(new Position(1,1), new XyCoordsPx(1556+xBorder, 1966+yBorder));
        positionMap.put(new Position(1,2), new XyCoordsPx(2323+xBorder, 1986+yBorder));
        positionMap.put(new Position(1,3), new XyCoordsPx(2317+xBorder, 1217+yBorder));
        positionMap.put(new Position(1,4), new XyCoordsPx(2325+xBorder, 438+yBorder));
        positionMap.put(new Position(1,5), new XyCoordsPx(1561+xBorder, 448+yBorder));
        positionMap.put(new Position(1,6), new XyCoordsPx(829+xBorder, 442+yBorder));
        positionMap.put(new Position(1,7), new XyCoordsPx(824+xBorder, 1206+yBorder));


        positionMap.put(new Position(2,0), new XyCoordsPx(1186+xBorder, 1567+yBorder));
        positionMap.put(new Position(2,1), new XyCoordsPx(1555+xBorder, 1560+yBorder));
        positionMap.put(new Position(2,2), new XyCoordsPx(1933+xBorder, 1567+yBorder));
        positionMap.put(new Position(2,3), new XyCoordsPx(1935+xBorder, 1212+yBorder));
        positionMap.put(new Position(2,4), new XyCoordsPx(1932+xBorder, 842+yBorder));
        positionMap.put(new Position(2,5), new XyCoordsPx(1565+xBorder, 842+yBorder));
        positionMap.put(new Position(2,6), new XyCoordsPx(1192+xBorder, 838+yBorder));
        positionMap.put(new Position(2,7), new XyCoordsPx(1196+xBorder, 1205+yBorder));


    }

    public static XyCoordsPx getCoord(Position position){
        return positionMap.get(position);
    }


}
