package com.saopay.apiyouzan.service;

import com.saopay.apiyouzan.data.dao.jpa.GoodsYZJpaDao;
import com.saopay.apiyouzan.data.pojo.po.GoodsYZ;
import com.saopay.apiyouzan.util.DataUtil;
import com.saopay.apiyouzan.util.file.CsvUtil;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author huangding
 * @description
 * @date 2018/11/16 10:47
 */
@Service
@Slf4j
public class GoodsService {

    @Autowired
    private GoodsYZJpaDao goodsYZJpaDao;

    @Transactional(rollbackFor = Exception.class)
    public String batchImport(MultipartFile file) {

        try (InputStream is = file.getInputStream()) {
            List<GoodsYZ> goodsYZS = new ArrayList<>();
            List<Map<String, String>> list = CsvUtil.readerCsv(is);
            if (list != null && list.size() > 0) {
                list.forEach(map -> {
                    GoodsYZ goodsYZ = new GoodsYZ();
                    goodsYZ.setSkuPropertiesName(map.get("商品规格"));
                    goodsYZ.setTitle(map.get("商品名称"));
                    goodsYZ.setOuterItemId(map.get("商品编码"));
                    goodsYZ.setGoodsType(map.get("商品类型"));
                    goodsYZ.setSkuId(Long.parseLong(map.get("规格id").trim()));
                    goodsYZ.setItemId(Long.parseLong(map.get("商品id").trim()));
                    String costOfPrice = map.get("成本价（元）").trim();
                    goodsYZ.setCostOfPrice(DataUtil.doubleOfInt(Double.parseDouble(costOfPrice)));
                    goodsYZ.setCreatorTime(LocalDateTime.now());
                    goodsYZ.setUpdateTime(LocalDateTime.now());
                    goodsYZS.add(goodsYZ);
                });
            }
            if (goodsYZS.size() > 0) {
                goodsYZJpaDao.saveAll(goodsYZS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
