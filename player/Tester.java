package player;

public class Tester {

	public static void main(String args[]) {
		Board testB = new Board();
		testB.addBlackPiece(4, 4);
		testB.addBlackPiece(4, 1);
		testB.addBlackPiece(6, 2);
		testB.addBlackPiece(6, 4);
		testB.addBlackPiece(6, 6);
		testB.addBlackPiece(4, 6);
		testB.addBlackPiece(2, 6);
		testB.addBlackPiece(2, 4);
		testB.addBlackPiece(2, 2);
		testB.addBlackPiece(4, 2);
		System.out.println(testB.singleConnections(4, 4));
		// TEST IF WHITE PIECE BLOCKS
	}
}