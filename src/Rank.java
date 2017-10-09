public enum Rank {
    ROYAL_FLUSH(10, "Royal Flush"),
    STRAIGHT_FLUSH(9, "Straight Flush"),
    FOUR_OF_A_KIND(8, "Four of a Kind"),
    FULL_HOUSE(7, "Full House"),
    FLUSH(6, "Flush"),
    STRAIGHT(5, "Straight"),
    THREE_OF_A_KIND(4, "Three of a Kind"),
    TWO_PAIR(3, "Two Pair"),
    PAIR(2, "Pair"),
    HIGH_CARD(1, "High Card");

    public static Rank getRank(int weight) {
        for (Rank rank : Rank.values()) {
            if (rank.getWeight() == weight) return rank;
        }

        // Make Java happy - this should never actually happen
        return null;
    }

    private final int weight;
    private final String name;

    private Rank(int weight, String name) {
        this.weight = weight;
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public String toString() {
        return name;
    }
}
