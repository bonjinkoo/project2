package player;

public class Tester {

	public static void main(String args[]) {
		Board testB = new Board();
		System.out.println(testB.cellContents(0, 0));
		testB.addBlackPiece(1, 0);
		testB.addWhitePiece(2, 0);
		System.out.println(testB.cellContents(1, 0));
		System.out.println(testB.cellContents(2, 0));
	}
}