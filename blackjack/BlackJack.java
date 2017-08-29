package blackjack;

import View;
import card.Poker;
import deck.DealerDeck;
import deck.Deck;
import deck.PlayerDeck;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BlackJack {
	
	public static final int CHOICE_HIT    = 1;
	public static final int CHOICE_STAND  = 2;
	public static final int CHOICE_DOUBLE = 3;
	public static final int CHOICE_SPLIT  = 4;
	
	String TITLE  = "┏━━━━━━━━━━━━━━━━━━━━━━┓\n"
				  + "┃　　　　　　　　　　　ＢＬＡＣＫ　ＪＡＣＫ　┃\n"
				  + "┃　　　　　　　　　　　　　　　　　　　　　　┃\n"
				  + "┃　ｗａｇｅｒ　　　　　　　　　　　　　　　　┃\n"
				  + "┗━━━━━━━━━━━━━━━━━━━━━━┛";
	String TOP    = "━━━━━━━━━━━━━━━━━━━━━━━┓";
	String CARD   = "　　　　　　　　　　　　　　　　　　　　　　　┃";
	String MIDDL1 = "━━━━━━━━━━━━━━━━━━━━━━━┫";
	String MIDDLE = "┏━━━━━━━━━━━━━━━━━━━━━━┫";
	String INFO   = "┣━　　　　　　　　　　　　　　　　　　　　　┃";
	String HIT    = "┣━　Ｈｉｔ　　　　　　　　　　　　　　　　　┃";
	String STAND  = "┣━　Ｓｔａｎｄ　　　　　　　　　　　　　　　┃";
	String DOUBLE = "┣━　Ｄｏｕｂｌｅ　　　　　　　　　　　　　　┃";
	String SPLIT  = "┣━　Ｓｐｌｉｔ　　　　　　　　　　　　　　　┃";
	String BOTM   = "┗━━━━━━━━━━━━━━━━━━━━━━┛";
	String BOTM1  = "━━━━━━━━━━━━━━━━━━━━━━━┛";
	String TOP1   = "┏━━━━━━━━━━━━━━━━━━━━━━┓";
	String STATS  =	"┣━　ｗｉｎ　　　　　　　　　　　　　　　　　┃\n"
				  + "┣━　ｐｕｓｈ　　　　　　　　　　　　　　　　┃\n"
				  + "┣━　ｌｏｓｓ　　　　　　　　　　　　　　　　┃\n"
				  + "┣━　ｂｕｓｔ　　　　　　　　　　　　　　　　┃\n"
				  + "┣━　ｂｊａｃｋ　　　　　　　　　　　　　　　┃\n"
				  + "┣━　ｓｕｍｍｉｔ　　　　　　　　　　　　　　┃";
	
	private static final int ISWIN   = 0;
	private static final int ISPUSH  = 1;
	private static final int ISLOSS  = 2;
	private static final int ISBUST  = 3;
	private static final int ISBJACK = 4;
	private static final int ISUMMIT = 5;

	private List<Deck> decks;
	private int[] scores;
	private int[] stats;
	private Poker poker;
	private int money;

	public BlackJack() {
		decks = new ArrayList<>();
		poker = new Poker();
		scores = new int[5];
		stats = new int[10];
		money = 1000;
	}
	
	public BlackJack poker(Poker poker) {
		this.poker = poker;
		return this;
	}
	
	public BlackJack money(int money) {
		this.money = money;
		return this;
	}
	
	public void play() throws IOException {
		initStats();
		while (money > 0) {
			title();
			deal();
			wager();
			player();
			dealer();
			gameover();
		}
		showStats();
	}
	
	private void title() {
		System.out.println(new View(TITLE));
	}
	
	private void deal() {
		poker.shuffle();
		Deck dealerDeck = new DealerDeck();
		dealerDeck.put(poker.take());
		dealerDeck.put(poker.take());
		Deck playerDeck = new PlayerDeck();
		playerDeck.put(poker.take());
		playerDeck.put(poker.take());
		decks.add(dealerDeck);
		decks.add(playerDeck);
	}
	
	private void wager() throws IOException {
		PlayerDeck deck = (PlayerDeck) decks.get(1);
		deck.wager = Helper.readNumber(prompt(), new Predicate<Integer>() {
			@Override
			public boolean test(Integer wager) {
				return money >= wager && wager > 0;
			}
		});
		money -= deck.wager;
	}
	
	private void player() throws IOException {
		for (int i = 1; i < decks.size(); i++) {
			PlayerDeck deck = (PlayerDeck) decks.get(i);
			while (deck.score() < 21) {
				System.out.println(gameView(i));
				int choice = choose(i);
				if (choice == CHOICE_HIT) {
					deck.put(poker.take());
				} else if (choice == CHOICE_STAND) {
					break;
				} else if (choice == CHOICE_DOUBLE) {
					money -= deck.wager;
					deck.wager *= 2;
					deck.put(poker.take());
					break;
				} else if (choice == CHOICE_SPLIT) {
					money -= deck.wager;
					PlayerDeck split = deck.split();
					deck.put(poker.take());
					split.put(poker.take());
					decks.add(split);
				}
			}
			scores[i] = deck.score();
		}
	}
	
	private void dealer() throws IOException {
		// check bust
		boolean bust = true;
		for (int i = 1; i < decks.size(); i++)
			if (scores[i] <= 21)
				bust = false;
		DealerDeck deck = (DealerDeck) decks.get(0);
		if (!bust) {
			while (deck.score() < 17) {
				System.out.println(gameView(0));
				System.out.print(prompt("continue"));
				Helper.readLine();
				deck.put(poker.take());
			}
		}
		scores[0] = deck.score();
	}
	
	private void gameover() throws IOException {
		System.out.println(gameView(-1));
		for (Deck deck : decks)
			poker.put(deck.cards);
		decks.clear();
		String prompt = prompt(money > 0 ? "new hand" : "go home");
		System.out.print(prompt);
		Helper.readLine();
	}
	
	private void initStats() {
		for (int i = 0; i < stats.length; i++)
			stats[i] = 0;
		stats[ISUMMIT] = money;
	}
	
	private void showStats() {
		System.out.println(TOP1);
		String[] sstats = STATS.split("\n");
		for (int i = 0; i < sstats.length; i++) {
			View view = new View(sstats[i]);
			String val = Helper.numberToFullWidth(stats[i]);
			view.fill(0, view.width() - val.length() - 2, val);
			System.out.println(view);
		}
		System.out.println(BOTM);
	}
	
	private int choose(int cur) throws IOException {
		PlayerDeck deck = (PlayerDeck) decks.get(cur);
		final boolean canDouble = deck.canDouble(money);
		final boolean canSplit = deck.canSplit(money);
		return Helper.readNumber(prompt(), new Predicate<Integer>() {
			@Override
			public boolean test(Integer choice) {
				if (choice == CHOICE_HIT || choice == CHOICE_STAND)
					return true;
				if (choice == CHOICE_DOUBLE)
					return canDouble;
				if (choice == CHOICE_SPLIT)
					return canSplit;
				return false;
			}
		});
	}
	
	private String result(int cur) {
		int dealerScore = scores[0];
		int playerScore = scores[cur];
		int win = 0;
		boolean blackjack = false;
		boolean push = false;
		PlayerDeck deck = (PlayerDeck) decks.get(cur);
		if (playerScore <= 21) {
			if (playerScore == dealerScore) {
				win = deck.wager;
				push = true;
				stats[ISPUSH]++;
				if (playerScore == 21)
					stats[ISBJACK]++;
			} else if (playerScore == 21) {
				win = 3 * deck.wager;
				blackjack = true;
				stats[ISBJACK]++;
				stats[ISWIN]++;
			} else if (dealerScore > 21 || playerScore > dealerScore) {
				win = 2 * deck.wager;
				stats[ISWIN]++;
			} else {
				stats[ISLOSS]++;
			}
		} else {
			stats[ISLOSS]++;
			stats[ISBUST]++;
		}
		money += win;
		String info = Helper.numberToFullWidth(win);
		if (blackjack) info += "（＇⒈５）";
		if (push) info += "（ｐｕｓｈ）";
		if (money > stats[ISUMMIT])
			stats[ISUMMIT] = money;
		return info;
	}
	
	private View gameView(int cur) {
		View view = new View(TOP);
		for (int i = 0; i < decks.size(); i++) {
			Deck deck = decks.get(i);
			View curView = new View();
			if (cur < 0)
				curView.fill(0, 0, deck.gameover());
			else if (cur == i)
				curView.fill(0, 0, deck.inTurn());
			else
				curView.fill(0, 0, deck.offTurn());
			for (int h = 0; h < curView.height(); h++)
				curView.fill(h, 0, CARD, View.SPACE_TRANS);
			view.fill(view.height(), 0, curView);
			if (i == 0) {
				//view.fill(view.height(), 0, MIDDL1);
			}
		}
		if (cur > 0) {
			view.fill(view.height(), 0, MIDDLE);
			// add choice
			view.fill(view.height(), 0, HIT);
			view.fill(view.height(), 0, STAND);
			PlayerDeck deck = (PlayerDeck) decks.get(cur);
			if (deck.canDouble(money))
				view.fill(view.height(), 0, DOUBLE);
			if (deck.canSplit(money))
				view.fill(view.height(), 0, SPLIT);
			view.fill(view.height(), 0, BOTM);
		} else if (cur < 0) {
			view.fill(view.height(), 0, MIDDLE);
			// game over
			for (int i = 1; i < decks.size(); i++) {
				view.fill(view.height(), 0, INFO);
				view.fill(view.height() - 1, 3, result(i)); 
			}
			view.fill(view.height(), 0, BOTM);
		} else {
			view.fill(view.height(), 0, BOTM1);
		}
		return view;
	}
	
	private String prompt() {
		return "(" + money + ")> ";
	}
	
	private String prompt(String msg) {
		return prompt() + msg;
	}
}
