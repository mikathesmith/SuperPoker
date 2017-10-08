/**
 * Etude 13: Super Sized Poker Hands
 * @author Mika Smith, Kimberley Louw, Nathan Hardy, Mathew Boyes
 *
 */

import java.util.*;
import java.util.stream.Collectors;

public class SuperPoker {
    private ArrayList<ArrayList<Card>> hands = new ArrayList<>();
    private ArrayList<Card> house = new ArrayList<>();
    public static HashMap<Integer, String> ranks = new HashMap<Integer, String>();

    public SuperPoker(ArrayList<ArrayList<Card>> hands, ArrayList<Card> house) {
        hands.stream()
            .forEach((hand) -> Collections.sort(hand));
        this.hands = hands;

        Collections.sort(house);
        this.house = house;
    }

    public void printResult() {
        System.out.println("Hands: " + hands);
        System.out.println("House: " + house);
        System.out.println("Rank: " + findRank(house));
    }

    public static boolean isRoyalFlush(ArrayList<Card> hand) {
        if (hand.get(0).getNumber() != 10) return false;
        if (hand.get(1).getNumber() != 11) return false;
        if (hand.get(2).getNumber() != 12) return false;
        if (hand.get(3).getNumber() != 13) return false;
        if (hand.get(4).getNumber() != 1) return false;

        return isFlush(hand);
    }

    public static boolean isStraightFlush(ArrayList<Card> hand) {
        return isFlush(hand) && isStraight(hand);
    }

    public static boolean isFourKind(ArrayList<Card> hand) {
        int first = hand.get(0).getNumber();
        int second = hand.get(1).getNumber();
        int third = hand.get(2).getNumber();
        int fourth = hand.get(3).getNumber();
        int fifth = hand.get(4).getNumber();

        if (first == second && second == third && third == fourth) return true;
        if (second == third && third == fourth && fourth == fifth) return true;

        return false;
    }

    public static boolean isFullHouse(ArrayList<Card> hand) {
        // TODO: This needs additional logic because 3 of a kind already is a pair
        // For Full House, we need to check that the make up of three-of-a-kind
        // and the pair are different values
        return isThreeKind(hand) && isPair(hand);
    }

    public static boolean isFlush(ArrayList<Card> hand) {
        Character suit = hand.get(0).getSuit();
        for (Card c : hand) {
            if (c.getSuit() != suit) {
                return false;
            }
        }
        return true;
    }

    public static boolean isStraight(ArrayList<Card> hand) {
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

    public  static boolean isThreeKind(ArrayList<Card> hand) {
        int first = hand.get(0).getNumber();
        int second = hand.get(1).getNumber();
        int third = hand.get(2).getNumber();
        int fourth = hand.get(3).getNumber();
        int fifth = hand.get(4).getNumber();

        if (first == second && second == third) return true;
        if (second == third && third == fourth) return true;
        if (third == fourth && fourth == fifth) return true;

        return false;
    }

    public static boolean isTwoPair(ArrayList<Card> hand) {
        int first = hand.get(0).getNumber();
        int second = hand.get(1).getNumber();
        int third = hand.get(2).getNumber();
        int fourth = hand.get(3).getNumber();
        int fifth = hand.get(4).getNumber();
        if (first == second) {
            if (third == fourth) return true;
            if (fourth == fifth) return true;
            return false;
        } else if (second == third) {
            return fourth == fifth;
        }
        return false;
    }

    public static boolean isPair(ArrayList<Card> hand) {
        int first = hand.get(0).getNumber();
        int second = hand.get(1).getNumber();
        int third = hand.get(2).getNumber();
        int fourth = hand.get(3).getNumber();
        int fifth = hand.get(4).getNumber();

        if (first == second) return true;
        if (second == third) return true;
        if (third == fourth) return true;
        if (fourth == fifth) return true;

        return false;
    }

    public static boolean isHighCard(ArrayList<Card> hand) {
        return true;
    }

    public static int findRank(ArrayList<Card> hand) {
        if (isRoyalFlush(hand)) return 1;
        if (isStraightFlush(hand)) return 2;
        if (isFourKind(hand)) return 3;
        if (isFullHouse(hand)) return 4;
        if (isFlush(hand)) return 5;
        if (isStraight(hand)) return 6;
        if (isThreeKind(hand)) return 7;
        if (isTwoPair(hand)) return 8;
        if (isPair(hand)) return 9;
        if (isHighCard(hand)) return 10;
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
