package EiBotBoard;

import game.Position;

import java.util.HashMap;

public class RingAndFieldCoordsCm {

    private static HashMap<Position, XyCoordsCm> positionMap = new HashMap<>();

    public RingAndFieldCoordsCm() {

        int xBorder = 2;
        int yBorder = 2;

        //Feldpositionen
        positionMap.put(new Position(0,0), new XyCoordsCm(32+xBorder, 0+yBorder));
        positionMap.put(new Position(0,1), new XyCoordsCm(20+xBorder, 0+yBorder));
        positionMap.put(new Position(0,2), new XyCoordsCm(8+xBorder, 0+yBorder));
        positionMap.put(new Position(0,3), new XyCoordsCm(8+xBorder, 12+yBorder));
        positionMap.put(new Position(0,4), new XyCoordsCm(8+xBorder, 24+yBorder));
        positionMap.put(new Position(0,5), new XyCoordsCm(20+xBorder, 24+yBorder));
        positionMap.put(new Position(0,6), new XyCoordsCm(32+xBorder, 24+yBorder));
        positionMap.put(new Position(0,7), new XyCoordsCm(32+xBorder, 12+yBorder));


        positionMap.put(new Position(1,0), new XyCoordsCm(28+xBorder, 4+yBorder));
        positionMap.put(new Position(1,1), new XyCoordsCm(20+xBorder, 4+yBorder));
        positionMap.put(new Position(1,2), new XyCoordsCm(12+xBorder, 4+yBorder));
        positionMap.put(new Position(1,3), new XyCoordsCm(12+xBorder, 12+yBorder));
        positionMap.put(new Position(1,4), new XyCoordsCm(12+xBorder, 20+yBorder));
        positionMap.put(new Position(1,5), new XyCoordsCm(20+xBorder, 20+yBorder));
        positionMap.put(new Position(1,6), new XyCoordsCm(28+xBorder, 20+yBorder));
        positionMap.put(new Position(1,7), new XyCoordsCm(28+xBorder, 12+yBorder));


        positionMap.put(new Position(2,0), new XyCoordsCm(24+xBorder, 8+yBorder));
        positionMap.put(new Position(2,1), new XyCoordsCm(20+xBorder, 8+yBorder));
        positionMap.put(new Position(2,2), new XyCoordsCm(16+xBorder, 8+yBorder));
        positionMap.put(new Position(2,3), new XyCoordsCm(16+xBorder, 12+yBorder));
        positionMap.put(new Position(2,4), new XyCoordsCm(16+xBorder, 16+yBorder));
        positionMap.put(new Position(2,5), new XyCoordsCm(20+xBorder, 16+yBorder));
        positionMap.put(new Position(2,6), new XyCoordsCm(24+xBorder, 16+yBorder));
        positionMap.put(new Position(2,7), new XyCoordsCm(24+xBorder, 12+yBorder));


        //Wartebank Player1
        positionMap.put(new Position(-1,0), new XyCoordsCm(40+xBorder, 0+yBorder));
        positionMap.put(new Position(-1,1), new XyCoordsCm(40+xBorder, 3+yBorder));
        positionMap.put(new Position(-1,2), new XyCoordsCm(40+xBorder, 6+yBorder));
        positionMap.put(new Position(-1,3), new XyCoordsCm(40+xBorder, 9+yBorder));
        positionMap.put(new Position(-1,4), new XyCoordsCm(40+xBorder, 12+yBorder));
        positionMap.put(new Position(-1,5), new XyCoordsCm(40+xBorder, 15+yBorder));
        positionMap.put(new Position(-1,6), new XyCoordsCm(40+xBorder, 18+yBorder));
        positionMap.put(new Position(-1,7), new XyCoordsCm(40+xBorder, 21+yBorder));
        positionMap.put(new Position(-1,8), new XyCoordsCm(40+xBorder, 24+yBorder));
        positionMap.put(new Position(-1,9), new XyCoordsCm(40+xBorder, 27+yBorder));

        //Wartebank Player2
        positionMap.put(new Position(-2,0), new XyCoordsCm(0+xBorder, 0+yBorder));
        positionMap.put(new Position(-2,1), new XyCoordsCm(0+xBorder, 3+yBorder));
        positionMap.put(new Position(-2,2), new XyCoordsCm(0+xBorder, 6+yBorder));
        positionMap.put(new Position(-2,3), new XyCoordsCm(0+xBorder, 9+yBorder));
        positionMap.put(new Position(-2,4), new XyCoordsCm(0+xBorder, 12+yBorder));
        positionMap.put(new Position(-2,5), new XyCoordsCm(0+xBorder, 15+yBorder));
        positionMap.put(new Position(-2,6), new XyCoordsCm(0+xBorder, 18+yBorder));
        positionMap.put(new Position(-2,7), new XyCoordsCm(0+xBorder, 21+yBorder));
        positionMap.put(new Position(-2,8), new XyCoordsCm(0+xBorder, 24+yBorder));
        positionMap.put(new Position(-2,9), new XyCoordsCm(0+xBorder, 27+yBorder));

        //Startposition für Bahnen (Move in Line), B0 bis B3 Player 1
        positionMap.put(new Position(-11,3), new XyCoordsCm(34+xBorder, 22+yBorder));
        positionMap.put(new Position(-11,2), new XyCoordsCm(34+xBorder, 14+yBorder));
        positionMap.put(new Position(-11,1), new XyCoordsCm(34+xBorder,10+yBorder));
        positionMap.put(new Position(-11,0), new XyCoordsCm(34+xBorder, 2+yBorder));

        //Startposition für Bahnen (Move in Line), B0 bis B3 Player 2
        positionMap.put(new Position(-12,3), new XyCoordsCm(6+xBorder, 22+yBorder));
        positionMap.put(new Position(-12,2), new XyCoordsCm(6+xBorder, 14+yBorder));
        positionMap.put(new Position(-12,1), new XyCoordsCm(6+xBorder,10+yBorder));
        positionMap.put(new Position(-12,0), new XyCoordsCm(6+xBorder, 2+yBorder));






    }

    public static XyCoordsCm getCoord(Position position){
        return positionMap.get(position);
    }


}
