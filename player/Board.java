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
	
	
	// Constructs an 8x8 board.
	public Board() {
		board = new int[8][8][1];
	}
	
	// Returns whether or not the move involves a corner on the Board. 
	// If it is a corner, returns true. Else, returns false.
	public boolean ifCorner(Move m) {
		if ((m.x1 + m.x2) == 00) {
			return true;
		}
		else if ((m.x1 + m.x2) == 70) {
			return true;
		}
		else if ((m.x1 + m.x2) == 07) {
			return true;
		}
		else if ((m.x1 + m.x2) == 77) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
}
