package com.saopay.apiyouzan.enums;

/**
 * @author huangding
 * @description
 * @date 2018/11/23 16:45
 */
public enum SequenceType {
    ACTIVITY_TYPE(1);
    private int type;

    SequenceType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
