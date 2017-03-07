package gla.joose.birdsim.boards;

import gla.joose.birdsim.pieces.Bird;

/**
 * Created by jonathansaje on 07/03/2017.
 */
public class RandomFly implements FlyBehaviour {

    public void fly() {

        Bird bird = new Bird();

        int randRow = Board.rand.nextInt((Board.getRows() - 3) + 1) + 0;
        int randCol = rand.nextInt((getColumns() - 3) + 1) + 0;

        place(bird,randRow, randCol);
        bird.setDraggable(false);
        bird.setSpeed(20);
        Board.performUpdateStockDisplay();

        while(!scareBirds){
            randRow = rand.nextInt((getRows() - 3) + 1) + 0;
            randCol = rand.nextInt((getColumns() - 3) + 1) + 0;
            bird.moveTo(randRow, randCol);
            bird.setSpeed(20);

        }
        bird.remove();
        updateStockDisplay();
    }
}
