package lab.cleancode.engine.board;

import lab.cleancode.engine.ships.Ship;

import java.util.List;

public class BoardConfigurationUtils {

    public static boolean isCapableToAddShips(List<Ship> newShips, BoardConstraints boardConstraints) {
        boolean isAnyShipTooLong = isAnyShipTooLong(newShips, boardConstraints);
        if (isAnyShipTooLong) return false;
        int maxCells = boardConstraints.getSizeX() * boardConstraints.getSizeY();
        int shipCells = newShips.stream().mapToInt(Ship::getLength).sum();
        int newShipCells = newShips.stream().mapToInt(Ship::getLength).sum();
        return shipCells + newShipCells < maxCells;
    }

    private static boolean isAnyShipTooLong(List<Ship> newShips, BoardConstraints boardConstraints) {
        return newShips.stream().anyMatch(s ->
                s.getLength() > boardConstraints.getSizeX()
                        || s.getLength() > boardConstraints.getSizeY()
        );
    }
}
