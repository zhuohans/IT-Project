package com.haste.yzx.common.enums;

/**
 */
public enum UserActEnum {
        ALL(0),VIEW(1),LIKE(2),COLLECT(3),COMMENT(4);
    //  0    1     2     3      4

        int code;
        UserActEnum(int code) {
        }

        public int getCode() {
                return code;
        }

        public void setCode(int code) {
                this.code = code;
        }
}
