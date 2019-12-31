package lab.cleancode;

import lab.cleancode.engine.Engine;
import lab.cleancode.view.GameView;
import lab.cleancode.view.cli.CliView;

public class App {

    public static void main(String[] args) {
        GameView view = new CliView();
        Engine battleshipGame = new Engine(view);
        battleshipGame.start();
    }
}
