package player;

import list.DList;

/**
 * A public class for constructing and holding the actual game board object. Add
 * more description?
 */

public class Board {

	final static int SIZE = 8;
	final static int EMPTY = 0;
	final static int WHITE = 1;
	final static int BLACK = 2;
	private int[][] board;

	// Constructs an 8x8 board.
	public Board() {
		board = new int[SIZE][SIZE];
	}

	// Returns whether or not the move involves a corner on the Board.
	// If it is a corner, returns true. Else, returns false.
	public boolean ifCorner(Move m) {
		if ((m.x1 + m.y1) == 00) {
			return true;
		} else if ((m.x1 + m.y1) == 70) {
			return true;
		} else if ((m.x1 + m.y1) == 07) {
			return true;
		} else if ((m.x1 + m.y1) == 77) {
			return true;
		} else {
			return false;
		}
	}

	// Returns a list of all possible connections between the input piece
	// and ONE other piece.
	public DList singleConnections(int x, int y) {
		DList connections = new DList();
		connections.insertBack(x + y);
		int color = board[x][y];
		if (board[x][y] != EMPTY) {
			if (board[x][y] == color) {
				// left
				for (int i = x; x >= 0; i--) {
					if (board[i][y] == color) {
						connections.insertBack(i + y);
						break;
					} else if (board[i][y] != color) {
						break;
					}
				}
				// right
				for (int i = x; x <= 7; i++) {
					if (board[i][y] == color) {
						connections.insertBack(i + y);
						break;
					} else if (board[i][y] != color) {
						break;
					}
				}
				// up
				for (int i = y; y >= 0; i--) {
					if (board[x][i] == color) {
						connections.insertBack(x + i);
						break;
					} else if (board[x][i] != color) {
						break;
					}
				}
				// down
				for (int i = y; y <= 7; i++) {
					if (board[x][i] == color) {
						connections.insertBack(x + i);
						break;
					} else if (board[x][i] != color) {
						break;
					}
				}
				// upper left
				int j = x;
				for (int i = y; y >= 0; i--) {
					if (board[j][i] == color) {
						connections.insertBack(j + i);
						break;
					} else if (board[j][i] != color) {
						break;
					} else if (j < 0 || i < 0) {
						break;
					}
					j -= 1;
				}
				// upper right
				j = x;
				for (int i = y; y >= 0; i--) {
					if (board[j][i] == color) {
						connections.insertBack(j + i);
						break;
					} else if (board[j][i] != color) {
						break;
					} else if (j < 0 || i < 0) {
						break;
					}
					j += 1;
				}
				// lower left
				j = x;
				for (int i = y; y <= 7; i++) {
					if (board[j][i] == color) {
						connections.insertBack(j + i);
						break;
					} else if (board[j][i] != color) {
						break;
					} else if (j < 0 || i < 0) {
						break;
					}
					j -= 1;
				}
				// lower right
				j = x;
				for (int i = y; y <= 7; i++) {
					if (board[j][i] == color) {
						connections.insertBack(j + i);
						break;
					} else if (board[j][i] != color) {
						break;
					} else if (j < 0 || i < 0) {
						break;
					}
					j += 1;
				}
			}
		}
		return connections;
	}

	// CODE FOR DEBUGGING

	// FOR TESTING. CHECKING CELL CONTENTS OF BOARD.
	public int cellContents(int x, int y) {
		return board[x][y];
	}

	// FOR TESTING. MANUAL ADD.
	public void addWhitePiece(int x, int y) {
		if (board[x][y] == EMPTY) {
			board[x][y] = WHITE;
		}
	}

	// FOR TESTING. MANUAL ADD.
	public void addBlackPiece(int x, int y) {
		if (board[x][y] == EMPTY) {
			board[x][y] = BLACK;
		}
	}

}
