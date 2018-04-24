package app.trial.eaglequotetrial.presenter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Device {
    private static Device instance = new Device();
    private ConnectivityManager connectivityManager;
    private boolean connected = false;

    public static Device getInstance() {
        return instance;
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
