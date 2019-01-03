// Creation Date: 01/02/2019
// Creator: Michael Markiewicz

public class Game
{
    // Start of Game Pieces

    private final static int numOfPlayers = 4;  // number of players in game
    private Player [] players;  // array of players
    private RandomDiceThrow dice; // dice used for game
    private Deck chance;    // chance cards
    private Deck community; // community Cards

    public Game()
    {
        players = new Player[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i+=1)
            players[i] = new Player();
        dice = new RandomDiceThrow();
        chance = new Deck(Cards.chanceCards.length);
        community = new Deck(Cards.communityChest.length);
    }

    public void gamePlay()
    {
        int j = 0;
        int doublesCounter = 0; // keeps track of how many times each player has rolled doubles during their turn.
        int dC = 0; // keeps track of how many doubles were rolled during a game. This is used purely for testing purposes.
        int totalTurns = 0; // keeps track of how many turns were taken including doubles.
        while(j < 100)
        {
            System.out.println("Turn Number: " + j);
            for(int i = 0; i < numOfPlayers; i+=1)
            {
                totalTurns += 1;
                int diceRoll = dice.returnSum();
                if (dice.isDoubles())
                {
                    dC += 1;
                }
                if (players[i].isInJail()) // leaves jail if the player rolls doubles or has spent 3 days in jail. The player may take his next turn the seqeuential turn after getting out of jail.
                {
                    if (!dice.isDoubles()) // if the player did not roll doubles
                    {
                        if(players[i].incrementJail()) // will execute if player has spent 3 days in jail
                        {
                            players[i].payMoney(50); // player is forced to pay $50 and continue with his move
                            players[i].getOutOfJail();
                            System.out.println("Player " + i + " pays $50 to leave Jail.\n");
                            continue;
                        }
                        else
                        {
                            System.out.println("Player " + i + " is in Jail. His Turn will be Skipped.\n");
                            continue; // if the player doesn't leave jail. Then the players turn is skipped.
                        }
                    }
                    else
                    {
                        players[i].getOutOfJail();
                        System.out.println("Player " + i + " has rolled Doubles. He will leave jail.\n");
                        continue;
                    }
                }
                if (doublesCounter >= 2 && dice.isDoubles()) // if the player has rolled doubles 3 times in a row.
                {
                    System.out.println("Player " +  " has rolled doubles 3 times. This player will go directly to jail.\n");
                    players[i].goToJail();
                    doublesCounter = 0;
                    continue;
                }
                else if (dice.isDoubles())
                {
                    System.out.println("Player " + i + " has rolled doubles. This player will take another turn after the current one.\n");
                    doublesCounter += 1;
                }
                else
                {
                    doublesCounter = 0;
                }


                players[i].move(diceRoll); // moves player forward


                int currentPosition = players[i].getPosition();     // current position of player making his move
                Board.frequency[currentPosition] += 1; // increases frequency array by 1

                if(Board.isProperty[currentPosition] && !Board.isOwned[currentPosition] && players[i].getMoney() >= Board.purchaseCost[currentPosition])
                // case in which tile is unowned and is able to be purchased
                {
                    purchase(i, currentPosition);
                }
                else if(Board.isProperty[currentPosition] && Board.isOwned[currentPosition] && (i != Board.ownedBy[currentPosition]))
                // case in which tile is owned -- rent!!
                {
                    rent(i, currentPosition);
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
                    chance(i, currentPosition);     // draws a chance card
                }
                else if(currentPosition == 2 || currentPosition == 17 || currentPosition == 33) // community chest
                {
                    community(i, currentPosition);  // draws a community chest card
                }
                else if(currentPosition == 30) // go to Jail tile
                {
                    players[i].goToJail();
                    System.out.println("Player " + i + " lands on 'Go To Jail' Tile. This Player will be sent directly to Jail.\n");
                    continue;
                }

                // Testing Purposes
                players[i].printProperties();
                System.out.println("Player " + i + " position: " + players[i].getPosition() + ", money: " + players[i].getMoney());
                System.out.println("Player's Location: " + Board.tileName[players[i].getPosition()] + "\n");

                if (dice.isDoubles())
                    i-=1; // makes it so that the player that just went will take another turn.
            }
            System.out.println("\n\n\n");

            j+=1;
        }

        System.out.println("Total number of doubles: " + dC + ". Frequency: " + (float)dC/(totalTurns));
    }

    private void chance(int i, int currentPosition) // draws a chance card
    {
        int index = chance.Draw();
        switch (index)
        {
            case 0:         // advance to GO (collect $200)
                players[i].goToStart();
            case 1:         // Advance to Illinois Ave.
                if (currentPosition > 24) // 24 = Tile Position for Illinois Avenue
                    players[i].earnMoney(200);  // $200 for passing GO
                players[i].moveTo(24); // moves player to Illinois Avenue
                if (Board.isOwned[24])
                    rent(i, 24);
                else
                    purchase(i, 24);
            case 2:
        }
        System.out.prinln("Player " + i + " has drawn a chance Card: " + Cards.chanceCards[index]);
    }

    private void community(int i, int currentPosition) // draws a community chest card
    {

    }

    private void purchase(int i, int currentPosition)   // purchases a unpurchased tile
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

    public void rent(int i, int currentPositon)
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
}
