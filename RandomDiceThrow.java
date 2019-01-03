// Creation Date: 01/02/2019
// Creator: Michael Markiewicz

import java.util.Random;

public class RandomDiceThrow
{
    private Random rand;
    private boolean doubles;

    public RandomDiceThrow()
    {
        rand = new Random(); // psudorandom number generator
        doubles = false;    // true if previous thrown dice were doubles: both die were the same magnitude
    }

    public int returnSum()  // returns sum of two 6-sided dice
    {
        int dice1 = rand.nextInt(6) + 1; // +1 is for making dice throw from 1-6 inclusive
        int dice2 = rand.nextInt(6) + 1;
        if (dice1 == dice2)
        {
            doubles = true;
        }
        else
        {
            doubles = false;
        }
        return dice1 + dice2;
    }
}


/*
// Testing Class for dice class

public class Main
{
        public static void main(String[] args)
        {
            int [] frequency = new int[13];
            RandomDiceThrow dice = new RandomDiceThrow();
            for(int i = 0; i < 1000000; i+=1)
                frequency[dice.returnSum()]+=1;

            System.out.println("Frequency of 2: " + frequency[2]/1000000.0);
            System.out.println("Frequency of 3: " + frequency[3]/1000000.0);
            System.out.println("Frequency of 4: " + frequency[4]/1000000.0);
            System.out.println("Frequency of 5: " + frequency[5]/1000000.0);
            System.out.println("Frequency of 6: " + frequency[6]/1000000.0);
            System.out.println("Frequency of 7: " + frequency[7]/1000000.0);
            System.out.println("Frequency of 8: " + frequency[8]/1000000.0);
            System.out.println("Frequency of 9: " + frequency[9]/1000000.0);
            System.out.println("Frequency of 10: " + frequency[10]/1000000.0);
            System.out.println("Frequency of 11: " + frequency[11]/1000000.0);
            System.out.println("Frequency of 12: " + frequency[12]/1000000.0);

        }
}
*/
