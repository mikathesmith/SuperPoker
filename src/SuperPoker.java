/**
 * Etude 13: Super Sized Poker Hands
 * @author Kimberley Louw, Matthew Boyes, Mika Smith
 *
 */

import java.util.*;
import java.util.stream.*;

public class SuperPoker {
    private ArrayList<ArrayList<Card>> hands = new ArrayList<>();
    private ArrayList<Card> house = new ArrayList<>();
    public static HashMap<Integer, String> ranks = new HashMap<Integer, String>();

    public SuperPoker(ArrayList<ArrayList<Card>> hands, ArrayList<Card> house) {
        hands.forEach((hand) -> Collections.sort(hand));
        this.hands = hands;

        Collections.sort(house);
        this.house = house;
    }

    public void printResult() {
        for (List<Card> h : hands) {
            List<Card> all = new ArrayList<>();
            all.addAll(h);
            all.addAll(house);
            for (List<Card> hand : new Combinations<Card>(all).choose(5)) {
                System.out.println("Dealt Hand: " + h + ", hand: " + hand);
            }
        }
        System.out.println("Hands: " + hands);
        System.out.println("House: " + house);
    }

    private static boolean isRoyalFlush(List<Card> hand) {
        if (hand.get(0).getNumber() != 10) return false;
        if (hand.get(1).getNumber() != 11) return false;
        if (hand.get(2).getNumber() != 12) return false;
        if (hand.get(3).getNumber() != 13) return false;
        if (hand.get(4).getNumber() != 1) return false;

        return isFlush(hand);
    }

    private static boolean isStraightFlush(List<Card> hand) {
        return isFlush(hand) && isStraight(hand);
    }

    private static boolean isFlush(List<Card> hand) {
        Character suit = hand.get(0).getSuit();
        for (Card c : hand) {
            if (c.getSuit() != suit) {
                return false;
            }
        }
        return true;
    }

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

    private static List<Integer> getHighCardWeights(List<Card> cards) {
        List<Card> reversed = new ArrayList<>();
        reversed.addAll(cards);
        Collections.reverse(reversed);

        return reversed.stream()
            .map((Card c) -> c.getWeight())
            .collect(Collectors.toList());
    }

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
            return result;
        }
        if (numbers[2] == numbers[3] && numbers[2] == numbers[4] && numbers[0] == numbers[1]) {
            result.add(Rank.FULL_HOUSE.getWeight());
            result.add(numbers[2]);
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

        // Otherwise, we have just a high card
        result.add(Rank.HIGH_CARD.getWeight());
        result.addAll(getHighCardWeights(hand));
        return result;
    }

    public static int compareHands(List<Integer> hand1, List<Integer> hand2) {
        for (int i = 0; i < hand1.size(); i++) {
            if (hand1.get(i) > hand2.get(i)) return 1;
            if (hand1.get(i) < hand2.get(i)) return -1;
        }
        return 0;
    }

    /**
     * Main method creates SuperPoker instance and prints result
     */
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
}
