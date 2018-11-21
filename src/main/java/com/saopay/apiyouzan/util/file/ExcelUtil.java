package com.saopay.apiyouzan.util.file;

import com.alibaba.fastjson.JSON;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

    private static Log log = LogFactory.getLog(ExcelUtil.class);

    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 验证EXCEL文件
     */
    public static boolean validateExcel(String filePath) {
        if (filePath == null || !(ExcelUtil.isExcel2003(filePath) || ExcelUtil
            .isExcel2007(filePath))) {
            return false;
        }
        return true;
    }

    /**
     * 根据excel里面的内容读取客户信息
     *
     * @param is 输入流
     * @param fileName 文件名
     */
    public static List<List<Object>> getExcelInfo(InputStream is, String fileName) {
        List<List<Object>> customerList = null;
        Workbook wb = null;
        try {
            if (ExcelUtil.isExcel2003(fileName)) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }
            customerList = readExcelValue(wb);
        } catch (IOException e) {
            log.error("excel读取错误，或者创建错误");
        } finally {
            try {
                if (wb != null) {
                    wb.close();
                }
                is.close();
            } catch (IOException e) {
                log.error("文件流关闭失败");
            }
        }
        return customerList;
    }

    public static void main(String[] args) {
        try {
            InputStream is = new FileInputStream("C:\\Users\\24020\\Desktop/nation.xls");
            List<List<Object>> lists = getExcelInfo(is, "nation.xlsx");
            List<Map<String, String>> maps = new ArrayList<>();

            lists.forEach(list -> {
                Map<String, String> map = new HashMap<>();
                map.put("code", list.get(0).toString());
                map.put("nation", list.get(1).toString());
                maps.add(map);
            });

            System.out.println(JSON.toJSONString(maps));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取Excel里面客户的信息
     */
    @SuppressWarnings("deprecation")
    public static List<List<Object>> readExcelValue(Workbook wb) {
        int totalRows = 0;
        int totalCells = 0;
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows >= 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<List<Object>> customerList = new ArrayList<List<Object>>();
        for (int r = 1; r < totalRows; r++) {
            List<Object> list = new ArrayList<Object>();
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    list.add(cell.getStringCellValue());
                }
            }
            customerList.add(list);
        }
        return customerList;
    }

    /**
     * 根据excel里面的内容读取客户信息
     *
     * @param is 输入流
     */
    public static List<List<String>> getExcelInfoStr(InputStream is) {
        List<List<String>> customerList = null;
        Workbook wb = null;
        try {
            wb = new HSSFWorkbook(is);
            if (wb != null) {
                customerList = readExcelValueStr(wb);
            }
        } catch (IOException e) {
            log.error("excel读取错误，或者创建错误");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                log.error("文件流关闭失败");
            }
        }
        return customerList;
    }

    /**
     * 读取Excel里面客户的信息
     */
    @SuppressWarnings("deprecation")
    public static List<List<String>> readExcelValueStr(Workbook wb) {
        int totalRows = 0;
        int totalCells = 0;
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows >= 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<List<String>> customerList = new ArrayList<List<String>>();
        for (int r = 1; r < totalRows; r++) {
            List<String> list = new ArrayList<String>();
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    cell.setCellType(CellType.STRING);
                    list.add(cell.getStringCellValue());
                }
            }
            customerList.add(list);
        }
        return customerList;
    }

    /**
     * 读取Excel里面客户的信息,包含空列
     *
     * @param wb excel内容
     * @param sheetIndex 第几个sheet　0开始
     * @param rowStartIndex 从第几行开始 0开始
     */
    @SuppressWarnings("deprecation")
    public static List<List<String>> readExcelValueStrWithNull(Workbook wb, final int sheetIndex,
        int rowStartIndex) {
        int totalRows = 0;
        int totalCells = 0;
        // 得到sheetIndex个shell
        Sheet sheet = wb.getSheetAt(sheetIndex);
        // 得到Excel的行数
        totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows >= 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<List<String>> customerList = new ArrayList<List<String>>();
        for (int r = rowStartIndex; r < totalRows; r++) {
            List<String> list = new ArrayList<String>();
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null == cell) {
                    list.add(null);
                } else {
                    cell.setCellType(CellType.STRING);
                    list.add(cell.getStringCellValue());
                }

            }
            customerList.add(list);
        }
        return customerList;
    }

    /**
     * 读取Excel里面客户的信息,包含空列
     *
     * @param sheet 第几个sheet　0开始
     * @param rowStartIndex 从第几行开始 0开始
     */
    public static List<List<String>> getExcelInfoStrWithNull(InputStream is, String filename,
        int sheet, int rowStartIndex) {
        List<List<String>> customerList = null;
        Workbook wb;
        try {
            if (validateExcel(filename)) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }
            if (wb != null) {
                customerList = readExcelValueStrWithNull(wb, sheet, rowStartIndex);
            }
        } catch (IOException e) {
            log.error("excel读取错误，或者创建错误");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                log.error("文件流关闭失败");
            }
        }
        return customerList;
    }

    public static List<List<String>> getExcelInfoStrWithNull(InputStream is, String filename) {
        int sheetIndex = 0;
        int rowStartIndex = 0;
        return getExcelInfoStrWithNull(is, filename, sheetIndex, rowStartIndex);
    }


    public static String getDataStr(List<String> dataParameterValues, String name,
        List<String> dataParameterNames) {
        return getData(dataParameterNames, dataParameterValues, name, StringUtils.EMPTY);
    }

    public static Double getDataDouble(List<String> dataParameterValues, String name,
        List<String> dataParameterNames) {
        return getData(dataParameterNames, dataParameterValues, name, 0d);
    }

    public static Integer getDataInt(List<String> dataParameterValues, String name,
        List<String> dataParameterNames) {
        return getData(dataParameterNames, dataParameterValues, name, 0);
    }

    public static <V> V getData(List<String> dataParameterNames,
        List<String> dataParameterValues, String name, V calss) {
        Integer index = null;
        for (int i = 0; i < dataParameterNames.size(); i++) {
            if (StringUtils.isNotBlank(dataParameterNames.get(i)) && name
                .equals(dataParameterNames.get(i).trim())) {
                index = i;
                break;
            }
        }
        if (index != null && StringUtils.isNotBlank(dataParameterValues.get(index))) {
            if (calss instanceof Double) {
                try {
                    return (V) Double.valueOf(dataParameterValues.get(index));
                } catch (NumberFormatException e) {
                    return calss;
                }
            }
            if (calss instanceof Integer) {
                try {
                    return (V) Integer.valueOf(dataParameterValues.get(index));
                } catch (NumberFormatException e) {
                    return calss;
                }
            }
            return (V) dataParameterValues.get(index);
        } else {
            return calss;
        }
    }


}
