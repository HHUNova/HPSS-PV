package Sensitive;

import java.io.IOException;
import java.util.*;

public class Main {
    static double[] GZMoneyActual = new double[24];
    static double[] MMYMoneyActual = new double[24];
    static double[] GZMoneySchedule = new double[24];
    static double[] MMYMoneySchedule = new double[24];
    static double[] GZMoneyErr = new double[24];
    static double[] MMYMoneyErr = new double[24];
    static double[] GZNPVPre = new double[24];
    static double[] MMYNPVPre = new double[24];
    static double[] DayaheadPrice = new double[24];
    static int InstalledCapacity = 800;
    static HashMap<Integer,String> SourceSet = new HashMap<>();
    static ArrayList<Double> set = new ArrayList<>();

    public static void main(String[] args) {
        double[] GZDailyMoneySchedule = new double[365];
        double[] MMYDailyMoneySchedule = new double[365];
        double[] GZDailyMoneyActual = new double[365];
        double[] MMYDailyMoneyActual = new double[365];
        double[] GZDailyMoneyErr = new double[365];
        double[] MMYDailyMoneyErr = new double[365];
        double[] sysDailyMoneyErr = new double[365];
        double GZSumMoneyErr = 0;
        double MMYSumMoneyErr = 0;
        double sysSumMoneyErr = 0;
        //for (int k = 0; k < 1; k=k+1) {
            //if (k == 0) {
                //GZSumMoneyErr = 0;
                //MMYSumMoneyErr = 0;
                //double alpha = (double) k / 10;
        double alpha = 0.1;
        for (int day = 0; day < 365; day++) {
            set = ReadExcel.function1("C://Users//Administrator//Desktop//混合式抽蓄-光伏//日前计划及实时调度//市场化//0.95//" + InstalledCapacity + "MW//" + alpha + "//" + String.format("%.1f",(double) InstalledCapacity) + "MW日前计划结果//第" + (day + 1) + "天日前计划结果.xls", 19);
            GZMoneySchedule = ArrayTrans.function2(set);
            set = ReadExcel.function1("C://Users//Administrator//Desktop//混合式抽蓄-光伏//日前计划及实时调度//市场化//0.95//" + InstalledCapacity + "MW//" + alpha + "//" + String.format("%.1f",(double) InstalledCapacity) + "MW日前计划结果//第" + (day + 1) + "天日前计划结果.xls", 37);
            MMYMoneySchedule = ArrayTrans.function2(set);
            set = ReadExcel.function1("C://Users//Administrator//Desktop//混合式抽蓄-光伏//日前计划及实时调度//市场化//0.95//" + InstalledCapacity + "MW//" + alpha + "//" + String.format("%.1f",(double) InstalledCapacity) + "MW日前计划结果//第" + (day + 1) + "天日前计划结果.xls", 16);
            GZNPVPre = ArrayTrans.function2(set);
            set = ReadExcel.function1("C://Users//Administrator//Desktop//混合式抽蓄-光伏//日前计划及实时调度//市场化//0.95//" + InstalledCapacity + "MW//" + alpha + "//" + String.format("%.1f",(double) InstalledCapacity) + "MW日前计划结果//第" + (day + 1) + "天日前计划结果.xls", 34);
            MMYNPVPre = ArrayTrans.function2(set);
            set = ReadExcel.function1("C://Users//Administrator//Desktop//混合式抽蓄-光伏//日前计划及实时调度//市场化//0.95//" + InstalledCapacity + "MW//" + alpha + "//" + String.format("%.1f",(double) InstalledCapacity) + "MW日前计划结果//第" + (day + 1) + "天日前计划结果.xls", 1);
            DayaheadPrice = ArrayTrans.function2(set);
            GZDailyMoneySchedule[day] = Arrays.stream(GZMoneySchedule).sum();
            MMYDailyMoneySchedule[day] = Arrays.stream(MMYMoneySchedule).sum();
            set = ReadExcel.function1("C://Users//Administrator//Desktop//混合式抽蓄-光伏//日前计划及实时调度//市场化//0.95//" + InstalledCapacity + "MW//" + alpha + "//" + String.format("%.1f",(double) InstalledCapacity) + "MW实时调度结果//第" + (day + 1) + "天实时调度结果.xls", 20);
            GZMoneyActual = ArrayTrans.function2(set);
            set = ReadExcel.function1("C://Users//Administrator//Desktop//混合式抽蓄-光伏//日前计划及实时调度//市场化//0.95//" + InstalledCapacity + "MW//" + alpha + "//" + String.format("%.1f",(double) InstalledCapacity) + "MW实时调度结果//第" + (day + 1) + "天实时调度结果.xls", 40);
            MMYMoneyActual = ArrayTrans.function2(set);
            GZDailyMoneyActual[day] = Arrays.stream(GZMoneyActual).sum();
            MMYDailyMoneyActual[day] = Arrays.stream(MMYMoneyActual).sum();
            for (int h = 0; h < 24; h++) {
                //GZMoneyErr[h] = GZMoneyActual[h] - GZMoneySchedule[h] - GZNPVPre[h]*DayaheadPrice[h]*1000;
                //MMYMoneyErr[h] = MMYMoneyActual[h] - MMYMoneySchedule[h] - MMYNPVPre[h]*DayaheadPrice[h]*1000;
                GZMoneyErr[h] = GZMoneyActual[h] - GZMoneySchedule[h];
                MMYMoneyErr[h] = MMYMoneyActual[h] - MMYMoneySchedule[h];
            }
            GZDailyMoneyErr[day] = Arrays.stream(GZMoneyErr).sum();
            MMYDailyMoneyErr[day] = Arrays.stream(MMYMoneyErr).sum();
            sysDailyMoneyErr[day] = GZDailyMoneyErr[day] + MMYDailyMoneyErr[day];
            GZSumMoneyErr = GZSumMoneyErr + Arrays.stream(GZMoneyErr).sum();
            MMYSumMoneyErr = MMYSumMoneyErr + Arrays.stream(MMYMoneyErr).sum();
            sysSumMoneyErr = GZSumMoneyErr + MMYSumMoneyErr;
        }
        Cal_sta cal = new Cal_sta();
        double GZVarSchedule = cal.POP_Variance(GZDailyMoneySchedule);
        double MMYVarSchedule = cal.POP_Variance(MMYDailyMoneySchedule);
        double GZVarActual = cal.POP_Variance(GZDailyMoneyActual);
        double MMYVarActual = cal.POP_Variance(MMYDailyMoneyActual);
        double GZVarErr = cal.POP_Variance(GZDailyMoneyErr);
        double MMYVarErr = cal.POP_Variance(MMYDailyMoneyErr);
        double SysVarErr = cal.POP_STD_dev(sysDailyMoneyErr);
        double SysSumMoneyActual = Arrays.stream(GZDailyMoneyActual).sum()+Arrays.stream(MMYDailyMoneyActual).sum();
        System.out.println("风险厌恶水平为：" + alpha);
        System.out.println("系统实际收益为：" + SysSumMoneyActual);
        //System.out.println("GZ计划收益方差为：" + GZVarSchedule);
        //System.out.println("MMY计划收益方差为：" + MMYVarSchedule);
        //System.out.println("GZ实际收益方差为：" + GZVarActual);
        //System.out.println("MMY实际收益方差为：" + MMYVarActual);
        //System.out.println("GZ收益偏差方差为：" + GZVarErr);
        //System.out.println("MMY收益偏差方差为：" + MMYVarErr);
        //System.out.println("GZ总收益偏差为：" + GZSumMoneyErr);
        //System.out.println("MMY总收益偏差为：" + MMYSumMoneyErr);
        System.out.println("系统总收益偏差为：" + sysSumMoneyErr);
        System.out.println("系统收益偏差标准差为：" + SysVarErr);
        System.out.println(" ");
            //}
        //}
    }
}
