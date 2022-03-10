package game;


public class Position implements Comparable<Position> {

    private final int ring, field;

    public Position(int ring, int field) {
        this.ring = ring;
        this.field = field;
    }

    public int getRing() {
        return ring;
    }

    public int getField() {
        return field;
    }

    @Override
    public boolean equals(Object o) {
        Position position = (Position) o;
        return ring == position.getRing() && field == position.getField();
    }

    @Override
    public int hashCode() {
        return field;
    }

    @Override
    public int compareTo(Position position) {
        if (ring == position.getRing() && field == position.getField()) return 0;
        if (ring == position.getRing() && field > position.getField()) return 1;
        if (ring > position.getRing()) return 1;
        return -1;
    }

    @Override
    public String toString() {
        return "Position " + getRing() + "/" + getField();
    }
}
