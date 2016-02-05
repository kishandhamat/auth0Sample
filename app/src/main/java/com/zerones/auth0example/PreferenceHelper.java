package com.zerones.auth0example;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author Kishan H Dhamat , Email: kishan.dhamat105@gmail.com
 */
public class PreferenceHelper {

    private SharedPreferences app_prefs;
    private final String USER_ID = "user_id";
    private final String CARD_TOKEN = "device_token";
    private Context context;

    public PreferenceHelper(Context context) {
        app_prefs = context.getSharedPreferences("auth",
                Context.MODE_PRIVATE);
        this.context = context;
    }

    public void putCardToken(String deviceToken) {
        Editor edit = app_prefs.edit();
        edit.putString(CARD_TOKEN, deviceToken);
        edit.commit();
    }

    public String getCardToken() {
        return app_prefs.getString(CARD_TOKEN, null);
    }

    public void putUserId(String userId) {
        Editor edit = app_prefs.edit();
        edit.putString(USER_ID, userId);
        edit.commit();
    }

    public String getUserId() {
        return app_prefs.getString(USER_ID, null);
    }


}
