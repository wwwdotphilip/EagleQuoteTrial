package app.trial.eaglequotetrial.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import app.trial.eaglequotetrial.R;
import app.trial.eaglequotetrial.model.BenefitIcon;
import app.trial.eaglequotetrial.model.HttpResponse;
import app.trial.eaglequotetrial.model.callback.RequestCallback;
import app.trial.eaglequotetrial.presenter.BenefitsPresenter;
import app.trial.eaglequotetrial.presenter.NewQuotePresenter;
import app.trial.eaglequotetrial.presenter.Request;
import mehdi.sakout.fancybuttons.FancyButton;

public class BenefitsActivity extends AppCompatActivity {
    private int[][] fbId = {{R.id.fbHealthCover, R.drawable.ic_01_health_benefits, R.drawable.ic_01_health_benefits_1},
            {R.id.fbLifeCover, R.drawable.ic_02_life_cover, R.drawable.ic_02_life_cover_1},
            {R.id.fbFamilyProtection, R.drawable.ic_03_family_protection, R.drawable.ic_03_family_protection_1},
            {R.id.fbTrauma, R.drawable.ic_04_trauma, R.drawable.ic_04_trauma_1},
            {R.id.fbDisability, R.drawable.ic_05_disability, R.drawable.ic_05_disability_1},
            {R.id.fbIncomeProtection, R.drawable.ic_06_income_protection, R.drawable.ic_06_income_protection_1},
            {R.id.fbMortgage, R.drawable.ic_07_mortgage, R.drawable.ic_07_mortgage_1},
            {R.id.fbRedundancy, R.drawable.ic_08_redundancy, R.drawable.ic_08_redundancy_1},
            {R.id.fbWaiver, R.drawable.ic_09_waiver_of_premium, R.drawable.ic_09_waiver_of_premium_1}};
    private List<BenefitIcon> mBenefitIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benefits);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBenefitIcons = new ArrayList<>();
        for (int[] item : fbId) {
            BenefitIcon temp = new BenefitIcon();
            temp.id = item[0];
            temp.selectedDrawable = item[1];
            temp.unselectedDrawable = item[2];
            mBenefitIcons.add(temp);

            FancyButton fancyButton = findViewById(temp.id);
            fancyButton.setOnClickListener(new BenefitOnClickListener());
            fancyButton.getText();
        }
        Request.Benefits();
        Request.Providers();
        Request.Product();
        Request.setCallback(new RequestCallback() {
            @Override
            public void onSuccess(String result) {
                Log.i(getClass().getSimpleName(), result);
                HttpResponse httpResponse = new Gson().fromJson(result, HttpResponse.class);
                if (httpResponse.data.benefits != null) {
                    NewQuotePresenter.setBenefits(httpResponse.data.benefits);
                } else if (httpResponse.data.products != null) {
                    NewQuotePresenter.setProducts(httpResponse.data.products);
                } else if (httpResponse.data.providers != null) {
                    NewQuotePresenter.setProviders(httpResponse.data.providers);
                }
            }

            @Override
            public void onError(String error) {
                Log.e(getClass().getSimpleName(), error);
            }
        });
        Log.i(getClass().getSimpleName(), "Client age: " + NewQuotePresenter.getClient().age);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void next(View view) {
        startActivity(new Intent(BenefitsActivity.this, ResultActivity.class));
    }

    private class BenefitOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            BenefitsPresenter benefitsPresenter = new BenefitsPresenter();
            benefitsPresenter.loadDialog(BenefitsActivity.this,
                    (FancyButton) v, mBenefitIcons);
            benefitsPresenter.setCallback(benefit -> {

            });
        }
    }
}
