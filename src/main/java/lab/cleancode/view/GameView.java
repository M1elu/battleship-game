package lab.cleancode.view;

import lab.cleancode.engine.Coordinate;
import lab.cleancode.engine.FieldState;
import lab.cleancode.engine.board.BoardConstraints;
import lab.cleancode.engine.board.PlayerBoard;

public interface GameView {

    void displayBoard(FieldState[][] board, BoardConstraints constraints);

    void displayEnd(FieldState[][] shotBoard, BoardConstraints boardConstraints);

    void displayShotResult(boolean isCoordinateWithinBoard, boolean isCoordinateAlreadyHit);

    PlayerBoard readBoardFromPlayer();

    Coordinate readShotCoordinate();
}
