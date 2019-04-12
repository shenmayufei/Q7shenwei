package com.spd.qsevendemo.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author xuyan  保存历史记录
 */
@Entity
public class DataBean {
    private String barcode;
    private String weight;
    private String tiji;
    private String count;
    @Id
    private long time;
    @Generated(hash = 280761562)
    public DataBean(String barcode, String weight, String tiji, String count,
            long time) {
        this.barcode = barcode;
        this.weight = weight;
        this.tiji = tiji;
        this.count = count;
        this.time = time;
    }
    @Generated(hash = 908697775)
    public DataBean() {
    }
    public String getBarcode() {
        return this.barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    public String getWeight() {
        return this.weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }
    public String getTiji() {
        return this.tiji;
    }
    public void setTiji(String tiji) {
        this.tiji = tiji;
    }
    public String getCount() {
        return this.count;
    }
    public void setCount(String count) {
        this.count = count;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }

}
