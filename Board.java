// Creation Date: 01/02/2019
// Creator: Michael Markiewicz

public class Board
{
    // description of each of the 40 tiles on the Board
    public final static String [] tileName =
    {
        "GO",                   // 0
        "Mediterranean Avenue", // 1
        "Community Chest",      // 2
        "Baltic Avenue",        // 3
        "Income Tax",           // 4
        "Reading Railroad",     // 5
        "Oriental Avenue",      // 6
        "Chance",               // 7
        "Vermont Avenue",       // 8
        "Connecticut Avenue",   // 9
        "Visiting Jail",        // 10   Real jail will have be an exception in player or game class, JAIL will be techically located on tile 30
        "St. Charles Place",    // 11
        "Electric Company",     // 12
        "States Avenue",        // 13
        "Virginia Avenue",      // 14
        "Pennsylvania Railroad",// 15
        "St. James Place",      // 16
        "Community Chest",      // 17
        "Tennessee Avenue",     // 18
        "New York Avenue",      // 19
        "Free Parking",         // 20
        "Kentucky Avenue",      // 21
        "Chance",               // 22
        "Indiana Avenue",       // 23
        "Illinois Avenue",      // 24
        "B. & O. Railroad",     // 25
        "Atlantic Avenue",      // 26
        "Ventnor Avenue",       // 27
        "Water Works",          // 28
        "Marvin Gardens",       // 29
        "Go To Jail",           // 30   Sucks to be BAD -- Also JAIL (techically)
        "Pacific Avenue",       // 31
        "North Carolina Avenue",// 32
        "Community Chest",      // 33
        "Pennsylavnia Avenue",  // 34
        "Short Line",           // 35
        "Chance",               // 36
        "Park Place",           // 37
        "Luxury Tax",           // 38
        "Boardwalk"             // 39
    };

    // cost with the associated tile, otherwise null
    public final static Integer [] purchaseCost =
    {
        null,   // "GO",                   // 0
        60,     // "Mediterranean Avenue", // 1
        null,   // "Community Chest",      // 2
        60,     // "Baltic Avenue",        // 3
        null,   // "Income Tax",           // 4
        200,    // "Reading Railroad",     // 5
        100,    // "Oriental Avenue",      // 6
        null,   // "Chance",               // 7
        100,    // "Vermont Avenue",       // 8
        120,    // "Connecticut Avenue",   // 9
        null,   // "Visiting Jail",        // 10   Real jail will have be an exception in player or game class, JAIL will be techically located on tile 30
        140,    // "St. Charles Place",    // 11
        150,    // "Electric Company",     // 12
        140,    // "States Avenue",        // 13
        160,    // "Virginia Avenue",      // 14
        200,    // "Pennsylvania Railroad",// 15
        180,    // "St. James Place",      // 16
        null,   // "Community Chest",      // 17
        180,    // "Tennessee Avenue",     // 18
        200,    // "New York Avenue",      // 19
        null,   // "Free Parking",         // 20
        220,    // "Kentucky Avenue",      // 21
        null,   // "Chance",               // 22
        220,    // "Indiana Avenue",       // 23
        240,    // "Illinois Avenue",      // 24
        200,    // "B. & O. Railroad",     // 25
        260,    // "Atlantic Avenue",      // 26
        260,    // "Ventnor Avenue",       // 27
        150,    // "Water Works",          // 28
        280,    // "Marvin Gardens",       // 29
        null,   // "Go To Jail",           // 30   Sucks to be BAD -- Also JAIL (techically)
        300,    // "Pacific Avenue",       // 31
        300,    // "North Carolina Avenue",// 32
        null,   // "Community Chest",      // 33
        320,    // "Pennsylavnia Avenue",  // 34
        200,    // "Short Line",           // 35
        null,   // "Chance",               // 36
        350,    // "Park Place",           // 37
        75,     // "Luxury Tax",           // 38
        400    // "Boardwalk"              // 39
    };

    // Base rent for all properties
    private final static Integer [] baseRent =
    {
        null,   // "GO",                   // 0
        2,      // "Mediterranean Avenue", // 1
        null,   // "Community Chest",      // 2
        4,      // "Baltic Avenue",        // 3
        null,   // "Income Tax",           // 4    Special Case (IncomeTax): Pay 10% or $200 (whichever is lower)
        null,   // "Reading Railroad",     // 5    Special Case (Railroad): 25 if 1 owned, 50 if 2 owned, 100 if 3 owned, 200 if all 4 owned
        6,      // "Oriental Avenue",      // 6
        null,   // "Chance",               // 7
        6,      // "Vermont Avenue",       // 8
        8,      // "Connecticut Avenue",   // 9
        null,   // "Visiting Jail",        // 10   Real jail will have be an exception in player or game class, JAIL will be techically located on tile 30
        10,     // "St. Charles Place",    // 11
        null,   // "Electric Company",     // 12   Special Case (Utilities): 4xdice if 1 owned, 10xdice if both owned
        10,     // "States Avenue",        // 13
        12,     // "Virginia Avenue",      // 14
        null,   // "Pennsylvania Railroad",// 15   Special Case (Railroad): 25 if 1 owned, 50 if 2 owned, 100 if 3 owned, 200 if all 4 owned
        14,     // "St. James Place",      // 16
        null,   // "Community Chest",      // 17
        14,     // "Tennessee Avenue",     // 18
        16,     // "New York Avenue",      // 19
        null,   // "Free Parking",         // 20
        18,     // "Kentucky Avenue",      // 21
        null,   // "Chance",               // 22
        18,     // "Indiana Avenue",       // 23
        20,     // "Illinois Avenue",      // 24
        null,   // "B. & O. Railroad",     // 25   Special Case (Railroad): 25 if 1 owned, 50 if 2 owned, 100 if 3 owned, 200 if all 4 owned
        22,     // "Atlantic Avenue",      // 26
        22,     // "Ventnor Avenue",       // 27
        null,   // "Water Works",          // 28   Special Case (Utilities): 4xdice if 1 owned, 10xdice if both owned
        24,     // "Marvin Gardens",       // 29
        null,   // "Go To Jail",           // 30   Sucks to be BAD -- Also JAIL (techically)
        26,     // "Pacific Avenue",       // 31
        26,     // "North Carolina Avenue",// 32
        null,   // "Community Chest",      // 33
        28,     // "Pennsylavnia Avenue",  // 34
        null,   // "Short Line",           // 35   Special Case (Railroad): 25 if 1 owned, 50 if 2 owned, 100 if 3 owned, 200 if all 4 owned
        null,   // "Chance",               // 36
        35,     // "Park Place",           // 37
        null,   // "Luxury Tax",           // 38
        50      // "Boardwalk"             // 39
    };

    // element is true if tile is purchasable (ie actually a property that can be owned)
    public final static boolean [] isProperty =
    {
        false,   // "GO",                   // 0
        true,    // "Mediterranean Avenue", // 1
        false,   // "Community Chest",      // 2
        true,    // "Baltic Avenue",        // 3
        false,   // "Income Tax",           // 4
        true,    // "Reading Railroad",     // 5
        true,    // "Oriental Avenue",      // 6
        false,   // "Chance",               // 7
        true,    // "Vermont Avenue",       // 8
        true,    // "Connecticut Avenue",   // 9
        false,   // "Visiting Jail",        // 10   Real jail will have be an exception in player or game class, JAIL will be techically located on tile 30
        true,    // "St. Charles Place",    // 11
        true,    // "Electric Company",     // 12
        true,    // "States Avenue",        // 13
        true,    // "Virginia Avenue",      // 14
        true,    // "Pennsylvania Railroad",// 15
        true,    // "St. James Place",      // 16
        false,   // "Community Chest",      // 17
        true,    // "Tennessee Avenue",     // 18
        true,    // "New York Avenue",      // 19
        false,   // "Free Parking",         // 20
        true,    // "Kentucky Avenue",      // 21
        false,   // "Chance",               // 22
        true,    // "Indiana Avenue",       // 23
        true,    // "Illinois Avenue",      // 24
        true,    // "B. & O. Railroad",     // 25
        true,    // "Atlantic Avenue",      // 26
        true,    // "Ventnor Avenue",       // 27
        true,    // "Water Works",          // 28
        true,    // "Marvin Gardens",       // 29
        false,   // "Go To Jail",           // 30   Sucks to be BAD -- Also JAIL (techically)
        true,    // "Pacific Avenue",       // 31
        true,    // "North Carolina Avenue",// 32
        false,   // "Community Chest",      // 33
        true,    // "Pennsylavnia Avenue",  // 34
        true,    // "Short Line",           // 35
        false,   // "Chance",               // 36
        true,    // "Park Place",           // 37
        false,   // "Luxury Tax",           // 38
        true    // "Boardwalk"              // 39
    };

    // current rent for all properties
    public static Integer [] currentRentPrice =
    {
        null,   // "GO",                   // 0
        2,      // "Mediterranean Avenue", // 1
        null,   // "Community Chest",      // 2
        4,      // "Baltic Avenue",        // 3
        null,   // "Income Tax",           // 4
        25,     // "Reading Railroad",     // 5    Special Case (Railroad): 25 if 1 owned, 50 if 2 owned, 100 if 3 owned, 200 if all 4 owned
        6,      // "Oriental Avenue",      // 6
        null,   // "Chance",               // 7
        6,      // "Vermont Avenue",       // 8
        8,      // "Connecticut Avenue",   // 9
        null,   // "Visiting Jail",        // 10   Real jail will have be an exception in player or game class, JAIL will be techically located on tile 30
        10,     // "St. Charles Place",    // 11
        null,   // "Electric Company",     // 12   Special Case (Utilities): 4xdice if 1 owned, 10xdice if both owned
        10,     // "States Avenue",        // 13
        12,     // "Virginia Avenue",      // 14
        25,     // "Pennsylvania Railroad",// 15   Special Case (Railroad): 25 if 1 owned, 50 if 2 owned, 100 if 3 owned, 200 if all 4 owned
        14,     // "St. James Place",      // 16
        null,   // "Community Chest",      // 17
        14,     // "Tennessee Avenue",     // 18
        16,     // "New York Avenue",      // 19
        null,   // "Free Parking",         // 20
        18,     // "Kentucky Avenue",      // 21
        null,   // "Chance",               // 22
        18,     // "Indiana Avenue",       // 23
        20,     // "Illinois Avenue",      // 24
        25,     // "B. & O. Railroad",     // 25   Special Case (Railroad): 25 if 1 owned, 50 if 2 owned, 100 if 3 owned, 200 if all 4 owned
        22,     // "Atlantic Avenue",      // 26
        22,     // "Ventnor Avenue",       // 27
        null,   // "Water Works",          // 28   Special Case (Utilities): 4xdice if 1 owned, 10xdice if both owned
        24,     // "Marvin Gardens",       // 29
        null,   // "Go To Jail",           // 30   Sucks to be BAD -- Also JAIL (techically)
        26,     // "Pacific Avenue",       // 31
        26,     // "North Carolina Avenue",// 32
        null,   // "Community Chest",      // 33
        28,     // "Pennsylavnia Avenue",  // 34
        25,     // "Short Line",           // 35   Special Case (Railroad): 25 if 1 owned, 50 if 2 owned, 100 if 3 owned, 200 if all 4 owned
        null,   // "Chance",               // 36
        35,     // "Park Place",           // 37
        null,   // "Luxury Tax",           // 38
        50      // "Boardwalk"             // 39
    };

    // array that holds the true/false value of if the property is currently owned or not. Intially, all properties are not owned (false)
    public static boolean [] isOwned = new boolean[40];

    // array that holds which player owns the property (if owned)
    public static Integer [] ownedBy = new Integer[40]; // each element initialized to null

    // array that holds the frequency that each tile was visited
    public static int [] frequency = new int[40];

    /*
    TODO:
        Morgage Rates for Properties
    */
}
