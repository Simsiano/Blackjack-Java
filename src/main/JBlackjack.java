package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

class Card {
	private final String tipo;
	private final String valore;

	public Card(String suit, String value) {
		this.tipo = suit;
		this.valore = value;
	}

	public String toString() {
		return valore + " di " + tipo;
	}

	public int getNumericValue() {
		if (valore.equals("Asso")) {
			return 11;
		} else if (valore.equals("Re") || valore.equals("Regina") || valore.equals("Jack")) {
			return 10;
		} else {
			return Integer.parseInt(valore);
		}
	}
}

class Deck {
	private final List<Card> Cards;
	private final List<Card> oldCards;

	public Deck() {
		Cards = new ArrayList<>();
		oldCards = new ArrayList<>();
		String[] suits = { "Cuori", "Quadri", "Fiori", "Picche" };
		String[] values = { "Asso", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Regina", "Re" };
		for (String suit : suits) {
			for (String value : values) {
				Cards.add(new Card(suit, value));
			}
		}
	}
	
	public void newDeck() {
		Cards.clear();
		String[] suits = { "Cuori", "Quadri", "Fiori", "Picche" };
		String[] values = { "Asso", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Regina", "Re" };
		for (String suit : suits) {
			for (String value : values) {
				Cards.add(new Card(suit, value));
			}
		}
	}

	public void shuffle() {
		Random rand = new Random();
		for (int i = Cards.size() - 1; i > 0; i--) {
			int j = rand.nextInt(i + 1);
			Card temp = Cards.get(i);
			Cards.set(i, Cards.get(j));
			Cards.set(j, temp);
		}
	}

	public Card drawCard(JFrame frame) {
		if (Cards.isEmpty()) {
			newDeck();
			System.out.println("new deck");
//			JOptionPane.showMessageDialog(frame, "Fiches insufficenti!", "Errore", JOptionPane.ERROR_MESSAGE);
//			throw new IllegalStateException("Nessuna carta nel mazzo.");
		}
		return Cards.remove(Cards.size() - 1);
	}
}

class BlackjackHand {
	public List<Card> cards;

	public BlackjackHand() {
		cards = new ArrayList<>();
	}

	public void addCard(Card card) {
		cards.add(card);
	}

	public int getHandValue() {
		int value = 0;
		int numAces = 0;

		for (Card card : cards) {
			value += card.getNumericValue();
			if (card.getNumericValue() == 11) {
				numAces++;
			}
		}

		while (value > 21 && numAces > 0) {
			value -= 10;
			numAces--;
		}

		return value;
	}
	
	public void clearHand() {
		cards.clear();
	}

	public void displayHand(JTextArea textArea) {
		for (Card card : cards) {
//            System.out.println(card);
			textArea.append(card.toString() + "\n");
		}
	}
}

public class JBlackjack extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextArea playerArea;
	private JTextArea dealerArea;
	private String[] valoriFiches = { "5", "10", "20", "25", "50", "100", "200", "500", "1000" };
	private JButton[] btnFiches;
	private JButton btnNewGame;
	private JButton confirm;
	private JButton btnHit;
	private JButton btnStand;
	private int fichesAttuali = 5000;
	private int fichesPuntate = 0;
	private int turnCounter = 0;
	private JLabel lblFichesAttuali = new JLabel("Fiches attuali:" + fichesAttuali, JLabel.CENTER);
	private JLabel lblFichesPuntate = new JLabel("Fiches puntate: " + fichesPuntate, JLabel.CENTER);
	private JLabel lblVincitore;
	private Deck deck;
	private BlackjackHand playerHand;
	private BlackjackHand dealerHand;

	public JBlackjack() {

		// Contiene bottone nuova partita e bottone info
		JPanel topPane = new JPanel();
		btnNewGame = new JButton("Nuova partita");
		btnNewGame.setEnabled(false);
		JButton btnInfo = new JButton("Info");
		btnNewGame.addActionListener(e -> newGame());
		topPane.setLayout(new FlowLayout(1));
		topPane.add(btnNewGame);
//		topPane.add(btnInfo);

		// Contiene il log delle carte del giocatore
		JPanel playerPane = new JPanel();
		TitledBorder playerBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"  Giocatore  ", 2, 5);
		playerArea = new JTextArea(20, 20);
		JScrollPane playerScroll = new JScrollPane(playerArea);
		playerPane.setBorder(playerBorder);
		playerPane.add(playerScroll);

		// Contiene label come: Fiches attuali, fiches puntate e il vincitore
		JPanel infoTopPane = new JPanel();
		TitledBorder infoTopBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "", 2, 5);
		lblVincitore = new JLabel("", JLabel.CENTER);
		JPanel subPane = new JPanel();
		subPane.setLayout(new BoxLayout(subPane, BoxLayout.Y_AXIS));
		infoTopPane.setLayout(new BorderLayout());
		lblFichesAttuali = new JLabel("Fiches attuali:" + fichesAttuali, JLabel.CENTER);
		lblFichesPuntate = new JLabel("Fiches puntate: " + fichesPuntate, JLabel.CENTER);
		lblFichesAttuali.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		lblFichesAttuali.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		lblFichesPuntate.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		lblFichesPuntate.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		lblVincitore.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		lblVincitore.setAlignmentY(JLabel.CENTER_ALIGNMENT);
//    	infoTopPane.setBorder(infoTopBorder);
		subPane.add(lblFichesAttuali);
		subPane.add(lblFichesPuntate);
		subPane.add(lblVincitore);
		infoTopPane.add(subPane, BorderLayout.CENTER);

		// Contiene i bottoni hit or stand
		JPanel infoBottomPane = new JPanel();
		TitledBorder infoBottomBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"  Azioni  ", 2, 5);
		btnHit = new JButton("Hit");
		btnHit.setEnabled(false);
		btnStand = new JButton("Stand");
		btnStand.setEnabled(false);
		btnHit.addActionListener(e -> actionHit());
		btnStand.addActionListener(e -> actionStand());
		infoBottomPane.setLayout(new FlowLayout(1));
		infoBottomPane.setBorder(infoBottomBorder);
		infoBottomPane.add(btnHit);
		infoBottomPane.add(btnStand);

		JPanel infoPane = new JPanel();
		infoPane.setLayout(new BorderLayout());
		infoPane.add(infoTopPane, BorderLayout.CENTER);
		infoPane.add(infoBottomPane, BorderLayout.SOUTH);

		// Contiene il log delle carte del dealer
		JPanel dealerPane = new JPanel();
		TitledBorder dealerBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "  Dealer  ",
				2, 5);
		dealerArea = new JTextArea(20, 20);
		JScrollPane dealerScroll = new JScrollPane(dealerArea);
		dealerPane.setBorder(dealerBorder);
		dealerPane.add(dealerScroll);

		// Contiene i bottoni per decidere quante fiches puntare
		JPanel betPane = new JPanel();
		TitledBorder betBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "  Punta  ", 1,
				2);

		confirm = new JButton("Conferma");
		betPane.setLayout(new FlowLayout(1));
		betPane.setBorder(betBorder);
		btnFiches = new JButton[valoriFiches.length];
		for (int i = 0; i < valoriFiches.length; i++) {
			btnFiches[i] = new JButton(valoriFiches[i]);
			betPane.add(btnFiches[i]);
			btnFiches[i].addActionListener(e -> actionFiches(e));
		}
		confirm.addActionListener(e -> actionConfirm());
		betPane.add(confirm);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(topPane, BorderLayout.PAGE_START);
		mainPanel.add(playerPane, BorderLayout.LINE_START);
		mainPanel.add(infoPane, BorderLayout.CENTER);
		mainPanel.add(dealerPane, BorderLayout.LINE_END);
		mainPanel.add(betPane, BorderLayout.PAGE_END);

		setTitle("Blackjack");
		getContentPane().add(mainPanel);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		deck = new Deck();
		playerHand = new BlackjackHand();
		dealerHand = new BlackjackHand();

		playerArea.setEditable(false);
		dealerArea.setEditable(false);

	}
	
	private void end() {
		playerHand.clearHand();
		dealerHand.clearHand();
		fichesPuntate = 0;
		lblFichesPuntate.setText("Fiches puntate: " + fichesPuntate);
		btnHit.setEnabled(false);
		btnStand.setEnabled(false);
		btnNewGame.setEnabled(true);
		
	}
	
	private void newGame() {
		end();
		playerArea.setText("");
		dealerArea.setText("");
		lblVincitore.setText("");
		for (JButton button : btnFiches) {
			button.setEnabled(true);
		}
		confirm.setEnabled(true);
/*		
		turnCounter++;
		
		if (turnCounter == 5 ) {
			deck.newDeck();
		}
*/		
		btnNewGame.setEnabled(false);
	}
	
	private void betConfirmed() {
		for (JButton button : btnFiches) {
			button.setEnabled(false);
		}
		confirm.setEnabled(false);
	}

	private void actionFiches(ActionEvent e) {
		String nFiches = e.getActionCommand();
		int fiches = 0;
		for (int i = 0; i < valoriFiches.length; i++) {
			if (nFiches.equals(valoriFiches[i])) {
				fiches = Integer.parseInt(valoriFiches[i]);
				break;
			}
		}
		
		if ((fichesPuntate+fiches) > fichesAttuali) {
			JOptionPane.showMessageDialog(this, "Fiches insufficenti!", "Errore", JOptionPane.ERROR_MESSAGE);
		} else {
			fichesPuntate = fichesPuntate + fiches;
			lblFichesPuntate.setText("Fiches puntate: " + fichesPuntate);
		}
	}

	public void actionConfirm() {
		if (fichesPuntate != 0) {
			fichesAttuali = fichesAttuali - fichesPuntate;
			lblFichesAttuali.setText("Fiches attuali:" + fichesAttuali);
			
			betConfirmed();

			deck.shuffle();

			playerHand.addCard(deck.drawCard(this));
			dealerHand.addCard(deck.drawCard(this));
			playerHand.addCard(deck.drawCard(this));
			dealerHand.addCard(deck.drawCard(this));

			playerArea.append("Mano del Giocatore:\n");
			playerHand.displayHand(playerArea);
			playerArea.append("Valore mano del Giocatore: " + playerHand.getHandValue() + "\n");
			playerArea.append("\n");
			
			dealerArea.append("Mano del Dealer:\n");
			dealerArea.append(dealerHand.cards.get(0)+"\n");
			dealerArea.append("Carta nascosta\n");
			dealerArea.append("\n");
			
			if (playerHand.getHandValue() == 21 && dealerHand.getHandValue() == 21) {
				lblVincitore.setText("Push! Sparegggio.");
				fichesAttuali = fichesAttuali + fichesPuntate;
				lblFichesAttuali.setText("Fiches attuali:" + fichesAttuali);
				end();
			} else if (playerHand.getHandValue() == 21) {
				lblVincitore.setText("Il Giocatore vince facendo Blackjack!");
				fichesAttuali = fichesAttuali + (fichesPuntate*3);
				end();
			} else if (dealerHand.getHandValue() == 21) {
				lblVincitore.setText("Il Dealer vince facendo Blackjack!");
				end();
			} else {
				playerArea.append("Hit o Stand?\n");
				playerArea.append("\n");
				btnHit.setEnabled(true);
				btnStand.setEnabled(true);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Non sono state puntate fiches.", "Errore", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void actionHit() {
		playerHand.addCard(deck.drawCard(this));
		playerArea.append("Mano del Giocatore:\n");
		playerHand.displayHand(playerArea);
		playerArea.append("Valore mano del Giocatore: " + playerHand.getHandValue() + "\n");
		playerArea.append("\n");

		if (playerHand.getHandValue() > 21) {
			lblVincitore.setText("Giocatore busts! Il Dealer vince.");
			end();
		}
	}

	private void actionStand() {
		dealerArea.append("Mano del Dealer:\n");
		dealerHand.displayHand(dealerArea);
		dealerArea.append("Valore mano del Dealer: " + dealerHand.getHandValue() + "\n");
		dealerArea.append("\n");

		while (dealerHand.getHandValue() < 17) {
			dealerHand.addCard(deck.drawCard(this));
			dealerArea.append("Il Dealer pesca una carta.\n");
			dealerArea.append("Mano del Dealer:\n");
			dealerHand.displayHand(dealerArea);
			dealerArea.append("Valore mano del Dealer: " + dealerHand.getHandValue() + "\n");
			dealerArea.append("\n");
		}

		if (dealerHand.getHandValue() > 21) {
			lblVincitore.setText("Dealer busts! Il giocatore vince.");
			fichesAttuali = fichesAttuali + (fichesPuntate*2);
			lblFichesAttuali.setText("Fiches attuali:" + fichesAttuali);
		} else if (playerHand.getHandValue() > dealerHand.getHandValue()) {
			lblVincitore.setText("Giocatore vince!");
			fichesAttuali = fichesAttuali + (fichesPuntate*2);
			lblFichesAttuali.setText("Fiches attuali:" + fichesAttuali);
		} else if (playerHand.getHandValue() < dealerHand.getHandValue()) {
			lblVincitore.setText("Dealer vince.");
		} else {
			lblVincitore.setText("Push! Spareggio.");
		}
		end();
	}

	public static void main(String[] args)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		JBlackjack game = new JBlackjack();
		game.setVisible(true);
		game.setResizable(false);
		game.setSize(new Dimension(750,500));
		game.setLocationRelativeTo(null);
		/*
		 * try {
		 * UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel"); }
		 * catch (UnsupportedLookAndFeelException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */ }
}