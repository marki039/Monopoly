// Creation Date: 01/02/2019
// Creator: Michael Markiewicz

// Player is a class used to represent the players in the game

public class Player
{
    // PropertyNode is a list-class used for storing the properties each player owns
    private class PropertyNode
    {
        private int tileNumber;     // refers to tile on board
        private PropertyNode next;  // refers to next property in list

        PropertyNode(int tileNumber, PropertyNode next)
        {
            this.tileNumber = tileNumber;
            this.next = next;
        }
    }


    // Player Member Variables
    private int money; // refers to how much money player currently has
    private int position; // refers to position of player on board
    private PropertyNode properties; // refers to list of proeprties this player owns
    private int colorGroupsOwned; // refers to number of Color Groups this player has monopolized
    private int numRR; // refers to the number of railroads this player owns
    private boolean inJail; // true if player is in jail
    private int jailCounter; // counts how many turns the player has been in jail (maximum of 3 turns in jail)
    private int getOutJailFree; // counts number of Get out of Jail Free Cards this player holds.

    Player()
    {
        money = 1500;  // Starting Cash = $1500
        position = 0;  // always starts at GO (tile 0)
        properties = null;  // starts with no properties
        colorGroupsOwned = 0;
        numRR = 0;
        inJail = false;
        jailCounter = 0;
        getOutJailFree = 0;
    }

    // function used to purchase property for player
    void purchaseProperty(int tileNumber)
    {
        // makes sure property is purchaseable and not already owned
        if (Board.isProperty[tileNumber] && !Board.isOwned[tileNumber])
        {
            money -= Board.purchaseCost[tileNumber];
            if (money < 0) // if money goes negative
            {
                throw new IllegalArgumentException("Error:   Player is out of money.");
            }
            if (properties == null) // special case in which this is first property purchase
            {
                properties = new PropertyNode(tileNumber, null); // adds purchased property to property list
            }
            else // every other case in which player already owns property
            {
                properties = new PropertyNode(tileNumber, properties); // adds purchased property to property list
            }
            Board.isOwned[tileNumber] = true;
        }
        else if (Board.isOwned[tileNumber])
        {
            throw new IllegalArgumentException("Error:    Property " + Board.tileName[tileNumber] + " is already owned.");
        }
        else
        {
            throw new IllegalArgumentException("Error:   " + Board.tileName[tileNumber] + " is not a property.");
        }
    }

    // function used to sell property for player at a specific price
    void sellProperty(int tileNumber, int sellPrice)
    {
        if (!Board.isProperty[tileNumber])
        {
            throw new IllegalArgumentException("Error:   " + Board.tileName[tileNumber] + " is not a sellable property.");
        }
        else
        {
            PropertyNode temp = properties;
            PropertyNode old;

            // checks first item in list
            if (temp.tileNumber == tileNumber)
            {
                money += sellPrice;
                temp = temp.next; // removes property from player's property list
                Board.isOwned[tileNumber] = false;
                return;
            }
            else
            {
                old = temp;
                temp = temp.next;
            }

            // checks other items in list
            while(temp != null)
            {
                if (temp.tileNumber == tileNumber)
                {
                    money += sellPrice;
                    old.next = temp.next; // removes property from player's property list
                    Board.isOwned[tileNumber] = false;
                    return;
                }
                old = temp;
                temp = temp.next;
            }
        }
    }

    public int getPosition() { return position; }
    public int getMoney() {  return money; }

    public void move(int forwardSpeed)  // moves forward player a certain number of tiles
    {
        position += forwardSpeed;

        if(position >= 40)
        {
            position = position % 40; // keeps players position under 40
            money += 200;       // collect $200 as you pass GO
        }
    }

    public void moveTo(int position)   // moves player to a certain position on the Board
    {
        this.position = position;
    }

    public void printProperties()       // prints Properties currently owned by player
    {
        if (properties == null)
        {
            System.out.println("No Owned Properties");
        }
        else
        {
            PropertyNode temp = properties;
            StringBuilder sb = new StringBuilder();
            sb.append("Owned Properties: ");
            while(temp.next != null)
            {
                sb.append(Board.tileName[temp.tileNumber] + ", ");
                temp = temp.next;
            }
            sb.append(Board.tileName[temp.tileNumber] + ".");

            System.out.println(sb.toString());
        }
    }

    public void payMoney(int cost)      // pays money to someone or something
    {
        money -= cost;
    }

    public void earnMoney(int cost)      // gets money from someone or something
    {
        money += cost;
    }

    public void increaseColor() { colorGroupsOwned += 1; }      // increments colorGroupsOwned
    public int returnColor() { return colorGroupsOwned; }       // returns number of color groups player has monopolized

    public void increaseRR() { numRR += 1; }        // increments number of railroads owned
    public int returnRR() { return numRR; }         // returns number of railroads player owns

    public void rrRentIncrease()    // changes rent when number of railroads owned is greater than 1
    {
        if (numRR > 1)
        {
            PropertyNode temp = properties;
            for (int j = 0; j < numRR; j+=1)
            {
                while(temp != null && temp.tileNumber % 10 != 5)    // all railroad tiles have a '5' in the one's position
                {
                    temp = temp.next;
                }
                if (temp == null)
                {
                    throw new NullPointerException("temp became null before all railroads were found");
                }
                else
                {
                    Board.currentRentPrice[temp.tileNumber] = 25 * (int)Math.pow(2, numRR - 1);     // increases rent price accordingly
                }
            }
        }
    }

    public void goToJail() // puts player in jail
    {
        position = 10; // changes position to jail cell
        inJail = true;
    }
    public void getOutOfJail() // takes player out of jail
    {
        jailCounter = 0;
        inJail = false;
    }
    public boolean isInJail() { return inJail; } // returns status of player in jail

    public boolean incrementJail() // incremements the number
    {
        jailCounter += 1;
        if (jailCounter >= 3)
        {
            jailCounter = 0;
            return true; // returns true if the player has spent three turns in jail
        }
        else
        {
            return false;
        }
    }

    public void goToStart()  // goes to GO tile and earns $200
    {
        position = 0;
        money += 200;
    }

    public void getOutOfJailFree() { getOutJailFree += 1; }       // increments the number of get out of jail free cards. There should never be more than 2 in a game at a time
    public boolean hasJailCard() { return getOutJailFree > 0; }     // returns true if the player has an avaliable Jail Card for use

    public void useJailCard()   // uses one of the jail cards to get out of jail (Free)
    {
        if (getOutJailFree <= 0)
        {
            throw new IllegalStateException("Cannot use jail card if you have no jail cards!!"); // useful error
        }
        getOutJailFree -= 1;
        getOutOfJail();
    }
}
