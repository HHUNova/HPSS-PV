package Sensitive;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ReadExcel {

    public static ArrayList<Double> function1(String s, int k) {
        ArrayList<Double> table = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(s);// 将excel模板文件转为输入流
             HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fileInputStream)){// 创建excel模板workbook，
            //获取工作簿下sheet的个数
            int sheetNum = hssfWorkbook.getNumberOfSheets();
            //遍历工作簿中的所有数据
            for(int i = 0;i<sheetNum;i++) {
                Sheet sheet = hssfWorkbook.getSheetAt(i);
                // 获得行数
                int rows = sheet.getLastRowNum() + 1;
                // 获得列数，先获得一行，在得到该行列数
                Row tmp = sheet.getRow(0);
                if (tmp == null) {
                    continue;
                }
                int cols = tmp.getPhysicalNumberOfCells();
                // 读取数据
                for (int j = 1; j < rows; j++) {
                    // 循环读取每一个格
                    Row row = sheet.getRow(j);
                    // row.getPhysicalNumberOfCells()获取总的列数
                    for (int index = k; index < k+1; index++) {
                        Cell cell = row.getCell(index);
                        // 转换为浮点类型
                        DataFormatter dataFormatter = new DataFormatter();
                        String cellContent = dataFormatter.formatCellValue(cell);
                        double cellValue = Double.parseDouble(cellContent);
                        table.add(cellValue);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }
}