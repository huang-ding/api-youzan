package com.saopay.apiyouzan.service;

import com.saopay.apiyouzan.data.dao.jpa.UserJpaDao;
import com.saopay.apiyouzan.data.pojo.po.User;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author huangding
 * @description
 * @date 2018/11/19 12:05
 */
@Service
public class UserService {



    @Autowired
    private UserJpaDao userJpaDao;


    /**
     * 验证该openId是否存在，不存在则保存
     */
    public boolean isOldOpenIdAndSaveOpenId(String openId) {
        User user = userJpaDao.findByOpenId(openId);
        if (user != null) {
            return true;
        } else {
            user = new User();
            LocalDateTime now = LocalDateTime.now();
            user.setCreatorTime(now);
            user.setUpdateTime(now);
            user.setOpenId(openId);
            userJpaDao.save(user);
            return false;
        }
    }

    /**
     * 验证该openId是否存在
     */
    public boolean isOldOpenId(String openId) {
        User user = userJpaDao.findByOpenId(openId);
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

}
