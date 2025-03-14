/**
 * @Author: Liang Qiao
 * @School: Hohai University
 * @操作说明：动态规划求解光照-马马崖水电站优化调度问题之初始化函数
 *input：上一时段解
 * output: 归零
 */
package org.example;

import static org.example.Main.*;

public class SetOrigin {

    public static void OriginPumping() {
        for (int i=0; i<stageNumber; i++){
            Pumping[i] = 0;
            NPumping[i] = 0;
            NPumpingInstant[i] = 0;
        }
    }

    public static void Origin2() {
        for (int ID = 0; ID < X; ID++) {
            for (int i = 0; i < stageNumber; i++) {
                Pumping[i] = 0;
                NPumping[i] = 0;
                vBegin[ID][i] = 0;
                vEnd[ID][i] = 0;
                zEnd[ID][i] = 0;
                Qe[ID][i] = 0;
                N[ID][i] = 0;
                W[ID][i] = 0;
                Q[ID][i] = 0;
                ZTail[ID][i] = 0;
                NetHead[ID][i] = 0;
                Money[ID][i] = 0;
                input[ID][i] = 0;
                inputQ[ID][i] = 0;
                NPVPredict[ID][i] = 0;
                NPVMeasure[ID][i] = 0;
            }
        }
    }
}
