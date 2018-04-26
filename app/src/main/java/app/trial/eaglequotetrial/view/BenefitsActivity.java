package app.trial.eaglequotetrial.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import app.trial.eaglequotetrial.R;
import app.trial.eaglequotetrial.model.BenefitIcon;
import app.trial.eaglequotetrial.model.Benefits;
import app.trial.eaglequotetrial.model.Client;
import app.trial.eaglequotetrial.model.HealthCover;
import app.trial.eaglequotetrial.model.LifeCover;
import app.trial.eaglequotetrial.model.callback.BenefitsCallback;
import app.trial.eaglequotetrial.presenter.BenefitsPresenter;
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
    private Benefits mBenefits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benefits);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBenefitIcons = new ArrayList<>();
        mBenefits = new Benefits();
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
    }

    public void next(View view) {
        Client.setBenefits(mBenefits);
    }

    private class BenefitOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            BenefitsPresenter benefitsPresenter = new BenefitsPresenter();
            benefitsPresenter.loadDialog(BenefitsActivity.this,
                    (FancyButton) v, mBenefitIcons, mBenefits);
            benefitsPresenter.setCallback(new BenefitsCallback() {
                @Override
                public void onHealthCoverUpdate(HealthCover healthCover) {
                    mBenefits.healthCover = healthCover;
                }

                @Override
                public void onLifeCoverUpdate(LifeCover lifeCover) {
                    mBenefits.lifeCover = lifeCover;
                }
            });
        }
    }
}
