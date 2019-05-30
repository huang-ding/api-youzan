package com.saopay.apiyouzan.controller;

import com.saopay.apiyouzan.util.youzan.YouZanBaseUtil;
import java.io.File;
import java.io.FileNotFoundException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

/**
 * @author huangding
 * @description
 * @date 2018/11/15 13:06
 */
@Component
public class YouZanBaseController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected YouZanBaseUtil youZanBaseUtil;


}
