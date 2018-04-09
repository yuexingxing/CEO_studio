package com.tajiang.ceo.common.utils;

import com.google.gson.Gson;
import com.tajiang.ceo.model.User;

/**
 * Created by Administrator on 2016/7/11.
 */
public class UserUtils {

    /**
     * 保存用户信息
     * @param user
     */
    public static void saveUser(User user) {
        SharedPreferencesUtils.put(SharedPreferencesUtils.USER_LOGIN_INFOR, user);
    }

    /**
     * 获取用户信息
     * @return
     */
    public static User getUser() {
        String userStr = (String) SharedPreferencesUtils.get(SharedPreferencesUtils.USER_LOGIN_INFOR, "");
        User user = new Gson().fromJson(userStr, User.class);
        return user;
    }
}
