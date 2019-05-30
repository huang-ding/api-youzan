package com.saopay.apiyouzan.data.dao.jpa;

import com.saopay.apiyouzan.data.pojo.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author huangding
 * @description 用户基本数据
 * @date 2018-11-28 10:21:50
 */
public interface UserJpaDao extends JpaRepository<User, Long> {

    User findByOpenId(String openId);

}