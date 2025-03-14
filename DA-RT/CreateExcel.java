/**
 * @author: Liang Qiao
 * @School: Hohai University
 * @操作说明：动态规划求解光照-马马崖水电站优化调度问题之表格创建
 *input：(文件位置，表头，实验结果)
 * output: 调度结果表格
 */
package org.example;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.FileOutputStream;

public class CreateExcel {
    public static void createExcel(String url, String[] head, String[][] result) throws Exception {

		/* url:要导出创建的excel文档的路径位置及名字
		 	eg:"文档路径...\\文档名.xls"

		 	head[]:表格首行标题
		 	result[][]:以二维数组形式存放的表格文档内容
		 */


        FileOutputStream os = new FileOutputStream(url);  //文件流

        //创建工作薄
        WritableWorkbook workbook = Workbook.createWorkbook(os);
        //创建新的一页
        WritableSheet sheet = workbook.createSheet("First Sheet", 0);
        //创建要显示的具体内容

        //首行标题内容
        for (int i = 0; i < head.length; i++) {
            jxl.write.Label l1 = new jxl.write.Label(i, 0, head[i]);
            sheet.addCell(l1);
        }
        //内容
        for (int i = 0; i < result.length; i++) {

            for (int j = 0; j < result[0].length; j++) {
                //	sheet.setColumnView(j, 28);
                jxl.write.Label l1 = new jxl.write.Label(j, i + 1, result[i][j]); // i j注意哈
                sheet.addCell(l1);
            }
        }
        workbook.write();
        workbook.close();
        os.close();
    }
}
