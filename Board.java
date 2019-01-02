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
    public final static Integer [] tileCost =
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

    /*
    TODO:
        Morgage Rates for Properties
        Grouping Properties together for Hotels and such
    */
}
