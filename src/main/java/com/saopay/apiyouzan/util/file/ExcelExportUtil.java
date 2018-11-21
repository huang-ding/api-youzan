package com.saopay.apiyouzan.util.file;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author huangding
 * @description
 * @date 2018/11/16 16:24
 */
@Slf4j
public class ExcelExportUtil {

    /**
     * 描述：基于excel模板文件导出（xls文件）
     *
     * @param dataConfigRowIndex 数据配置行索引(map：key->excel：columnPos)
     * @param tempFile 模板完整路径
     * @param exportFileName 导出文件excel文件名
     * @param autoRowHeight 是否自动行高
     */
    @SuppressWarnings("unchecked")
    public static void exportByBeans(int dataConfigRowIndex, File tempFile,
        String exportFileName,
        boolean autoRowHeight, String queryCondition, List<?> list, HttpServletRequest request,
        HttpServletResponse response) {
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        try {
            for (Object obj : list) {
                Map<String, Object> data = PropertyUtils.describe(obj);
                datas.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        exportByMaps(dataConfigRowIndex, tempFile, exportFileName, autoRowHeight,
            queryCondition, datas, request,
            response);
    }

    /**
     * 描述：基于excel模板文件导出（XLS文件）
     *
     * @param dataConfigRowIndex 数据配置行索引(map：key->excel：columnPos)
     * @param tempFile 模板
     * @param exportFileName 导出文件excel文件名
     * @param datas 填充到模板的数据 类型ListList<Map<?, ?>>
     * @param autoRowHeight 是否自动行高
     */
    public static void exportByMaps(int dataConfigRowIndex, File tempFile,
        String exportFileName,
        boolean autoRowHeight, String queryCondition, List<Map<String, Object>> datas,
        HttpServletRequest request,
        HttpServletResponse response) {
        XSSFWorkbook workbook = null;
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            workbook = new XSSFWorkbook(new FileInputStream(tempFile));
            XSSFSheet sheet = workbook.getSheetAt(0);
            List<Object[]> cellColumns = getDataConfig(dataConfigRowIndex, sheet, true);
            if (null != queryCondition) {// 查询内容填充
                for (int i = 0; i < dataConfigRowIndex; i++) {
                    XSSFRow row = sheet.getRow(i);
                    boolean hasSet = false;
                    if (null != row) {
                        for (int j = 0; j < cellColumns.size(); j++) {
                            XSSFCell cell = row.getCell(j);
                            if (null != cell) {
                                if ("queryCondition".equals(cell.getStringCellValue())) {
                                    setCellObjectValue(queryCondition, cell);
                                    hasSet = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (hasSet) {
                        break;
                    }
                }
            }
            Float dataRowHeight = null;
            if (null != datas && !datas.isEmpty()) {
                for (int i = 0; i < datas.size(); i++) {
                    XSSFRow dataRow = null;
                    if (0 == i) {
                        dataRow = sheet.getRow((short) i + dataConfigRowIndex);
                        if (!autoRowHeight) {
                            dataRowHeight = dataRow.getHeightInPoints();
                        }
                    } else {
                        dataRow = sheet.createRow((short) i + dataConfigRowIndex);
                        if (!autoRowHeight) {
                            dataRow.setHeightInPoints(dataRowHeight);
                        }
                    }
                    Map<String, Object> data = datas.get(i);
                    if (null == data.get("index")) {
                        data.put("index", i + 1);
                    }
                    for (int j = 0; j < cellColumns.size(); j++) {
                        XSSFCell cell = dataRow.createCell(j);
                        Object[] cellColumn = cellColumns.get(j);
                        String key = String.valueOf(cellColumn[0]);
                        setCellObjectValue(data.get(key), cell);
                        cell.setCellStyle((XSSFCellStyle) cellColumn[1]);
                    }
                }
            } else {
                XSSFRow dataRow = sheet.getRow((short) dataConfigRowIndex);
                XSSFCell cell = dataRow.createCell(0);
                cell.setCellValue("无相关数据");
                mergedRegion(workbook, sheet, new int[]{dataConfigRowIndex, dataConfigRowIndex, 0,
                    cellColumns.size() - 1});
            }
            setExportInfo(exportFileName, request, response);
            outputStream = response.getOutputStream();
            SXSSFWorkbook sXSSFWorkbook = new SXSSFWorkbook(workbook);
            sXSSFWorkbook.createSheet().setRandomAccessWindowSize(1000);
            sXSSFWorkbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            log.error("Excel文件导出出错", e);
        } finally {
            IOUtils.closeQuietly(outputStream);
            try {
                if (null != workbook) {
                    workbook.close();
                }
            } catch (IOException e) {
                log.error("Excel文件关闭失败", e);
            }
        }
    }

    /**
     * 描述： 设置导出文件格式和信息 <br/> 作者： ZhangHeng
     *
     * @param response void <br/>
     */
    private static void setExportInfo(String exportFileName, HttpServletRequest request,
        HttpServletResponse response)
        throws UnsupportedEncodingException {
        response.setCharacterEncoding("UTF-8");
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        String fileName = URLEncoder.encode(exportFileName, "UTF-8");
        String UserAgent = request.getHeader("USER-AGENT").toLowerCase();
        if (UserAgent.indexOf("firefox") >= 0) {
            fileName = new String((exportFileName).getBytes("UTF-8"), "iso-8859-1");
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
    }

    /**
     * 描述： 获取Excel数据模板行配置信息 <br/> 作者： ZHangHeng
     *
     * @param dataRowIndex 数据起始行索引
     * @param sheet 工作薄
     * @param getCellStyle 获取单元格样式
     * @return List<Object                               [                               ]> Object[columnName, cellStyle] <br/>
     */
    private static List<Object[]> getDataConfig(int dataRowIndex, XSSFSheet sheet,
        boolean getCellStyle) {
        List<Object[]> dataConfig = new ArrayList<Object[]>();
        XSSFRow dataRow = sheet.getRow(dataRowIndex);
        if (null != dataRow) {
            dataConfig = new ArrayList<Object[]>();
            int minColIx = dataRow.getFirstCellNum();
            int maxColIx = dataRow.getLastCellNum();
            for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                XSSFCell cell = dataRow.getCell(colIx);
                Object[] dataCellConfig =
                    new Object[]{cell.getStringCellValue(),
                        getCellStyle ? cell.getCellStyle() : null};
                dataConfig.add(dataCellConfig);
                cell.setCellValue("");
            }
        }
        return dataConfig;
    }

    /**
     * 获取Excel文件数据
     */
    public static <T> List<T> getDatas(String tableName, String templatePath,
        Map<String, Object> defaultValues, Class<T> clazz) {
        InputStream inputStream = null;
        List<T> list = new ArrayList<T>();
        try {
            inputStream = new FileInputStream(new File(templatePath));
            list = getDatas(tableName, inputStream, defaultValues, clazz);
        } catch (FileNotFoundException e) {
            log.error("获取Excel文件数据出错", e);
        }
        if (null != inputStream) {

        }
        return list;
    }

    public static <T> List<T> getDatas(String tableName, InputStream inputStream,
        Map<String, Object> defaultValues, Class<T> clazz) {
        XSSFWorkbook workbook = null;
        List<T> datas = new ArrayList<T>();
        try {
            workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow configRow = sheet.getRow(0);
            Map<Integer, String> config = new HashMap<Integer, String>();
            if (null != configRow) {
                int minColIx = configRow.getFirstCellNum();
                int maxColIx = configRow.getLastCellNum();
                for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                    XSSFCell cell = configRow.getCell(colIx);
                    String cellValue = cell.getStringCellValue();
                    String str = tableName + ".";
                    if (cellValue.startsWith(str)) {
                        config.put(colIx, cellValue.replace(str, ""));
                    }
                }
            }

            for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                XSSFRow dataRow = sheet.getRow(rowIndex);
                T obj = clazz.newInstance();
                for (Entry<Integer, String> entry : config.entrySet()) {
                    PropertyDescriptor pd = PropertyUtils
                        .getPropertyDescriptor(obj, entry.getValue());
                    if (null != pd) {
                        Class<?> fieldType = pd.getPropertyType();
                        Object value = null;
                        XSSFCell cell = dataRow.getCell(entry.getKey());
                        if (null != cell) {
                            value = getCellValue(cell, fieldType);
                        }
                        PropertyUtils.setProperty(obj, entry.getValue(), value);
                    }
                }
                if (null != defaultValues && defaultValues.size() > 0) {
                    for (Entry<String, Object> f : defaultValues.entrySet()) {
                        PropertyUtils.setProperty(obj, f.getKey(), f.getValue());
                    }
                }
                datas.add(obj);
            }
        } catch (Exception e) {
            log.error("获取Excel文件数据出错", e);
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
            } catch (IOException e) {
                log.error("Excel文件关闭失败", e);
            }
        }
        return datas;
    }

    /**
     * 描述： 设置单元格合并 <br/> 作者： ZhangHeng
     *
     * @param regionArr void <br/>
     */
    private static void mergedRegion(XSSFWorkbook workbook, XSSFSheet sheet, int[] regionArr) {
        CellRangeAddress region = new CellRangeAddress(regionArr[0], regionArr[1], regionArr[2],
            regionArr[3]);
        region.formatAsString();
        RegionUtil.setBorderTop(BorderStyle.DASHED, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.DASHED, region, sheet);
        RegionUtil.setBorderBottom(BorderStyle.DASHED, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.DASHED, region, sheet);
        sheet.addMergedRegion(region);
    }

    /**
     * 描述： 单元格填入值设置 <br/> 作者： ZhangHeng
     *
     * @param cell void <br/>
     */
    private static void setCellObjectValue(Object cellText, XSSFCell cell) {
        if (null != cellText) {
            if (Integer.class.isInstance(cellText)) {
                cell.setCellValue((Integer) cellText);
            } else if (Double.class.isInstance(cellText)) {
                cell.setCellValue((Double) cellText);
            } else if (Long.class.isInstance(cellText)) {
                cell.setCellValue((Long) cellText);
            } else if (Boolean.class.isInstance(cellText)) {
                cell.setCellValue((Boolean) cellText);
            } else if (Short.class.isInstance(cellText)) {
                cell.setCellValue((Short) cellText);
            } else if (String.class.isInstance(cellText)) {
                cell.setCellValue((String) cellText);
            } else if (Float.class.isInstance(cellText)) {
                cell.setCellValue((Float) cellText);
            } else if (BigDecimal.class.isInstance(cellText)) {
                cell.setCellValue(((BigDecimal) cellText).doubleValue());
            }
        }
    }

    /**
     * 获取单元格值
     */
    public static Object getCellValue(XSSFCell cell, Class<?> fieldType) {
        String cellValue = null;
        cell.setCellType(CellType.STRING);
        cellValue = cell.getStringCellValue();
        Object value = null;
        if (StringUtils.isNotBlank(cellValue) && fieldType.isAssignableFrom(Integer.class)) {
            value = Integer.parseInt(cellValue);
        } else if (StringUtils.isNotBlank(cellValue) && fieldType.isAssignableFrom(Double.class)) {
            value = Double.parseDouble(cellValue);
        } else if (StringUtils.isNotBlank(cellValue) && fieldType.isAssignableFrom(Long.class)) {
            value = Long.parseLong(cellValue);
        } else if (StringUtils.isNotBlank(cellValue) && fieldType.isAssignableFrom(Boolean.class)) {
            value = Boolean.parseBoolean(cellValue);
        } else if (StringUtils.isNotBlank(cellValue) && fieldType.isAssignableFrom(Short.class)) {
            value = Short.parseShort(cellValue);
        } else if (fieldType.isAssignableFrom(String.class)) {
            value = cellValue;
        } else if (StringUtils.isNotBlank(cellValue) && fieldType.isAssignableFrom(Float.class)) {
            value = Float.parseFloat(cellValue);
        } else if (StringUtils.isNotBlank(cellValue) && fieldType
            .isAssignableFrom(BigDecimal.class)) {
            value = new BigDecimal((String) cellValue);
        }
        return value;
    }

}
