package player;

/**
 * A public class for constructing and holding the actual game board object.
 * Add more description?
 */

public class Board {

	final static int EMPTY = 0;
	final static int WHITE = 1;
	final static int BLACK = 2;
	private int[][][] board;
	
	
	//Constructs an 8x8 board.
	public Board() {
		board = new int[8][8][1];
	}
	
	
}
