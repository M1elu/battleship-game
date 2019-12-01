package lab.cleancode.engine;

public class ShipSize {
    final private int sizeX;
    final private int sizeY;

    public ShipSize(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
}
