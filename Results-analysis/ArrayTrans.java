/**
 * @author: Liang Qiao
 * @School: Hohai University
 * @操作说明：动态规划求解光照-马马崖水电站优化调度问题之数组转换函数
 * input:(动态数组)
 * output :静态数组
 *
 */
package Sensitive;

import java.util.ArrayList;

public class ArrayTrans {
    public static double[] function2(ArrayList<Double> args) {
        int length = args.size();
        double[] set = new double[length];
        for(int i=0; i<length; i++){
            set[i]=args.get(i);
        }
        return set;
    }
}

