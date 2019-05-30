package com.saopay.apiyouzan.service.activity;

import com.saopay.apiyouzan.data.dao.jpa.WeChatQRCodeInfoJpaDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author huangding
 * @description 微信二维码
 * @date 2018-11-27 15:39:46
 */
@Service
@Slf4j
public class WeChatQRCodeInfoService {

    @Autowired
    private WeChatQRCodeInfoJpaDao weChatQRCodeInfoJpaDao;


}
