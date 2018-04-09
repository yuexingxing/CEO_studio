package com.tajiang.ceo.mess.data_base_op;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;

import com.tajiang.ceo.common.DBUtils.DBHelper;
import com.tajiang.ceo.model.City;
import com.tajiang.ceo.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class DBHelperGetProvinces extends DatabaseUtils {

    private DBHelper dbHelper;
    private Cursor cursor;

    public DBHelperGetProvinces(Context context) {
        dbHelper = new DBHelper(context);
    }

    public List<Province> getProvince() {
        List<Province> list = new ArrayList<Province>();
        Province province;
        dbHelper.openDataBase();
        cursor = dbHelper.queryProvince();

        while (cursor.moveToNext()) {
            province = new Province();
            province.setAreaId(cursor.getString(cursor.getColumnIndex(DBHelper.ID_PROVINCE)));
            province.setAreaName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PROVINCE)));
            list.add(province);
        }
        dbHelper.close();
        return list;
    }

    public List<City> getCity(String ProID) {
        List<City> list = new ArrayList<City>();
        City city;
        dbHelper.openDataBase();
        cursor = dbHelper.queryCity(ProID);
        while (cursor.moveToNext()) {
            city = new City();
            city.setAreaName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CITY)));
            list.add(city);
        }
        dbHelper.close();
        return list;
    }

}
