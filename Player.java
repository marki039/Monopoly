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

    // ColorGroupNode is a list-class used for storing the list of properties in which the player has a monopoly
    private class ColorGroupNode
    {
        private int [] group;   // referes to a particular group
        private String name;    // name of particular group
        private int housePrice; // refers to the price required to build a house
        private int numOfHouses;// refers to the number of houses on this group
        private int numOfHotels;// refers to the number of hotels on this group
        private ColorGroupNode next;    // refers to next color group owned

        ColorGroupNode(int [] group, String name, int housePrice, ColorGroupNode next)
        {
            this.group = group;
            this.name = name;
            this.housePrice = housePrice;
            numOfHouses = 0;
            numOfHotels = 0;
            this.next = next;
        }

        private boolean full () // returns true if the current property has the max number of houses and hotels on it
        {
            return (numOfHouses == 4 * group.length) && (numOfHotels == group.length);
        }

        private void increaseHouse(int i) // increases the number of houses on the color group
        {
            if (numOfHouses >= 4*group.length && numOfHotels < group.length)
            {
                int index = group[numOfHotels];     // index of property that will be improved
                numOfHotels += 1;
                totalNumOfHotels += 1;
                Board.currentRentPrice[index] = Board.baseRent[index][5];       // builds a hotel at the lowest tile number of the group that doesn't currently have a hotel
                money -= housePrice;

                System.out.println("Player " + i + " has purchased a hotel on tile " + index + ": " + Board.tileName[index]);
            }
            else if (numOfHouses < 4*group.length)
            {
                int houseNumber = numOfHouses / group.length + 1;   // refers to which round of houses is being build: will either be 1, 2, 3, or 4
                int index = group[numOfHouses % group.length];      // index of property that will be improved
                numOfHouses += 1;
                totalNumOfHouses += 1;
                // System.out.println("\n\n" + group.length + "\n\n" + numOfHouses + "\n\n" + houseNumber);
                Board.currentRentPrice[index] = Board.baseRent[index][houseNumber];       // builds a house at the lowest tile number of the group that doesn't currently have a house
                money -= housePrice;

                System.out.println("Player " + i + " has purchased house number " + houseNumber + " on tile " + index + ": " + Board.tileName[index]);
            }
            else
            {
                // System.out.println("Player " + i + " has purchased all the improvements.");
            }
        }

        private void decreaseHouse(int i)   // decreases the number of houses/hotels on the color groups
        {
            if(numOfHouses == 0 && numOfHotels == 0)
            {
                throw new IllegalStateException("No More Houses To Sell!");
            }
            else if (numOfHotels > 0)   // sells hotels first
            {
                int index = group[numOfHotels - 1];     // index of property that will be improved
                numOfHotels -= 1;
                totalNumOfHotels -= 1;
                Board.currentRentPrice[index] = Board.baseRent[index][4];       // sells a hotel and returns the rent price to that of a property with 4 houses
                money += (int)(0.5*housePrice); // refunds 50% of money

                System.out.println("Player " + i + " has sold a hotel on tile " + index + ": " + Board.tileName[index]);
            }
            else if (numOfHouses > 0)
            {
                int houseNumber = numOfHouses / group.length + 1;   // refers to which round of houses is being sold: will either be 1, 2, 3, or 4
                int index = group[numOfHouses % group.length];      // index of property that will be sold
                numOfHouses -= 1;
                totalNumOfHouses -= 1;
                // System.out.println("\n\n" + group.length + "\n\n" + numOfHouses + "\n\n" + houseNumber);
                Board.currentRentPrice[index] = Board.baseRent[index][houseNumber - 1];       // sells a house at the lowest tile number of the group that doesn't currently have a house
                money += (int)(0.5*housePrice); // refunds 50% of money

                System.out.println("Player " + i + " has sold house number " + houseNumber + " on tile " + index + ": " + Board.tileName[index]);
            }
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
    private ColorGroupNode colorGroup;      // refers to a list of the color groups this player owns
    private boolean totallyBankrupt;    // refers to whether the player is out of the game
    private int totalNumOfHotels;       // refers to the total number of hotels this player owns
    private int totalNumOfHouses;       // refers to the total number of houses this player owns
    private int numOfMortgages;     // refers to the number of properties this player has that are mortgaged

    Player()
    {
        money = 1500;  // Starting Cash = $1500
        position = 0;  // always starts at GO (tile 0)
        properties = null;  // starts with no properties
        colorGroupsOwned = 0;
        numRR = 0;
        inJail = false; // doesn't start in jail (born innocent)
        jailCounter = 0;
        getOutJailFree = 0;
        colorGroup = null;  // starts with no color groups
        totallyBankrupt = false;
        totalNumOfHotels = 0;
        totalNumOfHouses = 0;
        numOfMortgages = 0;
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
            /*
            System.out.println("Player has sold a property. The list of properties left are: ");
            temp = properties;
            while (temp != null)
            {
                System.out.println(Board.tileName[temp.tileNumber]);
                temp = temp.next;
            }
            */
            // temp = properties;

            // checks first item in list
            if (temp.tileNumber == tileNumber)
            {
                // System.out.println("Here.");
                money += sellPrice;
                properties = properties.next; // removes property from player's property list
                // Board.isOwned[tileNumber] = false;

                System.out.println("Player has sold a property. The list of properties left are: ");
                temp = properties;
                while (temp != null)
                {
                    System.out.println(Board.tileName[temp.tileNumber]);
                    temp = temp.next;
                }

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
                // System.out.println("No Here.");
                if (temp.tileNumber == tileNumber)
                {
                    money += sellPrice;
                    old.next = temp.next; // removes property from player's property list
                    // Board.isOwned[tileNumber] = false;

                    System.out.println("Player has sold a property. The list of properties left are: ");
                    temp = properties;
                    while (temp != null)
                    {
                        System.out.println(Board.tileName[temp.tileNumber]);
                        temp = temp.next;
                    }

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
    public void decreaseColor() { colorGroupsOwned -= 1; }      // decrements colorGroupOwned
    public int returnColor() { return colorGroupsOwned; }       // returns number of color groups player has monopolized

    public void increaseRR() { numRR += 1; }        // increments number of railroads owned
    public void decreaseRR() { numRR -= 1; }
    public int returnRR() { return numRR; }         // returns number of railroads player owns

    public void rrRentIncrease()    // changes rent when number of railroads owned is greater than 1
    {
        System.out.println("Rail Road Rent Increase!!");
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
                    System.out.println(Board.tileName[temp.tileNumber] + " " + Board.currentRentPrice[temp.tileNumber]);
                    temp = temp.next;
                }
            }
        }
    }

    public void rrRentDecrease()    // changes rent when the player looses a railroad
    {
        PropertyNode temp = properties;
        System.out.println("Loose RR");
        if (numRR > 1)
        {
            for (int j = 0; j < numRR + 1; j+=1)
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
                    Board.currentRentPrice[temp.tileNumber] = 25 * (int)Math.pow(2, numRR - 1);     // decreases rent price accordingly
                    System.out.println(Board.tileName[temp.tileNumber] + "'s rent has decreased to: " + Board.currentRentPrice[temp.tileNumber]);
                    temp = temp.next;
                }
            }
        }
        else
        {
            for (int j = 0; j < numRR + 1; j+=1)
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
                    Board.currentRentPrice[temp.tileNumber] = 25;     // decreases rent price accordingly
                    System.out.println(Board.tileName[temp.tileNumber] + "'s rent has decreased to: " + Board.currentRentPrice[temp.tileNumber]);
                    temp = temp.next;
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

    public boolean ownsGroup() { return colorGroupsOwned > 0; }     // returns true if the player has monopolized a certain color group

    public void addColorGroup(int [] group, String name, int housePrice)   // adds a color group to the list of groups this player owns
    {
        colorGroup = new ColorGroupNode(group, name, housePrice, colorGroup);
    }
    public void deleteColorGroup(String name)       // deletes a color group from the list of groups this player owns
    {
        ColorGroupNode temp = colorGroup;
        ColorGroupNode old = colorGroup;
        while (temp != null && !temp.name.equals(name))
        {
            old = temp;
            temp = temp.next;
        }
        if (temp == null)
        {
            throw new IllegalStateException("Trying to sell a color group that this player does not own.");
        }
        else
        {
            old = temp.next;        // gets rid of color group from its list
        }
    }

    public void addHouse(int i) // adds a house on a color group with the least number of improvements
    {
        ColorGroupNode temp = colorGroup;
        ColorGroupNode least = colorGroup;
        // System.out.println("I am before while loop.");
        while (temp != null)        // find the color group with the least number of improvements
        {
            if (!temp.full() && (temp.numOfHouses < least.numOfHouses || temp.numOfHotels < least.numOfHotels))
            {
                least = temp;
            }
            temp = temp.next;
        }
        // System.out.println("I am after while loop.");

        least.increaseHouse(i);     // increases the improvement on this particular tile
    }

    public int houses() { return totalNumOfHouses; }
    public int hotels() { return totalNumOfHotels; }

    public void sellHouse(int i)
    {
        ColorGroupNode temp = colorGroup;
        ColorGroupNode most = colorGroup;
        // System.out.println("I am before while loop.");
        while (temp != null)        // find the color group with the most number of improvements
        {
            if (temp.numOfHouses > most.numOfHouses || temp.numOfHotels > most.numOfHotels)
            {
                most = temp;
            }
            temp = temp.next;
        }

        most.decreaseHouse(i);
    }

    public void sellHotel(int i)
    {
        sellHouse(i);
    }

    public boolean mortgage(int i)        // returns true if it successfully mortgaged a property
    {
        PropertyNode temp = properties;
        while(temp != null && Board.mortgaged[temp.tileNumber])     // finds a property that is not yet mortgaged
        {
            temp = temp.next;
        }
        if (temp == null)
        {
            return false;
        }
        else
        {
            Board.mortgaged[temp.tileNumber] = true; // mortgages property
            money += (int)(0.5 * Board.purchaseCost[temp.tileNumber]);  // gives back half the money
            System.out.println("Player " + i + " has mortgaged property: " + Board.tileName[temp.tileNumber]);
            numOfMortgages += 1;
            return true;
        }
    }

    public void deMortgage(int i)     // demortgages a property
    {
        PropertyNode temp = properties;
        while(temp != null && !Board.mortgaged[temp.tileNumber])     // finds a property that is mortgaged
        {
            temp = temp.next;
        }
        if (temp == null)
        {
            throw new IllegalStateException("Somehow go to this point: " + numOfMortgages);
        }
        else
        {
            Board.mortgaged[temp.tileNumber] = false; // demortgages property
            money -= (int)(1.1 * 0.5 * Board.purchaseCost[temp.tileNumber]);  // pays 10% interest rate on mortgate
            System.out.println("Player " + i + " has paid off mortgaged property: " + Board.tileName[temp.tileNumber]);
            numOfMortgages -= 1;
        }
    }

    public boolean hasMortgaged() { return numOfMortgages > 0; }        // returns true if this player has a mortgaged property

    public int propertyToSell()     // returns the index of a property the player will sell
    {
        PropertyNode temp = properties;
        PropertyNode least = properties;
        // System.out.println("I am before while loop.");
        while (temp != null)        // find the color group with the least number of improvements
        {
            if (temp.tileNumber < least.tileNumber)
            {
                least = temp;
            }
            temp = temp.next;
        }

        return least.tileNumber;
    }

    public boolean hasProperty() { return properties != null; } // returns true if the player has properties

    public void totallyBankrupt() { totallyBankrupt = true; }
    public boolean isTotallyBankrupt() { return totallyBankrupt; }

    public void payForHousesAndHotels (int house, int hotel)
    {
        money -= totalNumOfHouses*house;
        money -= totalNumOfHotels*hotel;
    }

    public void increaseMortgage() { numOfMortgages += 1; }  // increases the number of mortgages
    public void decreaseMortgage() { numOfMortgages -= 1; }  // decreases the number of mortgages
}
