package com.saopay.apiyouzan.data.dao.jpa;

import com.saopay.apiyouzan.data.pojo.po.WeChatQRCodeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author huangding
 * @description
 * @date 2018/11/27 15:40
 */
public interface WeChatQRCodeInfoJpaDao extends JpaRepository<WeChatQRCodeInfo, Long> {

    WeChatQRCodeInfo findByTypeAndOpenId(Integer type, String openId);
}
