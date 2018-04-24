package app.trial.eaglequotetrial.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import app.trial.eaglequotetrial.R;
import app.trial.eaglequotetrial.model.callback.LoginCallback;
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
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
        new Request.Login().execute();
    }
}
