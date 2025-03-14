/**
 * @Author: Liang Qiao
 * @School: Hohai University
 * @操作说明：动态规划求解光照-马马崖水电站优化调度问题
 *input：源数据
 * output: 调度结果
 */
package org.example;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    static int X = 2;
    static int A = 10;
    static int levelNumber;
    static int levelNumberInstant = 1000000;
    static double NPumpingMax = 800;
    static int Day = 365;
    static int stageNumber = 24;
    static double lambda = 0.1;//风险厌恶因子
    static double alpha = 0.95;//VaR置信度
    static double[] CVaR = new double[stageNumber];
    static double[] ReCVaR = new double[stageNumber];
    static double[] t = new double[stageNumber];
    static double[] hour = new double[stageNumber];
    static double[] priceda = new double[stageNumber];
    static double[] pricert = new double[stageNumber];
    static double[] pricedaAll = new double[stageNumber];
    static double[] pricertAll = new double[stageNumber];
    static double[][] inputQ = new double[X][stageNumber];
    static double[][] input = new double[X][stageNumber];
    static double[][] NPVPredict = new double[X][stageNumber];
    static double[][] Q = new double[X][stageNumber];
    static double[][] N = new double[X][stageNumber];
    static double[][] W = new double[X][stageNumber];
    static double[][] vBegin = new double[X][stageNumber];
    static double[][] vEnd = new double[X][stageNumber];
    static double[][] zEnd = new double[X][stageNumber];
    static double[][] Qe = new double[X][stageNumber];
    static double[][] NetHead = new double[X][stageNumber];
    static double[][] ZTail = new double[X][stageNumber];
    static double[][] Money = new double[X][stageNumber];
    static double[] Pumping = new double[stageNumber];
    static double[] NPumping = new double[stageNumber];
    static double[] outputQeMax = new double[X];
    static double[] outputQMax = new double[X];
    static double[] NCoefficient = new double[X];
    static double[] NLimit = new double[X];
    static double[] NFirm = new double[X];
    static double[] z0 = new double[X];
    static double[] zt = new double[X];
    static double[] QBio = new double[X];
    static double[] c0 = new double[X];
    static ArrayList<Double> set = new ArrayList<>();
    static double[][] resourceData = new double[X][4];
    static double[][] z = new double[X][10000];
    static double[][] v = new double[X][10000];
    static double[][] q = new double[X][10000];
    static double[][] zTail = new double[X][10000];
    static double[][] NPlan = new double[X][stageNumber];
    static double[][] NPVMeasure = new double[X][stageNumber];
    static double[][] zInOneStageInstant = new double[stageNumber][levelNumberInstant+1];
    static double[][] zTailDecisionInOneStageInstant = new double[stageNumber][levelNumberInstant+1];
    static double[][] netHeadInOneStageInstant = new double[stageNumber][levelNumberInstant+1];
    static double[][] outputNInOneStageInstant = new double[stageNumber][levelNumberInstant+1];
    static double[] vDisInstant = new double[levelNumberInstant+1];
    static double[][] z0Plan = new double[X][Day];
    static double[][] inputAll = new double[X][Day*stageNumber];
    static double[][] inputQAll = new double[X][Day*stageNumber];
    static double[][] NPVPredictAll = new double[X][Day*stageNumber];
    static double[][] NPVMeasureAll = new double[X][Day*stageNumber];
    static double[] vDownFloor = new double[X];
    static double[] zEndYesterday = new double[X];
    static double[] v0 = new double[X];
    static double[] vt = new double[X];
    static double[] box = new double[stageNumber];
    static double M;
    static double P;
    static double P1;
    static double E;
    static double NTurbine;
    static double[] difference = new double[A];
    static double[] NPumpingInstant = new double[stageNumber];
    public static void main(String[] args) throws IOException {
        double start = System.currentTimeMillis();
        for (int ID = 0; ID < X; ID++) {
            /*读取数据*/
            set = readExcel.function1("C://Users//Administrator//Desktop//HPSS-PV//DA-RT//dataset//resourceData" + (ID + 1) + ".xlsx");
            resourceData[ID] = arrayTrans.function2(set);
            z0[ID] = resourceData[ID][2];
            outputQeMax[ID] = resourceData[ID][6];
            NCoefficient[ID] = resourceData[ID][7];
            NFirm[ID] = resourceData[ID][8];
            NLimit[ID] = resourceData[ID][9];
            outputQMax[ID] = resourceData[ID][10];
            QBio[ID] = resourceData[ID][11];
            c0[ID] = NLimit[ID]*0;
            v0[ID] = ChaZhi.function(z0[ID], z[ID], v[ID]);
            set = readExcel.function1("C://Users//Administrator//Desktop//HPSS-PV//DA-RT//dataset//CVaR"+String.format("%.2f",alpha)+".xlsx");
            CVaR = arrayTrans.function2(set);
            set = readExcel.function1("C://Users//Administrator//Desktop//HPSS-PV//DA-RT//dataset//ReCVaR"+String.format("%.2f",alpha)+".xlsx");
            ReCVaR = arrayTrans.function2(set);
            set = readExcel.function1("C://Users//Administrator//Desktop//HPSS-PV//DA-RT//dataset//z" + (ID + 1) + ".xlsx");
            z[ID] = arrayTrans.function2(set);
            set = readExcel.function1("C://Users//Administrator//Desktop//HPSS-PV//DA-RT//dataset//v" + (ID + 1) + ".xlsx");
            v[ID] = arrayTrans.function2(set);
            set = readExcel.function1("C://Users//Administrator//Desktop//HPSS-PV//DA-RT//dataset//q" + (ID + 1) + ".xlsx");
            q[ID] = arrayTrans.function2(set);
            set = readExcel.function1("C://Users//Administrator//Desktop//HPSS-PV//DA-RT//dataset//zTail" + (ID + 1) + ".xlsx");
            zTail[ID] = arrayTrans.function2(set);
            set = readExcel.function1("C://Users//Administrator//Desktop//HPSS-PV//DA-RT//dataset//input" + (ID + 1) + ".xlsx");
            inputAll[ID] = arrayTrans.function2(set);
            inputQAll[ID] = arrayTrans.function2(set);
            set = readExcel.function1("C://Users//Administrator//Desktop//HPSS-PV//DA-RT//dataset//PVPredict" + (ID + 1) + ".xlsx");
            NPVPredictAll[ID] = arrayTrans.function2(set);
            set = readExcel.function1("C://Users//Administrator//Desktop//HPSS-PV//DA-RT//dataset//PVMeasure" + (ID + 1) + ".xlsx");
            NPVMeasureAll[ID] = arrayTrans.function2(set);
            set = readExcel.function1("C://Users//Administrator//Desktop//HPSS-PV//DA-RT//dataset//t.xlsx");
            t = arrayTrans.function2(set);
            set = readExcel.function1("C://Users//Administrator//Desktop//HPSS-PV//DA-RT//dataset//hour.xlsx");
            hour = arrayTrans.function2(set);
            set = readExcel.function1("C://Users//Administrator//Desktop//HPSS-PV//DA-RT//dataset//Priceda.xlsx");
            pricedaAll = arrayTrans.function2(set);
            set = readExcel.function1("C://Users//Administrator//Desktop//HPSS-PV//DA-RT//dataset//Pricert.xlsx");
            pricertAll = arrayTrans.function2(set);
            set = readExcel.function1("C://Users//Administrator//Desktop//HPSS-PV//DA-RT//dataset//z0Plan" + (ID + 1) + ".xlsx");
            z0Plan[ID] = arrayTrans.function2(set);
            vDownFloor[ID] = ChaZhi.function(resourceData[ID][1], z[ID], v[ID]);
        }

        //zEndYesterday[0] = 740.1449730903994;
        //zEndYesterday[1] = 582.5;
        System.out.println("The source data has been entered, the calculation begins");
        for (int day = 0; day < Day; day++) {
            SetOrigin.OriginPumping();
            SetOrigin.Origin2();
            System.out.println("Start " + (day + 1) + " day DA-RT calculation");
            for (int ID = 0; ID < X; ID++) {
                if (!(day == 0)) {
                    z0[ID] = zEndYesterday[ID];
                    v0[ID] = ChaZhi.function(z0[ID], z[ID], v[ID]);
                }
                zt[ID] = z0Plan[ID][day];
                vt[ID] = ChaZhi.function(zt[ID], z[ID], v[ID]);
                for (int i = 0; i < stageNumber; i++) {
                    input[ID][i] = inputAll[ID][24 * day + i];
                    inputQ[ID][i] = inputQAll[ID][24 * day + i];
                    NPVPredict[ID][i] = NPVPredictAll[ID][24 * day + i];
                    NPVMeasure[ID][i] = NPVMeasureAll[ID][24 * day + i];
                    if(ID == 0) {
                        priceda[i] = pricedaAll[24 * day + i];
                        pricert[i] = pricertAll[24 * day + i];
                    }
                }
            }
            for (int i = 0; i <= stageNumber - 1; i++) {
                zEnd[1][i] = z0[1];
                vEnd[1][i] = ChaZhi.function(zEnd[1][i], z[1], v[1]);
                if (!(i == 0)) {
                    vBegin[1][i] = vEnd[1][i - 1];
                } else {
                    vBegin[1][i] = ChaZhi.function(z0[1], z[1], v[1]);
                }
            }

            double[] aim = new double[50];
            for (int a = 1; a <= A; a++) {
                System.out.println("Start " + a + " iteration");
                double[] sum = new double[X];
                for (int ID = 0; ID < X; ID++) {
                    if (ID == 0) {
                        levelNumber = 5400;
                    } else {
                        levelNumber = 500;
                    }
                    Reservoir Reservoir = new Reservoir();
                    /*设置不同时段水位上下限*/
                    double normalLevel;
                    double deadLevel;
                    normalLevel = resourceData[ID][0];
                    deadLevel = resourceData[ID][1];
                    double normalV;
                    double deadV;
                    double dispersedV;
                    double v0;
                    normalV = ChaZhi.function(normalLevel, z[ID], v[ID]);//读取正常蓄水位对应库容
                    deadV = ChaZhi.function(deadLevel, z[ID], v[ID]);//读取死库容
                    v0 = ChaZhi.function(z0[ID], z[ID], v[ID]);
                    dispersedV = (normalV - deadV) / levelNumber;
                    int dis = (int) ((v0 - deadV) * levelNumber / (normalV - deadV));
                    for (int j = 0; j <= levelNumber; j++) {
                        Reservoir.vDis[j] = v0 + (j - dis) * dispersedV;
                    }
                    dispersedV = (normalV - deadV) / levelNumberInstant;
                    int disInstant = (int) ((v0 - deadV) * levelNumberInstant / (normalV - deadV));
                    for (int j = 0; j <= levelNumberInstant; j++) {
                        vDisInstant[j] = v0 + (j - disInstant) * dispersedV;
                    }
                    /*主要计算步骤*/
                    /*第一个发电工况*/
                    System.out.println("Start-" + (ID + 1) + " reservoir-" + (day + 1) + " day-1 hour");
                    Effectiveness.functionV0(ID, 0, levelNumber, v0, Reservoir);
                    /*除最后一时段外，第二个时段至第倒数第二个时段计算*/
                    for (int i = 1; i < stageNumber - 1; i++) {
                        if (i % 24 == 0) {
                            day = (int) Math.ceil(1.0 * (i + 1) / 24);
                            System.out.println("Start-" + (ID + 1) + " reservoir-" + (day + 1) + " day-1 hour");
                        } else {
                            System.out.println("Start-" + (ID + 1) + " reservoir-" + (day + 1) + " day-" + (i % 24 + 1) + " hour");
                        }
                        Effectiveness.functionV(ID, i, levelNumber, Reservoir);
                    }
                    /*求解最后一天第二十四个时段（表示k水位到zt水位）*/
                    System.out.println("Start-" + (ID + 1) + " reservoir-" + (day + 1) + " day-24 hour");
                    double vt = ChaZhi.function(zt[ID], z[ID], v[ID]);
                    Effectiveness.functionVt(ID, stageNumber - 1, levelNumber, vt, Reservoir,dispersedV);

                    /*确定最优路线*/
                    System.out.println( (ID + 1) + " reservoir DA calculation has done, start" + (ID + 1) + "reservoir");
                    int index = 0;
                    for (int i = stageNumber - 1; i >= 0; i--) {
                        if (i == stageNumber - 1) {
                            double max = Integer.MIN_VALUE;
                            for (int k = 0; k <= levelNumber; k++) {
                                if (Reservoir.targetAllStage[i][k] >= max && Reservoir.labelInOneStage[i][k] == 1) {
                                    max = Reservoir.targetAllStage[i][k];
                                    index = k;//对于末时段，变量k是起调水位
                                }
                            }
                            sum[ID] = max;
                            vBegin[ID][i] = Reservoir.vEndInOneStage[i - 1][index];
                            vEnd[ID][i] = Reservoir.vEndInOneStage[i][index];
                            zEnd[ID][i] = Reservoir.zEndInOneStage[i][index];
                            Qe[ID][i] = Reservoir.outputQInOneStage[i][index];
                            N[ID][i] = Reservoir.outputNInOneStage[i][index];
                            W[ID][i] = Reservoir.wasteInOneStage[i][index];
                            Q[ID][i] = Qe[ID][i] + W[ID][i];
                            ZTail[ID][i] = Reservoir.zTailDecisionInOneStage[i][index];
                            NetHead[ID][i] = Reservoir.netHeadInOneStage[i][index];
                            Money[ID][i] = Reservoir.moneyAllStage[i][index] - Reservoir.moneyAllStage[i - 1][index];
                            if (ID == 0) {
                                NPumping[i] = Reservoir.NPumpingInOneStage[i][index];
                                Pumping[i] = Reservoir.PumpingInOneStage[i][index];
                                inputQ[ID + 1][i] = input[ID + 1][i] + Q[ID][i] - Pumping[i];
                            }
                        }
                        if (i == stageNumber - 2) {
                            box[i] = Reservoir.cij[i][index];
                            vBegin[ID][i] = Reservoir.vEndInOneStage[i - 1][Reservoir.cij[i][index]];
                            vEnd[ID][i] = Reservoir.vEndInOneStage[i][index];
                            zEnd[ID][i] = Reservoir.zEndInOneStage[i][index];
                            Qe[ID][i] = Reservoir.outputQInOneStage[i][index];
                            N[ID][i] = Reservoir.outputNInOneStage[i][index];
                            W[ID][i] = Reservoir.wasteInOneStage[i][index];
                            Q[ID][i] = Qe[ID][i] + W[ID][i];
                            ZTail[ID][i] = Reservoir.zTailDecisionInOneStage[i][index];
                            NetHead[ID][i] = Reservoir.netHeadInOneStage[i][index];
                            Money[ID][i] = Reservoir.moneyAllStage[i][index] - Reservoir.moneyAllStage[i - 1][Reservoir.cij[i][index]];
                            if (ID == 0) {
                                NPumping[i] = Reservoir.NPumpingInOneStage[i][index];
                                Pumping[i] = Reservoir.PumpingInOneStage[i][index];
                                inputQ[ID + 1][i] = input[ID + 1][i] + Q[ID][i] - Pumping[i];
                            }
                        }
                        if (i < stageNumber - 2) {
                            int j = Reservoir.cij[i + 1][index];
                            box[i] = j;
                            if (i == 0) {
                                vBegin[ID][i] = ChaZhi.function(z0[ID], z[ID], v[ID]);
                                Money[ID][i] = Reservoir.moneyAllStage[i][j];
                            } else {
                                vBegin[ID][i] = Reservoir.vEndInOneStage[i - 1][Reservoir.cij[i][j]];
                                Money[ID][i] = Reservoir.moneyAllStage[i][j] - Reservoir.moneyAllStage[i - 1][Reservoir.cij[i][j]];
                            }
                            vEnd[ID][i] = Reservoir.vEndInOneStage[i][j];
                            zEnd[ID][i] = Reservoir.zEndInOneStage[i][j];
                            Qe[ID][i] = Reservoir.outputQInOneStage[i][j];
                            N[ID][i] = Reservoir.outputNInOneStage[i][j];
                            W[ID][i] = Reservoir.wasteInOneStage[i][j];
                            Q[ID][i] = Qe[ID][i] + W[ID][i];
                            ZTail[ID][i] = Reservoir.zTailDecisionInOneStage[i][j];
                            NetHead[ID][i] = Reservoir.netHeadInOneStage[i][j];
                            if (ID == 0) {
                                NPumping[i] = Reservoir.NPumpingInOneStage[i][j];
                                Pumping[i] = Reservoir.PumpingInOneStage[i][j];
                                inputQ[ID + 1][i] = input[ID + 1][i] + Q[ID][i] - Pumping[i];
                            }
                            index = j;
                        }
                    }
                    if (zEnd[ID][stageNumber - 1] <= 0) {
                        double ztCorrect = 0;
                        for (int k = 0; k < levelNumber; k++) {
                            if (ztCorrect <= Reservoir.zEndInOneStage[stageNumber - 2][k]) {
                                ztCorrect = Reservoir.zEndInOneStage[stageNumber - 2][k];
                            }
                            zt[ID] = ztCorrect;
                        }
                    }
                    aim[a] = aim[a] + sum[ID];
                }
                double Difference = aim[a - 1] - aim[a];
                if (Difference < 1 && Difference > -1 && !(zEnd[1][stageNumber - 1] == 0) || a == A) {
                    difference[a-1] = aim[a - 1] - aim[a];
                    int count = a;
                    double[] subDifference = Arrays.copyOfRange(difference,0,count);
                    System.out.println("The optimal route is determined, the output results begin, and the convergence process is as follows:" + Arrays.toString(subDifference));
                    break;
                } else {
                    difference[a-1] = aim[a - 1] - aim[a];
                    int count = a;
                    double[] subDifference = Arrays.copyOfRange(difference,0,count);
                    System.out.println("Finish"+ a + " iteration, result:" + aim[a]);
                    System.out.println("The convergence process is:" + Arrays.toString(subDifference));
                }
            }
            /*输出数据*/
            // 表头数据
            String[] head = {"时段", "分时电价", "光照入库径流", "泵站功率", "抽蓄水量", "抽水量", "光照初库容", "光照末库容", "光照计划末水位", "光照计划下泄流量", "光照下泄流量", "光照发电流量", "光照弃水", "光照下游水位", "光照净水头", "光照水轮机出力", "岗坪出力", "梯级总出力", "最大外送通道", "梯级发电收益"
                    , "马马崖入库径流", "可逆式机组出力", "抽蓄水量", "抽水量", "马马崖初库容", "马马崖末库容", "马马崖计划末水位", "马马崖计划下泄流量", "马马崖下泄流量", "马马崖发电流量", "马马崖弃水", "马马崖下游水位", "马马崖净水头", "马马崖出力", "永新出力", "梯级总出力", "最大外送通道", "梯级发电收益"};
            //String file = "C://Users//Administrator//Desktop//HPSS-PV//DA-RT//results//"+String.format("%.2f",alpha)+"//"+String.format("%.0f",NPumpingMax)+"MW//"+lambda+"//" + NPumpingMax + "MW日前计划结果//第" + (day + 1) + "天日前计划结果.xls";
            String file = "C://Users//Administrator//Desktop//HPSS-PV//DA-RT//results//"+String.format("%.2f",alpha)+"//uniCVaR//"+lambda+"//" + NPumpingMax + "MW日前计划结果//第" + (day + 1) + "天日前计划结果.xls";
            String[][] result = new String[stageNumber][38];
            // 各时段数据
            for (int i = stageNumber - 1; i >= 0; i--) {
                for (int ID = 0; ID < X; ID++) {
                    /*
                    NPVPredict[ID][i] = NPVPredictAll[ID][24 * day + i];
                    NPVMeasure[ID][i] = NPVMeasureAll[ID][24 * day + i];
                    if (N[ID][i] + NPVPredict[ID][i] > NLimit[ID]){
                        NPVPredict[ID][i] = Math.max (NLimit[ID] - N[ID][i], 0);
                    }
                     */
                    result[i][0] = String.valueOf(hour[i]);
                    result[i][1] = String.valueOf(priceda[i]);
                    result[i][2 + 18 * ID] = String.valueOf(inputQ[ID][i]);
                    result[i][3 + 18 * ID] = String.valueOf(NPumping[i] * -1);
                    result[i][4 + 18 * ID] = String.valueOf(Pumping[i]);
                    result[i][5 + 18 * ID] = String.valueOf(Pumping[i] * -1);
                    result[i][6 + 18 * ID] = String.valueOf(vBegin[ID][i]);
                    result[i][7 + 18 * ID] = String.valueOf(vEnd[ID][i]);
                    result[i][8 + 18 * ID] = String.valueOf(zEnd[ID][i]);
                    result[i][9 + 18 * ID] = String.valueOf(Q[ID][i]);
                    result[i][10 + 18 * ID] = String.valueOf(Q[ID][i] * -1);
                    result[i][11 + 18 * ID] = String.valueOf(Qe[ID][i]);
                    result[i][12 + 18 * ID] = String.valueOf(W[ID][i]);
                    result[i][13 + 18 * ID] = String.valueOf(ZTail[ID][i]);
                    result[i][14 + 18 * ID] = String.valueOf(NetHead[ID][i]);
                    result[i][15 + 18 * ID] = String.valueOf(N[ID][i]);
                    result[i][16 + 18 * ID] = String.valueOf(NPVPredict[ID][i]);
                    result[i][17 + 18 * ID] = String.valueOf(N[ID][i] + NPVPredict[ID][i]);
                    result[i][18 + 18 * ID] = String.valueOf(NLimit[ID]);
                    result[i][19 + 18 * ID] = String.valueOf(Money[ID][i]);
                }
            }
            // 导出数据
            try {
                CreateExcel.createExcel(file, head, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("The results have been output: 第" + (day + 1) + "天日前计划结果.xlsx");

            //实时调度计算
            if (stageNumber >= 0) System.arraycopy(NPumping, 0, NPumpingInstant, 0, stageNumber);
            for (int ID = 0; ID < X; ID++) {
                for (int i = 0; i < stageNumber; i++) {
                    NPlan[ID][i] = N[ID][i] - NPVMeasure[ID][i] + NPVPredict[ID][i];
                    if (NPlan[ID][i] < QBio[ID] * NetHead[ID][i] * NCoefficient[ID]/1000) {
                        NPlan[ID][i] = QBio[ID] * NetHead[ID][i] * NCoefficient[ID] / 1000;
                        if (NPVPredict[ID][i] + N[ID][i] < NPVMeasure[ID][i] + NPlan[ID][i]) {
                            NPumpingInstant[i] = NPumpingInstant[i] - (NPVMeasure[ID][i] + NPlan[ID][i] - NPVPredict[ID][i] - N[ID][i]);
                        }
                        if (NPumpingInstant[i] < 0) {
                            NPumping[i] = Math.min(NPumping[i] - NPumpingInstant[i],NPumpingMax);
                            NPumpingInstant[i] = 0;
                        }
                    }
                }
            }

            for (int i = 0; i <= stageNumber - 1; i++) {
            double[] aimQ = new double[50];
            for (int a = 1; a <= A; a++) {
                System.out.println("Start " +(i+1)+" hour "+ a + " iteration");
                double[] sum = new double[X];
                for (int ID = 0; ID < X; ID++) {
                    /*设置不同时段水位上下限*/
                    double normalLevel;
                    double deadLevel;
                    normalLevel = resourceData[ID][0];
                    deadLevel = resourceData[ID][1];
                    double normalV;
                    double deadV;
                    double dispersedV;
                    double v0;
                    normalV = ChaZhi.function(normalLevel, z[ID], v[ID]);//读取正常蓄水位对应库容
                    deadV = ChaZhi.function(deadLevel, z[ID], v[ID]);//读取死库容
                    v0 = ChaZhi.function(z0[ID], z[ID], v[ID]);
                    dispersedV = (normalV - deadV) / levelNumberInstant;
                    int dis = (int) ((v0 - deadV) * levelNumberInstant / (normalV - deadV));
                    for (int j = 0; j <= levelNumberInstant; j++) {
                        vDisInstant[j] = v0 + (j - dis) * dispersedV;
                    }

                    if(i==0) {
                        /*第一个发电工况*/
                        System.out.println("Start " + (ID + 1) + " reservoir-" + (day + 1) + " day- 1 hour RT");
                        Instant.functionV0(ID, 0, levelNumberInstant, v0);
                        /*其余发电工况*/
                    }else {
                        if (i % 24 == 0) {
                            day = (int) Math.ceil(1.0 * (i + 1) / 24);
                            System.out.println("Start " + (ID + 1) + " reservoir-" + (day + 1) + " day- 1 hour RT");
                        } else {
                            System.out.println("Start " + (ID + 1) + " reservoir-" + (day + 1) + " day-" + (i % 24 + 1) + " hour RT");
                        }
                        Instant.functionV(ID, i, levelNumberInstant);
                    }

                    if (!(ID == 0)) {
                        inputQ[ID][i] = input[ID][i] + Q[ID - 1][i];
                    }
                    sum[ID] = sum[ID] + Q[ID][i];
                    aimQ[a] = aimQ[a] + sum[ID];
                }
                double Difference = aimQ[a - 1] - aimQ[a];
                if (Difference < 0.1 && Difference > -0.1) {
                    System.out.println("The optimal route of "+(i+1)+" hour is determined");
                    zEndYesterday[0] = zEnd[0][stageNumber - 1];
                    zEndYesterday[1] = zEnd[1][stageNumber - 1];
                    for (int ID = 0; ID < X; ID++) {
                        M = M + Money[ID][i];
                        E = E + (N[ID][i] + NPVPredict[ID][i]) * 1000;
                        if (NPumping[i] > 0 && ID == 0) {
                            P = (NPumping[i] / NPumpingMax + P) / 2;
                        }
                        if (ID == 0) {
                            NTurbine = NTurbine + NPumping[i] * 1000;
                            P1 = (NPumping[i] / NPumpingMax + P1) / 2;
                        }
                    }
                    break;
                } else {
                    System.out.println("Finish"+(i+1)+" hour "+ a + " iteration，result is: " + aimQ[a]);
                }
            }
        }

            /*输出数据*/
            // 表头数据
            String[] headInstant = {"时段", "分时电价", "光照入库径流", "泵站总功率","泵站购电功率", "抽蓄水量", "抽水量", "光照初库容", "光照末库容", "光照实际末水位", "光照实际下泄流量", "光照下泄流量", "光照发电流量", "光照弃水", "光照下游水位", "光照净水头", "光照水轮机出力", "岗坪出力", "梯级总出力", "最大外送通道", "梯级发电收益"
                    ,"","马马崖入库径流", "泵站总功率","泵站购电功率", "抽蓄水量", "抽水量", "马马崖初库容", "马马崖末库容", "马马崖实际末水位", "马马崖实际下泄流量", "马马崖下泄流量", "马马崖发电流量", "马马崖弃水", "马马崖下游水位", "马马崖净水头", "马马崖出力", "永新出力", "梯级总出力", "最大外送通道", "梯级发电收益"};
            //String fileInstant = "C://Users//Administrator//Desktop//HPSS-PV//DA-RT//results//"+String.format("%.2f",alpha)+"//"+String.format("%.0f",NPumpingMax)+"MW//"+lambda+"//"+ NPumpingMax +"MW实时调度结果//第"+(day + 1)+"天实时调度结果.xls";
            String fileInstant = "C://Users//Administrator//Desktop//HPSS-PV//DA-RT//results//"+String.format("%.2f",alpha)+"//uniCVaR//"+lambda+"//"+ NPumpingMax +"MW实时调度结果//第"+(day + 1)+"天实时调度结果.xls";
            String[][] resultInstant = new String[stageNumber][41];
            // 各时段数据
            for (int i = stageNumber - 1; i >= 0; i--) {
                for (int ID = 0; ID < X; ID++) {
                    resultInstant[i][0] = String.valueOf(hour[i]);
                    resultInstant[i][1] = String.valueOf(pricert[i]);
                    resultInstant[i][2 + 20 * ID] = String.valueOf(inputQ[ID][i]);
                    resultInstant[i][3 + 20 * ID] = String.valueOf(NPumping[i] * -1);
                    resultInstant[i][4 + 20 * ID] = String.valueOf(NPumpingInstant[i] * -1);
                    resultInstant[i][5 + 20 * ID] = String.valueOf(Pumping[i]);
                    resultInstant[i][6 + 20 * ID] = String.valueOf(Pumping[i] * -1);
                    resultInstant[i][7 + 20 * ID] = String.valueOf(vBegin[ID][i]);
                    resultInstant[i][8 + 20 * ID] = String.valueOf(vEnd[ID][i]);
                    resultInstant[i][9 + 20 * ID] = String.valueOf(zEnd[ID][i]);
                    resultInstant[i][10 + 20 * ID] = String.valueOf(Q[ID][i]);
                    resultInstant[i][11 + 20 * ID] = String.valueOf(Q[ID][i] * -1);
                    resultInstant[i][12 + 20 * ID] = String.valueOf(Qe[ID][i]);
                    resultInstant[i][13 + 20 * ID] = String.valueOf(W[ID][i]);
                    resultInstant[i][14 + 20 * ID] = String.valueOf(ZTail[ID][i]);
                    resultInstant[i][15 + 20 * ID] = String.valueOf(NetHead[ID][i]);
                    resultInstant[i][16 + 20 * ID] = String.valueOf(N[ID][i]);
                    resultInstant[i][17 + 20 * ID] = String.valueOf(NPVMeasure[ID][i]);
                    resultInstant[i][18 + 20 * ID] = String.valueOf(N[ID][i] + NPVMeasure[ID][i]);
                    resultInstant[i][19 + 20 * ID] = String.valueOf(NLimit[ID]);
                    resultInstant[i][20 + 20 * ID] = String.valueOf(Money[ID][i]);
                }
            }
            // 导出数据
            try {
                CreateExcel.createExcel(fileInstant, headInstant, resultInstant);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("The results have been output: 第"+(day+1)+"天实时调度结果.xlsx");
        }
        double end = System.currentTimeMillis();
        DecimalFormat df = new DecimalFormat("#.00");
        double s = (end - start) / 1000;
        double minute = (end - start) / 60000;
        //System.out.println("for循环耗时" + df.format(s) + "秒, 即" + df.format(minute) + "分钟");
        //System.out.println("泵站最大功率为"+ NPumpingMax +",风险厌恶因子为"+ lambda +"时，该年总发电量为"+ E +"，泵站耗电量为"+NTurbine+"年利用小时数为"+(NTurbine/(1000*NPumpingMax))+",净电总收益为"+ M +"元，泵站利用率为" + P + ",年均泵站利用率为" + P1);
    }
}

