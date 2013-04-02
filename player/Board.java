package player;

import dict.HashTableChained;
import list.*;

public class Board {
	// board size
	public final static int SIZE = 8;

	// types of players
	public final static int EMPTY = -1;
	public final static int WHITE = 1;
	public final static int BLACK = 0;

	// hold directions
	private final static int[] NORTH = { 0, 1 };
	private final static int[] SOUTH = { 0, -1 };
	private final static int[] WEST = { -1, 0 };
	private final static int[] EAST = { 1, 0 };
	private final static int[] SOUTHEAST = { 1, 1 };
	private final static int[] NORTHEAST = { -1, 1 };
	private final static int[] SOUTHWEST = { 1, -1 };
	private final static int[] NORTHWEST = { -1, -1 };

	// board structure and characteristics
	private int[][] table;
	private int addPiecesBlack = 10;
	private int addPiecesWhite = 10;
	private DList currentBlackPieces = new DList();
	private DList currentWhitePieces = new DList();

	public Board() {
		table = new int[SIZE][SIZE];
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				table[x][y] = EMPTY;
			}
		}
	}

	// gets the amount of pieces that we need
	public int getPieces(int side) {
		if (side == BLACK) {
			return addPiecesBlack;
		}
		return addPiecesWhite;
	}
	
	  /*
	 * Gets the starting goal pieces for either black or white. Only gets the
	 * positions of the goal pieces on the left (white) and the top (black).
	 */
	public DList getGoalPieces(int color) {
		DList goalPositions = new DList();
		DList currentColorPieces;
		if (color == WHITE) {
			currentColorPieces = currentWhitePieces;
		} else if (color == BLACK) {
			currentColorPieces = currentBlackPieces;
		} else {
			return goalPositions;
		}
		ListNode pointer = currentColorPieces.front();
		for (int i = 0; i < currentColorPieces.length(); i++) {
			int[] coor;
			try {
				coor = (int[]) pointer.item();
				if (coor[0] == 0 || coor[1] == 0) {
					goalPositions.insertBack(pointer.item());
				}
				pointer = pointer.next();
			} catch (InvalidNodeException e) {
				e.printStackTrace();
			}
		}
		return goalPositions;
	}

	/*
	 * doMove is used in our Minimax function of MachinePlayer if the move is an
	 * ADDmove then we'll add it to the board if the move is a STEPmove then
	 * we'll delete the piece we want to move and add the new piece
	 */
	public void doMove(Move m, int side) {
		System.out.println(m);
		if (m.moveKind == Move.ADD) {
			if (getPieces(side) <= 0) {
				return;
			}
			table[m.x1][m.y1] = side;
			if (side == WHITE) {
				int[] g = new int[2];
				g[0] = m.x1;
				g[1] = m.y1;
				currentWhitePieces.insertBack(g);
				addPiecesWhite--;
			}
			if (side == BLACK) {
				int[] g = new int[2];
				g[0] = m.x1;
				g[1] = m.y1;
				currentBlackPieces.insertBack(g);
				addPiecesBlack--;
			}
		}
		if (m.moveKind == Move.STEP) {
			table[m.x2][m.y2] = EMPTY;
			table[m.x1][m.y1] = side;
			if (side == WHITE) {
				int[] g = new int[2];
				g[0] = m.x1;
				g[1] = m.y1;
				currentWhitePieces.insertBack(g);
				int[] y = new int[2];
				y[0] = m.x2;
				y[1] = m.y2;
				currentWhitePieces.remove(y);
			}
			if (side == BLACK) {
				int[] g = new int[2];
				g[0] = m.x1;
				g[1] = m.y1;
				currentBlackPieces.insertBack(g);
				int[] y = new int[2];
				y[0] = m.x2;
				y[1] = m.y2;
				currentBlackPieces.remove(y);
			}
		}
	}

	/*
	 * undoMove is used in our Minimax function of MachinePlayer
	 */
	public void undoMove(Move m, int side) {
		if (m.moveKind == Move.ADD) {
			if (getPieces(side) >= 10) {
				return;
			}
			table[m.x1][m.y1] = EMPTY;
			if (side == WHITE) {
				int[] y = new int[2];
				y[0] = m.x2;
				y[1] = m.y2;
				currentWhitePieces.remove(y);
				addPiecesWhite++;
			}
			if (side == BLACK) {
				int[] y = new int[2];
				y[0] = m.x2;
				y[1] = m.y2;
				currentBlackPieces.remove(y);
				addPiecesBlack++;
			}
		}
		if (m.moveKind == Move.STEP) {
			table[m.x2][m.y2] = side;
			table[m.x1][m.y1] = EMPTY;
			if (side == WHITE) {
				int[] y = new int[2];
				y[0] = m.x2;
				y[1] = m.y2;
				currentWhitePieces.remove(y);
				addPiecesWhite++;
			}
			if (side == BLACK) {
				int[] y = new int[2];
				y[0] = m.x2;
				y[1] = m.y2;
				currentBlackPieces.remove(y);
				addPiecesBlack++;
			}
			Move g = new Move(m.x1, m.x2);
			doMove(g, side);
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
		if (x > 7 || x < 0 || y > 7 || y < 0) {
			return EMPTY;
		} else {
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
		int[] s = new int[2];
		s[0] = x;
		s[1] = y;
		connections.insertBack(s);
		int color = table[x][y];
		if (table[x][y] != EMPTY) {
			// left
			for (int i = x - 1; i >= 0; i--) {
				if (table[i][y] == color) {
					int[] p = new int[2];
					p[0] = i;
					p[1] = y;
					connections.insertBack(p);
					break;
				} else if (table[i][y] != color && table[i][y] != EMPTY) {
					break;
				}
			}
			// right
			for (int i = x + 1; i <= 7; i++) {
				if (table[i][y] == color) {
					int[] p = new int[2];
					p[0] = i;
					p[1] = y;
					connections.insertBack(p);
					break;
				} else if (table[i][y] != color && table[i][y] != EMPTY) {
					break;
				}
			}
			// up
			for (int i = y - 1; i >= 0; i--) {
				if (table[x][i] == color) {
					int[] p = new int[2];
					p[0] = x;
					p[1] = i;
					connections.insertBack(p);
					break;
				} else if (table[x][i] != color && table[x][i] != EMPTY) {
					break;
				}
			}
			// down
			for (int i = y + 1; i <= 7; i++) {
				if (table[x][i] == color) {
					int[] p = new int[2];
					p[0] = x;
					p[1] = i;
					connections.insertBack(p);
					break;
				}
			}
			// upper left
			int j = x - 1;
			for (int i = y - 1; i >= 0 && j >= 0; i--) {
				if (table[j][i] == color) {
					int[] p = new int[2];
					p[0] = j;
					p[1] = i;
					connections.insertBack(p);
					break;
				} else if (table[j][i] != color && table[j][i] != EMPTY) {
					break;
				} else if (j < 0 || i < 0) {
					break;
				}
				j -= 1;
			}
			// upper right
			j = x + 1;
			for (int i = y - 1; i >= 0 && j <= 7; i--) {
				if (table[j][i] == color) {
					int[] p = new int[2];
					p[0] = j;
					p[1] = i;
					connections.insertBack(p);
					break;
				} else if (table[j][i] != color && table[j][i] != EMPTY) {
					break;
				} else if (j < 0 || i < 0) {
					break;
				}
				j += 1;
			}
			// lower left
			j = x - 1;
			for (int i = y + 1; i <= 7 && j >= 0; i++) {
				if (table[j][i] == color) {
					int[] p = new int[2];
					p[0] = j;
					p[1] = i;
					connections.insertBack(p);
					break;
				} else if (table[j][i] != color && table[j][i] != EMPTY) {
					break;
				} else if (j < 0 || i < 0) {
					break;
				}
				j -= 1;
			}
			// lower right
			j = x + 1;
			for (int i = y + 1; i <= 7 && j <= 7; i++) {
				if (table[j][i] == color) {
					int[] p = new int[2];
					p[0] = j;
					p[1] = i;
					connections.insertBack(p);
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
	
	public boolean isItInGoal (int side, int [] position) {
		if (side == WHITE) {
			if (position[0] == 7) {
				return true;
			}
		}
		if (position[1] == 7) {
			return true;
		}
		return false; 
	}

	/*
	 * have isNetwork return 100 if you won, 0 if your still going, or -100 if
	 * you lost
	 * 
	 * @param x: takes in the color of the chip
	 */
	public int isNetwork(int side) {
		if (side == WHITE) {
			if (isNetworkHelper(BLACK)) {
				return -100;
			}
			if (isNetworkHelper(WHITE)) {
				return 100;
			}
		} else if (side == BLACK) {
			if (isNetworkHelper(WHITE)) {
				return -100;
			}
			if (isNetworkHelper(BLACK)) {
				return 100;
			}
		}
		return 0;
	}
	
	private boolean isNetworkHelper(int side) {
		DList goalsHolder = getGoalPieces(side);
		
		try {
			while (!goalsHolder.isEmpty()) {
				HashTableChained memory = new HashTableChained(); 
				DListNode current = (DListNode) goalsHolder.front(); //make sure to fix getGoalPieces implementation
				current.remove(); //fulfills the pop
				int [] position = (int []) current.item(); //position is the coordinates
				memory.insert(position, 1); //inserting the goal's coordinates into the memory. the value of 1 is arbitrary
				DList connections = singleConnections(position[0], position[1]); //a dlist of all singleconnections to a chip
				for (int i = 0; i < connections.length(); i++) {
					DListNode first = (DListNode) connections.front(); //the first connections
					int [] firstC = (int[]) first.item();
					first.remove();
					if (dfsGetLeaf(firstC, memory, 0, side, direction(position, firstC))) {
						return true;
					}
				}
				memory.remove(position);
			}
		}
		catch (InvalidNodeException e) {}
		return false; 
	}
	
	private boolean dfsGetLeaf (int[] node, HashTableChained memory, int depth, int side, int[] direction) {
		memory.insert(node, 1);
		if (depth >= 4 && isItInGoal(side, node)) {
			return true;
		} else {
			try {
			DList connections = singleConnections(node[0], node[1]);
			for (int i = 0; i < connections.length(); i++) {
				DListNode first = (DListNode) connections.front(); //the first connections
				int [] firstC = (int[]) first.item();
				first.remove();
				if (memory.find(firstC) != null) {
					continue;
				}
				if (direction(node, firstC) == direction) {
					continue;
				}
				if (dfsGetLeaf(firstC, memory, depth + 1, side, direction(node, firstC))) {
					return true;
				}
			}
			memory.remove(node);
			} catch (InvalidNodeException e) {}
		}		
		return false;
	}

	private int[] direction(int[] first, int[] second) {
		int diff1 = first[0] - second[0];
		int diff2 = first[1] - second[1];
		if (diff1 > 0) {
			if (diff2 == 0) {
				return EAST;
			} else if (diff2 > 0) {
				return SOUTHEAST;
			} else {
				return SOUTHWEST;
			}
		} else if (diff1 == 0) {
			if (diff2 > 0) {
				return SOUTH;
			} else {
				return NORTH;
			}
		} else {
			if (diff2 == 0) {
				return WEST;
			} else if (diff2 > 0) {
				return NORTHWEST;
			} else {
				return NORTHEAST;
			}
		}
	}

}