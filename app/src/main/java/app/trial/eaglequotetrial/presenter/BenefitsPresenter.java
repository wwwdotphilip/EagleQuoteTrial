package app.trial.eaglequotetrial.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.blackcat.currencyedittext.CurrencyEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.trial.eaglequotetrial.R;
import app.trial.eaglequotetrial.model.Benefit;
import app.trial.eaglequotetrial.model.BenefitIcon;
import app.trial.eaglequotetrial.model.BenefitProductList;
import app.trial.eaglequotetrial.model.Input;
import app.trial.eaglequotetrial.model.Inputs;
import app.trial.eaglequotetrial.model.Product;
import app.trial.eaglequotetrial.model.Provider;
import app.trial.eaglequotetrial.model.callback.BenefitsCallback;
import mehdi.sakout.fancybuttons.FancyButton;

public class BenefitsPresenter {
    private ImageView benefitIcon;
    private TextView benefitName;
    private CurrencyEditText coverAmount;
    private Switch specialist, prescription, dental, index, futureInsurability;
    private Spinner excess;
    private Spinner loading;
    private Spinner renewable;
    private BenefitsCallback mBenefitsCallback;
    private AlertDialog mAlertDialog;
    private String[] benefitList = {"Health Cover", "Life Cover"};
    private int mIcon = 0;
    private Inputs mInputs = new Inputs();
    private Input mInput = new Input();

    public void loadDialog(Activity activity, FancyButton fancyButton,
                           List<BenefitIcon> benefitIcons) {
        for (BenefitIcon item : benefitIcons) {
            if (item.id == fancyButton.getId()) {
                mIcon = item.selectedDrawable;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if (mIcon != 0) {
            builder.setIcon(activity.getResources().getDrawable(mIcon));
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

                benefitIcon = dialogView.findViewById(R.id.ivBenefitLogo);
                benefitIcon.setImageDrawable(activity.getResources().getDrawable(mIcon));
                benefitName = dialogView.findViewById(R.id.tvBenefitName);
                benefitName.setText(benefitList[0]);

                specialist = dialogView.findViewById(R.id.sSpecialist);
                prescription = dialogView.findViewById(R.id.sPrescription);
                dental = dialogView.findViewById(R.id.sDental);

                excess = dialogView.findViewById(R.id.sExcess);
                excess.setAdapter(createSpinnerArrayAdapter(activity,
                        activity.getResources().getStringArray(R.array.excess)));

                loading = dialogView.findViewById(R.id.sLoading);
                loading.setAdapter(createSpinnerArrayAdapter(activity,
                        activity.getResources().getStringArray(R.array.loading)));

                if (NewQuotePresenter.getData().inputs != null) {
                    Inputs[] inputs = NewQuotePresenter.getData().inputs;
                    for (Inputs inputItem : inputs) {
                        if (inputItem.clientId == Integer.parseInt(NewQuotePresenter.getClient().clientId)) {
                            int j;
                            mInput = inputItem.inputs;
                            specialist.setChecked(inputItem.inputs.specialistsTest);
                            prescription.setChecked(inputItem.inputs.gpPrescriptions);
                            dental.setChecked(inputItem.inputs.dentalOptical);

                            String[] arrayList = activity.getResources().getStringArray(R.array.excess_int);
                            for (j = 0; j < arrayList.length; j++) {
                                if (Integer.parseInt(arrayList[j]) == inputItem.inputs.excess) {
                                    excess.setSelection(j);
                                }
                            }
                            loading.setSelection((int) inputItem.inputs.loading - 1);
                            specialist.setChecked(inputItem.inputs.specialistsTest);
                            break;
                        }
                    }
                }
                break;
            case R.id.fbLifeCover:
                dialogView = inflater.inflate(R.layout.life_cover, null);
                builder.setView(dialogView);

                benefitIcon = dialogView.findViewById(R.id.ivBenefitLogo);
                benefitIcon.setImageDrawable(activity.getResources().getDrawable(mIcon));
                benefitName = dialogView.findViewById(R.id.tvBenefitName);
                benefitName.setText(benefitList[1]);

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

                if (NewQuotePresenter.getData().inputs != null) {
                    Inputs[] inputs = NewQuotePresenter.getData().inputs;
                    for (Inputs inputItem : inputs) {
                        if (inputItem.clientId == Integer.parseInt(NewQuotePresenter.getClient().clientId)) {
                            mInput = inputItem.inputs;
                            futureInsurability.setChecked(inputItem.inputs.isFutureInsurability);
                            coverAmount.setValue((long) inputItem.inputs.coverAmount);
                            renewable.setSelection(inputItem.inputs.calcPeriod);
                            loading.setSelection((int) inputItem.inputs.loading - 1);
                            break;
                        }
                    }
                }
                break;
            default:
                builder.setNegativeButton("Close", (dialog, which) -> {
                });
                break;
        }

        if (dialogView != null) {
            FancyButton applyChanges = dialogView.findViewById(R.id.btnApplyChanges);
            applyChanges.setOnClickListener(v -> {
                updateResources(activity, fancyButton, true, benefitIcons);
                notifyBenefitCallback(activity, fancyButton);
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
                    notifyBenefitCallback(activity, fancyButton);
                    mAlertDialog.dismiss();
                }).show();
            });
            ImageView close = dialogView.findViewById(R.id.ivClose);
            close.setOnClickListener(v -> mAlertDialog.dismiss());
        }

    }

    private void notifyBenefitCallback(Activity activity, FancyButton fancyButton) {
        Benefit benefit = null;
        Integer index = null;
        String[] stringInt;
        switch (fancyButton.getId()) {
            case R.id.fbHealthCover:
                index = 0;
                mInput.dentalOptical = dental.isChecked();
                mInput.specialistsTest = specialist.isChecked();
                mInput.gpPrescriptions = prescription.isChecked();
                mInput.loading = loading.getSelectedItemPosition() + 1;
                stringInt = activity.getResources().getStringArray(R.array.excess_int);
                mInput.excess = Integer.parseInt(stringInt[excess.getSelectedItemPosition()]);
                break;
            case R.id.fbLifeCover:
                index = 1;
                stringInt = activity.getResources().getStringArray(R.array.loading_int);
                mInput.loading = Integer.parseInt(stringInt[loading.getSelectedItemPosition()]);
                mInput.isFutureInsurability = futureInsurability.isChecked();
                mInput.coverAmount = coverAmount.getRawValue();
                stringInt = activity.getResources().getStringArray(R.array.yearly_renewable_int);
                mInput.calcPeriod = Integer.parseInt(stringInt[renewable.getSelectedItemPosition()]);
                break;
        }
        // TODO: 4/30/18 Need clarification start
        mInput.benefitPeriod = 0;
        mInput.isAccelerated = false;
        mInput.frequency = 12;
        mInput.isLifeBuyback = true;
        mInput.isTpdAddon = true;
        mInput.benefitPeriodType = "Term";
        mInput.occupationType = "AnyOccupation";
        mInput.wopWeekWaitPeriod = 0;
        mInput.booster = false;
        mInput.isTraumaBuyback = false;
        // TODO: 4/30/18 Need clarification end

        if (index != null) {
            for (Benefit item : NewQuotePresenter.getBenefits()) {
                if (item.name.equals(benefitList[index])) {
                    benefit = item;
                    List<BenefitProductList> bpList = new ArrayList<>();
                    BenefitProductList benefitProductList = new BenefitProductList();
                    for (Product ptItem : NewQuotePresenter.getProducts()) {
                        if (ptItem.benefitId == item.benefitId) {
                            benefitProductList.benefitId = ptItem.benefitId;
                            benefitProductList.productGroupId = ptItem.productGroupId;
                            benefitProductList.productName = ptItem.productName;
                            benefitProductList.providerId = ptItem.providerId;
                            for (Provider pItem : NewQuotePresenter.getProviders()) {
                                if (pItem.providerId == benefitProductList.providerId) {
                                    benefitProductList.providerName = pItem.providerName;
                                }
                            }
                            int amount = 0;
                            if (coverAmount != null) {
                                amount = (int) coverAmount.getRawValue();
                            }
                            benefitProductList.wopCoverAmount = amount;
                            bpList.add(benefitProductList);
                        }
                    }
                    mInput.benefitProductList = new BenefitProductList[bpList.size()];
                    for (int j = 0; j < bpList.size(); j++) {
                        mInput.benefitProductList[j] = bpList.get(j);
                    }
                    break;
                }
            }
            mInputs.inputs = mInput;
            if (mBenefitsCallback != null) {
                mBenefitsCallback.onBenefitUpdate(benefit);
            }
        }

        mInputs.clientId = Integer.parseInt(NewQuotePresenter.getClient().clientId);
        NewQuotePresenter.overrideInput(mInputs);
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
