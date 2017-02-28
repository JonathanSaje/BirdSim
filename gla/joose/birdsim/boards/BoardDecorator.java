package gla.joose.birdsim.boards;

import gla.joose.birdsim.pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.Observable;
import java.util.Stack;
import java.util.Vector;

/**
 * Created by jonathansaje on 28/02/2017.
 */
public abstract class BoardDecorator implements Board {

    protected Board Board;

    BoardDecorator(int rows, int colums){

    }

    @Override
    public void initBoard(JFrame frame) {
        this.Board.initBoard(JFrame frame);
    }

    @Override
    public void updateStockDisplay() {
        this.Board.updateStockDisplay();
    }

    @Override
    public void fly() {
        this.Board.fly();
    }

    @Override
    public void updateStock() {
        this.Board.updateStock();
    }


}
