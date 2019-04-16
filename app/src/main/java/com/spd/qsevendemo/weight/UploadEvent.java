package com.spd.qsevendemo.weight;

/**
 * @author :xu in  2018/1/29 12:32.
 *         联系方式:QQ:2419646399
 *         功能描述:
 */
public class UploadEvent {

    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UploadEvent(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "UploadEvent{event ='" + this.message + '}';
    }

}
