package com.saopay.apiyouzan.data.dao.jpa;

import com.saopay.apiyouzan.data.pojo.po.YouZanCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author huangding
 * @description 有赞消息
 * @date 2018-11-29 16:59:27
 */
public interface YouZanCustomerJpaDao extends JpaRepository<YouZanCustomer, Long> {

    YouZanCustomer findByOpenId(String openId);

    YouZanCustomer findByMobile(String mobile);

    YouZanCustomer findByAccountId(String accountId);

}
