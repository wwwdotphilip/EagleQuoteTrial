package app.trial.eaglequotetrial.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import app.trial.eaglequotetrial.R;
import app.trial.eaglequotetrial.model.callback.LoginCallback;
import app.trial.eaglequotetrial.presenter.Device;
import app.trial.eaglequotetrial.presenter.Request;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Request.setCallback(new LoginCallback() {
            @Override
            public void onSuccess() {
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                finish();
            }

            @Override
            public void onError(String error) {
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        error, Snackbar.LENGTH_LONG).show();
            }
        });
        if (Device.getInstance().isOnline(this)) {
            new Request.Login().execute();
        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(),
                    "No internet connection.", Snackbar.LENGTH_SHORT).show();
        }
    }
}
