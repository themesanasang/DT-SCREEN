package com.example.themesanasang.scandocnth;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by themesanasang on 28/3/59.
 */
public class Session {
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    public Session(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);

        /*sp = context.getSharedPreferences("is_logged_in", Context.MODE_PRIVATE);
        spEditor = sp.edit();

        spEditor.clear();
        spEditor.commit();*/
    }

    public boolean setLogin(boolean status) {
        spEditor = sp.edit();
        spEditor.putBoolean("is_logged_in", status);
        spEditor.commit();
        return true;
    }

    public boolean getLoggedIn() {
        return sp.getBoolean("is_logged_in", false);
    }
}
