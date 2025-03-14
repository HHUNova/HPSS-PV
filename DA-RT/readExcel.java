/**
 * @author: Liang Qiao
 * @School: Hohai University
 * @操作说明：动态规划求解光照-马马崖水电站优化调度问题之读取文件数据
 * input:(文件位置)
 * output:动态数组
 *
 */
package org.example;


import jxl.read.biff.File;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class readExcel {
    public static ArrayList<Double> function1(String s) throws IOException {

        ArrayList<Double> table = new ArrayList<>();
        // 获得工作簿
        try (FileInputStream fileInputStream = new FileInputStream(s);// 将excel模板文件转为输入流
             XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)){// 创建excel模板workbook，
            // 获得工作表个数
            int sheetCount = workbook.getNumberOfSheets();
            // 遍历工作表
            for (int i = 0; i < sheetCount; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                // 获得行数
                int rows = sheet.getLastRowNum() + 1;
                // 获得列数，先获得一行，在得到该行列数
                Row tmp = sheet.getRow(0);
                if (tmp == null) {
                    continue;
                }
                int cols = tmp.getPhysicalNumberOfCells();
                // 读取数据
                for (int j = 0; j < rows; j++) {
                    // 循环读取每一个格
                    Row row = sheet.getRow(j);
                    // row.getPhysicalNumberOfCells()获取总的列数
                    for (int index = 0; index < cols; index++) {
                        Cell cell = row.getCell(index);
                        // 转换为浮点类型
                        cell.setCellType(CellType.STRING);
                        double cellValue = Double.parseDouble(cell.getStringCellValue());
                        table.add(cellValue);
                    }
                }
            }
        }
        return table;
    }
}
