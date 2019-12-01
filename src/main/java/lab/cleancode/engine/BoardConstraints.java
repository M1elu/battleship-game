package lab.cleancode.engine;

import java.util.List;


public class BoardConstraints {

    public static BoardConstraints getDefault() {
        BoardConstraints constraints = new BoardConstraints();
        constraints.minSpaceBetweenShips = 1;
        constraints.shipSizeCounts = List.of(
                new ShipSize(4, 1),
                new ShipSize(3, 2),
                new ShipSize(2, 3),
                new ShipSize(1, 4));

        return constraints;

    }

    public int minSpaceBetweenShips;

    public List<ShipSize> shipSizeCounts;

    public int GetShipsCountLimit() {
        return shipSizeCounts.stream().mapToInt(s -> s.getSizeY()).sum();
    }
}
