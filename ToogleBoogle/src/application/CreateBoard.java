package application;

import java.util.*;

public class CreateBoard {

	private static char[][] box() {

		int boardSize = 0;

		int userBoxInput = 0;
		Scanner boxScanner = new Scanner(System.in);
		
		System.out.println("Please enter a board size! \n");
		System.out.print("1.  4 x 4 \n");
		System.out.print("2.  6 x 6 \n");
		System.out.print("3.  8 x 8 \n");
		System.out.print("4. 10 x 10 \n");

		userBoxInput = boxScanner.nextInt();

		switch (userBoxInput) {
		case 1:
			boardSize = 4;
			break;
		case 2:
			boardSize = 6;
			break;
		case 3:
			boardSize = 8;
			break;
		case 4:
			boardSize = 10;
			break;
		default:
			System.out.println("invalid board size! Set to default size four.");
			boardSize = 4;
			break;
		}

		int rows = boardSize;
		int columns = boardSize;

		char[][] letters = new char[rows][columns];

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				letters[r][c] = (char) ((int) (Math.random() * 26) + 'A');
				System.out.print(letters[r][c] + " ");
			}
			System.out.println("");

		}

		return letters;

	}

	public char[][] run() {
		return box();

	}

}
