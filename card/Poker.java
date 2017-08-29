package card;

import card.Card.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Poker {
	
	private List<Card> cards;
	
	public Poker() {
		cards = new ArrayList<>();
		for (Suit suit : Suit.values())
			for(Symbol symbol : Symbol.values())
				cards.add(new Card(suit, symbol));
	}
	
	public Poker shuffle() {
		Collections.shuffle(cards);
		return this;
	}
	
	public Card take() {
		return cards.remove(0);
	}
	
	public Poker put(Card card) {
		cards.add(card);
		return this;
	}
	
	public Poker put(List<Card> cards) {
		this.cards.addAll(cards);
		return this;
	}
	
	public Poker put(Poker poker) {
		cards.addAll(poker.cards);
		return this;
	}
	
	public Poker empty() {
		cards.clear();
		return this;
	}
}
