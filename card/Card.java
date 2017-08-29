package card;

import View;

public class Card {
	
	public static final enum Suit {
		Spade("♤"), Heart("♡"),
		Diamond("♢"), Club("♧");
		
		private final String str;
		
		private Suit(String str) {
			this.str = str;
		}

		@Override
		public String toString() {
			return str;
		}

	}
	
	public static final enum Symbol {
		Ace("Ａ", 1), Two("⒉", 2), Three("⒊", 3),
		Four("⒋", 4), Five("⒌", 5), Six("⒍", 6),
		Seven("⒎", 7), Eight("⒏", 8), Nine("⒐", 9),
		Ten("⒑", 10), Jack("Ｊ", 10), Queen("Ｑ", 10),
		King("Ｋ", 10);
		
		private final int score;
		
		private final String str;
		
		private Symbol(String str, int score) {
			this.str = str;
			this.score = score;  
		}

		@Override
		public String toString() {
			return str;
		}
	}
	
	private static String[] front = {
		"┏━━━━┓",
		"┃♤　　　┃",
		"┃Ａ　　　┃",
		"┃　　　　┃",
		"┃　　　Ａ┃",
		"┃　　　♤┃",
		"┗━━━━┛"
	};
	
	private static String[] back = {
		"┏━━━━┓",
		"┃▚▓▒▞┃",
		"┃▓▚▞▒┃",
		"┃▓▓▒▒┃",
		"┃▓▞▚▒┃",
		"┃▞▓▒▚┃",
		"┗━━━━┛"
	};
	
	public final Suit suit;
	public final Symbol symbol;
	public final int score;
	
	public Card(Suit suit, Symbol symbol) {
		this.suit = suit;
		this.symbol = symbol;
		this.score = symbol.score;
	}
	
	public View front() {
		return new View(front).
			fill(1, 1, suit.toString()).
			fill(2, 1, symbol.toString()).
			fill(4, 4, symbol.toString()).
			fill(5, 4, suit.toString());
	}
	
	public static View back() {
		return new View(back);
	}
}
