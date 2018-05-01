package app.trial.eaglequotetrial.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import app.trial.eaglequotetrial.R;
import app.trial.eaglequotetrial.model.HttpResponse;
import app.trial.eaglequotetrial.model.callback.RequestCallback;
import app.trial.eaglequotetrial.presenter.Device;
import app.trial.eaglequotetrial.presenter.NewQuotePresenter;
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

                HttpResponse httpResponse = new Gson().fromJson(result, HttpResponse.class);
                if (httpResponse.data.benefits != null) {
                    NewQuotePresenter.setBenefits(httpResponse.data.benefits);
                } else if (httpResponse.data.products != null) {
                    NewQuotePresenter.setProducts(httpResponse.data.products);
                } else if (httpResponse.data.providers != null) {
                    NewQuotePresenter.setProviders(httpResponse.data.providers);
                } else if (httpResponse.data.user != null) {
                    Session.saveSession(LoginActivity.this, httpResponse.data);
                    Request.Benefits();
                    Request.Providers();
                    Request.Product();
                }
                if (NewQuotePresenter.getBenefits() != null && NewQuotePresenter.getProviders() != null
                        && NewQuotePresenter.getProducts() != null) {
                    NewQuotePresenter.storeData(LoginActivity.this);
                    loadNextScreen();
                }
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
                NewQuotePresenter.loadData(this);
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
