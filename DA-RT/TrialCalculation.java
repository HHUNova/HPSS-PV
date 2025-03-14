/**
 * @Author: Liang Qiao
 * @School: Hohai University
 * @操作说明：动态规划求解光照-马马崖水电站优化调度问题之试算（以电定水）
 *input：出力
 * output: 下泄流量、上游水位、水头、下游水位
 */
package org.example;

import static org.example.Main.*;

public class TrialCalculation {
    public static double[] pumpingUp(int ID, int i, double NPumping, double zAverage1, double vDown,  double vBegin, double Q){
        double[] result = new double[4];
        double zDown = ChaZhi.function(vDown, v[ID+1], z[ID+1]);
        double netHead = zAverage1 - zDown;
        result[0] = NPumping*1000*0.8/(9.81*netHead);
        double VPumping = result[0]*t[i]/100000000;
        vDown = vDown - VPumping;
        result[3] = ChaZhi.function(vDown, v[ID+1], z[ID+1]);
        double vAverage2 = (2*vBegin+VPumping + (inputQ[ID][i]-Q)*t[i]/100000000)/2;
        double zAverage2 = ChaZhi.function(vAverage2, v[ID], z[ID]);
        result[1] = (zAverage1+zAverage2)/2;
        result[2] = result[1] - result[3];
        double difference = zAverage1 - zAverage2;
        if (!(difference < 1.0E-4) || !(difference > -1.0E-4)) {
            result = TrialCalculation.pumpingUp(ID, i, NPumping, result[1], vDown, vBegin, Q);
        }
        return result;
    }
}

