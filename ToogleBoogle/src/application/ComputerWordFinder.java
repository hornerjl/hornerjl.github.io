package application;

import java.util.*;

public class ComputerWordFinder {

	static ArrayList<String> mComputerWords = new ArrayList<String>();
	static ArrayList<int[]> mPositionsUsed = new ArrayList<int[]>();

	
	PointAwardingSystem points = new PointAwardingSystem();

	static int R;
	static int C;

	static int[] x = { -1, -1, -1, 0, 0, 1, 1, 1 };
	static int[] y = { -1, 0, 1, -1, 1, -1, 0, 1 };

	static boolean search2DArray(char[][] grid, int row, int col, String word) {
		if (word.length() < 3) {
			return false;
		}
		if (grid[row][col] != word.charAt(0))
			return false;

		mPositionsUsed.clear();
		mPositionsUsed.add(new int[] {row, col});		
		
		return recursive2DArray(grid, row, col, word, 1);
	}

	static boolean recursive2DArray(char[][] grid, int row, int col, String word, int k) {

		int len = word.length() - 1;

		directionLoop: for (int dir = 0; dir < 8; dir++) {
			int rd = row + x[dir];
			int cd = col + y[dir];

			if (rd >= R || rd < 0 || cd >= C || cd < 0) {
				continue;
			}
			
			for(int[] position : mPositionsUsed) {
				if(rd == position[0] && cd == position[1]) {
					continue directionLoop;
				}
			}
			if (grid[rd][cd] == word.charAt(k)) {

				if (k == len) {
					return true;
				} else {
					mPositionsUsed.add(new int[] {rd, cd});
					return recursive2DArray(grid, rd, cd, word, ++k);
				}
			}

		}

		return false;

	}

	static void patternSearch(char[][] grid, String word) {
		R = grid[0].length;
		C = grid.length;
		for (int row = 0; row < R; row++) {
			for (int col = 0; col < C; col++) {
				if (search2DArray(grid, row, col, word)) {
					mComputerWords.add(word);
				}
			}
		}
	}

	public static boolean checkUserInput(String word) {
		if (mComputerWords.contains(word)) {
			mComputerWords.remove(word);
			return true;
		} else {
			return false;
		}
	}

	private void checkDictionary(char[][] board) {

		DictionaryChecker.readWordFile();

		for (String word : DictionaryChecker.mDictionaryWords) {
			patternSearch(board, word);
		}

		for (String word : mComputerWords) {
			int wordLength = word.length();
			points.computerPointsTally(wordLength);

		}

	}

	public void run(char[][] board) {
		checkDictionary(board);
	}
}
