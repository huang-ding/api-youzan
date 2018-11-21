package com.saopay.apiyouzan.util;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author huangding
 * @description
 * @date 2018/11/13 9:34
 */
public class DataUtil {

    /**
     * 100倍
     */
    public static Integer doubleOfInt(Double d) {
        if (null == d) {
            return 0;
        }
        int i = new BigDecimal(d).multiply(new BigDecimal(100)).toBigInteger().intValue();
        return i;
    }

    /**
     * 1/100倍
     */
    public static Double intOfDouble(Integer i) {
        if (null == i) {
            return 0d;
        }
        BigDecimal bigDecimal = new BigDecimal(i)
            .divide(new BigDecimal(100), MathContext.DECIMAL128).setScale(2, BigDecimal.ROUND_DOWN);
        return bigDecimal.setScale(3, BigDecimal.ROUND_DOWN).setScale(2, BigDecimal.ROUND_HALF_UP)
            .doubleValue();
    }


}
