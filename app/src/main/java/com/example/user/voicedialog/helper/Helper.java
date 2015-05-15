package com.example.user.voicedialog.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.user.voicedialog.SettingsActivity;

/**
 * Created by user on 15.05.2015.
 */
public class Helper {

    private static String URL;
    private SharedPreferences prefs;
    private Context context;

    public static String getURL() {
        return URL;
    }

    public static void setURL(String URL) {
        Helper.URL = URL;
    }

    public void InitialHelper(Context context)
    {
        this.context=context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        URL = prefs.getString(SettingsActivity.KEY_PREF_SYNC_ADDRESS, "");
    }
}
