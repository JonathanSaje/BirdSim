package gla.joose.birdsim.boards;

import gla.joose.birdsim.pieces.Bird;
import java.util.Random;


/**
 * Created by jonathansaje on 07/03/2017.
 */
public class RandomFly implements FlyBehaviour {

    Random rand = new Random();
    

    public void fly(Board board) {

        Bird bird = new Bird();
        int moves = rand.nextInt(51);

        int randRow = board.rand.nextInt((board.getRows() - 3) + 1) + 0;
        int randCol = board.rand.nextInt((board.getColumns() - 3) + 1) + 0;

        board.place(bird,randRow, randCol);
        bird.setDraggable(false);
        bird.setSpeed(20);
        board.updateStockDisplay();
        
        
		while (!board.scareBirds && moves != 0) {
			randRow = rand.nextInt((board.getRows() - 3) + 1) + 0;
			randCol = rand.nextInt((board.getColumns() - 3) + 1) + 0;
			bird.moveTo(randRow, randCol);
			bird.setSpeed(20);
			moves--;
            
        }

        bird.remove();
        board.updateStockDisplay();
    }
}
