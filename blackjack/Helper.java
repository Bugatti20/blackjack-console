package blackjack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.function.Predicate;

public class Helper {
	
	public static int readNumber(String prompt, Predicate<Integer> filter) throws IOException {
		int number = 0;
		boolean get = false;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (!get) {
			System.out.print(prompt);
			try {
				number = Integer.parseInt(reader.readLine());
				get = filter.test(number);
			} catch (NumberFormatException e) { }
		}		
		return number;
	}
	
	public static String readLine() throws IOException {
		return new BufferedReader(new InputStreamReader(System.in)).readLine();
	}
	
	public static String numberToFullWidth(int number) {
		String normal = "" + number;
		String full = "";
		for (int i = 0; i < normal.length(); i++)
			full += (char)(normal.charAt(i) + '０' - '0');
		return full;
	}
}
