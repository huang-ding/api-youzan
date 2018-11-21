package com.saopay.apiyouzan.service;

import com.saopay.apiyouzan.util.youzan.YouZanBaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author huangding
 * @description
 * @date 2018/11/20 13:03
 */
@Component
public class BaseService {

    @Autowired
    protected YouZanBaseUtil youZanBaseUtil;

}
