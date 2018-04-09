package com.tajiang.ceo.common.utils;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Admins on 2017/3/15.
 */

public class TJJSONUtil<T> {
    //Json根元素类型
    public static final Class ELEMENT_STRING = String.class;

    public static String ParseObject2JsonString(@NonNull List<Object> list) {
        return new Gson().toJson(list);
    }

}
