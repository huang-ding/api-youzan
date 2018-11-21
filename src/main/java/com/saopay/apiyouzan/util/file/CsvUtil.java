package com.saopay.apiyouzan.util.file;

import com.alibaba.fastjson.JSON;
import com.csvreader.CsvReader;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huangding
 * @description csv 工具
 * @date 2018/11/16 11:15
 */
@Slf4j
public class CsvUtil {

    public static List<Map<String, String>> readerCsv(InputStream is) throws Exception {
        CsvReader csvReader = new CsvReader(is, Charset.forName("GBK"));
        List<String> list = new ArrayList<>();
        while (csvReader.readRecord()) {
            String rawRecord = csvReader.getRawRecord();
            list.add(rawRecord);
        }
        List<Map<String, String>> datas = new ArrayList<>();
        if (list.size() > 0) {
            String[] titles = list.get(0).split(",");
            for (int i = 1; i < list.size(); i++) {
                Map<String, String> map = new HashMap<>();
                String[] data = list.get(i).split(",");
                for (int j = 0; j < data.length; j++) {
                    map.put(titles[j], data[j]);
                }
                datas.add(map);
            }
        }
        return datas;
    }

}
