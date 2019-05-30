package com.saopay.apiyouzan.service.youzan;

import com.saopay.apiyouzan.data.pojo.dto.MsgPushEntity;

/**
 * @author huangding
 * @description
 * @date 2018/11/29 10:24
 */
public interface YouZanNotifyService {

    void youZanManageNotify(MsgPushEntity msgPushEntity);
}
