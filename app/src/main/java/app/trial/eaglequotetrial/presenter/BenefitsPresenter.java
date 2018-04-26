package app.trial.eaglequotetrial.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import com.blackcat.currencyedittext.CurrencyEditText;

import java.util.List;
import java.util.Locale;

import app.trial.eaglequotetrial.R;
import app.trial.eaglequotetrial.model.BenefitIcon;
import app.trial.eaglequotetrial.model.Benefits;
import app.trial.eaglequotetrial.model.HealthCover;
import app.trial.eaglequotetrial.model.LifeCover;
import app.trial.eaglequotetrial.model.callback.BenefitsCallback;
import mehdi.sakout.fancybuttons.FancyButton;

public class BenefitsPresenter {
    private CurrencyEditText coverAmount;
    private Switch specialist, prescription, dental, index, futureInsurability;
    private Spinner excess;
    private Spinner loading;
    private Spinner renewable;
    private BenefitsCallback mBenefitsCallback;
    private Benefits mBenefits;

    public void loadDialog(Activity activity, FancyButton fancyButton,
                           List<BenefitIcon> benefitIcons, Benefits benefits) {
        mBenefits = benefits;
        int icon = 0;
        for (BenefitIcon item : benefitIcons) {
            if (item.id == fancyButton.getId()) {
                icon = item.selectedDrawable;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(customText(fancyButton.getText().toString(), Color.WHITE));
        if (icon != 0) {
            builder.setIcon(activity.getResources().getDrawable(icon));
        }

        loadBenefitsUI(activity, builder, fancyButton, benefitIcons);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.app_blue2_transparent)));
        alertDialog.setOnShowListener(dialog -> {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
        });
        alertDialog.show();
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

    private void loadBenefitsUI(Activity activity, AlertDialog.Builder builder,
                                FancyButton fancyButton, List<BenefitIcon> benefitIcons) {

        LayoutInflater inflater;
        View dialogView;
        inflater = activity.getLayoutInflater();

        builder.setNegativeButton(customText("Cancel", Color.WHITE), (dialog, which) -> {

        });

        builder.setNeutralButton(customText("Clear", Color.WHITE), (dialog, which) -> {
            AlertDialog.Builder confirmation = new AlertDialog.Builder(activity);
            confirmation.setTitle("Are you sure?");
            confirmation.setIcon(activity.getResources().getDrawable(R.drawable.ic_error));
            confirmation.setMessage("You cannot revert once you clear this content.");
            confirmation.setNegativeButton("No", (dialog12, which12) -> {
                loadDialog(activity, fancyButton, benefitIcons, mBenefits);
            });
            confirmation.setPositiveButton("Yes", (dialog1, which1) -> {
                updateResources(activity, fancyButton, false, benefitIcons);
                switch (fancyButton.getId()) {
                    case R.id.fbHealthCover:
                        mBenefits.healthCover = null;
                        if (mBenefitsCallback != null){
                            mBenefitsCallback.onHealthCoverUpdate(null);
                        }
                        break;
                    case R.id.fbLifeCover:
                        mBenefits.lifeCover = null;
                        if (mBenefitsCallback != null){
                            mBenefitsCallback.onLifeCoverUpdate(null);
                        }
                        break;
                }
            }).show();
        });

        builder.setPositiveButton(customText("Apply Changes", Color.WHITE), (dialog, which) -> {
            updateResources(activity, fancyButton, true, benefitIcons);
            switch (fancyButton.getId()) {
                case R.id.fbHealthCover:
                    HealthCover healthCover = new HealthCover();
                    healthCover.excess = excess.getSelectedItem().toString();
                    healthCover.dentalAndOptical = dental.isChecked();
                    healthCover.gpAndPrescriptions = prescription.isChecked();
                    healthCover.loading = loading.getSelectedItem().toString();
                    healthCover.specialistAndTest = specialist.isChecked();
                    mBenefits.healthCover = healthCover;
                    if (mBenefitsCallback != null){
                        mBenefitsCallback.onHealthCoverUpdate(mBenefits.healthCover);
                    }
                    break;
                case R.id.fbLifeCover:
                    LifeCover lifeCover = new LifeCover();
                    lifeCover.coverAmount = coverAmount.getRawValue();
                    lifeCover.futureInsurability = futureInsurability.isChecked();
                    lifeCover.index = index.isChecked();
                    lifeCover.loading = loading.getSelectedItem().toString();
                    lifeCover.yearlyRenewable = renewable.getSelectedItem().toString();
                    mBenefits.lifeCover = lifeCover;
                    if (mBenefitsCallback != null){
                        mBenefitsCallback.onLifeCoverUpdate(mBenefits.lifeCover);
                    }
                    break;
            }
        });

        switch (fancyButton.getId()) {
            case R.id.fbHealthCover:
                HealthCover healthCover = null;
                try {
                    healthCover = mBenefits.healthCover;
                } catch (Exception ignore) {
                }
                dialogView = inflater.inflate(R.layout.health_cover, null);
                builder.setView(dialogView);

                specialist = dialogView.findViewById(R.id.sSpecialist);
                prescription = dialogView.findViewById(R.id.sPrescription);
                dental = dialogView.findViewById(R.id.sDental);

                excess = dialogView.findViewById(R.id.sExcess);
                excess.setAdapter(createSpinnerArrayAdapter(activity,
                        activity.getResources().getStringArray(R.array.excess)));

                loading = dialogView.findViewById(R.id.sLoading);
                loading.setAdapter(createSpinnerArrayAdapter(activity,
                        activity.getResources().getStringArray(R.array.loading)));
                if (healthCover != null) {
                    specialist.setChecked(healthCover.specialistAndTest);
                    prescription.setChecked(healthCover.gpAndPrescriptions);
                    dental.setChecked(healthCover.dentalAndOptical);
                    int i = 0;
                    for (String item : activity.getResources().getStringArray(R.array.excess)) {
                        if (item.equals(healthCover.excess)) {
                            excess.setSelection(i);
                        }
                        i++;
                    }
                    i = 0;
                    for (String item : activity.getResources().getStringArray(R.array.loading)) {
                        if (item.equals(healthCover.loading)) {
                            loading.setSelection(i);
                        }
                        i++;
                    }
                }
                break;
            case R.id.fbLifeCover:
                LifeCover lifeCover = null;
                try {
                    lifeCover = mBenefits.lifeCover;
                } catch (Exception ignore) {
                }
                dialogView = inflater.inflate(R.layout.life_cover, null);
                builder.setView(dialogView);

                index = dialogView.findViewById(R.id.sIndex);
                futureInsurability = dialogView.findViewById(R.id.sFutureInsurability);

                coverAmount = dialogView.findViewById(R.id.etCoverAmount);
                coverAmount.setLocale(Locale.US);

                renewable = dialogView.findViewById(R.id.sRenewable);
                renewable.setAdapter(createSpinnerArrayAdapter(activity,
                        activity.getResources().getStringArray(R.array.yearly_renewable)));

                loading = dialogView.findViewById(R.id.sLoading);
                loading.setAdapter(createSpinnerArrayAdapter(activity,
                        activity.getResources().getStringArray(R.array.loading)));
                if (lifeCover != null) {
                    index.setChecked(lifeCover.index);
                    futureInsurability.setChecked(lifeCover.futureInsurability);
                    coverAmount.setValue(lifeCover.coverAmount);
                    int i = 0;
                    for (String item : activity.getResources().getStringArray(R.array.yearly_renewable)) {
                        if (item.equals(lifeCover.yearlyRenewable)) {
                            renewable.setSelection(i);
                        }
                        i++;
                    }
                    i = 0;
                    for (String item : activity.getResources().getStringArray(R.array.loading)) {
                        if (item.equals(lifeCover.loading)) {
                            loading.setSelection(i);
                        }
                        i++;
                    }
                }
                break;
        }
    }

    private void updateResources(Activity activity, FancyButton fancyButton,
                                 boolean selected, List<BenefitIcon> benefitsIcons) {
        for (BenefitIcon item : benefitsIcons) {
            if (item.id == fancyButton.getId()) {
                fancyButton.setBorderColor(selected ?
                        activity.getResources().getColor(R.color.app_blue) :
                        activity.getResources().getColor(R.color.color_class_content));
                fancyButton.setTextColor(selected ?
                        activity.getResources().getColor(R.color.app_blue) :
                        activity.getResources().getColor(R.color.app_blue2));
                fancyButton.setIconResource(selected ?
                        activity.getResources().getDrawable(item.selectedDrawable) :
                        activity.getResources().getDrawable(item.unselectedDrawable));
            }
        }
    }

    private ArrayAdapter<String> createSpinnerArrayAdapter(Activity activity, String[] arrayResource) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                activity, R.layout.spinner_style, arrayResource);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_style);
        return spinnerArrayAdapter;
    }

    public void setCallback(BenefitsCallback benefitsCallback){
        mBenefitsCallback = benefitsCallback;
    }
}
