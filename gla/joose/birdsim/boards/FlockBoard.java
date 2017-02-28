package gla.joose.birdsim.boards;

import gla.joose.birdsim.pieces.Bird;
import gla.joose.birdsim.pieces.Grain;
import gla.joose.birdsim.pieces.Piece;
import javafx.beans.InvalidationListener;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A BirdSim board with generic bird flying behaviour.
 */
public class FlockBoard implements Board {

    @SuppressWarnings("rawtypes")
    private Vector[][] board;
    protected Vector<Piece> allPieces = new Vector<Piece>();
    private int[] selectedSquare;
    private int rows;
    private int columns;
    private int defaultSpeed = 5;
    private Board thisBoard;
    private JPanel display;
    protected boolean panelHasBeenResized = false;

    protected Random rand;
    protected boolean scareBirds;
    protected boolean starveBirds;
    protected int noofbirds;
    protected int noofgrains;

    JPanel buttonPanel;
    JButton hatchEggButton;
    JButton scareBirdsButton;
    JLabel noOfBirdsLabel;
    
    Thread runningthread;


    
	public FlockBoard(int rows, int columns) {

        rand = new Random();
        display = new DisplayPanel();
        this.rows = rows;
        this.columns = columns;
        thisBoard = this;
        board = new Vector[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = new Vector(1);
            }
        }
        display.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = yToRow(e.getY());
                int column = xToColumn(e.getX());
                selectedSquare = new int[] { row, column };
                //  setChanged();
                //  notifyObservers(selectedSquare);
            }
        });
        display.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent arg0) {
                panelHasBeenResized  = true;
            }
        });
	}

    public FlockBoard(Board board) {
	    return FlockBoard(rows,columns);
    }

    @Override
	public void initBoard(final JFrame frame) {
        JPanel display = getJPanel();
        frame.getContentPane().add(display, BorderLayout.CENTER);
        
        // Install button panel
        buttonPanel = new JPanel();
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        hatchEggButton = new JButton("hatch egg");
        buttonPanel.add(hatchEggButton);
        hatchEggButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	scareBirds = false;
            	runningthread = new Thread(new Runnable(){
					public void run() {
						fly();
					}            		
            	});
            	runningthread.start();
        }}); 

        scareBirdsButton = new JButton("scare birds");
        buttonPanel.add(scareBirdsButton);
        scareBirdsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	scareBirds = true;
        }}); 
        
        noOfBirdsLabel = new JLabel();
        noOfBirdsLabel.setText("#birds: "+0);
        buttonPanel.add(noOfBirdsLabel);

        
        // Implement window close box
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	//used to invoke birds removal from the board
            	scareBirds = true;
            	if(runningthread !=null){
                    clear();
                    try {
						runningthread.join();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
            	}
            	frame.dispose();
                System.exit(0);
        }});
        
        frame.pack();
        frame.setSize(650, 650);
        frame.setVisible(true);
        		
	}

	@Override
	public void updateStockDisplay(){
		updateStock();
		noOfBirdsLabel.setText("#birds: "+noofbirds);
	}

    @Override
    public void fly() {
        Bird bird = new Bird();

        int randRow = rand.nextInt((getRows() - 3) + 1) + 0;
        int randCol = rand.nextInt((getColumns() - 3) + 1) + 0;

        place(bird,randRow, randCol);
        bird.setDraggable(false);
        bird.setSpeed(20);
        updateStockDisplay();

        while(!scareBirds){
            randRow = rand.nextInt((getRows() - 3) + 1) + 0;
            randCol = rand.nextInt((getColumns() - 3) + 1) + 0;
            bird.moveTo(randRow, randCol);
            bird.setSpeed(20);

        }
        bird.remove();
        updateStockDisplay();
    }

    @Override
    public void updateStock() {
        synchronized(allPieces){
            noofbirds = 0;
            noofgrains = 0;
            for (int i=0;i< getAllPieces().size(); i++) {
                Piece piece = getAllPieces().get(i);
                if(piece instanceof Grain){
                    noofgrains = noofgrains +1;
                }
                else if(piece instanceof Bird){
                    noofbirds = noofbirds +1;
                }
            }

        }
    }

    @Override
    public JPanel getJPanel() {
        return display;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public Vector<Piece> getAllPieces() {
        synchronized(allPieces){
            return allPieces;
        }
    }

    @Override
    public Piece getPiece(int row, int column) {
        if (board[row][column].isEmpty()) {
            return null;
        }
        return (Piece) board[row][column].lastElement();
    }

    @Override
    public Stack getPieces(int row, int column) {
        Stack pieces = new Stack();
        for (Iterator iter = board[row][column].iterator(); iter.hasNext();) {
            pieces.push(iter.next());
        }
        return pieces;
    }

    @Override
    public boolean isEmpty(int row, int column) {
        return board[row][column].isEmpty();
    }

    @Override
    public Piece findPiece(int x, int y) {
        return getPiece(yToRow(y), xToColumn(x));
    }

    @Override
    public int xToColumn(int x) {
        return Math.min(columns - 1, (x * columns) / display.getWidth());
    }

    @Override
    public int yToRow(int y) {
        return Math.min(rows - 1, (y * rows) / display.getHeight());
    }

    @Override
    public int columnToX(int columnNumber) {
        return (columnNumber * (display.getWidth() - 1)) / columns;
    }

    @Override
    public int rowToY(int rowNumber) {
        return (rowNumber * (display.getHeight() - 1)) / rows;
    }

    @Override
    public void place(Piece piece, int row, int column) {
        if (piece.getBoard() != null) {
            throw new IllegalArgumentException("Piece " + piece + " is already on a board");
        }
        board[row][column].add(piece);
        synchronized (allPieces) {
            allPieces.add(piece);
        }
        piece.placeHelper(this, row, column);
    }

    @Override
    public void clear() {
        synchronized (allPieces) {
            for (int i = allPieces.size() - 1; i >= 0; i--) {
                remove (allPieces.get(i));
            }
        }
    }

    @Override
    public Piece remove(int row, int column) {
        Piece piece = getPiece(row, column);
        if (piece == null) {
            return null;
        } else {
            remove(piece);
            return piece;
        }
    }

    @Override
    public void changePositionOnBoard(Piece piece, int oldRow, int oldColumn, int newRow, int newColumn) {

        board[oldRow][oldColumn].remove(piece);
        board[newRow][newColumn].add(piece);
    }

    @Override
    public boolean remove(Piece piece) {
        if (piece == null || piece.getBoard() != this) {
            return false;
        }
        board[piece.getRow()][piece.getColumn()].remove(piece);
        synchronized (allPieces) {
            allPieces.remove(piece);
        }
        piece.removeHelper();
        return true;
    }

    @Override
    public void moveToTop(Piece piece) {
        synchronized (allPieces) {
            allPieces.remove(piece);
            allPieces.add(piece);
        }
    }

    @Override
    public void setSpeed(int speed) {
        if (speed > 0)
            defaultSpeed = speed;
    }

    @Override
    public int getSpeed() {
        return defaultSpeed;
    }

    @Override
    public int getCellWidth() {
        return display.getWidth() / columns;
    }

    @Override
    public int getCellHeight() {
        return display.getHeight() / rows;
    }

    @Override
    public boolean isLegalPosition(int row, int column) {
        if (row < 0 || row >= rows)
            return false;
        if (column < 0 || column >= columns)
            return false;
        return true;
    }

    @Override
    public void update(Observable changedPiece, Object rectangle) {
        Piece piece = (Piece)changedPiece;
        if (rectangle == null) {
            display.repaint();
        } else {
            Rectangle r = (Rectangle)rectangle;
            display.repaint(r.x, r.y, r.width, r.height);
        }
    }

    @Override
    public void paint(Graphics g) {
        int height = display.getHeight();
        int width = display.getWidth();
        int x, y;
        Color oldColor = g.getColor();
        Color backgroundColor = Color.white;
        Color lineColor = new Color(192, 192, 255);

        // Fill background with solid color
        g.setColor(backgroundColor);
        g.fillRect(0, 0, display.getWidth(), display.getHeight());

        // Paint vertical lines
        g.setColor(lineColor);
        for (int i = 0; i <= columns; i++) {
            x = columnToX(i);
            g.drawLine(x, 0, x, height);
        }
        // Paint horizontal lines
        for (int i = 0; i <= rows; i++) {
            y = rowToY(i);
            g.drawLine(0, y, width, y);
        }
        g.setColor(oldColor);
    }

    @Override
    public void dump() {
        System.out.println("----------- Board is " + rows + " rows, "
                + columns + " columns.");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!board[i][j].isEmpty()) {
                    System.out.println("Board [" + i + "][" + j + "] contains:");
                    for (Iterator iter = board[i][j].iterator(); iter.hasNext();) {
                        Piece piece = (Piece) iter.next();
                        System.out.println("    " + piece.toString());
                    }
                }
            }
        }
        synchronized (allPieces) {
            System.out.println("Vector allPieces:");
            for (Iterator<Piece> iter = allPieces.iterator(); iter.hasNext();) {
                Piece piece = iter.next();
                System.out.println("    " + piece.toString());
            }
//            System.out.println("Selected piece = " + selectedPiece);
            System.out.println("----------- Pieces: ");
            for (Iterator<Piece> iter = allPieces.iterator(); iter.hasNext();) {
                Piece piece = iter.next();
                System.out.print(piece.toString());
                piece.dump();
            }
        }
    }

    //  -------------------------------------------------- inner class DisplayPanel

    class DisplayPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        /**
         * Repaints this Board and everything on it.
         *
         * @param g
         *        The Graphics context on which this board is painted.
         */
        public void update(Graphics g) {
            paint(g);
        }

        /**
         * Repaints this Board and everything on it.
         *
         * @param g
         *        The Graphics context on which this board is painted.
         */
        public void paint(Graphics g) {
            // Paint the board
            thisBoard.paint(g);
            // Paint the pieces
            synchronized (allPieces) {
                for (Iterator<Piece> iter = allPieces.iterator(); iter.hasNext();) {
                    Piece piece = iter.next();
                    piece.paint(g, piece.getRectangle());
                }
            }
        }
    } // end inner class DisplayPanel


    @Override
    public int[] getSelectedSquare() {
        return selectedSquare;
    }

    @Override
    public void setSelectedSquare(int[] selection) {
        selectedSquare = selection;
    }

}
