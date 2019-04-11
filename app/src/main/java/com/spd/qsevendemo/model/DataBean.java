package com.spd.qsevendemo.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xuyan  保存历史记录
 */
@Entity
public class DataBean {
    private String barcode;
    private String weight;
    private String tiji;
    @Id
    private long time;
    @Generated(hash = 817222437)
    public DataBean(String barcode, String weight, String tiji, long time) {
        this.barcode = barcode;
        this.weight = weight;
        this.tiji = tiji;
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
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }

}
