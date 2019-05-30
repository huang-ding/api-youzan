package com.saopay.apiyouzan.data.dao.jpa;

import com.saopay.apiyouzan.data.pojo.po.ActivityInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author huangding
 * @description
 * @date 2018/11/23 15:50
 */
public interface ActivityInfoJpaDao extends JpaRepository<ActivityInfo, Long> {

    ActivityInfo findByTypeAndOpenId(Integer type, String openId);

    int countByTypeAndParentOpenId(Integer type, String parentOpenId);

}
