package blackJackGame;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Collections;

public class blackJackController {
    @FXML
    public ImageView playerCard1;
    @FXML
    public ImageView playerCard2;
    @FXML
    public ImageView playerCard3;
    @FXML
    public ImageView playerCard4;
    @FXML
    public ImageView dealerCard1;
    @FXML
    public ImageView dealerCard2;
    @FXML
    public ImageView dealerCard3;
    @FXML
    public ImageView dealerCard4;
    @FXML
    public TextField playerPoints;
    @FXML
    public TextField dealersPoints;
    @FXML
    public Label result;
    @FXML
    public Button hit;
    @FXML
    public Button stay;

    // Game variables
    private final ArrayList<Card> deck = new ArrayList<>(); // The deck of cards
    private final ArrayList<Card> playerHand = new ArrayList<>(); // The player's hand
    private final ArrayList<Card> dealerHand = new ArrayList<>(); // The dealer's hand
    private int playerSum = 0, dealerSum = 0; // Sum of the player's and dealer's hands

    //Initializes the game by building the deck, shuffling it, and starting the game.

    @FXML
    public void initialize() {
        buildDeck();  // Create and populate the deck with 52 cards
        shuffleDeck(); // Shuffle the deck for randomness
        startGame();   // Start a new game by dealing initial cards
    }

    // Called when the player presses the "Hit" button.
    // The player is dealt a new card.

    @FXML
    private void onBtnHit() {
        if (!deck.isEmpty()) {
            Card drawnCard = drawCard(playerHand);  // Draw a card for the player
            updatePlayerSum(drawnCard);  // Update player's hand value
            updateCardView(playerHand, playerCard1, playerCard2, playerCard3, playerCard4); // Update UI
            playerPoints.setText(String.valueOf(playerSum)); // Update player's points display

            // If player's sum exceeds 21, they lose
            if (playerSum > 21) {
                result.setText("Bust! You Lose!"); // Display bust message
                disableButtons(); // Disable "Hit" and "Stay" buttons
            }
        }
    }

    //Called when the player presses the "Stay" button.
    // The dealer will play their hand, and the game will be evaluated.

    @FXML
    private void onBTNStay() {
        // Dealer draws cards until their sum is at least 17
        while (dealerSum < 17) {
            Card drawnCard = drawCard(dealerHand);
            updateDealerSum(drawnCard);  // Update dealer's hand value
            updateCardView(dealerHand, dealerCard1, dealerCard2, dealerCard3, dealerCard4); // Update UI
        }

        dealersPoints.setText(String.valueOf(dealerSum)); // Update dealer's points display
        evaluateWinner();  // Compare hands and display the result
    }

    //Creates and populates the deck of 52 cards.

    private void buildDeck() {
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] suits = {"C", "D", "H", "S"};

        // Add each combination of value and suit to the deck
        for (String suit : suits) {
            for (String value : values) {
                deck.add(new Card(value, suit));
            }
        }
    }

    //Shuffles the deck to randomize the order of the cards.

    private void shuffleDeck() {
        Collections.shuffle(deck); // Shuffle the deck
    }

    //Starts a new game by dealing initial cards to the player and dealer.

    private void startGame() {
        playerHand.clear();
        dealerHand.clear();
        playerSum = dealerSum = 0;

        // Deal initial cards to player and dealer
        drawInitialCards();

        playerPoints.setText(String.valueOf(playerSum)); // Display player's initial points
        dealersPoints.setText("?"); // Hide dealer's points initially
        result.setText(""); // Clear the result label
        hit.setDisable(false); // Enable the "Hit" button
        stay.setDisable(false); // Enable the "Stay" button
    }

    //Deals two cards to the player and dealer, then updates the UI.

    private void drawInitialCards() {
        drawCard(playerHand);
        drawCard(playerHand);
        drawCard(dealerHand);
        drawCard(dealerHand);

        // Update UI for the initial hand of the player and dealer
        updateCardView(playerHand, playerCard1, playerCard2, null, null);
        updateCardView(dealerHand, dealerCard1, dealerCard2, null, null);

        // Calculate the initial sum of each hand
        playerSum = calculateHandValue(playerHand);
        dealerSum = calculateHandValue(dealerHand);
    }

     // Draws a card from the deck and adds it to the given hand.
     // at param hand: The hand (either player's or dealer's) to which the card will be added.
     // at return the drawn card.

    private Card drawCard(ArrayList<Card> hand) {
        if (deck.isEmpty()) return null; // No cards left to draw
        Card card = deck.remove(deck.size() - 1); // Remove the top card from the deck
        hand.add(card); // Add the drawn card to the hand
        return card;
    }

      //Updates the image view for the cards in a given hand.
     //@param hand The hand whose cards are to be displayed.
     //@param cardSlots The ImageView slots to display the cards.

    private void updateCardView(ArrayList<Card> hand, ImageView... cardSlots) {
        for (int i = 0; i < hand.size(); i++) {
            if (i < cardSlots.length && cardSlots[i] != null) {
                String imagePath = hand.get(i).getImagePath(); // Get the image path of the card
                cardSlots[i].setImage(new Image(getClass().getResource(imagePath).toExternalForm())); // Update the ImageView
            }
        }
    }


     //Updates the player's sum based on the current hand.
     //@param card is the newly drawn card to update the player's sum.

    private void updatePlayerSum(Card card) {
        playerSum = calculateHandValue(playerHand); // Recalculate the player's sum
    }


     //Updates the dealer's sum based on the current hand.
     //@param card The newly drawn card to update the dealer's sum.

    private void updateDealerSum(Card card) {
        dealerSum = calculateHandValue(dealerHand); // Recalculate the dealer's sum
    }


     //Calculates the total value of a hand. Aces are treated as 1 or 11, and face cards are worth 10.
     //@param hand The hand whose total value is being calculated.
     //@return The total value of the hand.

    private int calculateHandValue(ArrayList<Card> hand) {
        int sum = 0;
        int aceCount = 0;

        for (Card card : hand) {
            sum += Integer.parseInt(card.getValue()); // Add card's value to the sum
            if ("A".equals(card.getValueString())) {
                aceCount++; // Count the number of Aces
            }
        }

        // Adjust the sum if there are any aces and the sum exceeds 21
        while (sum > 21 && aceCount > 0) {
            sum -= 10; // Treat one Ace as 1 instead of 11
            aceCount--;
        }

        return sum; // Return the total hand value
    }

     //Evaluates the winner based on the final hand values and displays the result.

    private void evaluateWinner() {
        String message;
        if (dealerSum > 21 || playerSum > dealerSum) {
            message = "You Win!"; // Player wins if the dealer busts or has a lower hand value
        } else if (playerSum < dealerSum) {
            message = "You Lose!"; // Player loses if their hand is lower
        } else {
            message = "It's a Tie!"; // It's a tie if both hands have the same value
        }
        result.setText(message); // Display the result
        disableButtons(); // Disable the buttons after the game ends
    }


     //Disables both the "Hit" and "Stay" buttons.

    private void disableButtons() {
        hit.setDisable(true); // Disable "Hit" button
        stay.setDisable(true); // Disable "Stay" button
    }
}

