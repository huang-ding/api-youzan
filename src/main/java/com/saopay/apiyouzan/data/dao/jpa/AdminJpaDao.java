package com.saopay.apiyouzan.data.dao.jpa;

import com.saopay.apiyouzan.data.pojo.po.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author huangding
 * @description
 * @date 2018/11/20 10:22
 */
public interface AdminJpaDao extends JpaRepository<Admin, Long> {

    Admin findByOpenId(String openId);

}
