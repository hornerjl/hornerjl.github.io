package application;

import java.util.ArrayList;

public class PointAwardingSystem {
	private static int mComputerTotal = 0;
	private static int mUserTotal = 0;

	public int pointsAwarded(int wordLength) {

		int wordPoint = 0;

		switch (wordLength) {
		case 3:
			wordPoint = 1;
			break;
		case 4:
			wordPoint = 1;
			break;
		case 5:
			wordPoint = 2;
			break;
		case 6:
			wordPoint = 3;
			break;
		case 7:
			wordPoint = 5;
			break;
		case 8:
		case 9:
		case 10:
			wordPoint = 11;
			break;
		default:
			break;
		}

		return wordPoint;

	}

	public void printFinalTotal() {
		ArrayList<String> wordsComputerFound = ComputerWordFinder.mComputerWords;
		System.out.println("Your total score is: " + mUserTotal + " Out of a possible: " + mComputerTotal);
		System.out.println("Possible words: ");

		for (String word : wordsComputerFound) {
			if (word.length() > 2) {
				System.out.println(word);
			}

		}

	}

	public void computerPointsTally(int wordLength) {
		int wordPoint = pointsAwarded(wordLength);
		mComputerTotal += wordPoint;

	}

	public void userPointsTally(int wordLength) {
		int wordPoint = pointsAwarded(wordLength);
		mUserTotal += wordPoint;

		if (wordPoint == 0) {
			System.out.println("Invalid Word length");
			return;
		}

		System.out.println(wordPoint);
		System.out.println("Current Point Total = " + mUserTotal);
	}

	/*
	 * Points awarded no of letters | points per word 3 1 4 1 5 2 6 3 7 5 8+ 11
	 * 
	 */

}
