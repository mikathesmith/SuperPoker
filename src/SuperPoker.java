/**
 * Etude 13: Super Sized Poker Hands
 * @author Kimberley Louw, Matthew Boyes, Mika Smith
 */

import java.util.*;
import java.util.stream.*;

public class SuperPoker {
    private ArrayList<ArrayList<Card>> hands = new ArrayList<>();
    private ArrayList<Card> house = new ArrayList<>();
    private HashMap<List<Card>, String> players = new HashMap<List<Card>, String>();

    public SuperPoker(ArrayList<ArrayList<Card>> hands, ArrayList<Card> house) {
        hands.forEach((hand) -> Collections.sort(hand));
        this.hands = hands;

        Collections.sort(house);
        this.house = house;
    }

    /**
     * Utility method to stringify a {@code hand} of cards
     * @param hand List of Cards
     * @return Stringified representation of {@code hand}
     */
    public String stringifyHand(List<Card> hand) {
        StringBuilder result = new StringBuilder();
        result.append(hand.get(0));
        for (int i = 1; i < hand.size(); i++) {
            result.append(" " + hand.get(i));
        }
        return result.toString();
    }

    /**
     * Prints the final result according to output specifications
     */
    public void printResult() {
        List<List<Card>> bestHands = hands.stream()
            .map((List<Card> handOfTwo) -> {
                List<Card> allCards = new ArrayList<>();
                allCards.addAll(house);
                allCards.addAll(handOfTwo);

                List<List<Card>> allHands = new Combinations<Card>(allCards).choose(5);
                for (List<Card> hand : allHands) {
                    Collections.sort(hand);
                }
                Collections.sort(allHands, new CompareHands());

                return allHands.get(allHands.size() - 1);
            })
            .collect(Collectors.toList());

        for (int i = 0; i < bestHands.size(); i++) {
            players.put(bestHands.get(i), "Player " + (i+1));
        }

        Collections.sort(bestHands, new CompareHands());

        for (List<Card> hand : bestHands) {
            String player = players.get(hand);
            System.out.println(player + ": " + stringifyHand(hand) + " - " + Rank.getRank(getComparison(hand).get(0)));
        }
    }

    /**
     * Returns true if {@code hand} is a Royal Flush
     * @param hand List of Cards
     * @return Boolean
     */
    private static boolean isRoyalFlush(List<Card> hand) {
        if (hand.get(0).getNumber() != 10) return false;
        if (hand.get(1).getNumber() != 11) return false;
        if (hand.get(2).getNumber() != 12) return false;
        if (hand.get(3).getNumber() != 13) return false;
        if (hand.get(4).getNumber() != 1) return false;

        return isFlush(hand);
    }

    /**
     * Returns true if the hand contains a straight flush
     * @param hand List of Cards
     * @return Boolean
     */
    private static boolean isStraightFlush(List<Card> hand) {
        return isFlush(hand) && isStraight(hand);
    }

    /**
     * Returns {@code true} if {@code hand} is made of cards all of the same suit
     * @param hand List of Cards
     * @return Boolean
     */
    private static boolean isFlush(List<Card> hand) {
        Character suit = hand.get(0).getSuit();
        for (Card c : hand) {
            if (c.getSuit() != suit) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns {@code true} if {@code hand} consists of only consecutive cards
     * @param hand List of Cards
     * @return Boolean
     */
    private static boolean isStraight(List<Card> hand) {
        Card first = hand.get(0);
        Card previous = first;
        for (int i = 1; i < hand.size(); i++) {
            Card card = hand.get(i);
            if (i == hand.size() - 1 && card.getNumber() == 1) {
                return first.getNumber() == 2 || previous.getNumber() == 13;
            }
            if (hand.get(i).getNumber() != previous.getNumber() + 1) return false;
            previous = card;
        }
        return true;
    }

    /**
     * Returns a list of card weights in reverse order
     * @param cards List of Cards
     * @return List of Integers
     */
    private static List<Integer> getHighCardWeights(List<Card> cards) {
        List<Card> reversed = new ArrayList<>();
        reversed.addAll(cards);
        Collections.reverse(reversed);

        return reversed.stream()
            .map((Card c) -> c.getWeight())
            .collect(Collectors.toList());
    }

    /**
     * Gets a List of Integers useful for comparing hands
     * @param hand List of Cards
     * @return List of Integers
     */
    private static List<Integer> getComparison(List<Card> hand) {
        int numbers[] = hand.stream()
            .mapToInt((Card c) -> c.getNumber())
            .toArray();
        List<Integer> result = new ArrayList<>();

        // Royal Flush
        if (isRoyalFlush(hand)) {
            result.add(Rank.ROYAL_FLUSH.getWeight());
            return result;
        }
        // End Royal Flush

        // Straight Flush
        if (isStraightFlush(hand)) {
            result.add(Rank.STRAIGHT_FLUSH.getWeight());
            int highCard = hand.get(4).getWeight();
            if (highCard != 14) {
                result.add(highCard);
            } else {
                int firstCard = hand.get(0).getWeight();
                if (firstCard == 2) {
                    result.add(5);
                } else {
                    result.add(highCard);
                }
            }
            return result;
        }
        // End Straight Flush

        // Four of a Kind
        if (numbers[0] == numbers[1] && numbers[1] == numbers[2] && numbers[2] == numbers[3]) {
            result.add(Rank.FOUR_OF_A_KIND.getWeight());
            result.add(hand.get(0).getWeight());
            result.add(hand.get(4).getWeight());
            return result;
        }
        if (numbers[1] == numbers[2] && numbers[2] == numbers[3] && numbers[3] == numbers[4]) {
            result.add(Rank.FOUR_OF_A_KIND.getWeight());
            result.add(hand.get(1).getWeight());
            result.add(hand.get(0).getWeight());
            return result;
        }
        // End Four of a Kind

        // Full house
        if ((numbers[0] == numbers[1] && numbers[0] == numbers[2] && numbers[3] == numbers[4])) {
            result.add(Rank.FULL_HOUSE.getWeight());
            result.add(numbers[0]);
            result.add(numbers[3]);
            return result;
        }
        if (numbers[2] == numbers[3] && numbers[2] == numbers[4] && numbers[0] == numbers[1]) {
            result.add(Rank.FULL_HOUSE.getWeight());
            result.add(numbers[2]);
            result.add(numbers[0]);
        }
        // End full house

        // Flush
        if (isFlush(hand)) {
            result.add(Rank.FLUSH.getWeight());
            result.addAll(getHighCardWeights(hand));
            return result;
        }
        // Flush

        // Straight
        if (isStraight(hand)) {
            result.add(Rank.STRAIGHT.getWeight());
            if (hand.get(hand.size() - 1).getNumber() != 1) {
                result.add(hand.get(hand.size() - 1).getWeight());
            } else {
                int firstCard = hand.get(0).getWeight();
                if (firstCard == 2) {
                    result.add(5);
                } else {
                    result.add(hand.get(hand.size() - 1).getWeight());
                }
            }
            return result;
        }
        // End Straight

        // Three of a Kind
        if (numbers[0] == numbers[1] && numbers[0] == numbers[2]) {
            result.add(Rank.THREE_OF_A_KIND.getWeight());
            result.add(hand.get(0).getWeight());
            result.add(hand.get(4).getWeight());
            result.add(hand.get(3).getWeight());
            return result;
        }

        if (numbers[1] == numbers[2] && numbers[1] == numbers[3]) {
            result.add(Rank.THREE_OF_A_KIND.getWeight());
            result.add(hand.get(1).getWeight());
            result.add(hand.get(4).getWeight());
            result.add(hand.get(0).getWeight());
            return result;
        }

        if (numbers[2] == numbers[3] && numbers[2] == numbers[4]) {
            result.add(Rank.THREE_OF_A_KIND.getWeight());
            result.add(hand.get(2).getWeight());
            result.add(hand.get(1).getWeight());
            result.add(hand.get(0).getWeight());
            return result;
        }
        // End Three of a Kind

        // Two Pair
        if (numbers[0] == numbers[1]) {
            if (numbers[2] == numbers[3]) {
                result.add(Rank.TWO_PAIR.getWeight());
                result.add(hand.get(2).getWeight());
                result.add(hand.get(0).getWeight());
                result.add(hand.get(4).getWeight());
                return result;
            }
            if (numbers[3] == numbers[4]) {
                result.add(Rank.TWO_PAIR.getWeight());
                result.add(hand.get(3).getWeight());
                result.add(hand.get(0).getWeight());
                result.add(hand.get(2).getWeight());
                return result;
            }
        } else if (numbers[1] == numbers[2] && numbers[3] == numbers[4]) {
            result.add(Rank.TWO_PAIR.getWeight());
            result.add(hand.get(1).getWeight());
            result.add(hand.get(3).getWeight());
            result.add(hand.get(0).getWeight());
            return result;
        }
        // End Two Pair

        // Pair
        if (numbers[0] == numbers[1]) {
            result.add(Rank.PAIR.getWeight());
            result.add(hand.get(0).getWeight());
            result.add(hand.get(4).getWeight());
            result.add(hand.get(3).getWeight());
            result.add(hand.get(2).getWeight());
            return result;
        }
        if (numbers[1] == numbers[2]) {
            result.add(Rank.PAIR.getWeight());
            result.add(hand.get(1).getWeight());
            result.add(hand.get(4).getWeight());
            result.add(hand.get(3).getWeight());
            result.add(hand.get(0).getWeight());
            return result;
        }
        if (numbers[2] == numbers[3]) {
            result.add(Rank.PAIR.getWeight());
            result.add(hand.get(2).getWeight());
            result.add(hand.get(4).getWeight());
            result.add(hand.get(1).getWeight());
            result.add(hand.get(0).getWeight());
            return result;
        }
        if (numbers[3] == numbers[4]) {
            result.add(Rank.PAIR.getWeight());
            result.add(hand.get(3).getWeight());
            result.add(hand.get(2).getWeight());
            result.add(hand.get(1).getWeight());
            result.add(hand.get(0).getWeight());
            return result;
        }
        // End Pair

        // Otherwise, we have just a high card
        result.add(Rank.HIGH_CARD.getWeight());
        result.addAll(getHighCardWeights(hand));
        return result;
    }

    // /**
    //  * Main method creates SuperPoker instance, reads in data from {@code System.in} and prints result
    //  */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numHands = scanner.nextInt();
        scanner.nextLine(); // consume newline

        ArrayList<ArrayList<Card>> hands = new ArrayList<>();
        for (int i = 0; i < numHands; i++) {
            String line = scanner.nextLine();
            hands.add(Arrays.stream(line.split(" "))
                .map((c) -> Card.fromRaw(c))
                .collect(Collectors.toCollection(ArrayList::new)));
        }

        ArrayList<Card> house = Arrays.stream(scanner.nextLine().split(" "))
            .map((String c) -> Card.fromRaw(c))
            .collect(Collectors.toCollection(ArrayList::new));

        scanner.close();

        new SuperPoker(hands, house).printResult();
    }

    /**
     * Comparator for comparing hands of Cards
     */
    class CompareHands implements Comparator<List<Card>> {
        public int compare(List<Card> hand1, List<Card> hand2) {
            List<Integer> cmp1 = getComparison(hand1);
            List<Integer> cmp2 = getComparison(hand2);

            for (int i = 0; i < cmp1.size(); i++) {
                int cmp = cmp1.get(i) - cmp2.get(i);
                if (cmp > 0) return 1;
                if (cmp < 0) return -1;
            }

            return 0;
        }
    }
}
