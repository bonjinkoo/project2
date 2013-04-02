/* MachinePlayer.java */

package player;

import list.*;

/**
 * An implementation of an automatic Network player. Keeps track of moves made
 * by both players. Can select a move for itself.
 */
public class MachinePlayer extends Player {

	// instance variables here
	private Board gameboard;
	private int chipcolor;
	private int opponentcolor;
	private int searchdepth;

	// Creates a machine player with the given color. Color is either 0 (black)
	// or 1 (white). (White has the first move.)
	public MachinePlayer(int color) {
		this.chipcolor = color;
		if (chipcolor == gameboard.WHITE) {
			opponentcolor = gameboard.BLACK;
		} else {
			opponentcolor = gameboard.WHITE;
		}
		this.gameboard = new Board();
		searchdepth = 1000000;
	}

	public Board getBoard() {
		return gameboard;
	}

	// Creates a machine player with the given color and search depth. Color is
	// either 0 (black) or 1 (white). (White has the first move.)
	public MachinePlayer(int color, int searchDepth) {
		this(color);
		this.searchdepth = searchDepth;
	}

	// Returns a new move by "this" player. Internally records the move (updates
	// the internal game board) as a move by "this" player.
	public Move chooseMove() {
		Move holder = new Move(); // ***POSSIBLE BUG: we might be returning a
									// quitmove if the invalidnodeexception
									// occurs
		holder = minimax(chipcolor, -100, 100, searchdepth).move;
		gameboard.doMove(holder, chipcolor);
		return holder;
	}

	// helper 1: goes through the tree of the computers moves and the opponents
	/*
	 * @side is currently going (black or white)
	 * 
	 * @alpha is the computers score
	 * 
	 * @beta is the opponents score
	 */
	public Best minimax(int side, int alpha, int beta, int depth) { // side will
																	// tell us
																	// whose
																	// minimax
																	// its
																	// calling
		Best myBest = new Best(); // will be updated to mybest possible move
		Best reply; // will be updated to opponents best possible move
		int x = gameboard.isNetwork(chipcolor); // returns whether we won, lost,
												// or need to keep playing

		if (x == 100 || x == -100) { // base case: if you won or lost
			myBest.score = x;
			return myBest;
		}

		if (searchdepth == 0) {
			return new Best(eval());
		}

		if (side == chipcolor) { // first find out whose going
			myBest.score = alpha;
		} else {
			myBest.score = beta;
		}

		DList legalmoves = allLegalMoves(side); // look at all the legalmoves of
												// one side
		ListNode first = legalmoves.front();
		try { // ***POSSIBLE BUG: don't know what happens when there's a try and
				// a throw??
			while (first.isValidNode()) {
				gameboard.doMove((Move) first.item(), side); // Modifies "this"
																// Grid
				reply = minimax((side + 1) % 2, alpha, beta, depth - 1);
				gameboard.undoMove((Move) first.item(), side); // Restores
																// "this" Grid
				if ((side == chipcolor) && (reply.score >= myBest.score)) {
					myBest.move = (Move) first.item();
					myBest.score = reply.score;
					alpha = reply.score;
				} else if ((side == opponentcolor)
						&& (reply.score <= myBest.score)) {
					myBest.move = (Move) first.item();
					myBest.score = reply.score;
					beta = reply.score;
				}
				if (alpha >= beta) {
					return myBest;
				}
			}
		} catch (InvalidNodeException e) {
			System.out.println(e);
		}
		return myBest;
	}

	// helper 2: offers a score to the leaf of the node
	// to make the score between 1 and -1 make sure you divide by the maximum
	// number of combinations that there can possible be
	// lets use 80 to be safe...no way there are more than 80 connections
	public int eval() {
		int counter = 0;
		int counterOpponent = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (gameboard.findPiece(x, y) == chipcolor) {
					counter += gameboard.singleConnections(x, y).length();
				}
				if (gameboard.findPiece(x, y) == opponentcolor) {
					counterOpponent += gameboard.singleConnections(x, y)
							.length();
				}
			}
		}
		int score = (counter - counterOpponent);
		return score;
	}

	// If the Move m is legal, records the move as a move by the opponent
	// (updates the internal game board) and returns true. If the move is
	// illegal, returns false without modifying the internal state of "this"
	// player. This method allows your opponents to inform you of their moves.
	public boolean opponentMove(Move m) {
		if (!islegal(m, opponentcolor)) {
			return false;
		}
		gameboard.doMove(m, opponentcolor);
		return true;
	}

	// If the Move m is legal, records the move as a move by "this" player
	// (updates the internal game board) and returns true. If the move is
	// illegal, returns false without modifying the internal state of "this"
	// player. This method is used to help set up "Network problems" for your
	// player to solve.
	public boolean forceMove(Move m) {
		if (!islegal(m, chipcolor)) {
			return false;
		}
		gameboard.doMove(m, chipcolor);
		return true;
	}

	// islegal helper
	public boolean moveOutOfBounds(Move m) {
		if (m.x1 < 0 || m.x1 > 7 || m.y1 < 0 || m.y1 > 7 || m.x2 < 0
				|| m.x2 > 7 || m.y2 < 0 || m.y2 > 7) {
			return true;
		}
		return false;
	}

	// islegal helper that counts up whether there are too many neighbors or not
	public boolean badNeighbors(Move m, int chipcolor) {
		Move holder = new Move();
		int counter = 0;
		if (gameboard.findPiece(m.x1 + 1, m.y1) == chipcolor) {
			holder = new Move(m.x1 + 1, m.y1);
			counter++;
		}
		if (gameboard.findPiece(m.x1 - 1, m.y1) == chipcolor) {
			holder = new Move(m.x1 - 1, m.y1);
			counter++;
		}
		if (gameboard.findPiece(m.x1, m.y1 + 1) == chipcolor) {
			holder = new Move(m.x1, m.y1 + 1);
			counter++;
		}
		if (gameboard.findPiece(m.x1, m.y1 - 1) == chipcolor) {
			holder = new Move(m.x1, m.y1 - 1);
			counter++;
		}
		if (gameboard.findPiece(m.x1 + 1, m.y1 + 1) == chipcolor) {
			holder = new Move(m.x1 + 1, m.y1 + 1);
			counter++;
		}
		if (gameboard.findPiece(m.x1 - 1, m.y1 - 1) == chipcolor) {
			holder = new Move(m.x1 - 1, m.y1 - 1);
			counter++;
		}
		if (gameboard.findPiece(m.x1 + 1, m.y1 - 1) == chipcolor) {
			holder = new Move(m.x1 + 1, m.y1 - 1);
			counter++;
		}
		if (gameboard.findPiece(m.x1 - 1, m.y1 + 1) == chipcolor) {
			holder = new Move(m.x1 - 1, m.y1 + 1);
			counter++;
		}
		if (counter > 1) {
			return true;
		}
		if (counter == 1) {
			if (badNeighbors2(holder, chipcolor) >= 1) {
				return true;
			}
		}
		return false;
	}

	public int badNeighbors2(Move m, int chipcolor) {
		int counter = 0;
		if (gameboard.findPiece(m.x1 + 1, m.y1) == chipcolor) {
			counter++;
		}
		if (gameboard.findPiece(m.x1 - 1, m.y1) == chipcolor) {
			counter++;
		}
		if (gameboard.findPiece(m.x1, m.y1 + 1) == chipcolor) {
			counter++;
		}
		if (gameboard.findPiece(m.x1, m.y1 - 1) == chipcolor) {
			counter++;
		}
		if (gameboard.findPiece(m.x1 + 1, m.y1 + 1) == chipcolor) {
			counter++;
		}
		if (gameboard.findPiece(m.x1 - 1, m.y1 - 1) == chipcolor) {
			counter++;
		}
		if (gameboard.findPiece(m.x1 + 1, m.y1 - 1) == chipcolor) {
			counter++;
		}
		if (gameboard.findPiece(m.x1 - 1, m.y1 + 1) == chipcolor) {
			counter++;
		}
		return counter;
	}

	public boolean islegal(Move m, int chipcolor) {
		// outofbounds
		if (moveOutOfBounds(m)) {
			return false;
		}

		// corners
		if (gameboard.ifCorner(m)) {
			return false;
		}

		// non-empty squares
		if (gameboard.findPiece(m.x1, m.y1) != Board.EMPTY) {
			return false;
		}

		// goal areas
		if (chipcolor == Board.WHITE) {
			if (m.y1 == 0 || m.y1 == 7) {
				return false;
			}
		}
		if (chipcolor == Board.BLACK) {
			if (m.x1 == 0 | m.x1 == 7) {
				return false;
			}
		}

		// addmoves that should be stepmoves
		if (m.moveKind == Move.ADD) {
			if (gameboard.getPieces(chipcolor) <= 0) {
				return false;
			}
		}

		// stepmoves that should be addmoves and stepmoves that try to move the
		// other team
		if (m.moveKind == Move.STEP) {
			if (gameboard.getPieces(chipcolor) > 0) {
				return false;
			}
			if (gameboard.findPiece(m.x2, m.y2) != chipcolor) {
				return false;
			}
		}

		// checks triples
		if (badNeighbors(m, chipcolor)) {
			return false;
		}

		return true;
	}

	/*
	 * if add move for loop through all empty array slots if m is legal on that
	 * slot store it if step move for loop through ten times total (the number
	 * of total pieces) the piece to move = a specific chosen piece for loop
	 * through all the possible step moves of that chosen piece store it
	 */
	public DList allLegalMoves(int chipcolor) {
		DList storer = new DList();
		// Add Moves
		if (gameboard.getPieces(chipcolor) > 0) {
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (gameboard.findPiece(x, y) == (Board.EMPTY)) {
						Move tempMove = new Move(x, y);
						if (islegal(tempMove, chipcolor)) {
							storer.insertBack(tempMove);
						}
					}
				}
			}
		}

		// Step Moves
		if (gameboard.getPieces(chipcolor) == 0) {
			Move[] moveHolder = gameboard.allPiecesOnBoard(chipcolor);
			for (int i = 0; i < moveHolder.length; i++) {
				Move current = moveHolder[i]; // ***POSSIBLE BUG: holds addMoves
												// only!!! not stepMoves...
												// abstraction?
				for (int x = 0; x < 8; x++) {
					for (int y = 0; y < 8; y++) {
						if (gameboard.findPiece(x, y) == (Board.EMPTY)) {
							Move tempMove2 = new Move(x, y, current.x1,
									current.y2); // ***POSSIBLE BUG: not sure if
													// coordinates are right
							if (islegal(tempMove2, chipcolor)) {
								storer.insertBack(tempMove2);
							}
						}
					}
				}
			}
		}

		return storer;
	}

}
