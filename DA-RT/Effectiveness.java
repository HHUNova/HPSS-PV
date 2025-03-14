/**
 * @Author: Liang Qiao
 * @School: Hohai University
 * @操作说明：动态规划求解光照-马马崖水电站优化调度问题之时段效益计算
 *input：水库序号、时段序号、库容离散系数、水位边界条件
 * output: 时段最优解
 */
package org.example;

import static org.example.Main.*;

public class Effectiveness {
    public static void functionV0(int ID, int i, int levelNumber, double v0, Reservoir r) {
        double v0OtherBegin = 0;
        int penalty = 0;
        for (int j = 0; j <= levelNumber; j++) {
            int m = 0;
            r.vInOneStage[i][j] = (r.vDis[j] + v0) / 2;
            r.zInOneStage[i][j] = ChaZhi.function(r.vInOneStage[i][j], v[ID], z[ID]);
            if (ID == 0) {
                r.outputQInOneStage[i][j] = inputQ[ID][i] + (v0 - r.vDis[j]) * 100000000 / t[i];/**/
                v0OtherBegin = ChaZhi.function(z0[ID + 1], z[ID + 1], v[ID + 1]);
            } else {
                r.outputQInOneStage[i][j] = inputQ[ID][i]  + (v0 - r.vDis[j]) * 100000000 / t[i];
            }

            if (r.outputQInOneStage[i][j] > outputQMax[ID]) {
                r.labelInOneStage[i][j] = 0;
            }
            else if (r.outputQInOneStage[i][j] > outputQeMax[ID] && r.outputQInOneStage[i][j] <= outputQMax[ID]) out:{
                r.labelInOneStage[i][j] = 1;
                r.zTailDecisionInOneStage[i][j] = ChaZhi.function(r.outputQInOneStage[i][j], q[ID], zTail[ID]);
                r.netHeadInOneStage[i][j] = r.zInOneStage[i][j] - r.zTailDecisionInOneStage[i][j];
                double Q = r.outputQInOneStage[i][j];
                r.outputQInOneStage[i][j] = outputQeMax[ID];
                r.outputNInOneStage[i][j] = NCoefficient[ID] * r.netHeadInOneStage[i][j] * r.outputQInOneStage[i][j] / 1000;
                if (r.outputNInOneStage[i][j] >= NLimit[ID] - NPVPredict[ID][i]) {
                    r.outputNInOneStage[i][j] = NLimit[ID] - NPVPredict[ID][i];
                    if (ID == 0 && !(r.outputNInOneStage[i][j] == NLimit[ID])) {
                        r.NPumpingInOneStage[i][j] = 0 ;/**/
                        if (r.NPumpingInOneStage[i][j] > NPumpingMax) {
                            r.NPumpingInOneStage[i][j] = NPumpingMax;
                        }
                        double[] result;
                        result = TrialCalculation.pumpingUp(ID, i, r.NPumpingInOneStage[i][j], r.zInOneStage[i][j], (vEnd[ID + 1][i] + vBegin[ID + 1][i]) / 2, v0, Q);
                        r.PumpingInOneStage[i][j] = result[0];
                        r.zInOneStage[i][j] = result[1];
                        r.netHeadInOneStage[i][j] = r.zInOneStage[i][j] - r.zTailDecisionInOneStage[i][j];
                        r.outputQInOneStage[i][j] = r.outputNInOneStage[i][j] * 1000 / (NCoefficient[ID] * r.netHeadInOneStage[i][j]);
                        double vEnd = v0 + (inputQ[ID][i] - r.outputQInOneStage[i][j] + r.PumpingInOneStage[i][j])*t[i]/100000000;
                        r.vInOneStage[i][j] = (v0+vEnd)/2;
                        r.zInOneStage[i][j] = ChaZhi.function(r.vInOneStage[i][j], v[ID], z[ID]);
                        r.wasteInOneStage[i][j] = 0;
                        double vPumping = r.PumpingInOneStage[i][j] * t[i] / 100000000;
                        r.vOtherEndInOneStage[i][j] = v0OtherBegin - vPumping;
                        if (r.vOtherEndInOneStage[i][j] < vDownFloor[ID+1]) {
                            r.labelInOneStage[i][j] = 0;
                        }
                    }else {
                        r.outputQInOneStage[i][j] = r.outputNInOneStage[i][j] * 1000 / (NCoefficient[ID] * r.netHeadInOneStage[i][j]);
                        r.wasteInOneStage[i][j] = Q - r.outputQInOneStage[i][j];
                    }
                    break out;
                }
                if (r.outputNInOneStage[i][j] >= 0 && r.outputNInOneStage[i][j] < NLimit[ID] - NPVPredict[ID][i]) {
                    r.wasteInOneStage[i][j] = Q - r.outputQInOneStage[i][j];
                     if (ID == 0 && !(r.outputNInOneStage[i][j] == NLimit[ID])) {
                        r.NPumpingInOneStage[i][j] = 0;/**/
                        if (r.NPumpingInOneStage[i][j] > NPumpingMax) {
                            r.NPumpingInOneStage[i][j] = NPumpingMax;
                        }
                        double[] result;
                        result = TrialCalculation.pumpingUp(ID, i, r.NPumpingInOneStage[i][j], r.zInOneStage[i][j], (vEnd[ID + 1][i] + vBegin[ID + 1][i]) / 2, v0, Q);
                        r.PumpingInOneStage[i][j] = result[0];
                        r.zInOneStage[i][j] = result[1];
                        r.netHeadInOneStage[i][j] = r.zInOneStage[i][j] - r.zTailDecisionInOneStage[i][j];
                        double vEnd = v0 + (inputQ[ID][i] - r.outputQInOneStage[i][j] + r.PumpingInOneStage[i][j])*t[i]/100000000;
                        r.vInOneStage[i][j] = (v0+vEnd)/2;
                        r.zInOneStage[i][j] = ChaZhi.function(r.vInOneStage[i][j], v[ID], z[ID]);
                        r.wasteInOneStage[i][j] = 0;
                        double vPumping = r.PumpingInOneStage[i][j] * t[i] / 100000000;
                        r.vOtherEndInOneStage[i][j] = v0OtherBegin - vPumping;
                        if (r.vOtherEndInOneStage[i][j] < vDownFloor[ID+1]) {
                            r.labelInOneStage[i][j] = 0;
                        }
                    }
                    break out;
                }
                if(r.outputNInOneStage[i][j] < 0){
                    r.labelInOneStage[i][j] = 0;
                }
            }
            else if (r.outputQInOneStage[i][j] >= QBio[ID] && r.outputQInOneStage[i][j] <= outputQeMax[ID]) out:{
                r.labelInOneStage[i][j] = 1;
                r.zTailDecisionInOneStage[i][j] = ChaZhi.function(r.outputQInOneStage[i][j], q[ID], zTail[ID]);
                r.netHeadInOneStage[i][j] = r.zInOneStage[i][j] - r.zTailDecisionInOneStage[i][j];
                r.outputNInOneStage[i][j] = NCoefficient[ID] * r.netHeadInOneStage[i][j] * r.outputQInOneStage[i][j] / 1000;
                double Q = r.outputQInOneStage[i][j];
                if (r.outputNInOneStage[i][j] >= NLimit[ID] - NPVPredict[ID][i]) {
                    r.outputNInOneStage[i][j] = NLimit[ID] - NPVPredict[ID][i];
                    if (ID == 0 && !(r.outputNInOneStage[i][j] == NLimit[ID])) {
                        r.NPumpingInOneStage[i][j] = 0 ;/**/
                        if (r.NPumpingInOneStage[i][j] > NPumpingMax) {
                            r.NPumpingInOneStage[i][j] = NPumpingMax;
                        }
                        r.zTailDecisionInOneStage[i][j] = ChaZhi.function(Q, q[ID], zTail[ID]);
                        r.netHeadInOneStage[i][j] = r.zInOneStage[i][j] - r.zTailDecisionInOneStage[i][j];
                        double[] result;
                        result = TrialCalculation.pumpingUp(ID, i, r.NPumpingInOneStage[i][j], r.zInOneStage[i][j], (vEnd[ID + 1][i] + vBegin[ID + 1][i]) / 2, v0, r.outputQInOneStage[i][j]);
                        r.PumpingInOneStage[i][j] = result[0];
                        r.zInOneStage[i][j] = result[1];
                        r.netHeadInOneStage[i][j] = r.zInOneStage[i][j] - r.zTailDecisionInOneStage[i][j];
                        r.outputQInOneStage[i][j] = r.outputNInOneStage[i][j] * 1000 / (NCoefficient[ID] * r.netHeadInOneStage[i][j]);
                        double vEnd = v0 + (inputQ[ID][i] - r.outputQInOneStage[i][j] + r.PumpingInOneStage[i][j])*t[i]/100000000;
                        r.vInOneStage[i][j] = (v0+vEnd)/2;
                        r.zInOneStage[i][j] = ChaZhi.function(r.vInOneStage[i][j], v[ID], z[ID]);
                        r.wasteInOneStage[i][j] = 0;
                        double vPumping = r.PumpingInOneStage[i][j] * t[i] / 100000000;
                        r.vOtherEndInOneStage[i][j] = v0OtherBegin - vPumping;
                        if (r.vOtherEndInOneStage[i][j] < vDownFloor[ID+1]) {
                            r.labelInOneStage[i][j] = 0;
                        }
                    }else {
                        r.wasteInOneStage[i][j] = Q - r.outputNInOneStage[i][j] * 1000 / (NCoefficient[ID] * r.netHeadInOneStage[i][j]);
                        r.outputQInOneStage[i][j] = r.outputNInOneStage[i][j] * 1000 / (NCoefficient[ID] * r.netHeadInOneStage[i][j]);
                    }
                    break out;
                }
                if (r.outputNInOneStage[i][j] >= 0 && r.outputNInOneStage[i][j] < NLimit[ID] - NPVPredict[ID][i]) {
                    r.wasteInOneStage[i][j] = Q - r.outputQInOneStage[i][j];
                    if (ID == 0 && !(r.outputNInOneStage[i][j] == NLimit[ID])) {
                        r.NPumpingInOneStage[i][j] = 0;/**/
                        if (r.NPumpingInOneStage[i][j] > NPumpingMax) {
                            r.NPumpingInOneStage[i][j] = NPumpingMax;
                        }
                        double[] result;
                        result = TrialCalculation.pumpingUp(ID, i, r.NPumpingInOneStage[i][j], r.zInOneStage[i][j], (vEnd[ID + 1][i] + vBegin[ID + 1][i]) / 2, v0, Q);
                        r.PumpingInOneStage[i][j] = result[0];
                        r.zInOneStage[i][j] = result[1];
                        r.netHeadInOneStage[i][j] = r.zInOneStage[i][j] - r.zTailDecisionInOneStage[i][j];
                        double vEnd = v0 + (inputQ[ID][i] - r.outputQInOneStage[i][j] + r.PumpingInOneStage[i][j])*t[i]/100000000;
                        r.vInOneStage[i][j] = (v0+vEnd)/2;
                        r.zInOneStage[i][j] = ChaZhi.function(r.vInOneStage[i][j], v[ID], z[ID]);
                        r.wasteInOneStage[i][j] = 0;
                        double vPumping = r.PumpingInOneStage[i][j] * t[i] / 100000000;
                        r.vOtherEndInOneStage[i][j] = v0OtherBegin - vPumping;
                        if (r.vOtherEndInOneStage[i][j] < vDownFloor[ID+1]) {
                            r.labelInOneStage[i][j] = 0;
                        }
                    }
                    break out;
                }
                if(r.outputNInOneStage[i][j]<0){
                    r.labelInOneStage[i][j] = 0;
                }
            }
            else if (r.outputQInOneStage[i][j] < QBio[ID]) {
                if (ID == 0) {
                    r.labelInOneStage[i][j] = 1;
                    r.outputQInOneStage[i][j] = QBio[ID];
                    r.zTailDecisionInOneStage[i][j] = ChaZhi.function(r.outputQInOneStage[i][j], q[ID], zTail[ID]);
                    r.netHeadInOneStage[i][j] = r.zInOneStage[i][j] - r.zTailDecisionInOneStage[i][j];
                    r.outputNInOneStage[i][j] = r.outputQInOneStage[i][j]*r.netHeadInOneStage[i][j]*NCoefficient[ID]/1000;
                    r.wasteInOneStage[i][j] = 0;
                    r.PumpingInOneStage[i][j] = r.outputQInOneStage[i][j] - inputQ[ID][i] - (v0 - r.vDis[j]) * 100000000 / t[i];
                    r.NPumpingInOneStage[i][j] = 9.81 * (r.zInOneStage[i][j] - zEnd[ID + 1][i]) * r.PumpingInOneStage[i][j] / (0.8 * 1000);
                    double vPumping = r.PumpingInOneStage[i][j] * t[i] / 100000000;
                    r.vOtherEndInOneStage[i][j] = v0OtherBegin - vPumping;
                    m = 1;
                    if (r.NPumpingInOneStage[i][j] > NPumpingMax || r.NPumpingInOneStage[i][j] < 0 || r.vOtherEndInOneStage[i][j] < vDownFloor[ID+1]) {
                        r.labelInOneStage[i][j] = 0;
                    }
                }else {
                    r.labelInOneStage[i][j] = 0;
                }
            }
            if (r.outputNInOneStage[i][j] < NFirm[ID]) {
                penalty = 1;
            }

            //水位控制条件

            /*
            if(Math.abs(ChaZhi.function(r.vDis[j], v[ID], z[ID]) - z0[ID])>0.04*(resourceData[ID][0]-resourceData[ID][1])){
                r.labelInOneStage[i][j] = 0;
            }
            if(ID == 0 && Math.abs(ChaZhi.function(r.vOtherEndInOneStage[i][j], v[ID+1], z[ID+1]) - ChaZhi.function(v0OtherBegin, v[ID+1], z[ID+1]))>0.04*(resourceData[ID+1][0]-resourceData[ID+1][1])){
                r.labelInOneStage[i][j] = 0;
            }
             */


            if ( ID == 0 && r.labelInOneStage[i][j] == 1) {
                //r.targetAllStage[i][j] = (1-lambda)*(r.outputNInOneStage[i][j] * 1000 * priceda[i] - m * r.NPumpingInOneStage[i][j] * 1000 * priceda[i] - penalty * c0[ID] * 1000 * priceda[i] + NPVPredict[ID][i]*1000*priceda[i]) - lambda*((1-m)*CVaR[i]*r.outputNInOneStage[i][j] * 1000 - m*ReCVaR[i]* r.NPumpingInOneStage[i][j] * 1000 + NPVPredict[ID][i]*1000*priceda[i]);
                r.targetAllStage[i][j] = (1-lambda)*((1-m)*r.outputNInOneStage[i][j] * 1000 * priceda[i] - m * r.NPumpingInOneStage[i][j] * 1000 * priceda[i] - penalty * c0[ID] * 1000 * priceda[i] + NPVPredict[ID][i]*1000*priceda[i]) - lambda*CVaR[i]*((1-m)*r.outputNInOneStage[i][j] * 1000 - m* r.NPumpingInOneStage[i][j] * 1000 + NPVPredict[ID][i]*1000*priceda[i]);
                r.moneyAllStage[i][j] = r.outputNInOneStage[i][j] * 1000 * priceda[i] - m * r.NPumpingInOneStage[i][j] * 1000 * priceda[i] + NPVPredict[ID][i]*1000*priceda[i];
                r.vEndInOneStage[i][j] = r.vInOneStage[i][j] * 2 - v0;
                r.zEndInOneStage[i][j] = ChaZhi.function(r.vEndInOneStage[i][j], v[ID], z[ID]);
            }
            if(ID == 1 && r.labelInOneStage[i][j] == 1){
                r.targetAllStage[i][j] = (1-lambda)*(r.outputNInOneStage[i][j] * 1000 * priceda[i]  - penalty * c0[ID] * 1000 * priceda[i] + NPVPredict[ID][i]*1000*priceda[i]) - lambda*CVaR[i]*(r.outputNInOneStage[i][j] * 1000 + NPVPredict[ID][i]*1000*priceda[i]);
                r.moneyAllStage[i][j] = r.outputNInOneStage[i][j] * 1000 * priceda[i] + NPVPredict[ID][i]*1000*priceda[i];
                r.vEndInOneStage[i][j] = r.vInOneStage[i][j] * 2 - v0;
                r.zEndInOneStage[i][j] = ChaZhi.function(r.vEndInOneStage[i][j], v[ID], z[ID]);
            }
        }
    }

    public static void functionV(int ID, int i, int levelNumber, Reservoir r) {
        double[] max = new double[levelNumber + 1];
        for (int j = 0; j <= levelNumber; j++) {
            max[j] = Integer.MIN_VALUE;
            for (int k = 0; k <= levelNumber; k++) {
                int m = 0;
                int penalty = 0;
                double vDecision;
                double zDecision;
                double zTailDecision = 0;
                double waste = 0;
                double netHead = 0;
                double outputQ;
                double outputN = 0;
                double nPumping = 0;
                double pumping = 0;
                int label = 0;
                double target = 0;
                double vOtherEnd = 0;
                double money = 0;
                if (r.labelInOneStage[i - 1][k] == 1) {
                    vDecision = (r.vEndInOneStage[i - 1][k] + r.vDis[j]) / 2;
                    zDecision = ChaZhi.function(vDecision, v[ID], z[ID]);
                    if (ID == 0) {
                        outputQ = inputQ[ID][i] + (r.vEndInOneStage[i - 1][k] - r.vDis[j]) * 100000000 / t[i];/**/
                    } else {
                        outputQ = inputQ[ID][i] + (r.vEndInOneStage[i - 1][k] - r.vDis[j]) * 100000000 / t[i];
                    }

                    if (outputQ > outputQeMax[ID] && outputQ <= outputQMax[ID]) out:{
                        label = 1;
                        zTailDecision = ChaZhi.function(outputQ, q[ID], zTail[ID]);
                        netHead = zDecision - zTailDecision;
                        double Q = outputQ;
                        outputQ = outputQeMax[ID];
                        outputN = NCoefficient[ID] * netHead * outputQ / 1000;
                        if (outputN >= NLimit[ID] - NPVPredict[ID][i]) {
                            outputN = NLimit[ID] - NPVPredict[ID][i];
                            if (ID == 0 && !(r.outputNInOneStage[i][j] == NLimit[ID])) {
                                nPumping = 0 ;/**/
                                if (nPumping > NPumpingMax) {
                                    nPumping = NPumpingMax;
                                }
                                zTailDecision = ChaZhi.function(Q, q[ID], zTail[ID]);
                                double[] result;
                                result = TrialCalculation.pumpingUp(ID, i, nPumping, zDecision, (vEnd[ID + 1][i] + vBegin[ID + 1][i]) / 2, r.vEndInOneStage[i - 1][k], Q);
                                pumping = result[0];
                                zDecision = result[1];
                                netHead = zDecision - zTailDecision;
                                outputQ = outputN * 1000 / (NCoefficient[ID] * netHead);
                                double vEnd = r.vEndInOneStage[i-1][k] + (inputQ[ID][i] - outputQ + pumping)*t[i]/100000000;
                                vDecision = (r.vEndInOneStage[i-1][k] + vEnd)/2;
                                waste = 0;
                                double vPumping = pumping * t[i] / 100000000;
                                vOtherEnd = r.vOtherEndInOneStage[i - 1][k] - vPumping;
                                if (vOtherEnd < vDownFloor[ID+1]) {
                                    label = 0;
                                }
                            }else {
                                outputQ = outputN * 1000 / (NCoefficient[ID] * netHead);
                                waste = Q - outputQ;
                                if(ID == 0){
                                    vOtherEnd = r.vOtherEndInOneStage[i - 1][k];
                                }
                            }
                            break out;
                        }
                        if (outputN >= 0 && outputN < NLimit[ID] - NPVPredict[ID][i]) {
                            waste = Q - outputQ;
                            if (ID == 0 && !(r.outputNInOneStage[i][j] == NLimit[ID])) {
                                nPumping = 0 ;/**/
                                if (nPumping > NPumpingMax) {
                                    nPumping = NPumpingMax;
                                }
                                double[] result;
                                result = TrialCalculation.pumpingUp(ID, i, nPumping, zDecision, (vEnd[ID + 1][i] + vBegin[ID + 1][i]) / 2, r.vEndInOneStage[i - 1][k], Q);
                                pumping = result[0];
                                zDecision = result[1];
                                netHead = zDecision - zTailDecision;
                                double vEnd = r.vEndInOneStage[i-1][k] + (inputQ[ID][i] - outputQ + pumping)*t[i]/100000000;
                                vDecision = (r.vEndInOneStage[i-1][k] + vEnd)/2;
                                waste = 0;
                                double vPumping = pumping * t[i] / 100000000;
                                vOtherEnd = r.vOtherEndInOneStage[i - 1][k] - vPumping;
                                if (vOtherEnd < vDownFloor[ID+1]) {
                                    label = 0;
                                }
                            }else {
                                if(ID == 0){
                                    vOtherEnd = r.vOtherEndInOneStage[i - 1][k];
                                }
                            }
                            break out;
                        }
                        if (outputN < 0 ){
                            label = 0;
                        }
                    }
                    else if (outputQ >= QBio[ID] && outputQ <= outputQeMax[ID]) out:{
                        label = 1;
                        zTailDecision = ChaZhi.function(outputQ, q[ID], zTail[ID]);
                        netHead = zDecision - zTailDecision;
                        outputN = NCoefficient[ID] * netHead * outputQ / 1000;
                        double Q = outputQ;
                        if (outputN >= NLimit[ID] - NPVPredict[ID][i]) {
                            outputN = NLimit[ID] - NPVPredict[ID][i];
                            if (ID == 0 && !(r.outputNInOneStage[i][j] == NLimit[ID])) {
                                nPumping = 0;/**/
                                if (nPumping > NPumpingMax) {
                                    nPumping = NPumpingMax;
                                }
                                zTailDecision = ChaZhi.function(Q, q[ID], zTail[ID]);
                                double[] result;
                                result = TrialCalculation.pumpingUp(ID, i, nPumping, zDecision, (vEnd[ID + 1][i] + vBegin[ID + 1][i]) / 2, r.vEndInOneStage[i - 1][k], outputQ);
                                pumping = result[0];
                                zDecision = result[1];
                                netHead = zDecision - zTailDecision;
                                outputQ = outputN * 1000 / (NCoefficient[ID] * netHead);
                                double vEnd = r.vEndInOneStage[i-1][k] + (inputQ[ID][i] - outputQ + pumping)*t[i]/100000000;
                                vDecision = (r.vEndInOneStage[i-1][k] + vEnd)/2;
                                waste = 0;
                                double vPumping = pumping * t[i] / 100000000;
                                vOtherEnd = r.vOtherEndInOneStage[i - 1][k] - vPumping;
                                if (vOtherEnd < vDownFloor[ID+1]) {
                                    label = 0;
                                }
                            }else {
                                waste = Q - outputN * 1000 / (NCoefficient[ID] * netHead);
                                outputQ = outputN * 1000 / (NCoefficient[ID] * netHead);
                                if(ID == 0){
                                    vOtherEnd = r.vOtherEndInOneStage[i - 1][k];
                                }
                            }
                            break out;
                        }
                        if (outputN >0 && outputN < NLimit[ID] - NPVPredict[ID][i]) {
                            waste = Q - outputQ;
                            if (ID == 0 && !(r.outputNInOneStage[i][j] == NLimit[ID])) {
                                nPumping = 0;/**/
                                if (nPumping > NPumpingMax) {
                                    nPumping = NPumpingMax;
                                }
                                double[] result;
                                result = TrialCalculation.pumpingUp(ID, i, nPumping, zDecision, (vEnd[ID + 1][i] + vBegin[ID + 1][i]) / 2, r.vEndInOneStage[i - 1][k], Q);
                                pumping = result[0];
                                zDecision = result[1];
                                netHead = zDecision - zTailDecision;
                                double vEnd = r.vEndInOneStage[i-1][k] + (inputQ[ID][i] - outputQ + pumping)*t[i]/100000000;
                                vDecision = (r.vEndInOneStage[i-1][k] + vEnd)/2;
                                waste = 0;
                                double vPumping = pumping * t[i] / 100000000;
                                vOtherEnd = r.vOtherEndInOneStage[i - 1][k] - vPumping;
                                if (vOtherEnd < vDownFloor[ID+1]) {
                                    label = 0;
                                }
                            }else {
                                if(ID == 0){
                                    vOtherEnd = r.vOtherEndInOneStage[i - 1][k];
                                }
                            }
                            break out;
                        }
                        if(outputN < 0){
                            label = 0;
                        }
                    }
                    else if (outputQ < QBio[ID]) {
                        if (ID == 0) {
                            label = 1;
                            outputQ = QBio[ID];
                            zTailDecision = ChaZhi.function(QBio[ID], q[ID], zTail[ID]);
                            netHead = zDecision - zTailDecision;
                            outputN = outputQ*netHead*NCoefficient[ID]/1000;
                            waste = 0;
                            pumping = outputQ - inputQ[ID][i] - (r.vEndInOneStage[i - 1][k] - r.vDis[j]) * 100000000 / t[i];
                            nPumping = 9.81 * (zDecision - zEnd[ID + 1][i]) * pumping / (0.8 * 1000);
                            double vPumping = pumping * t[i] / 100000000;
                            vOtherEnd = r.vOtherEndInOneStage[i - 1][k] - vPumping;
                            m = 1;
                            if (nPumping > NPumpingMax || nPumping < 0 || vOtherEnd < vDownFloor[ID+1]) {
                                label = 0;
                            }
                        }
                    }
                    if (outputN < NFirm[ID]){
                        penalty = 1;
                    }

                    //水位控制条件
                    /*
                    if(Math.abs(ChaZhi.function(r.vDis[j], v[ID], z[ID]) - r.zEndInOneStage[i-1][k])>0.04*(resourceData[ID][0]-resourceData[ID][1])){
                        label = 0;
                    }
                    if(ID == 0 && Math.abs(ChaZhi.function(vOtherEnd, v[ID+1], z[ID+1]) - ChaZhi.function(r.vOtherEndInOneStage[i - 1][k], v[ID+1], z[ID+1]))>0.04*(resourceData[ID+1][0]-resourceData[ID+1][1])){
                        label = 0;
                    }
                     */


                    if (ID == 0 && label == 1) {
                        //target = (1-lambda)*(outputN * 1000 * priceda[i] - m * nPumping * 1000 * priceda[i] - penalty * c0[ID] * 1000 * priceda[i]+ NPVPredict[ID][i]*1000*priceda[i]) - lambda*((1-m)*CVaR[i]*outputN * 1000 - m*ReCVaR[i] * nPumping * 1000 + NPVPredict[ID][i]*1000*priceda[i]) + r.targetAllStage[i - 1][k];
                        money = outputN * 1000 * priceda[i] - m * nPumping * 1000 * priceda[i] + r.moneyAllStage[i - 1][k] + NPVPredict[ID][i]*1000*priceda[i];
                        target = (1-lambda)*(outputN * 1000 * priceda[i] - m * nPumping * 1000 * priceda[i] - penalty * c0[ID] * 1000 * priceda[i]+ NPVPredict[ID][i]*1000*priceda[i]) - lambda*CVaR[i]*((1-m)*outputN * 1000 - m * nPumping * 1000 + NPVPredict[ID][i]*1000*priceda[i]) + r.targetAllStage[i - 1][k];
                    }
                    if (ID == 1 && label == 1){
                        target = (1-lambda)*(outputN * 1000 * priceda[i] - penalty * c0[ID] * 1000 * priceda[i] + NPVPredict[ID][i]*1000*priceda[i]) - lambda*CVaR[i]* (1000 *outputN +NPVPredict[ID][i]*1000*priceda[i])+ r.targetAllStage[i - 1][k];
                        money = outputN * 1000 * priceda[i]  + r.moneyAllStage[i - 1][k] + NPVPredict[ID][i]*1000*priceda[i];
                    }
                    if (max[j] <= target && label == 1 && r.labelInOneStage[i - 1][k] == 1) {
                        max[j] = target;
                        r.targetAllStage[i][j] = target;
                        r.moneyAllStage[i][j] = money;
                        r.outputQInOneStage[i][j] = outputQ;
                        r.outputNInOneStage[i][j] = outputN;
                        r.wasteInOneStage[i][j] = waste;
                        r.zTailDecisionInOneStage[i][j] = zTailDecision;
                        r.netHeadInOneStage[i][j] = netHead;
                        r.vEndInOneStage[i][j] = vDecision * 2 - r.vEndInOneStage[i - 1][k];
                        r.zEndInOneStage[i][j] = ChaZhi.function(r.vEndInOneStage[i][j], v[ID], z[ID]);
                        r.NPumpingInOneStage[i][j] = nPumping;
                        r.PumpingInOneStage[i][j] = pumping;
                        r.labelInOneStage[i][j] = 1;
                        r.cij[i][j] = k;
                        r.vOtherEndInOneStage[i][j] = vOtherEnd;
                    }
                }
            }
        }
    }

    public static void functionVt(int ID, int i, int levelNumber, double vt, Reservoir r, double vSeparate) {
        int index = 0;
        for (int k = 0; k <= levelNumber; k++) {
            int m = 0;
            int penalty = 0;
            if (r.labelInOneStage[i - 1][k] == 1) {
                r.vInOneStage[i][k] = (r.vEndInOneStage[i - 1][k] + vt) / 2;
                r.zInOneStage[i][k] = ChaZhi.function(r.vInOneStage[i][k], v[ID], z[ID]);
                if (ID == 0) {
                    r.outputQInOneStage[i][k] = inputQ[ID][i] + (r.vEndInOneStage[i - 1][k] - vt) * 100000000 / t[i];
                } else {
                    r.outputQInOneStage[i][k] = inputQ[ID][i] + (r.vEndInOneStage[i - 1][k] - vt) * 100000000 / t[i];
                }

                if (r.outputQInOneStage[i][k] > outputQMax[ID]) {
                    r.labelInOneStage[i][k] = 0;
                } else if (r.outputQInOneStage[i][k] > outputQeMax[ID] && r.outputQInOneStage[i][k] < outputQMax[ID]) out:{
                        r.labelInOneStage[i][k] = 1;
                        r.zTailDecisionInOneStage[i][k] = ChaZhi.function(r.outputQInOneStage[i][k], q[ID], zTail[ID]);
                        r.netHeadInOneStage[i][k] = r.zInOneStage[i][k] - r.zTailDecisionInOneStage[i][k];
                        double Q = r.outputQInOneStage[i][k];
                        r.outputQInOneStage[i][k] = outputQeMax[ID];
                        r.outputNInOneStage[i][k] = NCoefficient[ID] * r.netHeadInOneStage[i][k] * r.outputQInOneStage[i][k] / 1000;
                        double N = r.outputNInOneStage[i][k];
                        if (N > NLimit[ID]) N = NLimit[ID];
                        if (r.outputNInOneStage[i][k] >= NLimit[ID] - NPVPredict[ID][i]) {
                            r.outputNInOneStage[i][k] = NLimit[ID] - NPVPredict[ID][i];
                            if (ID == 0 && !(r.outputNInOneStage[i][k] == NLimit[ID])) {
                                r.NPumpingInOneStage[i][k] = N - r.outputNInOneStage[i][k];/**/
                                if (r.NPumpingInOneStage[i][k] > NPumpingMax) {
                                    r.NPumpingInOneStage[i][k] = NPumpingMax;
                                }
                                r.zTailDecisionInOneStage[i][k] = ChaZhi.function(Q, q[ID], zTail[ID]);
                                r.netHeadInOneStage[i][k] = r.zInOneStage[i][k] - r.zTailDecisionInOneStage[i][k];
                                r.outputQInOneStage[i][k] = r.outputNInOneStage[i][k] * 1000 / (NCoefficient[ID] * r.netHeadInOneStage[i][k]);
                                double[] result;
                                result = TrialCalculation.pumpingUp(ID, i, r.NPumpingInOneStage[i][k], r.zInOneStage[i][k], (vEnd[ID + 1][i] + vBegin[ID + 1][i]) / 2, r.vEndInOneStage[i - 1][k], Q);
                                r.PumpingInOneStage[i][k] = result[0];
                                r.zInOneStage[i][k] = result[1];
                                r.netHeadInOneStage[i][k] = r.zInOneStage[i][k] - r.zTailDecisionInOneStage[i][k];
                                r.vInOneStage[i][k] = ChaZhi.function(r.zInOneStage[i][k], z[ID], v[ID]);
                                double vPumping = r.PumpingInOneStage[i][k] * t[i] / 100000000;
                                r.vOtherEndInOneStage[i][k] = r.vOtherEndInOneStage[i - 1][k] - vPumping;
                                if (r.vOtherEndInOneStage[i][k] < vDownFloor[ID + 1]) {
                                    r.labelInOneStage[i][k] = 0;
                                }
                            } else {
                                r.outputQInOneStage[i][k] = r.outputNInOneStage[i][k] * 1000 / (NCoefficient[ID] * r.netHeadInOneStage[i][k]);
                                r.wasteInOneStage[i][k] = Q - r.outputQInOneStage[i][k];
                            }
                            break out;
                        }
                        if (r.outputNInOneStage[i][k] > 0 && r.outputNInOneStage[i][k] < NLimit[ID] - NPVPredict[ID][i]) {
                            r.wasteInOneStage[i][k] = Q - r.outputQInOneStage[i][k];
                            if (ID == 0 && !(r.outputNInOneStage[i][k] == NLimit[ID])) {
                                double n = NCoefficient[ID] * r.netHeadInOneStage[i][k] * r.wasteInOneStage[i][k] / 1000;
                                r.NPumpingInOneStage[i][k] = Math.min(n, NLimit[ID] - NPVPredict[ID][i] - r.outputNInOneStage[i][k]);/**/
                                if (r.NPumpingInOneStage[i][k] > NPumpingMax) {
                                    r.NPumpingInOneStage[i][k] = NPumpingMax;
                                }
                                double[] result;
                                result = TrialCalculation.pumpingUp(ID, i, r.NPumpingInOneStage[i][k], r.zInOneStage[i][k], (vEnd[ID + 1][i] + vBegin[ID + 1][i]) / 2, r.vEndInOneStage[i - 1][k], Q);
                                r.PumpingInOneStage[i][k] = result[0];
                                r.zInOneStage[i][k] = result[1];
                                r.netHeadInOneStage[i][k] = r.zInOneStage[i][k] - r.zTailDecisionInOneStage[i][k];
                                double QWasteE = r.NPumpingInOneStage[i][k] * 1000 / (NCoefficient[ID] * r.netHeadInOneStage[i][k]);
                                r.wasteInOneStage[i][k] = Q - QWasteE - r.outputQInOneStage[i][k];
                                r.vInOneStage[i][k] = ChaZhi.function(r.zInOneStage[i][k], z[ID], v[ID]);
                                double vPumping = r.PumpingInOneStage[i][k] * t[i] / 100000000;
                                r.vOtherEndInOneStage[i][k] = r.vOtherEndInOneStage[i - 1][k] - vPumping;
                                if (r.vOtherEndInOneStage[i][k] < vDownFloor[ID + 1]) {
                                    r.labelInOneStage[i][k] = 0;
                                }
                            }
                            break out;
                        }
                        if (r.outputNInOneStage[i][k] < 0) {
                            r.labelInOneStage[i][k] = 0;
                        }
                    }
                else if (r.outputQInOneStage[i][k] >= QBio[ID] && r.outputQInOneStage[i][k] <= outputQeMax[ID]) out:{
                    r.labelInOneStage[i][k] = 1;
                    r.zTailDecisionInOneStage[i][k] = ChaZhi.function(r.outputQInOneStage[i][k], q[ID], zTail[ID]);
                    r.netHeadInOneStage[i][k] = r.zInOneStage[i][k] - r.zTailDecisionInOneStage[i][k];
                    r.outputNInOneStage[i][k] = NCoefficient[ID] * r.netHeadInOneStage[i][k] * r.outputQInOneStage[i][k] / 1000;
                    double Q = r.outputQInOneStage[i][k];
                    double N = r.outputNInOneStage[i][k];
                    if (N > NLimit[ID]) N = NLimit[ID];
                    if (r.outputNInOneStage[i][k] >= NLimit[ID] - NPVPredict[ID][i]) {
                        if (ID == 0 && !(r.outputNInOneStage[i][k] == NLimit[ID])) {
                            r.outputNInOneStage[i][k] = NLimit[ID] - NPVPredict[ID][i];
                            r.NPumpingInOneStage[i][k] = N - r.outputNInOneStage[i][k];/**/
                            if (r.NPumpingInOneStage[i][k] > NPumpingMax) {
                                r.NPumpingInOneStage[i][k] = NPumpingMax;
                            }
                            r.zTailDecisionInOneStage[i][k] = ChaZhi.function(Q, q[ID], zTail[ID]);
                            r.netHeadInOneStage[i][k] = r.zInOneStage[i][k] - r.zTailDecisionInOneStage[i][k];
                            double[] result;
                            result = TrialCalculation.pumpingUp(ID, i, r.NPumpingInOneStage[i][k], r.zInOneStage[i][k], (vEnd[ID + 1][i] + vBegin[ID + 1][i]) / 2, r.vEndInOneStage[i - 1][k], r.outputQInOneStage[i][k]);
                            r.PumpingInOneStage[i][k] = result[0];
                            r.zInOneStage[i][k] = result[1];
                            r.netHeadInOneStage[i][k] = r.zInOneStage[i][k] - r.zTailDecisionInOneStage[i][k];
                            r.outputQInOneStage[i][k] = r.outputNInOneStage[i][k] * 1000 / (NCoefficient[ID] * r.netHeadInOneStage[i][k]);
                            double QWasteE = r.NPumpingInOneStage[i][k] * 1000 / (NCoefficient[ID] * r.netHeadInOneStage[i][k]);
                            r.wasteInOneStage[i][k] = Q - QWasteE - r.outputQInOneStage[i][k];
                            r.vInOneStage[i][k] = ChaZhi.function(r.zInOneStage[i][k], z[ID], v[ID]);
                            double vPumping = r.PumpingInOneStage[i][k] * t[i] / 100000000;
                            r.vOtherEndInOneStage[i][k] = r.vOtherEndInOneStage[i - 1][k] - vPumping;
                            if (r.vOtherEndInOneStage[i][k] < vDownFloor[ID + 1]) {
                                r.labelInOneStage[i][k] = 0;
                            }
                        } else {
                            r.wasteInOneStage[i][k] = Q - r.outputNInOneStage[i][k] * 1000 / (NCoefficient[ID] * r.netHeadInOneStage[i][k]);
                            r.outputQInOneStage[i][k] = r.outputNInOneStage[i][k] * 1000 / (NCoefficient[ID] * r.netHeadInOneStage[i][k]);
                        }
                        break out;
                    }
                    if (r.outputNInOneStage[i][k] > 0 && r.outputNInOneStage[i][k] < NLimit[ID] - NPVPredict[ID][i]) {
                        r.wasteInOneStage[i][k] = Q - r.outputQInOneStage[i][k];
                        if (ID == 0 && !(r.outputNInOneStage[i][k] == NLimit[ID])) {
                            double n = NCoefficient[ID] * r.netHeadInOneStage[i][k] * r.wasteInOneStage[i][k] / 1000;
                            r.NPumpingInOneStage[i][k] = Math.min(n, NLimit[ID] - NPVPredict[ID][i] - r.outputNInOneStage[i][k]);/**/
                            if (r.NPumpingInOneStage[i][k] > NPumpingMax) {
                                r.NPumpingInOneStage[i][k] = NPumpingMax;
                            }
                            double[] result;
                            result = TrialCalculation.pumpingUp(ID, i, r.NPumpingInOneStage[i][k], r.zInOneStage[i][k], (vEnd[ID + 1][i] + vBegin[ID + 1][i]) / 2, r.vEndInOneStage[i - 1][k], Q);
                            r.PumpingInOneStage[i][k] = result[0];
                            r.zInOneStage[i][k] = result[1];
                            r.netHeadInOneStage[i][k] = r.zInOneStage[i][k] - r.zTailDecisionInOneStage[i][k];
                            double QWasteE = r.NPumpingInOneStage[i][k] * 1000 / (NCoefficient[ID] * r.netHeadInOneStage[i][k]);
                            r.wasteInOneStage[i][k] = Q - QWasteE - r.outputQInOneStage[i][k];
                            r.vInOneStage[i][k] = ChaZhi.function(r.zInOneStage[i][k], z[ID], v[ID]);
                            double vPumping = r.PumpingInOneStage[i][k] * t[i] / 100000000;
                            r.vOtherEndInOneStage[i][k] = r.vOtherEndInOneStage[i - 1][k] - vPumping;
                            if (r.vOtherEndInOneStage[i][k] < vDownFloor[ID + 1]) {
                                r.labelInOneStage[i][k] = 0;
                            }
                        }
                        break out;
                    }
                    if (r.outputNInOneStage[i][k] < 0) {
                        r.labelInOneStage[i][k] = 0;
                    }
                }
                else if (r.outputQInOneStage[i][k] < QBio[ID]) {
                    if (ID == 0) {
                        r.labelInOneStage[i][k] = 1;
                        r.outputQInOneStage[i][k] = QBio[ID];
                        r.zTailDecisionInOneStage[i][k] = ChaZhi.function(r.outputQInOneStage[i][k], q[ID], zTail[ID]);
                        r.netHeadInOneStage[i][k] = r.zInOneStage[i][k] - r.zTailDecisionInOneStage[i][k];
                        r.outputNInOneStage[i][k] = r.outputQInOneStage[i][k] * r.netHeadInOneStage[i][k] * NCoefficient[ID] / 1000;
                        r.wasteInOneStage[i][k] = 0;
                        r.PumpingInOneStage[i][k] = r.outputQInOneStage[i][k] - inputQ[ID][i] - (r.vEndInOneStage[i - 1][k] - vt) * 100000000 / t[i];
                        r.NPumpingInOneStage[i][k] = 9.81 * (r.zInOneStage[i][k] - zEnd[ID + 1][i]) * r.PumpingInOneStage[i][k] / (0.8 * 1000);
                        double vPumping = r.PumpingInOneStage[i][k] * t[i] / 100000000;
                        r.vOtherEndInOneStage[i][k] = r.vOtherEndInOneStage[i - 1][k] - vPumping;
                        m = 1;
                        if (r.NPumpingInOneStage[i][k] > NPumpingMax || r.NPumpingInOneStage[i][k] < 0 || r.vOtherEndInOneStage[i][k] < vDownFloor[ID + 1]) {
                            r.labelInOneStage[i][k] = 0;
                        }
                    } else {
                        r.labelInOneStage[i][k] = 0;
                    }
                }

                if (r.outputNInOneStage[i][k] < NFirm[ID]) {
                    penalty = 1;
                }

                /*
                if(Math.abs(zt[ID]-ChaZhi.function(r.vDis[k], v[ID], z[ID]))>0.04*(resourceData[ID][0]-resourceData[ID][1])){
                    r.labelInOneStage[i][k] = 0;
                }
                if(ID == 0 && Math.abs(ChaZhi.function(r.vOtherEndInOneStage[i][k], v[ID+1], z[ID+1]) - ChaZhi.function(r.vOtherEndInOneStage[i - 1][k], v[ID+1], z[ID+1]))>0.04*(resourceData[ID+1][0]-resourceData[ID+1][1])){
                    r.labelInOneStage[i][k] = 0;
                }
                 */

                if (ID == 0 && r.labelInOneStage[i][k] == 1) {
                    //r.targetAllStage[i][k] = (1 - lambda) * (r.outputNInOneStage[i][k] * 1000 * priceda[i] - m * r.NPumpingInOneStage[i][k] * 1000 * priceda[i] - penalty * c0[ID] * 1000 * priceda[i] + NPVPredict[ID][i]*1000*priceda[i]) - lambda * ((1 - m) * CVaR[i] * r.outputNInOneStage[i][k] * 1000 - m * ReCVaR[i] * r.NPumpingInOneStage[i][k] * 1000 + NPVPredict[ID][i]*1000*priceda[i]) + r.targetAllStage[i - 1][k];
                    r.moneyAllStage[i][k] = r.outputNInOneStage[i][k] * 1000 * priceda[i] - m * r.NPumpingInOneStage[i][k] * 1000 * priceda[i] + NPVPredict[ID][i]*1000*priceda[i] + r.moneyAllStage[i - 1][k];
                    r.zEndInOneStage[i][k] = ChaZhi.function(vt,v[ID],z[ID]);
                    r.vEndInOneStage[i][k] = vt;
                    r.targetAllStage[i][k] = (1 - lambda) * (r.outputNInOneStage[i][k] * 1000 * priceda[i] - m * r.NPumpingInOneStage[i][k] * 1000 * priceda[i] - penalty * c0[ID] * 1000 * priceda[i] + NPVPredict[ID][i]*1000*priceda[i]) - lambda * CVaR[i]*((1 - m) * r.outputNInOneStage[i][k] * 1000 - m * r.NPumpingInOneStage[i][k] * 1000 + NPVPredict[ID][i]*1000*priceda[i]) + r.targetAllStage[i - 1][k];
                    index = 1;
                }
                if (ID == 1 && r.labelInOneStage[i][k] == 1) {
                    r.targetAllStage[i][k] = (1 - lambda) * (r.outputNInOneStage[i][k] * 1000 * priceda[i] - penalty * c0[ID] * 1000 * priceda[i] + NPVPredict[ID][i]*1000*priceda[i]) - lambda * CVaR[i] * (r.outputNInOneStage[i][k] * 1000 + NPVPredict[ID][i]*1000*priceda[i]) + r.targetAllStage[i - 1][k];
                    r.moneyAllStage[i][k] = r.outputNInOneStage[i][k] * 1000 * priceda[i] + r.moneyAllStage[i - 1][k] + NPVPredict[ID][i]*1000*priceda[i];
                    r.zEndInOneStage[i][k] = ChaZhi.function(vt,v[ID],z[ID]);
                    r.vEndInOneStage[i][k] = vt;
                    index = 1;
                }
                outputQMax[ID] = resourceData[ID][10];
            }
        }
        if(index == 0) {
            double[] arr;
            arr = r.vEndInOneStage[stageNumber - 2].clone();
            java.util.Arrays.sort(arr);
            int count = 0;
            while (count < arr.length) {
                if (arr[count] > 0) {
                    break;
                }
                count++;
            }
            if(count == arr.length){
                index = 1;
            }else {
                if(ID == 0) {
                    if (arr[count] > vt) {
                        vt = vt + vSeparate * 10;
                    } else {
                        vt = vt - vSeparate * 10;//末水位过高无法达到
                        if (ChaZhi.function(vt, v[ID], z[ID]) < resourceData[ID][1]) {
                            vt = ChaZhi.function(resourceData[ID][1], z[ID], v[ID]);
                        }
                    }
                }else {
                    outputQMax[ID] = outputQMax[ID] * 10;
                }
                Effectiveness.functionVt(ID, stageNumber - 1, levelNumber, vt, r, vSeparate);
            }
        }
    }
}
