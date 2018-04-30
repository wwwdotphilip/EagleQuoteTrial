package app.trial.eaglequotetrial.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;

import app.trial.eaglequotetrial.R;
import app.trial.eaglequotetrial.model.Data;
import app.trial.eaglequotetrial.model.callback.RequestCallback;
import app.trial.eaglequotetrial.presenter.Request;
import app.trial.eaglequotetrial.presenter.ResultPresenter;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Request.setCallback(new RequestCallback() {
            @Override
            public void onSuccess(String result) {
                ResultPresenter.setData(new Gson().fromJson(result, Data.class));
            }

            @Override
            public void onError(String error) {
                Log.e(getClass().getSimpleName(), error);
            }
        });
        Request.Quote();
    }
}
