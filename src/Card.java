/**
 * @author Kimberley Louw, Matthew Boyes, Mika Smith, Nathan Hardy
 */

/**
 * A data structure which represents a single card and holds its number and suit.
 */
public class Card implements Comparable<Card> {
    private final int number;
    private final char suit;

    /**
     * Creates a new Card instance from a number and suit
     * @param number Card number
     * @param suit Suit character
     */
    public Card(int number, char suit) {
        this.number = number;
        this.suit = suit;
    }

    /**
     * Creates a card from a String in the input format specified
     * @param raw String
     * @return Card
     */
    public static Card fromRaw(String raw) {
        String tmp = raw.toUpperCase();

        String numberString = tmp.substring(0, tmp.length() - 1);
        int number;
        switch (numberString) {
            case "A":
                number = 1;
                break;
            case "T":
                number = 10;
                break;
            case "J":
                number = 11;
                break;
            case "Q":
                number = 12;
                break;
            case "K":
                number = 13;
                break;
            default:
                number = Integer.parseInt(numberString);
        }
        if (number < 0 || number > 13) return null;

        char suit = tmp.charAt(tmp.length() - 1);
        if (!"CDHS".contains("" + suit)) return null;

        return new Card(number, suit);
    }

    public int getNumber() {
        return number;
    }

    public char getSuit() {
        return suit;
    }

    public boolean equals(Card other) {
        return number == other.number && suit == other.suit;
    }

    public int getWeight() {
        if (number == 1) return 14;
        return number;
    }

    /**
     * Compares {@code this} Card with {@code other}. Note that
     * in Texas Hold'em there is no ordering of the suit, so
     * this method should not be used for comparing high-card
     * @param other Other Card
     * @return Comparator int
     */
    public int compareTo(Card other) {
        int difference = getWeight() - other.getWeight();
        return difference == 0
            ? Character.compare(suit, other.suit)
            : difference;
    }

    /**
     * Gets the string representation of the numeric portion of the card
     * @return String
     */
    public static String getNumberRepresentation(int number) {
        switch (number) {
            case 1:
            case 14:
                return "A";
            case 11:
                return "J";
            case 12:
                return "Q";
            case 13:
                return "K";
            default:
                return Integer.toString(number);
        }
    }

    public String toString() {
        return getNumberRepresentation(number) + suit;
    }
}
