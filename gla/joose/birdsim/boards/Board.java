package gla.joose.birdsim.boards;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gla.joose.birdsim.pieces.Bird;
import gla.joose.birdsim.pieces.Grain;
import gla.joose.birdsim.pieces.Piece;


/**
 * A generic board that can be used to display any piece.
 */
public interface Board{



    /**
     * Creates a board with the given number of rows and columns. This
     * board is a Swing <code>JPanel</code> and may be used wherever a
     * <code>JPanel</code> may be used.
     * 
     * @param rows
     *        Desired number of rows.
     * @param columns
     *        Desired number of columns.
     */

    
    /**
     * Configures a board with specific set of behaviour;
     * must be implemented by a subclass.
     * 
     * @param frame The JFrame on which the board will be created.
     */
    void initBoard(JFrame frame);
    
    
    /**
     * Notifies the board frame on changes in the number of birds/grains;
     * must be implemented by a subclass.
     * 
     */
    void updateStockDisplay();

    
    /**
     * Generic bird behaviour for any concrete board. 
     * This class is overridden when a different board behaviour is preferred
     * 
     */
	void fly();

    /**
     * updates the number of birds and grains on the board.
     */
	void updateStock();


    /**
     * Returns the JPanel on which this board is displayed.
     * 
     * @return The JPanel on which this Board is displayed.
     */
    JPanel getJPanel();

    /**
     * Returns the number of rows in this Board.
     * 
     * @return The number of rows.
     */
    int getRows();

    /**
     * Returns the number of columns in this Board.
     * 
     * @return The number of columns.
     */
    int getColumns();

    Vector<Piece> getAllPieces();

    /**
     * Returns the topmost piece at the given row and column in this Board, or
     * null if the given location is empty.
     * 
     * @param row
     *        The row number.
     * @param column
     *        The column number.
     * @return The <code>Piece</code> in the given [row][column], or
     *         <code>null</code> if that location is empty. If the board
     *         location contains more than one piece, the "topmost" piece is
     *         returned.
     * @throws ArrayIndexOutOfBoundsException
     *         If the specified location does not exist.
     */
    Piece getPiece(int row, int column);

    /**
     * Returns a (possibly empty) Stack of all the pieces in the given position.
     * The top element of the stack is the topmost element in that board
     * location.
     * 
     * @param row
     *        A row number on this board.
     * @param column
     *        A column number on this board.
     * @return The pieces in this board location.
     * @throws ArrayIndexOutOfBoundsException
     *         If the specified location does not exist.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	Stack getPieces(int row, int column);
    
    /**
     * Returns <code>true</code> if the given row and column on this board
     * contains no Pieces.
     * 
     * @param row The row to examine.
     * @param column The column to examine.
     * @return <code>true</code> if this location is empty.
     */
    boolean isEmpty(int row, int column);

    /**
     * Given x-y coordinates, finds and returns the topmost piece at that
     * location on this board, or null if there is no such piece.
     * 
     * @param x
     *        The local x coordinate.
     * @param y
     *        The local y coordinate.
     * @return The <code>Piece</code> in the [row][column] containing the
     *         given (x, y) coordinates, or <code>null</code> if that location
     *         is empty. If the board location contains more than one piece, the
     *         "topmost" piece is returned.
     * @throws ArrayIndexOutOfBoundsException
     *         If the specified location does not exist.
     */
    Piece findPiece(int x, int y);

    /**
     * Given an x coordinate, determines which column it is in.
     * 
     * @param x
     *        A local x coordinate.
     * @return The number of the column containing the given x coordinate.
     */
    int xToColumn(int x);

    /**
     * Given a y coordinate, determines which row it is in.
     * 
     * @param y
     *        A local y coordinate.
     * @return The number of the row containing the given y coordinate.
     */
    int yToRow(int y);

    /**
     * Returns the X coordinate of the left side of cells in the given column of
     * this Board.
     * 
     * @param columnNumber
     *        A column number.
     * @return The X coordinate of the left side of that column.
     */
    int columnToX(int columnNumber);

    /**
     * Returns the Y coordinate of the top side of cells in the given column of
     * this Board.
     * 
     * @param rowNumber
     *        A row number.
     * @return The Y coordinate of the top side of that row.
     *      */
    public int rowToY(int rowNumber);

    /**
     * Places the given piece at the given location in this board.
     * It is possible to place more than one piece in a given board
     * location, in which case later pieces go "on top of" earlier
     * pieces.
     * 
     * @param piece
     *        The <code>Piece</code> to be placed.
     * @param row
     *        The row in which to place the piece.
     * @param column
     *        The column in which to place the piece.
     * @throws ArrayIndexOutOfBoundsException
     *         If the specified location does not exist.
     */
    @SuppressWarnings("unchecked")
	void place(Piece piece, int row, int column);
    
    /**
     * Removes all Pieces from this Board.
     */
    void clear();

    /**
     * Removes the top piece at the given row and column on this Board.
     * 
     * @param row
     *        The row of the piece to be removed.
     * @param column
     *        The column of the piece to be removed.
     * @throws ArrayIndexOutOfBoundsException
     *         If the specified location does not exist.
     */
    Piece remove(int row, int column);
    
    @SuppressWarnings("unchecked")
	void changePositionOnBoard(Piece piece, int oldRow, int oldColumn, int newRow, int newColumn);

    /**
     * Removes this piece from the board. Does nothing if the piece
     * is not, in fact, on the board.
     * 
     * @param piece
     *        The piece to remove.
     * @param row
     *        The row containing the piece.
     * @param column
     *        The column containing the piece.
     */
    boolean remove(Piece piece);

    /**
     * Ensures that the given piece will be drawn on top of any other pieces
     * in the same array location.
     * 
     * @param piece
     *        The piece to promote to the top.
     */
    void moveToTop(Piece piece);

    /**
     * Sets the default speed of movement for pieces on this board, in squares
     * per second. This value is used only for pieces that do not specify their
     * own speed.
     * 
     * @param speed
     *        The default speed for pieces on this board.
     */
    void setSpeed(int speed);

    /**
     * Returns the default speed (in squares per second) of pieces on this
     * board.
     * 
     * @return The default speed for pieces on this board.
     */
    int getSpeed();

    /**
     * Returns the current width, in pixels, of a single cell on this Board. The
     * value will change if this Board is resized.
     */
    int getCellWidth();

    /**
     * @return Returns the current height, in pixels, of a single cell on this
     *         Board. The value will change if this Board is resized.
     */
    int getCellHeight();

    /**
     * Determines whether the given row and column denote a legal position on
     * this Board.
     * 
     * @param row
     *        The given row number.
     * @param column
     *        The given column number.
     * @return <code>true</code> if the given row and column number represent
     *         a valid location on this board
     */
    boolean isLegalPosition(int row, int column);
    
    /**
     * Redraws this Board whenever a Piece is modified.
     * This method should <b>not</b> be overridden.
     * 
     * @param piece
     *        The piece that needs to be redrawn.
     * @param nothing
     *        Not used.
     */
    @SuppressWarnings("unused")
	void update(Observable changedPiece, Object rectangle);
    
    /**
     * Paints th1s board itself, not including the pieces.
     * 
     * @param g
     *        The Graphics context on which this board is painted.
     */
    void paint(Graphics g);


    /**
     * Displays the board contents (for debugging).
     */
    @SuppressWarnings("rawtypes")
	void dump();
    

    /**
     * @return
     */
    int[] getSelectedSquare();
    
    void setSelectedSquare(int[] selection);
}