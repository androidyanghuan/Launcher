package cn.sn.zwcx.sdk.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.sn.zwcx.sdk.config.DBConfig;

/**
 * Created by on 2018/1/10 11:07.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class DBUtil {
    public static final String CREATE_TABLE_IF_NOT_EXISTS = "create table if not exists %s "
            + "(_id integer primary key autoincrement,key text unique,is_read integer)";
    private static DBUtil mDBUtil;
    private SQLiteDatabase mdatabase;

    // 构造函数私有
    private DBUtil(Context context){
        mdatabase = new DBHelper(context,DBConfig.DB_NAME + ".db").getWritableDatabase();
    }

    /**
     * 获取DBUtil类实例
     * @param context
     * @return
     */
    public static DBUtil newInstance(Context context){
        if (mDBUtil == null){
            synchronized (DBUtil.class){
                if (mDBUtil == null)
                    mDBUtil = new DBUtil(context);
            }
        }
        return mDBUtil;
    }

    /**
     * 插入一个item已读状态到数据表
     * @param table 数据表名
     * @param key   key值
     * @param value 数据值
     * @return 插入结果
     */
    public boolean insertRead(final String table, String key, int value) {
        Cursor cursor = mdatabase.query(table, null, null, null, null, null, "id asc");
        // 最多缓存200条
        if (cursor.getCount() > 0 && cursor.moveToNext())
        mdatabase.delete(table,"id=?",new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex("id")))});
        cursor.close();
        ContentValues cv = new ContentValues();
        cv.put("key",key);
        cv.put("is_read",value);
        return mdatabase.insertWithOnConflict(table,null,cv,SQLiteDatabase.CONFLICT_REPLACE) > 0;
    }

    /**
     * 判断item是否已经阅读过
     * @param table 数据表名
     * @param key   key值
     * @param value
     * @return
     */
    public boolean isRead(String table, String key, int value) {
        boolean isRead = false;
        Cursor cursor = mdatabase.query(table, null, "key=?", new String[]{key}, null, null, null);
        if (cursor.moveToNext() && (cursor.getInt(cursor.getColumnIndex("is_read")) == value))
            isRead = true;
        cursor.close();
        return isRead;
    }


    private class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context, String name) {
            super(context, name, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //onCreate()方法只有数据库第一次被创建时才会调用，若数据库已存在，此方法不会被调用
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            //数据库打开时就会被调用，将插入新表的操作方到onOpen中
            db.execSQL(String.format(CREATE_TABLE_IF_NOT_EXISTS, DBConfig.TABLE_ZHIHU));
            db.execSQL(String.format(CREATE_TABLE_IF_NOT_EXISTS,DBConfig.TABLE_WANGYI));
            db.execSQL(String.format(CREATE_TABLE_IF_NOT_EXISTS,DBConfig.TABLE_WEIXIN));
            db.execSQL(String.format(CREATE_TABLE_IF_NOT_EXISTS,DBConfig.TABLE_GANKIO_DAY));
            db.execSQL(String.format(CREATE_TABLE_IF_NOT_EXISTS,DBConfig.TABLE_GANKIO_CUSTOM));
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //只有数据库进行版本升级时被调用
        }
    }
}
