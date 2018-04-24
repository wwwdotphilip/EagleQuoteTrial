package app.trial.eaglequotetrial.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import app.trial.eaglequotetrial.model.callback.LoginCallback;

public class Request {
    private static final String MAIN_URL = "https://staging.blackfin.technology/mobile/";
    private static LoginCallback mCallback;

    public static void setCallback(LoginCallback loginCallback) {
        mCallback = loginCallback;
    }

    public static class Login extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(MAIN_URL + "LogIn");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", "android@trial.com");
                jsonParam.put("password", "Abcd@1234567");
                jsonParam.put("rememberMe", true);

                Log.i("JSON", jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                os.writeBytes(jsonParam.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG", conn.getResponseMessage());

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Log.i("MSG", response.toString());

                conn.disconnect();
                app.trial.eaglequotetrial.model.Login login = new Gson().fromJson(response.toString(), app.trial.eaglequotetrial.model.Login.class);
                Session.saveSession(login.data);
                Log.v("MainActivityBasic", Session.getSession().authorization.token);
                if (mCallback != null) {
                    mCallback.onSuccess();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String error) {
            super.onPostExecute(error);
            if (mCallback != null && error != null) {
                mCallback.onError(error);
            }
        }
    }
}
