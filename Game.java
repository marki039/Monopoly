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
                players[i].move(dice.returnSum());
                int currentPosition = players[i].getPosition();     // current position of player making his move
                Board.frequency[currentPosition] += 1; // increases frequency array by 1

                if(Board.isProperty[currentPosition] && !Board.isOwned[currentPosition] && players[i].getMoney() >= Board.purchaseCost[currentPosition])
                {
                    players[i].purchaseProperty(currentPosition);
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
