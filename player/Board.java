
package player;

import list.*;

public class Board {
	
	public final static int SIZE = 8; 
	public final static int EMPTY = -1;
	public final static int WHITE = 1;
	public final static int BLACK = 0;
	private int[][] table; 
	private int addPiecesBlack = 10;
	private int addPiecesWhite = 10; 

	public Board() {
		table = new int[SIZE][SIZE];
		for (int x = 0; x < SIZE; x++) { 
			for (int y = 0; y < SIZE; y++) {
				table[x][y] = EMPTY;
			}
		}
	}
	
	public int getPieces(int side) {
		if (side == BLACK) {
			return addPiecesBlack;
		}
		return addPiecesWhite; 
	}
	
	/*
	 * doMove is used in our Minimax function of MachinePlayer
	 * if the move is an ADDmove then we'll add it to the board
	 * if the move is a STEPmove then we'll delete the piece we want to move and add the new piece
	 */
	public void doMove (Move m, int side) {
		if (m.moveKind == Move.ADD) {
			table[m.x1][m.y1] = side;
			if (side == WHITE) {
				if (addPiecesWhite <= 0) {
					return;
				}
				addPiecesWhite--; 
			}
			if (side == BLACK) {
				if (addPiecesBlack <= 0) {
					return;
				}
				addPiecesBlack--; 
			}
		}
		if (m.moveKind == Move.STEP) {
			table[m.x2][m.y2] = EMPTY;
			table[m.x1][m.y1] = side; 
		}
	}
	
	/*
	 * undoMove is used in our Minimax function of MachinePlayer
	 */
	public void undoMove (Move m, int side) {
		if (m.moveKind == Move.ADD) {
			table[m.x1][m.y1] = EMPTY;
			if (side == WHITE) {
				if (addPiecesWhite >= 10) {
					return;
				}
				addPiecesWhite++;
			}
			if (side == BLACK) {
				if (addPiecesBlack >= 10) {
					return;
				}
				addPiecesBlack++; 
			}
		}
		if (m.moveKind == Move.STEP) {
			table[m.x2][m.y2] = side;
			table[m.x1][m.y1] = EMPTY; 
		}
	}
	
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
	
	public int findPiece(int x, int y) {
		if (x>7 || x<0 || y>7 || y<0) {
			return EMPTY;
		}
		else {
			return table[x][y]; 
		}
	}
	
	public Move[] allPiecesOnBoard(int side) {
		Move[] holder = new Move[10];
		int counter = 0;
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				if (table[x][y] == side) {
					Move newMove = new Move(x, y);
					holder[counter] = newMove;
					counter++; 
				}
			}
		}
		return holder; 
	}
	
	public DList singleConnections(int x, int y) {
		DList connections = new DList();
		connections.insertBack("" + x + y);
		int color = table[x][y];
		if (table[x][y] != EMPTY) {
			// left
			for (int i = x-1; i >= 0; i--) {
				if (table[i][y] == color) {
					connections.insertBack("" + i + y);
					break;
				} else if (table[i][y] != color && table[i][y] != EMPTY) {
					break;
				}
			}
			// right
			for (int i = x+1; i <= 7; i++) {
				if (table[i][y] == color) {
					connections.insertBack("" + i + y);
					break;
				} else if (table[i][y] != color && table[i][y] != EMPTY) {
					break;
				}
			}
			// up
			for (int i = y-1; i >= 0; i--) {
				if (table[x][i] == color) {
					connections.insertBack("" + x + i);
					break;
				} else if (table[x][i] != color && table[x][i] != EMPTY) {
					break;
				}
			}
			// down
			for (int i = y+1; i <= 7; i++) {
				if (table[x][i] == color) {
					connections.insertBack("" + x + i);
					break;
				} else if (table[x][i] != color && table[x][i] != EMPTY) {
					break;
				}
			}
			// upper left
			int j = x-1;
			for (int i = y-1; i >= 0 && j >= 0; i--) {
				if (table[j][i] == color) {
					connections.insertBack("" + j + i);
					break;
				} else if (table[j][i] != color && table[j][i] != EMPTY) {
					break;
				} else if (j < 0 || i < 0) {
					break;
				}
				j -= 1;
			}
			// upper right
			j = x+1;
			for (int i = y-1; i >= 0 && j <= 7; i--) {
				if (table[j][i] == color) {
					connections.insertBack("" + j + i);
					break;
				} else if (table[j][i] != color && table[j][i] != EMPTY) {
					break;
				} else if (j < 0 || i < 0) {
					break;
				}
				j += 1;
			}
			// lower left
			j = x-1;
			for (int i = y+1; i <= 7 && j >= 0; i++) {
				if (table[j][i] == color) {
					connections.insertBack("" + j + i);
					break;
				} else if (table[j][i] != color && table[j][i] != EMPTY) {
					break;
				} else if (j < 0 || i < 0) {
					break;
				}
				j -= 1;
			}
			// lower right
			j = x+1;
			for (int i = y+1; i <= 7 && j <= 7; i++) {
				if (table[j][i] == color) {
					connections.insertBack("" + j + i);
					break;
				} else if (table[j][i] != color && table[j][i] != EMPTY) {
					break;
				} else if (j < 0 || i < 0) {
					break;
				}
				j += 1;
			}
		}
		return connections;
	}
	
	/*
	 * have isNetwork return 1 if you won, 0 if your still going, or -1 if you lost 
	 * @param x: takes in the color of the chip
	 */
	public int isNetwork(int x) {
		
		/*
		if (# of chips minus the ones in the goal area >= 4)
		if (at least 1 chip in each goal area)

		Goal: have to consider all options and check next available options

		Guidelines:
		-no going backwards
		-cannot use a piece previously used to make a network 
		-only check goal area if current network length is >= 5
		-cannot go the same direction twice (give each direction a value?)
		-prefer the network that uses less pieces (6 network rather than 8) (DJIKSTRA will take care of that)
		-force network to build forward rather than checking side or backward (but still check)

		//USE DJIKSTRA'S ALGORITHM
		-from the starting node (s1, s2, etc) find the shortest distance to finish (f1, f2, etc)
		-once you find the shortest path, there is your network
		-apply the filters (cannot go same direction twice) as you go
		-give values to cardinal directions (up, down, etc)
		-

		-ALL edges are length of one (one move)
		-need to mod DJIKSTRA to know more information (filters)
		-find a way to store the information as you go

		==============================================================
		//code starts here

		int color = x;
		Create a STACK possibleNetworks
		Create a HASHMAP memory			//pieces we already seen
		*/


	}
}

