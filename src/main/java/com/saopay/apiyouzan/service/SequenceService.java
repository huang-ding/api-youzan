package com.saopay.apiyouzan.service;

import com.saopay.apiyouzan.data.dao.jpa.SequenceJapDao;
import com.saopay.apiyouzan.data.pojo.po.Sequence;
import com.saopay.apiyouzan.enums.SequenceType;
import com.saopay.apiyouzan.exception.SequenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author huangding
 * @description
 * @date 2018/11/23 16:40
 */
@Service
public class SequenceService {

    @Autowired
    private SequenceJapDao sequenceJapDao;

    public synchronized String getSequenceNextActivityCode() {
        Sequence sequence = sequenceJapDao.findByType(SequenceType.ACTIVITY_TYPE.getType());
        if (null != sequence) {
            Integer value = sequence.getValue();
            sequence.setValue(sequence.getValue() + 1);
            sequenceJapDao.save(sequence);
            return String.valueOf(value);
        }
        throw new SequenceException("编码获取失败");
    }

    public static String zeroFill(int source, int length) {
        return String.format("%0" + length + "d", source);
    }

}
