package deck;

public class PlayerDeck extends Deck {
	
	public int wager;
	
	public boolean canDouble(int money) {
		return cards.size() == 2 && money >= wager;
	}
	
	public boolean canSplit(int money) {
		return canDouble(money) &&
			cards.get(0).symbol == cards.get(1).symbol;
	}
	
	public PlayerDeck split() {
		PlayerDeck deck = new PlayerDeck();
		deck.put(cards.remove(0));
		deck.wager = wager;
		return deck;
	}
}
