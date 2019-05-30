package com.saopay.apiyouzan.data.dao.jpa;

import com.saopay.apiyouzan.data.pojo.po.ActivityNo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author huangding
 * @description
 * @date 2018/11/23 16:00
 */
public interface ActivityNoJpaDao extends JpaRepository<ActivityNo, Long> {

    /**
     * 根据活动类型，推荐码，查询推荐人openId
     */
    ActivityNo findByTypeAndCode(Integer type, String code);

    ActivityNo findByTypeAndOpenId(Integer type,String openId);

}
