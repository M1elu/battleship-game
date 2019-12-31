package lab.cleancode.engine.board;

import lab.cleancode.engine.ships.Ship;

import java.util.List;

public class BoardConfigurationUtils {

    public static boolean isCapableToAddShips(
            List<Ship> ships,
            BoardConstraints boardConstraints
    ) {
        boolean isAnyShipTooLong = isAnyShipTooLong(ships, boardConstraints);
        if (isAnyShipTooLong) return false;
        int maxCells = boardConstraints.getSizeX() * boardConstraints.getSizeY();
        int shipCells = ships.stream().mapToInt(Ship::getLength).sum();
        int newShipCells = ships.stream().mapToInt(Ship::getLength).sum();
        return shipCells + newShipCells < maxCells;
    }

    private static boolean isAnyShipTooLong(
            List<Ship> ships,
            BoardConstraints boardConstraints
    ) {
        return ships.stream().anyMatch(s ->
                s.getLength() > boardConstraints.getSizeX()
                        || s.getLength() > boardConstraints.getSizeY()
        );
    }
}
