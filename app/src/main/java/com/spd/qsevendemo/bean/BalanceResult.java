package com.spd.qsevendemo.bean;

import java.util.List;

/**
 * @author xuyan  称重数据上传后的返回
 */
public class BalanceResult {

    /**
     * SuccessCount : 1
     * FalseCount : 2
     * SuccessMsgs : [{"CustomerAndOrderNO":"sample string 1","Rate":"sample string 2"},{"CustomerAndOrderNO":"sample string 1","Rate":"sample string 2"}]
     * FalseMsgs : [{"CustomerAndQrCode":"sample string 1","Msg":"sample string 2"},{"CustomerAndQrCode":"sample string 1","Msg":"sample string 2"}]
     */

    private int SuccessCount;
    private int FalseCount;
    private List<SuccessMsgsBean> SuccessMsgs;
    private List<FalseMsgsBean> FalseMsgs;

    public int getSuccessCount() {
        return SuccessCount;
    }

    public void setSuccessCount(int SuccessCount) {
        this.SuccessCount = SuccessCount;
    }

    public int getFalseCount() {
        return FalseCount;
    }

    public void setFalseCount(int FalseCount) {
        this.FalseCount = FalseCount;
    }

    public List<SuccessMsgsBean> getSuccessMsgs() {
        return SuccessMsgs;
    }

    public void setSuccessMsgs(List<SuccessMsgsBean> SuccessMsgs) {
        this.SuccessMsgs = SuccessMsgs;
    }

    public List<FalseMsgsBean> getFalseMsgs() {
        return FalseMsgs;
    }

    public void setFalseMsgs(List<FalseMsgsBean> FalseMsgs) {
        this.FalseMsgs = FalseMsgs;
    }

    public static class SuccessMsgsBean {
        /**
         * CustomerAndOrderNO : sample string 1
         * Rate : sample string 2
         */

        private String CustomerAndOrderNO;
        private String Rate;

        public String getCustomerAndOrderNO() {
            return CustomerAndOrderNO;
        }

        public void setCustomerAndOrderNO(String CustomerAndOrderNO) {
            this.CustomerAndOrderNO = CustomerAndOrderNO;
        }

        public String getRate() {
            return Rate;
        }

        public void setRate(String Rate) {
            this.Rate = Rate;
        }
    }

    public static class FalseMsgsBean {
        /**
         * CustomerAndQrCode : sample string 1
         * Msg : sample string 2
         */

        private String CustomerAndQrCode;
        private String Msg;

        public String getCustomerAndQrCode() {
            return CustomerAndQrCode;
        }

        public void setCustomerAndQrCode(String CustomerAndQrCode) {
            this.CustomerAndQrCode = CustomerAndQrCode;
        }

        public String getMsg() {
            return Msg;
        }

        public void setMsg(String Msg) {
            this.Msg = Msg;
        }
    }
}
