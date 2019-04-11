package com.spd.qsevendemo.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xuyan
 */
@Entity
public class SevenBean {

    private String name;

    private boolean check;

    private int code;

    @Id
    private Long id;

    @Generated(hash = 140184845)
    public SevenBean(String name, boolean check, int code, Long id) {
        this.name = name;
        this.check = check;
        this.code = code;
        this.id = id;
    }

    @Generated(hash = 1997850736)
    public SevenBean() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getCheck() {
        return this.check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
