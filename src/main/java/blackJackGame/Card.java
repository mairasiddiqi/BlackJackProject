package blackJackGame;

/**
 * Represents a playing card in the Blackjack game.
 */
public class Card {
    private String value; // The value of the card (A, 2, 3, ..., K)
    private String suit;  // The suit of the card (Clubs, Diamonds, Hearts, Spades)

    /**
     * Constructor to create a new card with a value and suit.
     *
     * @param value The value of the card (A, 2, 3, ..., K).
     * @param suit The suit of the card (Clubs, Diamonds, Hearts, Spades).
     */
    public Card(String value, String suit) {
        this.value = value;
        this.suit = suit;
    }

    /**
     * Returns the numeric value of the card. Face cards (J, Q, K) are worth 10,
     * and Ace (A) can be either 1 or 11, but here we return "A" for simplicity.
     *
     * @return The value of the card in string format (e.g., "A", "10").
     */
    public String getValue() {
        if ("A".equals(value)) {
            return "A";  // Ace can be treated as 1 or 11, handled in hand calculation
        } else if ("J".equals(value) || "Q".equals(value) || "K".equals(value)) {
            return "10";  // Face cards are worth 10
        } else {
            return value;  // Return the numeric value (e.g., "2", "3")
        }
    }

    /**
     * Returns the string representation of the card's value.
     *
     * @return The string representation (e.g., "A", "2", "J").
     */
    public String getValueString() {
        return value; // Simply returns the string value (e.g., "A", "2", "J").
    }

    /**
     * Returns the path to the card's image based on its value and suit.
     *
     * @return The path to the image of the card (e.g., "/images/cards/2H.png").
     */
    public String getImagePath() {
        return "/images/cards/" + value + suit + ".png"; // Example: "2H.png" for 2 of Hearts
    }
}
