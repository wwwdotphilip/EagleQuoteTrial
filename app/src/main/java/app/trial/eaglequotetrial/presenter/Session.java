package app.trial.eaglequotetrial.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import app.trial.eaglequotetrial.model.Data;

public class Session {
    private static final String KEY = "session";
    private static final String NAME = "eaglequote";
    private static volatile Session instance;
    private Data mData;
    private SharedPreferences pref;

    private static Session getInstance() {
        if (instance == null) {
            synchronized (Session.class) {
                instance = new Session();
            }
        }
        return instance;
    }

    public static void saveSession(Context context, Data data) {
        getInstance().mData = data;
        getInstance().pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        getInstance().pref.edit().putString(KEY, new Gson().toJson(data)).apply();
    }

    public static boolean loadSession(Context context) {
        getInstance().pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String dataString = getInstance().pref.getString(KEY, null);
        if (dataString != null) {
            getInstance().mData = new Gson().fromJson(dataString, Data.class);
            return true;
        }
        return false;
    }

    public static Data getSession() {
        return getInstance().mData;
    }
}
