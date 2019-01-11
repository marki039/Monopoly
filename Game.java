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
        while(j < 1000)
        {
            System.out.println("Turn Number: " + j);
            for(int i = 0; i < numOfPlayers; i+=1)
            {
                if (players[i].getMoney() <= 0)
                    System.out.println("Sucks to Suck.");

                if (players[i].getMoney() >= 750 && players[i].getMoney() <= 10000 && players[i].ownsGroup())     // if the player has enough money and owns a color group        (< 10000 is there so this function wont be called after the player has enough money)
                {
                    // System.out.println("I am at begining of the end.");
                    int housesToBuy = (players[i].getMoney() - 500) / 200;      // saves about $500 to be used in case of rent, allocated about $200 per house buy
                    for (int k = 0; k < housesToBuy; k++)
                        players[i].addHouse(i);
                }

                totalTurns += 1;
                int diceRoll = dice.returnSum();
                if (dice.isDoubles())
                {
                    dC += 1;
                }
                if (players[i].isInJail()) // leaves jail if the player rolls doubles or has spent 3 days in jail. The player may take his next turn the seqeuential turn after getting out of jail.
                {
                    if (players[i].hasJailCard())
                    {
                        players[i].useJailCard();    // uses get out of jail free card
                        System.out.println("Player " + i + " has used Get Out of Jail Free Card. The Player may continue the turn.");
                        if (!chance.doesDeckHaveJailCard()) // if the chance deck does not have a jail card
                        {
                            chance.putJailCardBack();   // returns jail card back to chance deck
                        }
                        else
                        {
                            community.putJailCardBack();    // returns jail card back to community deck
                        }
                    }
                    else if (!dice.isDoubles()) // if the player did not roll doubles
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
                    if (players[i].getMoney() > (int)(1.5 * Board.purchaseCost[currentPosition]))    // purchases if player has at least 150% of the money required to buy the property
                        purchase(i, currentPosition);
                    else
                    {
                        System.out.println("Player " + i + " chooses to send the property " + Board.tileName[currentPosition] + " to auction.");
                        auction(i, currentPosition, (int)(0.1 * Board.purchaseCost[currentPosition]));       // unowned tile goes to auction, starts at 10% of the cost
                    }
                }
                else if(Board.isProperty[currentPosition] && Board.isOwned[currentPosition] && (i != Board.ownedBy[currentPosition]))
                // case in which tile is owned -- rent!!
                {
                    rent(i, currentPosition, diceRoll);
                }
                else if(currentPosition == 4)       // income tax
                {
                    incomeTax(i);
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
        int index = chance.Draw(); // draws new chance card
        int evenCurrenterPosition; // used for storing new position of player
        switch (index)
        {
            case 0:         // advance to GO (collect $200)
                players[i].goToStart();
                break;
            case 1:         // Advance to Illinois Ave.
                if (currentPosition > 24) // 24 = Tile Position for Illinois Avenue
                    players[i].earnMoney(200);  // $200 for passing GO
                players[i].moveTo(24); // moves player to Illinois Avenue
                if (Board.isOwned[24])
                    rent(i, 24, -1);    // diceRoll = -1 since it does not matter
                else
                    purchase(i, 24);
                break;
            case 2:         // Advance token to nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total ten times the amount thrown.
                // utilities at postion 12 and 28
                if (currentPosition > 28 || currentPosition <= 12) // closet utility is on tile 12
                {
                    if (currentPosition > 28)
                        players[i].earnMoney(200);  // + $200 for passing GO
                    players[i].moveTo(12);
                }
                else    // closest utility is on tile 28
                {
                    players[i].moveTo(28);
                }
                evenCurrenterPosition = players[i].getPosition();
                if (Board.isOwned[evenCurrenterPosition])
                {
                    RandomDiceThrow dice = new RandomDiceThrow();
                    int temp = dice.returnSum()*10;    // 10 * amount thrown on dice
                    players[i].payMoney(temp);  // player that landed on the utility
                    players[Board.ownedBy[evenCurrenterPosition]].earnMoney(temp);   // player that owns the utility
                    System.out.println("Player " + i + " pays $" + temp + " to player " + Board.ownedBy[evenCurrenterPosition] + " to stay at " + Board.tileName[evenCurrenterPosition]);
                }
                else
                {
                    purchase(i, evenCurrenterPosition);  // player can purchase if unowned
                }

                break;
            case 3:         // cards 3 and 4 are the same card
            case 4:         // Advance token to the nearest Railroad and pay owner twice the rental to which he/she is otherwise entitled. If Railroad is unowned, you may buy it from the Bank.
                // railroads at 5, 15, 25, 35
                if (currentPosition > 35 || currentPosition <= 5)   // closest railroad is on tile 5
                {
                    if (currentPosition > 35)
                        players[i].earnMoney(200);  // + $200 for passing GO
                    players[i].moveTo(5);
                }
                else if (currentPosition > 5 && currentPosition <= 15) // closest railroad is on tile 15
                {
                    players[i].moveTo(15);
                }
                else if (currentPosition > 15 && currentPosition <= 25) // closest railroad is on tile 25
                {
                    players[i].moveTo(25);
                }
                else // closest railroad is on tile 35
                {
                    players[i].moveTo(35);
                }
                evenCurrenterPosition = players[i].getPosition();
                if (Board.isOwned[evenCurrenterPosition])
                {
                    int temp = Board.currentRentPrice[evenCurrenterPosition]*2;    // 2 * the current rent
                    players[i].payMoney(temp);  // player that landed on the railroad
                    players[Board.ownedBy[evenCurrenterPosition]].earnMoney(temp);   // player that owns the railroad
                }
                else
                {
                    purchase(i, evenCurrenterPosition);  // player can purchase if unowned
                }
                break;
            case 5:     // Advance to St. Charles Place – if you pass Go, collect $200
                if (currentPosition > 11) // 11 = Tile Position for St. Charles Place
                    players[i].earnMoney(200);  // $200 for passing GO
                players[i].moveTo(11); // moves player to Illinois Avenue
                if (Board.isOwned[11])
                    rent(i, 11, -1);    // diceRoll = -1 since it does not matter
                else
                    purchase(i, 11);
                break;
            case 6:     // Bank pays you dividend of $50
                players[i].earnMoney(50);   // $50
                break;
            case 7:     // You have won a crossword competition - collect $100
                players[i].earnMoney(100);  // $100
                break;
            case 8:     // Go back 3 spaces
                if(currentPosition == 7)    // going back 3 spaces will land on Income Tax
                {
                    players[i].moveTo(4);
                    incomeTax(i);
                }
                else if (currentPosition == 22)     // going back 3 spaces will land on New York Avenue: tile 19
                {
                    players[i].moveTo(19);
                    if (Board.isOwned[19])
                    {
                        rent(i, 19, -1);    // pays rent // diceRoll = -1 since it does not matter
                    }
                    else
                    {
                        purchase(i, 19);    // buys if unknown
                    }
                }
                else if (currentPosition == 36)     // going back 3 spaces will land on a Community Chest
                {
                    community(i, currentPosition);  // community chest
                }
                else    // not possible if player was on a chance tile
                {
                    throw new IllegalStateException("Player was somehow not on Chance Tile for moving back 3 spaces; current Position of player: " + currentPosition);
                }
                break;
            case 9:     // Go directly to Jail – do not pass Go, do not collect $200
                players[i].goToJail();
                break;
            case 10:    // Make general repairs on all your property – for each house pay $25 – for each hotel $100
                // Not currently supported
                System.out.println("Cannot yet pay house and hotel fees.");
                break;
            case 11:    // Pay poor tax of $15
                players[i].payMoney(15);
                break;
            case 12:    // Take a trip to Reading Railroad – if you pass Go collect $200
                // reading railroad at location 5
                players[i].moveTo(5);
                if (currentPosition > 5)
                {
                    players[i].earnMoney(200);      // + $200 for passing GO
                }
                if (Board.isOwned[5])
                {
                    rent(i, 5, -1);    // diceRoll = -1 since it does not matter
                }
                else
                {
                    purchase(i, 5);
                }
                break;
            case 13:    // Take a walk on the Boardwalk – advance token to Boardwalk
                players[i].moveTo(39);
                if (Board.isOwned[39])
                {
                    rent(i, 39, -1);    // // diceRoll = -1 since it does not matter
                }
                else
                {
                    purchase(i, 39);
                }
                break;
            case 14:    // You have been elected chairman of the board – pay each player $50
                for (int j = 1; j < numOfPlayers; j++)  // pays each player $50
                {
                    players[i].payMoney(50);
                    players[(i+j)%4].earnMoney(50);
                }
                break;
            case 15:    // Your building loan matures – collect $150
                players[i].earnMoney(150);
                break;
            case 16:    // Get out of Jail free – this card may be kept until needed, or traded/sold
                players[i].getOutOfJailFree();
                break;
            default:    // shouldn't happen
                throw new IllegalStateException("Somehow drew a chance card with an index greater than 16");
        }
        System.out.println("Player " + i + " has drawn a Chance Card: " + Cards.chanceCards[index]);
    }

    private void community(int i, int currentPosition) // draws a community chest card
    {
        int index = community.Draw(); // draws new community chest card
        switch (index)
        {
            case 0:     // Advance to Go (Collect $200)
                players[i].goToStart();
                break;
            case 1:     // Bank error in your favor – collect $75
                players[i].earnMoney(75);
                break;
            case 2:     // Doctor's fees – Pay $50
                players[i].payMoney(50);
                break;
            case 3:     // Holiday Fund matures - Receive $100
                players[i].earnMoney(100);
                break;
            case 4:     // Go to jail – go directly to jail – Do not pass Go, do not collect $200
                players[i].goToJail();
                break;
            case 5:     // It is your birthday Collect $10 from each player
                for (int j = 1; j < numOfPlayers; j++)  // takes 10 from each player
                {
                    players[i].earnMoney(10);
                    players[(i+j)%4].payMoney(10);
                }
                break;
            case 6:     // Grand Opera Night – collect $50 from every player for opening night seats
                for (int j = 1; j < numOfPlayers; j++)  // takes 50 from each player
                {
                    players[i].earnMoney(50);
                    players[(i+j)%4].payMoney(50);
                }
                break;
            case 7:     // Income Tax refund – collect $20
                players[i].earnMoney(20);
                break;
            case 8:     // Life Insurance Matures – collect $100
                players[i].earnMoney(100);
                break;
            case 9:     // Pay Hospital Fees of $100
                players[i].payMoney(100);
                break;
            case 10:    // Pay School Fees of $50
                players[i].payMoney(50);
                break;
            case 11:    // Receive $25 Consultancy Fee
                players[i].earnMoney(25);
                break;
            case 12:    // You are assessed for street repairs – $40 per house, $115 per hotel
                // Not currently supported
                System.out.println("Cannot yet pay house and hotel fees.");
                break;
            case 13:    // You have won second prize in a beauty contest– collect $10
                players[i].earnMoney(10);
                break;
            case 14:    // You inherit $100
                players[i].earnMoney(100);
                break;
            case 15:    // From sale of stock you get $50
                players[i].earnMoney(50);
                break;
            case 16:    // Get out of jail free – this card may be kept until needed, or sold
                players[i].getOutOfJailFree();
                break;
            default:    // shouldn't happen
                throw new IllegalStateException("Somehow drew a community chest card with an index greater than 16");
        }
        System.out.println("Player " + i + " has drawn a Community Chance Card: " + Cards.communityChest[index]);
    }

    private void purchase(int i, int currentPosition)   // purchases a unpurchased tile
    {
        players[i].purchaseProperty(currentPosition);
        Board.ownedBy[currentPosition] = i;
        System.out.println("Player " + i + " purchases property " + Board.tileName[currentPosition]);

        colorAdd(i, currentPosition);
    }

    public void rent(int i, int currentPosition, int diceRoll)
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

    public void incomeTax (int i)    // pays income tax for player i
    {
        int tenPercent = players[i].getMoney() % 10;
        if (tenPercent < 200)   // choose lowest option: 10% or $200
            players[i].payMoney(tenPercent);
        else
            players[i].payMoney(200);
    }

    public void auction (int i, int currentPosition, int currentAuctionPrice)     // currentAuctionPrice represents the price that the auction will start at
    {
        // int currentAuctionPrice = 0.1 * purchasePrice;      // auction starts at 10% of base purchase price

        boolean [] willPass = new boolean[numOfPlayers];    // an array that keeps track of whether each player will pass or not
        int passes = 0;     // counts the number of passes

        for (i = (i+1)%numOfPlayers; true; i = (i+1)%numOfPlayers)
        {
            if (passes == numOfPlayers - 1) // if all players but one have passed
            {
                break;
            }
            else if (passes > numOfPlayers - 1) // should never happen
            {
                throw new IllegalStateException("Auctioning Gone Wrong.");
            }
            else if (willPass[i])    // continues if the player has decided to pass
            {
                continue;
            }
            else if (players[i].getMoney() < 1.5 * currentAuctionPrice)  // if the players money is 150% the current auction price, then pass on auctioning
            {
                System.out.println("Player " + i + " passes on the property.");
                willPass[i] = true;
                passes += 1;
                continue;
            }
            else
            {
                currentAuctionPrice += (int)(0.05 * Board.purchaseCost[currentPosition]);     // auction price increases by 5%
                System.out.println("Player " + i + " bids $" + currentAuctionPrice + " for the property.");
            }
        }

        while (true)    // this loop finds the player that auctioned the highest price
        {
            if (!willPass[i])
                break;
            i = (i+1)%numOfPlayers;
        }

        System.out.println("Player " + i + " has won the auction for $" + currentAuctionPrice + ". The player now owns " + Board.tileName[currentPosition]);
        players[i].payMoney(currentAuctionPrice);
        players[i].purchaseProperty(currentPosition);
        Board.ownedBy[currentPosition] = i;
        players[i].earnMoney(Board.purchaseCost[currentPosition]);      // gives back money taken away in purchase property function
        colorAdd(i, currentPosition);
    }

    public void colorAdd(int i, int currentPosition)        // adds ownership to property groups
    {
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
                    players[i].addColorGroup(Improvement.P, Improvement.P_name, Improvement.PHouse);        // adds a color group to a players list

                    System.out.println("Player " + i + " has obtained the Purple Color Group.");
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

                    players[i].addColorGroup(Improvement.LB, Improvement.LB_name, Improvement.LBHouse);        // adds a color group to a players list

                    System.out.println("Player " + i + " has obtained the Light Blue Color Group.");
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
                    players[i].addColorGroup(Improvement.M, Improvement.M_name, Improvement.MHouse);        // adds a color group to a players list

                    System.out.println("Player " + i + " has obtained the Maroon Color Group.");
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
                    players[i].addColorGroup(Improvement.O, Improvement.O_name, Improvement.OHouse);        // adds a color group to a players list

                    System.out.println("Player " + i + " has obtained the Orange Color Group.");
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
                    players[i].addColorGroup(Improvement.R, Improvement.R_name, Improvement.RHouse);        // adds a color group to a players list

                    System.out.println("Player " + i + " has obtained the Red Color Group.");
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
                    players[i].addColorGroup(Improvement.Y, Improvement.Y_name, Improvement.YHouse);        // adds a color group to a players list

                    System.out.println("Player " + i + " has obtained the Yellow Color Group.");
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
                    players[i].addColorGroup(Improvement.G, Improvement.G_name, Improvement.GHouse);        // adds a color group to a players list

                    System.out.println("Player " + i + " has obtained the Green Color Group.");
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
                    players[i].addColorGroup(Improvement.DB, Improvement.DB_name, Improvement.DBHouse);        // adds a color group to a players list

                    System.out.println("Player " + i + " has obtained the Dark Blue Color Group.");
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
