package com.spd.qsevendemo.net;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author xuyan 自动更新
 */
public class UpdateResult {

    /**
     * data : {"apkName":"com.spd.scan","downloadLink":"http://123.56.4.195:8082/apk/apkVersion/com.spd.scan/20180829191648.apk","id":10,"publishTime":{"date":29,"hours":19,"seconds":48,"month":7,"timezoneOffset":-480,"year":118,"minutes":16,"time":1535541408000,"day":3},"versionDesc":"更新测试","versionNumber":2}
     * errorMessage : 有新的版本请及时更新
     * success : true
     */

    private DataBean data;
    private String errorMessage;
    private boolean success;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean implements Parcelable {
        /**
         * apkName : com.spd.scan
         * downloadLink : http://123.56.4.195:8082/apk/apkVersion/com.spd.scan/20180829191648.apk
         * id : 10
         * publishTime : {"date":29,"hours":19,"seconds":48,"month":7,"timezoneOffset":-480,"year":118,"minutes":16,"time":1535541408000,"day":3}
         * versionDesc : 更新测试
         * versionNumber : 2
         */

        private String apkName;
        private String downloadLink;
        private int id;
        private PublishTimeBean publishTime;
        private String versionDesc;
        private int versionNumber;

        public String getApkName() {
            return apkName;
        }

        public void setApkName(String apkName) {
            this.apkName = apkName;
        }

        public String getDownloadLink() {
            return downloadLink;
        }

        public void setDownloadLink(String downloadLink) {
            this.downloadLink = downloadLink;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public PublishTimeBean getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(PublishTimeBean publishTime) {
            this.publishTime = publishTime;
        }

        public String getVersionDesc() {
            return versionDesc;
        }

        public void setVersionDesc(String versionDesc) {
            this.versionDesc = versionDesc;
        }

        public int getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(int versionNumber) {
            this.versionNumber = versionNumber;
        }

        public static class PublishTimeBean implements Parcelable {
            /**
             * date : 29
             * hours : 19
             * seconds : 48
             * month : 7
             * timezoneOffset : -480
             * year : 118
             * minutes : 16
             * time : 1535541408000
             * day : 3
             */

            private int date;
            private int hours;
            private int seconds;
            private int month;
            private int timezoneOffset;
            private int year;
            private int minutes;
            private long time;
            private int day;

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(int timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.date);
                dest.writeInt(this.hours);
                dest.writeInt(this.seconds);
                dest.writeInt(this.month);
                dest.writeInt(this.timezoneOffset);
                dest.writeInt(this.year);
                dest.writeInt(this.minutes);
                dest.writeLong(this.time);
                dest.writeInt(this.day);
            }

            public PublishTimeBean() {
            }

            protected PublishTimeBean(Parcel in) {
                this.date = in.readInt();
                this.hours = in.readInt();
                this.seconds = in.readInt();
                this.month = in.readInt();
                this.timezoneOffset = in.readInt();
                this.year = in.readInt();
                this.minutes = in.readInt();
                this.time = in.readLong();
                this.day = in.readInt();
            }

            public static final Creator<PublishTimeBean> CREATOR = new Creator<PublishTimeBean>() {
                @Override
                public PublishTimeBean createFromParcel(Parcel source) {
                    return new PublishTimeBean(source);
                }

                @Override
                public PublishTimeBean[] newArray(int size) {
                    return new PublishTimeBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.apkName);
            dest.writeString(this.downloadLink);
            dest.writeInt(this.id);
            dest.writeParcelable(this.publishTime, flags);
            dest.writeString(this.versionDesc);
            dest.writeInt(this.versionNumber);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.apkName = in.readString();
            this.downloadLink = in.readString();
            this.id = in.readInt();
            this.publishTime = in.readParcelable(PublishTimeBean.class.getClassLoader());
            this.versionDesc = in.readString();
            this.versionNumber = in.readInt();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
