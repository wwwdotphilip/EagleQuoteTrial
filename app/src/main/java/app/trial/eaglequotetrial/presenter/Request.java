package app.trial.eaglequotetrial.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import app.trial.eaglequotetrial.model.Data;
import app.trial.eaglequotetrial.model.callback.RequestCallback;

public class Request {
    private static final String MAIN_URL = "https://staging.blackfin.technology/mobile/";
    private static RequestCallback mCallback;

    public static void setCallback(RequestCallback requestCallback) {
        mCallback = requestCallback;
    }

    public static void Login() {
        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("email", "android@trial.com");
            jsonParam.put("password", "Abcd@1234567");
            jsonParam.put("rememberMe", true);
            new HttpRequest("LogIn", jsonParam, "POST", null).execute();
        } catch (JSONException e) {
            if (mCallback != null) {
                mCallback.onError(e.toString());
            }
        }
    }

    public static void Benefits() {
        new HttpRequest("benefit", null,
                "GET", Session.getSession().authorization.token).execute();
    }

    public static void Providers() {
        new HttpRequest("provider", null,
                "GET", Session.getSession().authorization.token).execute();
    }

    public static void Product() {
        new HttpRequest("product", null,
                "GET", Session.getSession().authorization.token).execute();
    }

    public static void Quote(){
        try {
            JSONObject jsonParam = new JSONObject(new Gson().toJson(NewQuotePresenter.getData(), Data.class));
            new HttpRequest("quote/request-quote", jsonParam,
                    "POST", Session.getSession().authorization.token).execute();
        } catch (JSONException e) {
            if (mCallback != null) {
                mCallback.onError(e.toString());
            }
        }
    }

    private static class HttpRequest extends AsyncTask<String, String, String> {
        String endPoint;
        JSONObject jsonParam;
        String method;
        String token;

        HttpRequest(String endPoint, JSONObject jsonParam, String method, String token) {
            this.endPoint = endPoint;
            this.jsonParam = jsonParam;
            this.method = method;
            this.token = token;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String result;
                URL url = new URL(MAIN_URL + endPoint);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(method);
                if (token != null) {
                    conn.setRequestProperty("authorization", "Bearer " + token);
                }

                if (jsonParam != null && method.equals("POST")) {
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();
                } else {
                    conn.connect();
                }

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
                result = response.toString();
                conn.disconnect();
                if (mCallback != null) {
                    mCallback.onSuccess(result);
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
