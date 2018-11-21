package com.saopay.apiyouzan.controller;

import com.saopay.apiyouzan.service.GoodsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author huangding
 * @description
 * @date 2018/11/15 16:56
 */
@Api("有赞商品数据")
@RestController("goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/import-gppds-excdel")
    public boolean addUser(@RequestParam("file") MultipartFile file) {
        boolean a = false;
        String s = goodsService.batchImport(file);
        return a;
    }

}
