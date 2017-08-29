package blackjack;

import java.io.IOException;

public class Game {
	
	public static void main(String[] args) throws IOException {
	
		warmup();
		new BlackJack().play();
	}
	
	public static void warmup() {
		for (int i = 0; i < 110; i++) {
			for (int j = 0; j < 40; j++)
				System.out.print(".");
			System.out.println();
		}
	}
}
