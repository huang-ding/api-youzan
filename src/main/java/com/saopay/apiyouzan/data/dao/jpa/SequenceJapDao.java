package com.saopay.apiyouzan.data.dao.jpa;

import com.saopay.apiyouzan.data.pojo.po.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author huangding
 * @description
 * @date 2018/11/23 16:33
 */
public interface SequenceJapDao extends JpaRepository<Sequence, Long> {

    Sequence findByType(Integer type);
}
