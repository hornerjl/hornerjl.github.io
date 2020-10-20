package application;

public class Main {

	public static void main(String[] args) {

		System.out.println("Welcome to Toogle Boogle!");
		System.out.println();
		System.out.println("The rules are simple.");
		System.out.println("Determine the boardsize from the menu below.");
		System.out.println("Your three minute timer will start immediatly after.");
		System.out.println("Find as many words as you can without repeating the same word");
		System.out.println("    unless it is found in a different board location.");
		System.out.println();
		System.out.println("Points are awarded based on the length of the word as follows: ");
		System.out.println();
		System.out.printf("Word Length:   1-2\t3-4\t5\t6\t7\t8+\n");
		System.out.printf("Point awarded:  0 \t 1 \t2\t3\t5\t11\n");
		System.out.println();
		System.out.println("Your points will then be told vs possible points for that given board.");
		System.out.println();
		System.out.println("Good Luck and have fun!");
		System.out.println();


		CreateBoard boardCreater = new CreateBoard();
		
		char[][] board = boardCreater.run();
		
		ComputerWordFinder findWord = new ComputerWordFinder();
		findWord.run(board);
		
		Thread input = new Thread(new ChecksUserInput());
		Thread timer = new Thread(new Timer(input));

		try {
			timer.join();
		} catch (InterruptedException e) {
			System.out.println("timer stopped");
			e.printStackTrace();
		}

		try {
			input.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		timer.start();
		input.start();

	}

}
