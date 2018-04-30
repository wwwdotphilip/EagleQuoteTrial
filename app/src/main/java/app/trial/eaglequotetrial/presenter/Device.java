package app.trial.eaglequotetrial.presenter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class Device {
    private static Device instance = new Device();
    private ConnectivityManager connectivityManager;
    private boolean connected = false;

    public static Device getInstance() {
        return instance;
    }

    public static String loadJSONFromAsset(Activity activity, String filename) {
        String json;
        try {
            InputStream is = activity.getAssets().open("json/" + filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public boolean isOnline(Activity activity) {
        try {
            connectivityManager = (ConnectivityManager) activity
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo;
            if (connectivityManager != null) {
                networkInfo = connectivityManager.getActiveNetworkInfo();
                connected = networkInfo != null && networkInfo.isAvailable() &&
                        networkInfo.isConnected();
            }
        } catch (Exception e) {
            Log.v("connectivity", e.toString());
        }
        return connected;
    }
}
