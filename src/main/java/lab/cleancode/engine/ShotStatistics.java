package lab.cleancode.engine;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ShotStatistics {

    private int allShotsCount;
    private int missedShotsCount;

    public ShotStatistics(FieldState[][] shotBoard) {
        this.allShotsCount = calculateAllShotsCount(shotBoard);
        this.missedShotsCount = calculateMissedShotsCount(shotBoard);
    }

    private int calculateMissedShotsCount(FieldState[][] shotBoard) {
        return Arrays.stream(shotBoard).mapToInt((s) ->
                IntStream.range(0, shotBoard.length).map((index) -> {
                            boolean condition = s[index] == FieldState.Miss;
                            return condition ? 1 : 0;
                        }
                ).sum()
        ).sum();
    }

    private int calculateAllShotsCount(FieldState[][] shotBoard) {
        return Arrays.stream(shotBoard).mapToInt((fieldStates) ->
                IntStream.range(0, shotBoard.length).map((index) -> {
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
