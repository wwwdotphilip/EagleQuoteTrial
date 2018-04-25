package app.trial.eaglequotetrial.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import app.trial.eaglequotetrial.R;
import app.trial.eaglequotetrial.model.BenefitIcon;
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
        }
    }

    public void next(View view) {

    }

    private void updateResources(FancyButton fancyButton, boolean selected) {
        for (BenefitIcon item : mBenefitIcons) {
            if (item.id == fancyButton.getId()) {
                fancyButton.setBorderColor(selected ?
                        getResources().getColor(R.color.app_blue) :
                        getResources().getColor(R.color.color_class_content));
                fancyButton.setTextColor(selected ?
                        getResources().getColor(R.color.app_blue) :
                        getResources().getColor(R.color.app_blue2));
                fancyButton.setIconResource(selected ?
                        getResources().getDrawable(item.selectedDrawable) :
                        getResources().getDrawable(item.unselectedDrawable));
            }
        }

    }

    private class BenefitOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BenefitsActivity.this);
            builder.setTitle("Title");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i(getClass().getSimpleName(), "Cancelled");
                    updateResources((FancyButton) v, false);
                }
            });
            builder.setPositiveButton("Apply Changes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i(getClass().getSimpleName(), "Applied");
                    updateResources((FancyButton) v, true);
                }
            });
            builder.show();
        }
    }
}
