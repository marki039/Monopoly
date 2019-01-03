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
                    purchase(i, currentPosition);
                }
                else if(Board.isProperty[currentPosition] && Board.isOwned[currentPosition] && (i != Board.ownedBy[currentPosition]))
                // case in which tile is owned
                {
                    if (currentPosition == 12 || currentPosition == 28)  // exception for a utility tile
                    {
                        if (Board.isOwned[12] && Board.isOwned[28] && (Board.ownedBy[12] == Board.ownedBy[28])) // case if both utilities are owned by same player
                        {
                            players[i].payMoney(10*diceRoll);
                            players[Board.ownedBy[currentPosition]].earnMoney(10*diceRoll);
                            System.out.println("Player " + i + " pays $" + 10*diceRoll + " to player " + Board.ownedBy[currentPosition] + " to stay at " + Board.tileName[currentPosition]);
                        }
                        else    // case in which player only owns one utility
                        {
                            players[i].payMoney(4*diceRoll);
                            players[Board.ownedBy[currentPosition]].earnMoney(4*diceRoll);
                            System.out.println("Player " + i + " pays $" + 4*diceRoll + " to player " + Board.ownedBy[currentPosition] + " to stay at " + Board.tileName[currentPosition]);
                        }
                    }
                    else    // any other rentable property
                    {
                        players[i].payMoney(Board.currentRentPrice[currentPosition]);
                        players[Board.ownedBy[currentPosition]].earnMoney(Board.currentRentPrice[currentPosition]);
                        System.out.println("Player " + i + " pays $" + Board.currentRentPrice[currentPosition] + " to player " + Board.ownedBy[currentPosition] + " to stay at " + Board.tileName[currentPosition]);
                    }
                }
                else if(currentPosition == 4)       // income tax
                {
                    int tenPercent = players[i].getMoney() % 10;
                    if (tenPercent < 200)   // choose lowest option: 10% or $200
                        players[i].payMoney(tenPercent);
                    else
                        players[i].payMoney(200);
                }
                else if(currentPosition == 38)  // luxury tax -- flat fee of $75
                {
                    players[i].payMoney(75);
                }
                else if(currentPosition == 7 || currentPosition == 22 || currentPosition == 36) // chance cards
                {
                    // TODO
                }
                else if(currentPosition == 2 || currentPosition == 17 || currentPosition == 33) // community chest
                {
                    // TODO
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

    private void purchase(int i, int currentPosition)
    {
        players[i].purchaseProperty(currentPosition);
        Board.ownedBy[currentPosition] = i;
        System.out.println("Player " + i + " purchases property " + Board.tileName[currentPosition]);

        for (int j = 0; j < 2; j++) // adds ownership to Purple color group
        {
            if(currentPosition == Improvement.P[j]) // case in which the purchased property is in the selected color group
            {
                Improvement.ownsP[j] = i;
                if (Improvement.ownsP[(j+1)%2] != null && Improvement.ownsP[(j+1)%2] == i) // case in which the player owns all of the color group
                {
                    Board.currentRentPrice[Improvement.P[j]] *= 2;
                    Board.currentRentPrice[Improvement.P[(j+1)%2]] *= 2;
                    players[i].increaseColor();

                    System.out.println("\n\n YOU DID IT \n\n");
                }
                return;
            }
        }

        for (int j = 0; j < 3; j++) // adds ownership to Light Blue color group
        {
            if(currentPosition == Improvement.LB[j]) // case in which the purchased property is in the selected color group
            {
                Improvement.ownsLB[j] = i;
                if (Improvement.ownsLB[(j+1)%3] != null && Improvement.ownsLB[(j+2)%3] != null && Improvement.ownsLB[(j+1)%3] == i && Improvement.ownsLB[(j+2)%3] == i) // case in which the player owns all of the color group
                {
                    Board.currentRentPrice[Improvement.LB[j]] *= 2;
                    Board.currentRentPrice[Improvement.LB[(j+1)%3]] *= 2;
                    Board.currentRentPrice[Improvement.LB[(j+2)%3]] *= 2;
                    players[i].increaseColor();

                    System.out.println("\n\n YOU DID IT \n\n");
                }
                return;
            }
        }

        for (int j = 0; j < 3; j++) // adds ownership to Maroon color group
        {
            if(currentPosition == Improvement.M[j]) // case in which the purchased property is in the selected color group
            {
                Improvement.ownsM[j] = i;
                if (Improvement.ownsM[(j+1)%3] != null && Improvement.ownsM[(j+2)%3] != null && Improvement.ownsM[(j+1)%3] == i && Improvement.ownsM[(j+2)%3] == i) // case in which the player owns all of the color group
                {
                    Board.currentRentPrice[Improvement.M[j]] *= 2;
                    Board.currentRentPrice[Improvement.M[(j+1)%3]] *= 2;
                    Board.currentRentPrice[Improvement.M[(j+2)%3]] *= 2;
                    players[i].increaseColor();

                    System.out.println("\n\n YOU DID IT \n\n");
                }
                return;
            }
        }

        for (int j = 0; j < 3; j++) // adds ownership to Orange color group
        {
            if(currentPosition == Improvement.O[j]) // case in which the purchased property is in the selected color group
            {
                Improvement.ownsO[j] = i;
                if (Improvement.ownsO[(j+1)%3] != null && Improvement.ownsO[(j+2)%3] != null && Improvement.ownsO[(j+1)%3] == i && Improvement.ownsO[(j+2)%3] == i) // case in which the player owns all of the color group
                {
                    Board.currentRentPrice[Improvement.O[j]] *= 2;
                    Board.currentRentPrice[Improvement.O[(j+1)%3]] *= 2;
                    Board.currentRentPrice[Improvement.O[(j+2)%3]] *= 2;
                    players[i].increaseColor();

                    System.out.println("\n\n YOU DID IT \n\n");
                }
                return;
            }
        }

        for (int j = 0; j < 3; j++) // adds ownership to Red color group
        {
            if(currentPosition == Improvement.R[j]) // case in which the purchased property is in the selected color group
            {
                Improvement.ownsR[j] = i;
                if (Improvement.ownsR[(j+1)%3] != null && Improvement.ownsR[(j+2)%3] != null && Improvement.ownsR[(j+1)%3] == i && Improvement.ownsR[(j+2)%3] == i) // case in which the player owns all of the color group
                {
                    Board.currentRentPrice[Improvement.R[j]] *= 2;
                    Board.currentRentPrice[Improvement.R[(j+1)%3]] *= 2;
                    Board.currentRentPrice[Improvement.R[(j+2)%3]] *= 2;
                    players[i].increaseColor();

                    System.out.println("\n\n YOU DID IT \n\n");
                }
                return;
            }
        }

        for (int j = 0; j < 3; j++) // adds ownership to Yellow color group
        {
            if(currentPosition == Improvement.Y[j]) // case in which the purchased property is in the selected color group
            {
                Improvement.ownsY[j] = i;
                if (Improvement.ownsY[(j+1)%3] != null && Improvement.ownsY[(j+2)%3] != null && Improvement.ownsY[(j+1)%3] == i && Improvement.ownsY[(j+2)%3] == i) // case in which the player owns all of the color group
                {
                    Board.currentRentPrice[Improvement.Y[j]] *= 2;
                    Board.currentRentPrice[Improvement.Y[(j+1)%3]] *= 2;
                    Board.currentRentPrice[Improvement.Y[(j+2)%3]] *= 2;
                    players[i].increaseColor();

                    System.out.println("\n\n YOU DID IT \n\n");
                }
                return;
            }
        }

        for (int j = 0; j < 3; j++) // adds ownership to Green color group
        {
            if(currentPosition == Improvement.G[j]) // case in which the purchased property is in the selected color group
            {
                Improvement.ownsG[j] = i;
                if (Improvement.ownsG[(j+1)%3] != null && Improvement.ownsG[(j+2)%3] != null && Improvement.ownsG[(j+1)%3] == i && Improvement.ownsG[(j+2)%3] == i) // case in which the player owns all of the color group
                {
                    Board.currentRentPrice[Improvement.G[j]] *= 2;
                    Board.currentRentPrice[Improvement.G[(j+1)%3]] *= 2;
                    Board.currentRentPrice[Improvement.G[(j+2)%3]] *= 2;
                    players[i].increaseColor();

                    System.out.println("\n\n YOU DID IT \n\n");
                }
                return;
            }
        }

        for (int j = 0; j < 2; j++) // adds ownership to Dark Blue color group
        {
            if(currentPosition == Improvement.DB[j]) // case in which the purchased property is in the selected color group
            {
                Improvement.ownsDB[j] = i;
                if (Improvement.ownsDB[(j+1)%2] != null && Improvement.ownsDB[(j+1)%2] == i) // case in which the player owns all of the color group
                {
                    Board.currentRentPrice[Improvement.DB[j]] *= 2;
                    Board.currentRentPrice[Improvement.DB[(j+1)%2]] *= 2;
                    players[i].increaseColor();

                    System.out.println("\n\n YOU DID IT \n\n");
                }
                return;
            }
        }

        if (currentPosition % 10 == 5) // case for railroads
        {
            players[i].increaseRR();
            players[i].rrRentIncrease();
        }
    }
}
