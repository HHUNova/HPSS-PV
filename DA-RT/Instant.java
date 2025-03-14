/**
 * @Author: Liang Qiao
 * @School: Hohai University
 * @操作说明：动态规划求解光照-马马崖水电站优化调度问题之实时调度计算
 *input：计划出力、水库序号、时段序号、离散常数、库容边界条件
 * output: 时段最优解
 */
package org.example;

import static org.example.Main.*;

public class Instant {
    public static void functionV0(int ID, int i, int levelNumber, double v0) {
        int m;
        if (ID == 0) {
            m = 1;
        } else {
            m = -1;
        }
        double Error = Integer.MAX_VALUE;
        for (int j = 0; j <= levelNumber; j++) {
            double v1 = vDisInstant[j];
            double vAverage = (v1 + v0) / 2;
            double QDecision1 = (v0 - v1) * 100000000 / t[i] + inputQ[ID][i] + m*Pumping[i] - W[ID][i];
            zInOneStageInstant[i][j] = ChaZhi.function(vAverage, v[ID], z[ID]);
            zTailDecisionInOneStageInstant[i][j] = ChaZhi.function(QDecision1, q[ID], zTail[ID]);
            netHeadInOneStageInstant[i][j] = zInOneStageInstant[i][j] - zTailDecisionInOneStageInstant[i][j];
            outputNInOneStageInstant[i][j] = NPlan[ID][i];
            double QDecision2 = outputNInOneStageInstant[i][j] * 1000 / (NCoefficient[ID] * netHeadInOneStageInstant[i][j]);
            double error = Math.abs(QDecision2 - QDecision1);
            if (error < Error) {
                Error = error;
                vBegin[ID][i] = v0;
                vEnd[ID][i] = v1;
                zEnd[ID][i] = ChaZhi.function(v1, v[ID], z[ID]);
                Q[ID][i] = QDecision1+W[ID][i];
                Qe[ID][i] = (QDecision1 + QDecision2) / 2;
                ZTail[ID][i] = zTailDecisionInOneStageInstant[i][j];
                NetHead[ID][i] = netHeadInOneStageInstant[i][j];
                N[ID][i] = NPlan[ID][i];
                if (ID == 1) {
                    Pumping[i] = NPumping[i] * 0.8 * 1000 / (9.81 *((zEnd[ID-1][0]+z0[ID-1])/2-zInOneStageInstant[i][j]));
                }
                if(ID == 0){
                    Pumping[i] = NPumping[i]*0.8*1000/(9.81 *(zInOneStageInstant[i][j]-(zEnd[ID+1][0]+z0[ID+1])/2));
                }
                vEnd[ID][i] = vBegin[ID][i] + (inputQ[ID][i] - Q[ID][i] +m*Pumping[i])*t[i]/100000000;
                zEnd[ID][i] = ChaZhi.function(vEnd[ID][i], v[ID], z[ID]);
                /*
                if (N[ID][i] + NPVMeasure[ID][i] > NLimit[ID]){
                    NPVMeasure[ID][i] = Math.max (NLimit[ID] - N[ID][i], 0);
                }
                 */
                if(ID == 0) {
                    Money[ID][i] = N[ID][i] * 1000 * pricert[i] + NPVMeasure[ID][i] * 1000 * pricert[i] - NPumpingInstant[i] * 1000 * pricert[i];
                }else{
                    Money[ID][i] = N[ID][i] * 1000 * pricert[i] + NPVMeasure[ID][i] * 1000 * pricert[i] ;
                }
            }
        }
    }

    public static void functionV(int ID, int i, int levelNumber) {
        int m;
        if (ID == 0) {
            m = 1;
        } else {
            m = -1;
        }
        double Error = Integer.MAX_VALUE;
        for (int j = 0; j <= levelNumber; j++) {
            double v0 = vEnd[ID][i - 1];
            double v1 = vDisInstant[j];
            double vAverage = (v1 + v0) / 2;
            double QDecision1 = (v0 - v1) * 100000000 / t[i] + inputQ[ID][i] + m*Pumping[i]-W[ID][i];
            zInOneStageInstant[i][j] = ChaZhi.function(vAverage, v[ID], z[ID]);
            zTailDecisionInOneStageInstant[i][j] = ChaZhi.function(QDecision1, q[ID], zTail[ID]);
            netHeadInOneStageInstant[i][j] = zInOneStageInstant[i][j] - zTailDecisionInOneStageInstant[i][j];
            outputNInOneStageInstant[i][j] = NPlan[ID][i];
            double QDecision2 = outputNInOneStageInstant[i][j] * 1000 / (NCoefficient[ID] * netHeadInOneStageInstant[i][j]);
            double error = Math.abs(QDecision2 - QDecision1);
            if (error < Error) {
                Error = error;
                vBegin[ID][i] = v0;
                vEnd[ID][i] = v1;
                zEnd[ID][i] = ChaZhi.function(v1, v[ID], z[ID]);
                Q[ID][i] = QDecision1+W[ID][i];
                Qe[ID][i] = (QDecision1 + QDecision2) / 2;
                ZTail[ID][i] = zTailDecisionInOneStageInstant[i][j];
                NetHead[ID][i] = netHeadInOneStageInstant[i][j];
                N[ID][i] = NPlan[ID][i];
                if (ID == 1) {
                    Pumping[i] = NPumping[i] * 0.8 * 1000 / (9.81 *((zEnd[ID-1][i]+zEnd[ID-1][i-1])/2-zInOneStageInstant[i][j]));
                }
                if(ID == 0){
                    Pumping[i] = NPumping[i]*0.8*1000/(9.81 *(zInOneStageInstant[i][j]-(zEnd[ID+1][i]+zEnd[ID+1][i-1])/2));
                }
                vEnd[ID][i] = vBegin[ID][i] + (inputQ[ID][i] - Q[ID][i] +m*Pumping[i])*t[i]/100000000;
                zEnd[ID][i] = ChaZhi.function(vEnd[ID][i], v[ID], z[ID]);
                /*
                if (N[ID][i] + NPVMeasure[ID][i] > NLimit[ID]){
                    NPVMeasure[ID][i] = Math.max (NLimit[ID] - N[ID][i], 0);
                }
                 */
                if(ID == 0) {
                    Money[ID][i] = N[ID][i] * 1000 * pricert[i] + NPVMeasure[ID][i] * 1000 * pricert[i] - NPumpingInstant[i] * 1000 * pricert[i];
                }else{
                    Money[ID][i] = N[ID][i] * 1000 * pricert[i] + NPVMeasure[ID][i] * 1000 * pricert[i] ;
                }
            }
        }
    }
}
