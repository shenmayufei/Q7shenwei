package com.spd.qsevendemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author xuyan  传入重量数据列表
 */
public class BalanceBean implements Parcelable {

    /**
     * RequestType : sample string 1
     * ScanTime : 2019-04-11T10:16:03.4570519+08:00
     * QrCodes : ["sample string 1","sample string 2"]
     * Pcs : 2
     * Weight : 3
     * Volume : 4
     */

    private String RequestType;
    private String ScanTime;
    private int Pcs;
    private int Weight;
    private int Volume;
    private List<String> QrCodes;

    public String getRequestType() {
        return RequestType;
    }

    public void setRequestType(String RequestType) {
        this.RequestType = RequestType;
    }

    public String getScanTime() {
        return ScanTime;
    }

    public void setScanTime(String ScanTime) {
        this.ScanTime = ScanTime;
    }

    public int getPcs() {
        return Pcs;
    }

    public void setPcs(int Pcs) {
        this.Pcs = Pcs;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int Weight) {
        this.Weight = Weight;
    }

    public int getVolume() {
        return Volume;
    }

    public void setVolume(int Volume) {
        this.Volume = Volume;
    }

    public List<String> getQrCodes() {
        return QrCodes;
    }

    public void setQrCodes(List<String> QrCodes) {
        this.QrCodes = QrCodes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.RequestType);
        dest.writeString(this.ScanTime);
        dest.writeInt(this.Pcs);
        dest.writeInt(this.Weight);
        dest.writeInt(this.Volume);
        dest.writeStringList(this.QrCodes);
    }

    public BalanceBean() {
    }

    protected BalanceBean(Parcel in) {
        this.RequestType = in.readString();
        this.ScanTime = in.readString();
        this.Pcs = in.readInt();
        this.Weight = in.readInt();
        this.Volume = in.readInt();
        this.QrCodes = in.createStringArrayList();
    }

    public static final Parcelable.Creator<BalanceBean> CREATOR = new Parcelable.Creator<BalanceBean>() {
        @Override
        public BalanceBean createFromParcel(Parcel source) {
            return new BalanceBean(source);
        }

        @Override
        public BalanceBean[] newArray(int size) {
            return new BalanceBean[size];
        }
    };
}
