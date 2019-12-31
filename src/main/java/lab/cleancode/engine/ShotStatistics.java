package lab.cleancode.engine;

import lab.cleancode.engine.board.BoardConstraints;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ShotStatistics {

    private int allShotsCount;
    private int missedShotsCount;

    public ShotStatistics(FieldState[][] shotBoard, BoardConstraints boardConstraints) {
        this.allShotsCount = calculateAllShotsCount(shotBoard, boardConstraints);
        this.missedShotsCount = calculateMissedShotsCount(shotBoard, boardConstraints);
    }

    private int calculateMissedShotsCount(FieldState[][] shotBoard, BoardConstraints boardConstraints) {
        return Arrays.stream(shotBoard).mapToInt((s) ->
                IntStream.range(0, boardConstraints.getSizeY() - 1).map((index) -> {
                            boolean condition = s[index] == FieldState.Miss;
                            return condition ? 1 : 0;
                        }
                ).sum()
        ).sum();
    }

    private int calculateAllShotsCount(FieldState[][] shotBoard, BoardConstraints boardConstraints) {
        return Arrays.stream(shotBoard).mapToInt((fieldStates) ->
                IntStream.range(0, boardConstraints.getSizeY() - 1).map((index) -> {
                            boolean condition = fieldStates[index] == FieldState.Miss
                                    || fieldStates[index] == FieldState.Hit
                                    || fieldStates[index] == FieldState.Sunk;
                            return condition ? 1 : 0;
                        }
                ).sum()
        ).sum();
    }

    public int getAllShotsCount() {
        return allShotsCount;
    }

    public int getMissedShotsCount() {
        return missedShotsCount;
    }
}
