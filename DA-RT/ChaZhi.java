/**
 * @author: Liang Qiao
 * @School: Hohai University
 * @操作说明：动态规划求解光照-马马崖水电站优化调度问题之定义插值函数
 * input:(对应水位，水位集，库容集，库容离散系数)
 * output :对应库容
 */
package org.example;

public class ChaZhi{
    public static double function(double a, double[] b, double[] c) {
        double f = 0;
        int m1 = ChaZhi.binarySearch(b, a);
        if(a>b[0] && a<b[b.length-2]) {
            f = c[m1] + ((a - b[m1]) * (c[m1 + 1] - c[m1]) / (b[m1 + 1] - b[m1]));
        } else if (a <= b[0]) {
            f = c[0];
        } else if (a >= b[b.length-2]){
            f = c[c.length-1];
        }
        return f;
    }

    public static int binarySearch(double[] nums, double target) {
        int left = 0;
        int right = nums.length - 1;

        while(left < right-1) {
            int mid = (right + left) / 2;
            if(nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid - 1;
            }
        }
        return left;
    }
}