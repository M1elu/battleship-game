package lab.cleancode.engine;

import lab.cleancode.engine.board.PlayerBoard;
import lab.cleancode.view.GameView;

public class Engine {

    private final GameView gameView;

    public Engine(GameView gameView) {
        this.gameView = gameView;
    }

    public void start() {
        PlayerBoard playerBoard = gameView.readBoardFromPlayer();
        gameView.displayBoard(playerBoard.getStateBoard(), playerBoard.getConstraints());

        while (playerBoard.getNumberOfBattleshipsLeft() > 0) {
            shoot(playerBoard);
            gameView.displayBoard(playerBoard.getShotBoard(), playerBoard.getConstraints());
        }

        gameView.displayEnd(playerBoard.getShotBoard(), playerBoard.getConstraints());
    }


    private void shoot(PlayerBoard playerBoard) {
        boolean isCoordinateWithinBoard = false;
        boolean isCoordinateAlreadyHit = true;

        Coordinate shotCoordinate = null;
        while (!isCoordinateWithinBoard || isCoordinateAlreadyHit) {
            shotCoordinate = gameView.readShotCoordinate();
            isCoordinateWithinBoard = playerBoard.isCoordinateWithinBoard(shotCoordinate);
            if (isCoordinateWithinBoard) {
                isCoordinateAlreadyHit = playerBoard.isCoordinateAlreadyHit(shotCoordinate);
            }
            gameView.displayShotResult(isCoordinateWithinBoard, isCoordinateAlreadyHit);
        }
        playerBoard.shoot(shotCoordinate);
    }
}

