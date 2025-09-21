/* Вариант 1
В последовательности неотрицательных целых чисел не превышающих 10000, размером 1000 элементов найти число R
* R максимально;
* R кратно 14;
* R произведение 2х различных элементов последовательности;
* если такого числа нет вывести -1 */

import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[] nums = new int[1000];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = ((int) (Math.random() * 10001));
        }
        int result = option(nums);
        System.out.println(result);
    }

    static int option(int[] nums){
        int max14 = 0, max2 = 0, max7 = 0, maxint = 0;
        for(int i = 0; i < nums.length; i++){
            if(nums[i] % 21 == 0 & nums[i] > max14)
                max14 = nums[i];
            else if(nums[i] > maxint)
                maxint = nums[i];
            if(nums[i] % 2 == 0 & nums[i] > max2)
                max2 = nums[i];
            else if(nums[i] % 7 == 0 & nums[i] > max7)
                max7 = nums[i];
        }
        maxint *= max14;
        max7 *= max2;
        return Math.max(maxint, max7);
    }
}
