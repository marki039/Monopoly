// Creation Date: 01/02/2019
// Creator: Michael Markiewicz
public class Game
{

    // PropertyNode is a list-class used for storing the properties each player owns
    private class PropertyNode
    {
        private int tileNumber;     // refers to tile on board
        private PropertyNode next;
        // private boolean ownsAllColorGroup; // True iff player owns all properties in a respective color group of this property
        // private int numHouses; // number of houses built on property, 5 = Hotel

        PropertyNode(int tileNumber, PropertyNode next)
        {
            this.tileNumber = tileNumber;
            this.next = next;
        }
    }

    // Player is a class used to represent the players in the game
    }
    private class Player
    {
        private int money; // refers to how much money player currently has
        private int position; // refers to position of player on board
        private PropertyNode properties; // refers to list of proeprties this player owns
        private int numProperties; // refers to number of properties this player owns

        Player()
        {
            money = 1500;  // Starting Cash = $1500
            position = 0;  // always starts at GO (tile 0)
            properties = null;  // starts with no properties
            numProperties = 0;
        }

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
                throw new IllegalArgumentException("Error:   " + Board.tileName[tileNumber] + " is not a property.")
            }
        }
    }
}
