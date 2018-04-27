package app.trial.eaglequotetrial.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import com.blackcat.currencyedittext.CurrencyEditText;

import java.util.List;
import java.util.Locale;

import app.trial.eaglequotetrial.R;
import app.trial.eaglequotetrial.model.BenefitIcon;
import app.trial.eaglequotetrial.model.callback.BenefitsCallback;
import mehdi.sakout.fancybuttons.FancyButton;

public class BenefitsPresenter {
    private CurrencyEditText coverAmount;
    private Switch specialist, prescription, dental, index, futureInsurability;
    private Spinner excess;
    private Spinner loading;
    private Spinner renewable;
    private BenefitsCallback mBenefitsCallback;
    private AlertDialog mAlertDialog;

    public void loadDialog(Activity activity, FancyButton fancyButton,
                           List<BenefitIcon> benefitIcons) {
        int icon = 0;
        for (BenefitIcon item : benefitIcons) {
            if (item.id == fancyButton.getId()) {
                icon = item.selectedDrawable;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if (icon != 0) {
            builder.setIcon(activity.getResources().getDrawable(icon));
        }

        loadBenefitsUI(activity, builder, fancyButton, benefitIcons);

        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void loadBenefitsUI(Activity activity, AlertDialog.Builder builder,
                                FancyButton fancyButton, List<BenefitIcon> benefitIcons) {

        LayoutInflater inflater;
        View dialogView = null;
        inflater = activity.getLayoutInflater();

        switch (fancyButton.getId()) {
            case R.id.fbHealthCover:
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
                break;
            case R.id.fbLifeCover:
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
                break;
            default:
                builder.setNegativeButton("Close", (dialog, which) -> {});
                break;
        }
        if (dialogView != null) {
            FancyButton applyChanges = dialogView.findViewById(R.id.btnApplyChanges);
            applyChanges.setOnClickListener(v -> {
                updateResources(activity, fancyButton, true, benefitIcons);
                switch (fancyButton.getId()) {
                    case R.id.fbHealthCover:
                        if (mBenefitsCallback != null) {
                            mBenefitsCallback.onHealthCoverUpdate();
                        }
                        break;
                    case R.id.fbLifeCover:
                        if (mBenefitsCallback != null) {
                            mBenefitsCallback.onLifeCoverUpdate();
                        }
                        break;
                }
                mAlertDialog.dismiss();
            });
            FancyButton remove = dialogView.findViewById(R.id.btnRemove);
            remove.setOnClickListener(v -> {
                AlertDialog.Builder confirmation = new AlertDialog.Builder(activity);
                confirmation.setTitle("Are you sure?");
                confirmation.setIcon(activity.getResources().getDrawable(R.drawable.ic_error));
                confirmation.setMessage("You cannot undo this.");
                confirmation.setNegativeButton("No", (dialog, which) -> {
                });
                confirmation.setPositiveButton("Yes", (dialog1, which1) -> {
                    updateResources(activity, fancyButton, false, benefitIcons);
                    switch (fancyButton.getId()) {
                        case R.id.fbHealthCover:
                            if (mBenefitsCallback != null) {
                                mBenefitsCallback.onHealthCoverUpdate();
                            }
                            break;
                        case R.id.fbLifeCover:
                            if (mBenefitsCallback != null) {
                                mBenefitsCallback.onLifeCoverUpdate();
                            }
                            break;
                    }
                    mAlertDialog.dismiss();
                }).show();
            });
            ImageView close = dialogView.findViewById(R.id.ivClose);
            close.setOnClickListener(v -> mAlertDialog.dismiss());
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

    public void setCallback(BenefitsCallback benefitsCallback) {
        mBenefitsCallback = benefitsCallback;
    }
}
