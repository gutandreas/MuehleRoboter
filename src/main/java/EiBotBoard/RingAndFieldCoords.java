package EiBotBoard;

import game.Position;

import java.util.HashMap;

public class RingAndFieldCoords {





    private static HashMap<Position, XyCoordsCm> positionMap = new HashMap<>();

    public RingAndFieldCoords() {
       positionMap.put(new Position(0,0), new XyCoordsCm(12, 25));
       positionMap.put(new Position(0,1), new XyCoordsCm(24, 25));
       positionMap.put(new Position(0,2), new XyCoordsCm(36, 25));
       positionMap.put(new Position(0,3), new XyCoordsCm(36, 13));
       positionMap.put(new Position(0,4), new XyCoordsCm(36, 1));
       positionMap.put(new Position(0,5), new XyCoordsCm(24, 1));
       positionMap.put(new Position(0,6), new XyCoordsCm(12, 1));
       positionMap.put(new Position(0,7), new XyCoordsCm(12, 13));

       positionMap.put(new Position(1,0), new XyCoordsCm(18, 21));
       positionMap.put(new Position(1,1), new XyCoordsCm(24, 21));
       positionMap.put(new Position(1,2), new XyCoordsCm(32, 21));
       positionMap.put(new Position(1,3), new XyCoordsCm(32, 13));
       positionMap.put(new Position(1,4), new XyCoordsCm(32, 5));
       positionMap.put(new Position(1,5), new XyCoordsCm(24, 5));
       positionMap.put(new Position(1,6), new XyCoordsCm(16, 5));
       positionMap.put(new Position(1,7), new XyCoordsCm(16, 13));

       positionMap.put(new Position(2,0), new XyCoordsCm(20, 17));
       positionMap.put(new Position(2,1), new XyCoordsCm(24, 17));
       positionMap.put(new Position(2,2), new XyCoordsCm(28, 17));
       positionMap.put(new Position(2,3), new XyCoordsCm(28, 13));
       positionMap.put(new Position(2,4), new XyCoordsCm(29, 9));
       positionMap.put(new Position(2,5), new XyCoordsCm(24, 9));
       positionMap.put(new Position(2,6), new XyCoordsCm(20, 9));
       positionMap.put(new Position(2,7), new XyCoordsCm(20, 13));

        positionMap.put(new Position(-1,0), new XyCoordsCm(2, 1));
        positionMap.put(new Position(-1,1), new XyCoordsCm(2, 4));
        positionMap.put(new Position(-1,2), new XyCoordsCm(2, 7));
        positionMap.put(new Position(-1,3), new XyCoordsCm(2, 10));
        positionMap.put(new Position(-1,4), new XyCoordsCm(2, 13));
        positionMap.put(new Position(-1,5), new XyCoordsCm(2, 16));
        positionMap.put(new Position(-1,6), new XyCoordsCm(2, 19));
        positionMap.put(new Position(-1,7), new XyCoordsCm(2, 22));
        positionMap.put(new Position(-1,8), new XyCoordsCm(2, 25));
        positionMap.put(new Position(-1,9), new XyCoordsCm(2, 28));

        positionMap.put(new Position(-10,0), new XyCoordsCm(10, 23));
        positionMap.put(new Position(-10,1), new XyCoordsCm(10, 15));
        positionMap.put(new Position(-10,2), new XyCoordsCm(10,11));
        positionMap.put(new Position(-10,3), new XyCoordsCm(10, 3));




    }

    public static void add(Position position){
        positionMap.put(position, new XyCoordsCm(10, 15));
    }

    public static XyCoordsCm getCoord(Position position){
        return positionMap.get(position);
    }


}
