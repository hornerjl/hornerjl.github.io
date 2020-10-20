package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DictionaryChecker {
	
	static ArrayList<String> mDictionaryWords = new ArrayList<String>();
	
	public static boolean readWordFile() {

		try {
			BufferedReader readFile = new BufferedReader(new FileReader("dictionary-yawl.txt"));
			String string;
			while ((string = readFile.readLine()) != null) {
				mDictionaryWords.add(string);
			}

			readFile.close();

		}

		catch (IOException e) {
			System.out.println(e);
		}
		return false;

	}

}
