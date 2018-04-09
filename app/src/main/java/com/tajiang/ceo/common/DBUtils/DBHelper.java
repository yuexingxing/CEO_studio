package com.tajiang.ceo.common.DBUtils;

/**
 * Created by Administrator on 2016/7/28.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tajiang.ceo.common.utils.LogUtils;

public class DBHelper extends SQLiteOpenHelper {

    private final static int    VERSION         = 1;
    public final static String DB_FILE_NAME         = "china_Province_city_zone.db"; //数据库文件名称
    private final        String TABLE_CITY      = "T_City";                      //城市表名称
    private final        String TABLE_PROVINCE  = "T_Province";                  //省份表名称
    private final        String TABLE_ZONE      = "T_Zone";                      //区名称

    public final static String COLUMN_CITY      = "CityName";                    //城市
    public final static String COLUMN_PROVINCE  = "ProName";                      //省份
    public final static String ID_PROVINCE      = "ProSort";                      //省份ID
    public final static String ID_CITY_BELONG_PROVINCE = "ProID";                //城市外键省份ID

    private Context context;
    private Cursor c;
    private SQLiteDatabase db;

    //SQLiteOpenHelper子类必须要的一个构造函数
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version) {
        //必须通过super 调用父类的构造函数
        super(context, name, factory, version);
        initData(context);
        openDataBase();
    }

    public DBHelper(Context context, String name, int version){
        this(context, name, null, version);
    }

    //数据库的构造函数，传递一个参数的， 数据库名字和版本号都固定
    public DBHelper(Context context){
        this(context, DB_FILE_NAME, null, VERSION);
    }

    private void initData(Context context) {
        this.context = context;
    }

    // 回调函数，第一次创建时可以调用此函数，创建一个数据库表
    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    //回调函数，当你构造DBHelper的传递的Version与之前的Version调用此函数
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.i("update Database");
    }
    //查询省份
    public Cursor queryProvince(){
        String[] columns = {COLUMN_PROVINCE, ID_PROVINCE};
        c = db.query(TABLE_PROVINCE, columns
                , null, null, null, null, null, null);
        return c;
    }
    //查询城市
    public Cursor queryCity(String ProID){
        String[] columns = {COLUMN_CITY};
        //获取Cursor
        c = db.query(TABLE_CITY, columns, ID_CITY_BELONG_PROVINCE + "=" + ProID
                , null, null, null, null, null);
        return c;
    }

    //打开数据库
    public void openDataBase() {
        try {
            db = DBManager.getInstance().openDatabase(context, DB_FILE_NAME);
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    //关闭数据库
    public void close(){
        if (c != null) {
            c.close();
        }
        if(db != null){
            db.close();
        }
    }
}
