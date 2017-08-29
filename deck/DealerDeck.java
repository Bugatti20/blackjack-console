package deck;

import View;
import card.Card;

public class DealerDeck extends Deck {

	@Override
	public View offTurn() {
		View view = cards.get(0).front();
		return view.fill(0, 2, Card.back());
	}
}
