package lab.cleancode.engine;

public class BoardConstraints {

    private int sizeX;
    private int sizeY;

    public BoardConstraints(int sizeX, int sizeY) {
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
