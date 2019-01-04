// Creation Date: 01/03/2019
// Creator: Michael Markiewicz

import java.util.Random;

public class Cards
{
    public final static String [] communityChest =
    {
        "Advance to Go (Collect $200)",                                                 // 0
        "Bank error in your favor – collect $75",                                       // 1
        "Doctor's fees – Pay $50",                                                      // 2
        "Holiday Fund matures - Receive $100",                                          // 3
        "Go to jail – go directly to jail – Do not pass Go, do not collect $200",       // 4
        "It is your birthday Collect $10 from each player",                             // 5
        "Grand Opera Night – collect $50 from every player for opening night seats",    // 6
        "Income Tax refund – collect $20",                                              // 7
        "Life Insurance Matures – collect $100",                                        // 8
        "Pay Hospital Fees of $100",                                                    // 9
        "Pay School Fees of $50",                                                       // 10
        "Receive $25 Consultancy Fee",                                                  // 11
        "You are assessed for street repairs – $40 per house, $115 per hotel",          // 12
        "You have won second prize in a beauty contest– collect $10",                   // 13
        "You inherit $100",                                                             // 14
        "From sale of stock you get $50",                                               // 15
        "Get out of jail free – this card may be kept until needed, or sold"            // 16
    };

    public final static String [] chanceCards =
    {
        "Advance to Go (Collect $200)",                                                                                                                                         // 0
        "Advance to Illinois Ave.",                                                                                                                                             // 1
        "Advance token to nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total ten times the amount thrown.",                  // 2
        "Advance token to the nearest Railroad and pay owner twice the rental to which he/she is otherwise entitled. If Railroad is unowned, you may buy it from the Bank.",    // 3
        "Advance token to the nearest Railroad and pay owner twice the rental to which he/she is otherwise entitled. If Railroad is unowned, you may buy it from the Bank.",    // 4
        "Advance to St. Charles Place – if you pass Go, collect $200",                                                                                                          // 5
        "Bank pays you dividend of $50",                                                                                                                                        // 6
        "You have won a crossword competition - collect $100",                                                                                                                  // 7
        "Go back 3 spaces",                                                                                                                                                     // 8
        "Go directly to Jail – do not pass Go, do not collect $200",                                                                                                            // 9
        "Make general repairs on all your property – for each house pay $25 – for each hotel $100",                                                                             // 10
        "Pay poor tax of $15",                                                                                                                                                  // 11
        "Take a trip to Reading Railroad – if you pass Go collect $200",                                                                                                        // 12
        "Take a walk on the Boardwalk – advance token to Boardwalk",                                                                                                            // 13
        "You have been elected chairman of the board – pay each player $50",                                                                                                    // 14
        "Your building loan matures – collect $150",                                                                                                                            // 15
        "Get out of Jail free – this card may be kept until needed, or traded/sold"                                                                                             // 16
    };
}

class Deck
{
    private class Node // helper class used to impliment linked-list deck
    {
        private int index; // index of card in base deck
        private Node next;

        public Node(int index, Node next)
        {
            this.index = index;
            this.next = next;
        }
    }

    private int size;   // total number of cards in Deck
    private Node top;   // top of Deck
    // Note: Get out of Jail Free card will always be the last card in the deck if there is one.
    private boolean includeJail; // true if Get Out of Jail Free card should be included in next shuffle -- false if a player holds the GOJF card
    private Random r;   // pseudorandom number generator for shuffle algorithm

    public Deck(int size)
    {
        this.size = size;
        r = new Random();
        shuffle();
        includeJail = true;
    }

    private void push(int index)    // pushes a card onto the deck. Only used by shuffle function
    {
        top = new Node(index, top);
    }

    public int Draw()    // returns index of card at top of deck and removes card from deck
    {
        if(isEmpty())
        {
            shuffle();  // shuffle deck if all cards are in discard pile
        }
        int index = top.index;
        if (index == size - 1) // if the selected card is a Get Out of Jail Free Card
        {
            includeJail = false; // Get Out of Jail Free Card will not be included in next shuffle unless it is used before
        }
        top = top.next;
        return index;
    }

    private boolean isEmpty() { return top == null; }    // returns true if deck has no more cards

    private void shuffle()
    {
        int size = this.size;   // size of deck that we will be shuffling
        if (!includeJail)
        {
            size -= 1;  // if we shouldn't include the Get Out of Jail Free card, then decrease the size so it doesn't include the last card of the deck.
        }

        int [] deck = new int[size];
        for (int i = 0; i < size; i += 1)
        {
            deck[i] = i;
        }

        // uses Durstenfeld-Fisher-Yates shuffling algorithm

        for (int i = size - 1; i >= 1; i -= 1)
		{
			int j =  Math.abs(r.nextInt()) % (i + 1);
			// it is modulated by (i + 1) so the random number is from 0 to i inclusive
            // switches position of two cards
            int temp = deck[i];
            deck[i] = deck[j];
            deck[j] = temp;
		}

        for (int i = 0; i < size; i += 1)
        {
            push(deck[i]);
        }

        System.out.println("\nShuffled!\n");
    }
}
