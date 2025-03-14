/**
 * @Author: Liang Qiao
 * @School: Hohai University
 * @操作说明：动态规划求解光照-马马崖水电站优化调度问题之水库类
 */
package org.example;

public class Reservoir {
    int[][] cij = new int[Main.stageNumber][Main.levelNumber + 1];
    double[][] vInOneStage = new double[Main.stageNumber][Main.levelNumber + 1];
    double[][] vEndInOneStage = new double[Main.stageNumber][Main.levelNumber+1];
    double[][] zInOneStage = new double[Main.stageNumber][Main.levelNumber + 1];
    double[][] zEndInOneStage = new double[Main.stageNumber][Main.levelNumber+1];
    double[][] wasteInOneStage = new double[Main.stageNumber][Main.levelNumber + 1];
    double[][] netHeadInOneStage = new double[Main.stageNumber][Main.levelNumber + 1];
    double[][] outputQInOneStage = new double[Main.stageNumber][Main.levelNumber + 1];
    double[][] outputNInOneStage = new double[Main.stageNumber][Main.levelNumber + 1];
    double[][] zTailDecisionInOneStage = new double[Main.stageNumber][Main.levelNumber + 1];
    double[][] targetAllStage = new double[Main.stageNumber][Main.levelNumber + 1];
    double[][] NPumpingInOneStage = new double[Main.stageNumber][Main.levelNumber + 1];
    double[][] PumpingInOneStage = new double[Main.stageNumber][Main.levelNumber + 1];
    int[][] labelInOneStage = new int[Main.stageNumber][Main.levelNumber + 1];
    double[][] vOtherEndInOneStage = new double[Main.stageNumber][Main.levelNumber+1];
    double[][] moneyAllStage = new double[Main.stageNumber][Main.levelNumber + 1];
    double[] vDis = new double[Main.levelNumber+1];
}
