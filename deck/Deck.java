package deck;

import View;
import card.Card;
import card.Card.Symbol;
import java.util.ArrayList;
import java.util.List;

public abstract class Deck {
	
	public List<Card> cards;
	
	public Deck() {
		cards = new ArrayList<>();
	}
	
	public void put(Card card) {
		cards.add(card);
	}
	
	public View inTurn() {
		View view = cardList();
		return view.fill(5, 1, "＊");
	}
	
	public View offTurn() {
		return cardList();
	}
	
	public View gameover() {
		return cardList();
	}
	
	private View cardList() {
		View view = new View();
		for (int i = 0; i < cards.size(); i++)
			view.fill(0, 2 * i, cards.get(i).front());
		String score = parseScore(score());
		view.fill(1, view.width() - score.length() - 1, score);
		return view;
	}
	
	public int score() {
		int ace = 0;
		int score = 0;
		for (Card card : cards) {
			Symbol symbol = card.symbol;
			if (symbol == Symbol.Ace)
				ace++;
			score += card.score;
		}
		while (ace-- > 0 && score < 12)
			score += 10;
		return score;
	}
	
	public String parseScore(int score) {
		String parse = "";
		if (score == 21)
			parse = "ＢＪ";
		else if (score > 21)
			parse = "ＢＳ";
		else {
			String t = score + "";
			for (int i = 0; i < t.length(); i++)
				parse += (char)(t.charAt(i) + '０' - '0');
		}
		return parse;
	}
}
