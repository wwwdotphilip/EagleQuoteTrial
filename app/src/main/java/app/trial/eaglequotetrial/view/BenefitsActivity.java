package app.trial.eaglequotetrial.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
            fancyButton.getText();
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

    private SpannableStringBuilder customText(String text, int color) {
        // Initialize a new spannable string builder instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
        SpannableStringBuilder ssBuilderText = new SpannableStringBuilder(text);

        // Apply the text color span
        ssBuilderText.setSpan(
                foregroundColorSpan,
                0,
                text.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return ssBuilderText;
    }

    private class BenefitOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            FancyButton fancyButton = (FancyButton) v;
            int icon = 0;
            for (BenefitIcon item : mBenefitIcons) {
                if (item.id == v.getId()) {
                    icon = item.selectedDrawable;
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(BenefitsActivity.this);
            builder.setTitle(customText(fancyButton.getText().toString(), Color.WHITE));
            builder.setNegativeButton(customText("Cancel", Color.WHITE), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i(getClass().getSimpleName(), "Cancelled");
                    updateResources(fancyButton, false);
                }
            });
            builder.setPositiveButton(customText("Apply Changes", Color.WHITE), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i(getClass().getSimpleName(), "Applied");
                    updateResources(fancyButton, true);
                }
            });
            if (icon != 0) {
                builder.setIcon(getResources().getDrawable(icon));
            }

            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.health_cover, null);
            builder.setView(dialogView);

            Spinner excess = dialogView.findViewById(R.id.sExcess);
            String[] excessArray = getResources().getStringArray(R.array.health_cover);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                    BenefitsActivity.this, R.layout.spinner_style, excessArray);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_style);
            excess.setAdapter(spinnerArrayAdapter);

            Spinner loading = dialogView.findViewById(R.id.sLoading);
            String[] loadingArray = getResources().getStringArray(R.array.loading);
            spinnerArrayAdapter = new ArrayAdapter<>(
                    BenefitsActivity.this, R.layout.spinner_style, loadingArray);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_style);
            loading.setAdapter(spinnerArrayAdapter);

            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_blue2_transparent)));
            alertDialog.setOnShowListener(dialog -> {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
            });
            alertDialog.show();
        }
    }
}
