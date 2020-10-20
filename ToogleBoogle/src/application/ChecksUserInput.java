package application;

import java.util.*;

public class ChecksUserInput implements Runnable {

	/*
	 * 
	 * is it possible to have it more than once?
	 * 
	 */
	private static String UserInput() {

		PointAwardingSystem points = new PointAwardingSystem();

		Scanner sc = new Scanner(System.in);
		System.out.println("Begin entering words!");
		String word = null;

		for (int i = 0; i < 10000; i++) {
			word = sc.nextLine();
			int wordLength = length(word);

			if (ComputerWordFinder.checkUserInput(word.toUpperCase()) == true) {
				points.userPointsTally(wordLength);
			} else {
				System.out.println("Invalid word");

			}
		}

		sc.close();
		return word;

	}

	private static int length(String word) {
		int wordLength = word.length();
		return wordLength;
	}

	public void run() {
		UserInput();

	}

}
