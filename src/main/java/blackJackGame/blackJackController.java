package blackJackGame;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Random;

public class blackJackController {

    @FXML
    public ImageView playerCard1, playerCard2, playerCard3, playerCard4;
    @FXML
    public ImageView dealerCard1, dealerCard2, dealerCard3, dealerCard4;
    @FXML
    public TextField playerPoints, dealersPoints, playerMoneyField, betAmountField;
    @FXML
    public Label result;
    @FXML
    public Button stay, reset, placeBet;  // Stay, Reset and Place Bet buttons

    private ArrayList<Card> deck;
    private Random random = new Random();
    private ArrayList<Card> dealerHand = new ArrayList<>();
    private ArrayList<Card> playerHand = new ArrayList<>();
    private int dealerSum, playerSum, dealerAceCount, playerAceCount;

    private int playerMoney = 1000;  // Starting money
    private int currentBet = 0;      // Current bet amount

    public class Card {
        String value;
        String type;

        Card(String value, String type) {
            this.value = value;
            this.type = type;
        }

        public int getValue() {
            if ("AJQK".contains(value)) {
                if (value.equals("A")) return 11;
                return 10;
            }
            return Integer.parseInt(value);
        }

        public boolean isAce() {
            return value.equals("A");
        }

        public String getImagePath() {
            return "/blackJackGame/cards/" + value + "-" + type + ".png";
        }
    }

    @FXML
    public void initialize() {
        startGame();  // Set up the game when the program starts.
        stay.setDisable(true);  // Disable the stay button at the start of the game
        placeBet.setDisable(false);  // Enable the place bet button
        updatePlayerMoney();  // Show the player's money
    }

    @FXML
    public void onCardClicked(MouseEvent event) {
        ImageView clickedCard = (ImageView) event.getSource();

        // Allow the player to draw a card if they haven't reached 5 cards.
        if (playerHand.size() < 5) {
            drawPlayerCard();
            updateUI();
        }

        // If the player's total points exceed 21, disable the stay button.
        if (reducePlayerAce() > 21) {
            result.setText("You Lose!");
            stay.setDisable(true); // Disable stay if player loses
            updatePlayerMoney(false);  // Player lost money
        }
    }

    // Method to draw a card for the player
    private void drawPlayerCard() {
        Card card = drawCard();  // Draw a card from the deck
        playerHand.add(card);    // Add the card to the player's hand
        playerSum += card.getValue();  // Add the value of the card to the player's total sum
        playerAceCount += card.isAce() ? 1 : 0;  // Count the Aces for later adjustment
    }

    @FXML
    public void onBtnStay() {
        //stay.setDisable(true);   // Disable Stay after the player chooses it
        placeBet.setDisable(true); // Disable placing bet once the player stays

        // Dealer plays
        while (dealerSum < 17) {
            if (deck.isEmpty()) break;

            Card card = drawCard();
            dealerSum += card.getValue();
            dealerAceCount += card.isAce() ? 1 : 0;
            dealerHand.add(card);
        }

        // Final UI update and determine winner
        updateUI();
        determineWinner();
    }

    @FXML
    public void onBtnReset() {
        startGame();  // Reset the game state
        result.setText("");  // Reset the result text
        stay.setDisable(true);  // Disable Stay button for the new game
        placeBet.setDisable(false); // Re-enable the place bet button
        updatePlayerMoney();  // Update the player's money display
    }

    @FXML
    public void onPlaceBet() {
        try {
            int bet = Integer.parseInt(betAmountField.getText());
            if (bet > 0 && bet <= playerMoney) {
                currentBet = bet; // Set the current bet
                playerMoney -= bet; // Deduct the bet from player's money
                updatePlayerMoney();  // Update player money display
                startGame();  // Start the game after placing a bet
                placeBet.setDisable(true); // Disable placing bet once the game starts
                stay.setDisable(false);  // Enable stay button when the game starts
            } else {
                result.setText("Invalid bet amount!");
            }
        } catch (NumberFormatException e) {
            result.setText("Please enter a valid number for the bet.");
        }
    }

    private void startGame() {
        buildDeck();
        shuffleDeck();

        dealerHand.clear();
        dealerSum = 0;
        dealerAceCount = 0;
        playerHand.clear();
        playerSum = 0;
        playerAceCount = 0;

        // Deal two cards to the dealer and player.
        dealerHand.add(drawCard());
        dealerSum += dealerHand.get(0).getValue();
        dealerAceCount += dealerHand.get(0).isAce() ? 1 : 0;

        for (int i = 0; i < 2; i++) {
            Card card = drawCard();
            playerHand.add(card);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
        }

        updateUI();
    }

    private Card drawCard() {
        return deck.remove(deck.size() - 1); // Draw card from deck
    }

    private void buildDeck() {
        deck = new ArrayList<>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"};

        for (String type : types) {
            for (String value : values) {
                deck.add(new Card(value, type));
            }
        }
    }

    private void shuffleDeck() {
        for (int i = 0; i < deck.size(); i++) {
            int j = random.nextInt(deck.size());
            Card temp = deck.get(i);
            deck.set(i, deck.get(j));
            deck.set(j, temp);
        }
    }

    private void updateUI() {
        updateCardImages(playerHand, playerCard1, playerCard2, playerCard3, playerCard4);
        playerPoints.setText(String.valueOf(reducePlayerAce()));  // Update player points

        updateCardImages(dealerHand, dealerCard1, dealerCard2, dealerCard3, dealerCard4);
        dealersPoints.setText(String.valueOf(dealerSum));  // Update dealer points

        result.setText("");  // Reset the result label for a clean state
    }

    private void updateCardImages(ArrayList<Card> hand, ImageView... cardViews) {
        for (int i = 0; i < cardViews.length; i++) {
            if (i < hand.size()) {
                try {
                    Image img = new Image(getClass().getResource(hand.get(i).getImagePath()).toExternalForm());
                    cardViews[i].setImage(img);
                } catch (Exception e) {
                    cardViews[i].setImage(null);
                    System.out.println("Error loading image: " + hand.get(i).getImagePath());
                }
            } else {
                cardViews[i].setImage(new Image(getClass().getResource("/blackJackGame/cards/BACK.png").toExternalForm()));
            }
        }
    }

    private int reducePlayerAce() {
        int total = playerSum;
        while (total > 21 && playerAceCount > 0) {
            total -= 10;
            playerAceCount--;
        }
        return total;
    }

    private int reduceDealerAce() {
        int total = dealerSum;
        while (total > 21 && dealerAceCount > 0) {
            total -= 10;
            dealerAceCount--;
        }
        return total;
    }

    private void determineWinner() {
        int adjustedPlayerTotal = reducePlayerAce();  // Final player total after Ace adjustments
        int adjustedDealerTotal = reduceDealerAce();  // Final dealer total after Ace adjustments

        // If player busts
        if (adjustedPlayerTotal > 21) {
            result.setText("You Lose!");
            updatePlayerMoney(false);  // Player loses money
        }
        // If dealer busts
        else if (adjustedDealerTotal > 21) {
            result.setText("Dealer Busts! You Win!");
            updatePlayerMoney(true);  // Player wins
        }
        // Compare totals if no one busts
        else if (adjustedDealerTotal >= adjustedPlayerTotal) {
            result.setText("Dealer Wins!");
            updatePlayerMoney(false);  // Player loses money
        } else {
            result.setText("You Win!");
            updatePlayerMoney(true);  // Player wins
        }
    }

    private void updatePlayerMoney() {
        playerMoneyField.setText("Money: $" + playerMoney);  // Update the money display
    }

    private void updatePlayerMoney(boolean win) {
        if (win) {
            playerMoney += currentBet * 2;  // Double the bet if the player wins
        }
        currentBet = 0;  // Reset current bet
        updatePlayerMoney();  // Update the displayed money
    }
}
