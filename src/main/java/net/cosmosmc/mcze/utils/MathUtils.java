package net.cosmosmc.mcze.utils;

/**
 * Created by JJOL on 16/08/2015.
 */
public class MathUtils {

    public static double max(double... nums) {
        double largest = nums[0];
        for(double n : nums) {
            if (n > largest) {
                largest = n;
            }
        }

        return largest;
    }

}
