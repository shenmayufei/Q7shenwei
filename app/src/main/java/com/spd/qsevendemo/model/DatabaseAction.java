package com.spd.qsevendemo.model;


import java.util.List;

/**
 * @author :Reginer in  2017/11/6 12:15.
 * 联系方式:QQ:282921012
 * 功能描述:数据库操作类
 */
public class DatabaseAction {
    /**
     * 保存网点交接入库交接任务
     *
     * @param handoverList 入库交接任务
     */
    public static void savePrepareBox(List<SevenBean> handoverList) {
        DatabaseManager.getInstance().getDao()
                .getSevenBeanDao().insertOrReplaceInTx(handoverList);
    }

    /**
     * 查询要展示出来的内容
     *
     * @return 要展示的内容
     */
    public static List<SevenBean> queryShowContent() {
        return DatabaseManager.getInstance().getDao().getSevenBeanDao().queryBuilder().list();
    }



    /**
     * 保存data
     *
     * @param handoverList 入库交接任务
     */
    public static void saveData(List<DataBean> handoverList) {
        DatabaseManager.getInstance().getDao()
                .getDataBeanDao().insertOrReplaceInTx(handoverList);
    }

    /**
     * 查询data
     *
     * @return 要展示的内容
     */
    public static List<DataBean> queryData() {
        return DatabaseManager.getInstance().getDao().getDataBeanDao().queryBuilder().list();
    }



}
