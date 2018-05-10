package com.tang.fileParse;

import com.tang.constants.FileTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-04-11 10:25
 **/
@Slf4j
public class ParseExcel {

    /**
     * @param voClass
     * @param is
     * @param fileName
     * @param queue
     * @Title: importDataFromExcel
     * @Description: 将sheet中的数据保存到list中，
     * 1、调用此方法时，vo的属性个数必须和excel文件每行数据的列数相同且一一对应，vo的所有属性都为String
     * 2、在action调用此方法时，需声明
     * private File excelFile;上传的文件
     * private String excelFileName;原始文件的文件名
     * 3、页面的file控件name需对应File的文件名
     */
    public static void parse(Class voClass, InputStream is, String fileName, ArrayBlockingQueue<Object> queue) {
        try {
            //创建工作簿
            Workbook workbook = createWorkbook(is, fileName);
            //创建工作表sheet
            Sheet sheet = getSheet(workbook, 0);
            //获取sheet中数据的行数
            int rows = sheet.getPhysicalNumberOfRows();
            //获取表头单元格个数
            int cells = sheet.getRow(0).getPhysicalNumberOfCells();
            //利用反射，给JavaBean的属性进行赋值
            Field[] fields = voClass.getDeclaredFields();
            //第一行为标题栏，从第二行开始取数据
            for (int i = 1; i < rows; i++) {
                //重新创建一个vo对象
                Object vo = voClass.getConstructor(new Class[]{}).newInstance(new Object[]{});
                Row row = sheet.getRow(i);
                int index = 0;
                while (index < cells) {
                    Cell cell = row.getCell(index);
                    if (null == cell) {
                        cell = row.createCell(index);
                    }
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String value = null == cell.getStringCellValue() ? "" : cell.getStringCellValue().trim();
                    Field field = fields[index];
                    String fieldName = field.getName();
                    String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method setMethod = voClass.getMethod(methodName, new Class[]{String.class});
                    setMethod.invoke(vo, new Object[]{value});
                    index++;
                }
                queue.put(vo);
            }
        } catch (Exception e) {
            log.error("解析EXCEL失败：" + e.getMessage());
        } finally {
            try {
                //关闭流
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param is
     * @param excelFileName
     * @return Workbook
     * @Title: createWorkbook
     * @Description: 判断excel文件后缀名，生成不同的workbook
     */
    public static Workbook createWorkbook(InputStream is, String excelFileName) throws IOException {
        if (excelFileName.endsWith(FileTypeEnum.EXCEL_XLS.getSuffix())) {
            return new HSSFWorkbook(is);
        } else if (excelFileName.endsWith(FileTypeEnum.EXCEL_XLSX.getSuffix())) {
            return new XSSFWorkbook(is);
        }
        return null;
    }

    /**
     * @param workbook
     * @return Sheet
     * @Title: getSheet
     * @Description: 根据sheet索引号获取对应的sheet
     */
    public static Sheet getSheet(Workbook workbook, int sheetIndex) {
        return workbook.getSheetAt(sheetIndex);
    }

}
