package com.lee.produce.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: excel 组件
 * @Author: Lee
 * @DateTime: 2018/12/10 10:20
 */
public class ExcelUtil {

    /**
     * Excel 2003
     */
    private static final String XLS = "xls";
    /**
     * Excel 2007
     */
    private static final String XLSX = "xlsx";

    public static List<List> exportFromExcel(InputStream in, String extensionName, int sheetNum) throws IOException {
        Workbook workbook = null;

        if (extensionName.toLowerCase().equals(XLS)) {
            workbook = new HSSFWorkbook(in);
        } else if (extensionName.toLowerCase().equals(XLSX)) {
            workbook = new XSSFWorkbook(in);
        }
        return exportListFromExcel(workbook, sheetNum);
    }

    private static List<List> exportListFromExcel(Workbook workbook,
                                                  int sheetNum) {

        Sheet sheet = workbook.getSheetAt(sheetNum);

        // 解析公式结果
        FormulaEvaluator evaluator = workbook.getCreationHelper()
                .createFormulaEvaluator();

        List<List> finalList = new ArrayList<>();

        int minRowIx = sheet.getFirstRowNum();
        int maxRowIx = sheet.getLastRowNum();
        for (int rowIx = minRowIx; rowIx <= maxRowIx; rowIx++) {
            Row row = sheet.getRow(rowIx);
            if (row == null) {
                break;
            }
            List list = new ArrayList();

            short minColIx = 0;
            short maxColIx = row.getLastCellNum();
            boolean flag = false;
            for (short colIx = minColIx; colIx <= maxColIx; colIx++) {
                Cell cell = row.getCell(new Integer(colIx));
                CellValue cellValue = evaluator.evaluate(cell);
                if (cellValue == null) {
                    list.add(null);
                    continue;
                }
                flag = true;
                // 经过公式解析，最后只存在Boolean、Numeric和String三种数据类型，此外就是Error了
                switch (cell.getCellTypeEnum()) {
                    case BOOLEAN:
                        list.add(cellValue.getBooleanValue());
                        break;
                    case NUMERIC:
                        // 这里的日期类型会被转换为数字类型，需要判别后区分处理
                        if (DateUtil.isCellDateFormatted(cell)) {
                            list.add(cell.getDateCellValue());
                        } else {
                            DecimalFormat df = new DecimalFormat("0.###############");
                            String r = df.format(cellValue.getNumberValue());
                            list.add(r);
                        }
                        break;
                    case STRING:
                        list.add(cellValue.getStringValue());
                        break;
                    case FORMULA:
                        break;
                    case BLANK:
                        break;
                    case ERROR:
                        break;
                    default:
                        break;
                }
            }
            if (flag) {
                finalList.add(list);
            }
        }
        return finalList;
    }
}

