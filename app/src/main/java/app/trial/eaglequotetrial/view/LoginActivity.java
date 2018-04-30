package app.trial.eaglequotetrial.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;

import app.trial.eaglequotetrial.R;
import app.trial.eaglequotetrial.model.Login;
import app.trial.eaglequotetrial.model.callback.RequestCallback;
import app.trial.eaglequotetrial.presenter.Device;
import app.trial.eaglequotetrial.presenter.Request;
import app.trial.eaglequotetrial.presenter.Session;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Request.setCallback(new RequestCallback() {
            @Override
            public void onSuccess(String result) {
                Login login = new Gson().fromJson(result, app.trial.eaglequotetrial.model.Login.class);
                Session.saveSession(LoginActivity.this, login.data);
                Log.v(getClass().getSimpleName(), Session.getSession().authorization.token);
                loadNextScreen();
            }

            @Override
            public void onError(String error) {
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        error, Snackbar.LENGTH_LONG).show();
            }
        });
        if (Device.getInstance().isOnline(this)) {
            if (!Session.loadSession(this)) {
                Request.Login();
            } else {
                loadNextScreen();
            }
        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(),
                    "No internet connection.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void loadNextScreen() {
        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        finish();
    }
}
