/*
 * Etude 13: Super Sized Poker Hands
 * Authors : Mika Smith, Kimberley Louw, Nathan Hardy, Mathew Boyes
 *
 */

import java.io.*;
import java.util.*;

public class SuperPoker{

	private static int[] order;
	private static HashMap<Character, Integer> nums = new HashMap<Character, Integer>();
	private static ArrayList<Character> suits = new ArrayList<Character>();
	private static ArrayList<Character> seperators = new ArrayList<Character>();
	public static ArrayList<Card> hand = new ArrayList<Card>();
	public static HashMap<Integer, String> ranks = new HashMap<Integer, String>();
	public static char seperatorType;
	public static Boolean valid;

	/*
	 * Constructor to initialise values.
	 */
	public SuperPoker(){
		order = new int[]{2,3,4,5,6,7,8,9,10,11,12,13,1};
		hand = new ArrayList<Card>();

		nums.put('A',1);
		nums.put('T', 10);
		nums.put('J', 11);
		nums.put('Q', 12);
		nums.put('K', 13);

		suits.add('C');
		suits.add('D');
		suits.add('H');
		suits.add('S');

		seperators.add(' ');
		seperators.add('/');
		seperators.add('-');
	}

	public static Boolean isRoyalFlush(ArrayList<Card> hand){
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

	public static Boolean isStraightFlush(ArrayList<Card> hand){
		if (isFlush(hand) && isStraight(hand)) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean isFourKind(ArrayList<Card> hand){
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

	public static Boolean isFullHouse(ArrayList<Card> hand){
		if (isThreeKind(hand) && isPair(hand)) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean isFlush(ArrayList<Card> hand){
		Character suit = hand.get(0).getSuit();
		for (Card c : hand) {
			if (c.getSuit() != suit) {
				return false;
			}
		}
		return true;
	}

	public static Boolean isStraight(ArrayList<Card> hand){
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

	public  static Boolean isThreeKind(ArrayList<Card> hand){
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

	public static Boolean isTwoPair(ArrayList<Card> hand){
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

	public static Boolean isPair(ArrayList<Card> hand){
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

	public static Boolean isHighCard(ArrayList<Card> hand){
		return true;
	}

	public static int findRank(ArrayList<Card> hand){
		if(isRoyalFlush(hand)){
			return 1;
		}else if(isStraightFlush(hand)){
			return 2;
		}else if(isFourKind(hand)){
			return 3;
		}else if(isFullHouse(hand)){
			return 4;
		}else if(isFlush(hand)){
			return 5;
		}else if(isStraight(hand)){
			return 6;
		}else if(isThreeKind(hand)){
			return 7;
		}else if(isTwoPair(hand)){
			return 8;
		}else if(isPair(hand)){
			return 9;
		}else if(isHighCard(hand)){
			return 10;
		}

		return 0;
	}

	/*
	 * The main method scans in lines as individual poker hands
	 * and sorts the cards in each line in ascending order of value.
	 */
	public static void main(String[]args){

		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()){
			SuperPoker poker = new SuperPoker();
			String line = sc.nextLine();
			System.out.println(line);
			Card card;
			valid = true;
			String numString="";
			int num=0;
			char suit='#';
			seperatorType = '#';

			for(int i=0; i<line.length();i++){
				char c = Character.toUpperCase(line.charAt(i)); //scan in every char

				if(Character.isDigit(c)){
					//Check if integer is in range
					numString += c; //add to current number as it could be a double digit
					num = Integer.parseInt(numString);
					if(num<= 0 || num > 13){
						valid = false;
					}
				}else if(nums.containsKey(c)){
					num = nums.get(c);
				}else if(suits.contains(c)){
					//Reached the end of a card so add it to the hand.
					suit = c;
					card = new Card(num, suit);
					hand.add(card);
				}else if(isSeperator(c)){
					//Reset variables for next card.
					num = 0;
					numString = "";
					suit = '#';
				}else{ //Not a valid character
					valid = false;
				}
			}

			System.out.println("sorting cards ");
		//	for(Card i : hand) System.out.print(i.getNumber() + i.getSuit());
			//Check validity and sort hand
			sortHand(hand);

			if(hand.size()!=5 || !valid){
				System.out.println("Invalid: " + line);
			}else{
				StringBuilder sb = new StringBuilder();

				//Print out as letter not number for K, Q, T, J, A
				for(Card c: hand){
					//print number as letter

					int n = c.getNumber();
					Boolean added = false;
					//for each entry, if n is = to the value, then get the key
					for(Map.Entry<Character, Integer> x : nums.entrySet()){
						if(n==x.getValue()){
							sb.append(x.getKey());
							added = true;
							break;
						}
					}
					if(!added){
						sb.append(n);
					}
					sb.append(c.getSuit()+" ");
				}
				System.out.println(sb.toString());
			    System.out.println("Rank of hand is " + findRank(hand));
				//System.out.println();
			}
		}

		sc.close();
	}



	/*
	 * Uses insertion sort to sort the hand as it is a small set of numbers with a
	 * small range. While we are comparing, it also checks if the cards are identical,
	 * in which case the hand will be invalid.
	 */
	public static void sortHand(ArrayList<Card> hand){
		//for each value in the hand.
	//	System.out.println("hand is " + hand);
		for(int i=1; i < hand.size(); i++){
	        Card value = hand.get(i);
	        int j = i-1;

	        //Checks if these are the same card.
	        if(compare(value, hand.get(j))==0){
				valid = false; //The hand is invalid if there is a duplicate
			}

	        while(j>=0 && compare(value, hand.get(j))==-1){
	        	//Swaps values.
	        	Card temp = hand.get(j);
	        	hand.set(j, hand.get(j+1));
	        	hand.set(j+1, temp);
	        	j--;
	        }
	    }
	}

	/*
	 * Compares two cards and returns -1 if x is less than y, 0 if they are equal and
	 * 1 if y is greater than x.
	 */
	public static int compare(Card x, Card y){
		if(x.getNumber() != y.getNumber()){
			return compareNumValue(x.getNumber(), y.getNumber());
		}else{ //will only compare if numbers are equal
			return compareSuit(x.getSuit(), y.getSuit());
		}
	}

	/* Compares two suits and returns -1 if x is less than y, 0 if they are equal and
	 * 1 if y is greater than x.
	 */
	public static int compareSuit(char x, char y){
		int xcount = 0;
		int ycount = 0;
		boolean xfin = false;
		boolean yfin = false;

		for(char c : suits){
			if(x == c){
				xfin = true;
			}else{
				if(!xfin){
					xcount++;
				}
			}

			if(y == c){
				yfin = true;
			}else{
				if(!yfin){
					ycount++;
				}
			}
		}

		if(xcount==ycount){
			return 0;
		}else if(xcount < ycount){
			return -1;
		}else{
			return 1;
		}
	}

	/*
	 * Compares two numbers and returns -1 if x is less value than y, 0 if they are equal value and
	 * 1 if y is greater value than x.
	 */
	public static int compareNumValue(int x, int y){
		int xcount = 0;
		int ycount = 0;
		boolean xfin = false;
		boolean yfin = false;
		for(int i: order){
			if(x == i){
				xfin = true;
			}else{
				if(!xfin){
					xcount++;
				}
			}

			if(y == i){
				yfin = true;
			}else{
				if(!yfin){
					ycount++;
				}
			}
		}

		if(xcount==ycount){
			return 0;
		}else if(xcount < ycount){
			return -1;
		} else {
			return 1;
		}
	}

	/*
	 * This method returns true if a character is a seperator and is the
	 * specified type of seperator for this line
	 */
	public static boolean isSeperator(char c) {
		if (seperators.contains(c)) {
			if (seperatorType=='#') {
				seperatorType = c; //set new seperator type as this char
				return true;
			} else if (c == seperatorType ){
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/*
	 * A data structure which represents a single card and holds its number and suit.
	 */
	public static class Card {
		private final int number;
		private final char suit;

		public Card(int number, char suit) {
			this.number = number;
			this.suit = suit;
		}

		public int getNumber() {
			return number;
		}

		public char getSuit() {
			return suit;
		}
	}
}