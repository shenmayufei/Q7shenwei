package com.spd.qsevendemo.model;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

/**
 * @author :Reginer in  2017/10/9 16:30.
 *         联系方式:QQ:282921012
 *         功能描述:数据库初始化
 */
public class DatabaseManager {
    private static DaoSession sDaoSession;

    public static void init(Application application) {
        SqLiteHelper helper = new SqLiteHelper(application, "seven.db");
        Database db = helper.getWritableDb();
        sDaoSession = new DaoMaster(db).newSession();
    }

    public static DatabaseManager getInstance() {
        return DatabaseManagerHolder.INSTANCE;
    }

    public DaoSession getDao() {
        return sDaoSession;
    }

    private static class DatabaseManagerHolder {
        private static final DatabaseManager INSTANCE = new DatabaseManager();
    }

}
