/**
 * @author benwunderlich, gavinpogson, andrewwaugaman, danielleon*/

package controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.MinesweeperModel;
import utilities.GameOverException;
import utilities.ModelSpace;

class MinesweeperControllerTest {

    /*
     * Tests to make sure checking spaces works correctly.
     */
    @Test
    void testController1() throws GameOverException {
        int rows = 18;
        int cols = 14;
        ModelSpace[][] board = new ModelSpace[rows][cols];
        MinesweeperModel model = new MinesweeperModel(18, 14, .8);
        MinesweeperController controller = new MinesweeperController();
        controller.setBoard(model, rows, cols);
        controller.checkSpace(5, 5);
    }

    /*
     * Also tests to make sure checking spaces works correctly.
     */
    @Test
    void testController2() throws GameOverException {
        int rows = 18;
        int cols = 14;
        ModelSpace[][] board = new ModelSpace[rows][cols];
        MinesweeperModel model = new MinesweeperModel(18, 14, .8);
        MinesweeperController controller = new MinesweeperController();
        controller.setBoard(model, rows, cols);
        controller.checkSpace(5, 5);
        controller.checkSpace(5, 8);
    }
    
    /*
     * Tests to make sure loading works after making a new game.
     */
    @Test
    void testController3() throws GameOverException {
        int rows = 18;
        int cols = 14;
        ModelSpace[][] board = new ModelSpace[rows][cols];
        MinesweeperModel model = new MinesweeperModel(18, 14, .8);
        MinesweeperController controller = new MinesweeperController();
        controller.setBoard(model, rows, cols);
        controller.loadBoard(board, rows, cols);
    }

    /*
     * Tests to make sure updating works after loading and making a game.
     */
    @Test
    void testController4() throws GameOverException {
        int rows = 18;
        int cols = 14;
        ModelSpace[][] board = new ModelSpace[rows][cols];
        MinesweeperModel model = new MinesweeperModel(18, 14, .8);
        MinesweeperController controller = new MinesweeperController();
        controller.setBoard(model, rows, cols);
        controller.loadBoard(board, rows, cols);
        controller.update();
    }
}
