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
        this.hands = hands;
        this.house = house;
    }

    public void printResult() {
        System.out.println("Hands: " + hands);
        System.out.println("House: " + house);
    }

    public static boolean isRoyalFlush(ArrayList<Card> hand) {
        int ten = hand.get(0).getNumber();
        if (ten != 10) {
            return false;
        }
        int jack = hand.get(1).getNumber();
        if (jack != 11) {
            return false;
        }
        int queen = hand.get(2).getNumber();
        if (queen != 12) {
            return false;
        }
        int king = hand.get(3).getNumber();
        if (king != 13) {
            return false;
        }
        int ace = hand.get(4).getNumber();
        if (ace != 1) {
            return false;
        }
        Character suit = hand.get(0).getSuit();
        for (Card c : hand) {
            if (c.getSuit() != suit) {
                return false;
            }
        }
        return true;
    }

    public static boolean isStraightFlush(ArrayList<Card> hand) {
        if (isFlush(hand) && isStraight(hand)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isFourKind(ArrayList<Card> hand) {
        int first = hand.get(0).getNumber();
        int second = hand.get(1).getNumber();
        int third = hand.get(2).getNumber();
        int fourth = hand.get(3).getNumber();
        int fifth = hand.get(4).getNumber();
        if (first == second && second == third && third == fourth) {
            return true;
        } else if (second == third && third == fourth && fourth == fifth) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isFullHouse(ArrayList<Card> hand){
        if (isThreeKind(hand) && isPair(hand)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isFlush(ArrayList<Card> hand){
        Character suit = hand.get(0).getSuit();
        for (Card c : hand) {
            if (c.getSuit() != suit) {
                return false;
            }
        }
        return true;
    }

    public static boolean isStraight(ArrayList<Card> hand){
        int firstNum = hand.get(0).getNumber();
        for (Card c : hand) {
            int nextNum = firstNum++;
            if (c.getNumber() != nextNum) {
                return false;
            }
            nextNum++;
        }
        return true;
    }

    public  static boolean isThreeKind(ArrayList<Card> hand){
        int first = hand.get(0).getNumber();
        int second = hand.get(1).getNumber();
        int third = hand.get(2).getNumber();
        int fourth = hand.get(3).getNumber();
        int fifth = hand.get(4).getNumber();
        if (first == second && second == third) {
            return true;
        } else if (second == third && third == fourth) {
            return true;
        } else if (third == fourth && fourth == fifth) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isTwoPair(ArrayList<Card> hand) {
        int first = hand.get(0).getNumber();
        int second = hand.get(1).getNumber();
        int third = hand.get(2).getNumber();
        int fourth = hand.get(3).getNumber();
        int fifth = hand.get(4).getNumber();
        if (first == second) {
            if (third == fourth) {
                return true;
            } else if (fourth == fifth) {
                return true;
            } else {
                return false;
            }
        } else if (second == third) {
            if (fourth == fifth) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isPair(ArrayList<Card> hand) {
        int first = hand.get(0).getNumber();
        int second = hand.get(1).getNumber();
        int third = hand.get(2).getNumber();
        int fourth = hand.get(3).getNumber();
        int fifth = hand.get(4).getNumber();
        if (first == second) {
            return true;
        } else if (second == third) {
            return true;
        } else if (third == fourth) {
            return true;
        } else if (fourth == fifth) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isHighCard(ArrayList<Card> hand) {
        return true;
    }

    public static int findRank(ArrayList<Card> hand) {
        if (isRoyalFlush(hand)) {
            return 1;
        } else if(isStraightFlush(hand)) {
            return 2;
        } else if(isFourKind(hand)) {
            return 3;
        } else if(isFullHouse(hand)) {
            return 4;
        } else if(isFlush(hand)) {
            return 5;
        } else if(isStraight(hand)) {
            return 6;
        } else if(isThreeKind(hand)) {
            return 7;
        } else if(isTwoPair(hand)) {
            return 8;
        } else if(isPair(hand)) {
            return 9;
        } else if(isHighCard(hand)) {
            return 10;
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
