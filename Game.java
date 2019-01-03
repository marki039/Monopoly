// Creation Date: 01/02/2019
// Creator: Michael Markiewicz

public class Game
{
    // Start of Game Pieces

    private final static int numOfPlayers = 4;  // number of players in game
    private Player [] players;  // array of players
    private RandomDiceThrow dice; // dice used for game

    public Game()
    {
        players = new Player[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i+=1)
            players[i] = new Player();
        dice = new RandomDiceThrow();
    }

    public void gamePlay()
    {
        int j = 0;
        while(j < 100)
        {
            System.out.println("Turn Number: " + j);
            for(int i = 0; i < numOfPlayers; i+=1)
            {
                int diceRoll = dice.returnSum();
                players[i].move(diceRoll);
                int currentPosition = players[i].getPosition();     // current position of player making his move
                Board.frequency[currentPosition] += 1; // increases frequency array by 1

                if(Board.isProperty[currentPosition] && !Board.isOwned[currentPosition] && players[i].getMoney() >= Board.purchaseCost[currentPosition])
                // case in which tile is unowned and is able to be purchased
                {
                    players[i].purchaseProperty(currentPosition);
                    Board.ownedBy[currentPosition] = i;
                    System.out.println("Player " + i + " purchases property " + Board.tileName[currentPosition]);
                }
                else if(Board.isProperty[currentPosition] && Board.isOwned[currentPosition] && (i != Board.ownedBy[currentPosition]))
                // case in which tile is owned
                {
                    if (currentPosition == 12 || currentPosition == 28)  // exception for a utility tile
                    {
                        if (Board.isOwned[12] && Board.isOwned[28] && (Board.ownedBy[12] == Board.ownedBy[28])) // case if both utilities are owned by same player
                        {
                            players[i].payMoney(10*diceRoll);
                            players[Board.ownedBy[currentPosition]].getMoney(10*diceRoll);
                            System.out.println("Player " + i + " pays $" + 10*diceRoll + " to player " + Board.ownedBy[currentPosition] + " to stay at " + Board.tileName[currentPosition]);
                        }
                        else    // case in which player only owns one utility
                        {
                            players[i].payMoney(4*diceRoll);
                            players[Board.ownedBy[currentPosition]].getMoney(4*diceRoll);
                            System.out.println("Player " + i + " pays $" + 4*diceRoll + " to player " + Board.ownedBy[currentPosition] + " to stay at " + Board.tileName[currentPosition]);
                        }
                    }
                    else    // any other rentable property
                    {
                        players[i].payMoney(Board.currentRentPrice[currentPosition]);
                        players[Board.ownedBy[currentPosition]].getMoney(Board.currentRentPrice[currentPosition]);
                        System.out.println("Player " + i + " pays $" + Board.currentRentPrice[currentPosition] + " to player " + Board.ownedBy[currentPosition] + " to stay at " + Board.tileName[currentPosition]);
                    }
                }

                // Testing Purposes
                players[i].printProperties();
                System.out.println("Player " + i + " position: " + players[i].getPosition() + ", money: " + players[i].getMoney());
                System.out.println("Player's Location: " + Board.tileName[players[i].getPosition()] + "\n");
            }
            System.out.println("\n\n\n");


            j+=1;
        }
    }
}
