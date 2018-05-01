package app.trial.eaglequotetrial.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.blackcat.currencyedittext.CurrencyEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.trial.eaglequotetrial.R;
import app.trial.eaglequotetrial.model.Benefit;
import app.trial.eaglequotetrial.model.BenefitIcon;
import app.trial.eaglequotetrial.model.BenefitProductList;
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
    private Map<String, BenefitProductList> benefitProductListMap = new HashMap<>();
    private Integer mIndex = null;

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
                mIndex = 0;
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
                        if (inputItem.clientId == Integer.parseInt(NewQuotePresenter.getCurrentClient().clientId)) {
                            int j;
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
                mIndex = 1;
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
                        if (inputItem.clientId == Integer.parseInt(NewQuotePresenter.getCurrentClient().clientId)) {
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
                    removeBenefitProductList();
                    updateResources(activity, fancyButton, false, benefitIcons);
                    notifyBenefitCallback(activity, fancyButton);
                    mAlertDialog.dismiss();
                }).show();
            });
            ImageView close = dialogView.findViewById(R.id.ivClose);
            close.setOnClickListener(v -> mAlertDialog.dismiss());
        }

    }

    private void removeBenefitProductList() {
        int i = 0;
        for (Map.Entry<String, BenefitProductList> item :
                benefitProductListMap.entrySet()) {
            String key = String.valueOf(mIndex + i);
            if (key.equals(item.getKey())) {
                benefitProductListMap.remove(key);
            }
            i++;
        }
    }

    private void notifyBenefitCallback(Activity activity, FancyButton fancyButton) {
        Benefit benefit = null;
        String[] stringInt;
        switch (fancyButton.getId()) {
            case R.id.fbHealthCover:
                NewQuotePresenter.getCurrentInput().inputs.dentalOptical = dental.isChecked();
                NewQuotePresenter.getCurrentInput().inputs.specialistsTest = specialist.isChecked();
                NewQuotePresenter.getCurrentInput().inputs.gpPrescriptions = prescription.isChecked();
                NewQuotePresenter.getCurrentInput().inputs.loading = loading.getSelectedItemPosition() + 1;
                stringInt = activity.getResources().getStringArray(R.array.excess_int);
                NewQuotePresenter.getCurrentInput().inputs.excess = Integer.parseInt(stringInt[excess.getSelectedItemPosition()]);
                break;
            case R.id.fbLifeCover:
                stringInt = activity.getResources().getStringArray(R.array.loading_int);
                NewQuotePresenter.getCurrentInput().inputs.loading = Integer.parseInt(stringInt[loading.getSelectedItemPosition()]);
                NewQuotePresenter.getCurrentInput().inputs.isFutureInsurability = futureInsurability.isChecked();
                NewQuotePresenter.getCurrentInput().inputs.coverAmount = coverAmount.getRawValue();
                stringInt = activity.getResources().getStringArray(R.array.yearly_renewable_int);
                NewQuotePresenter.getCurrentInput().inputs.calcPeriod = Integer.parseInt(stringInt[renewable.getSelectedItemPosition()]);
                break;
        }
        // TODO: 4/30/18 Need clarification start
        NewQuotePresenter.getCurrentInput().inputs.benefitPeriod = 0;
        NewQuotePresenter.getCurrentInput().inputs.isAccelerated = false;
        NewQuotePresenter.getCurrentInput().inputs.frequency = 12;
        NewQuotePresenter.getCurrentInput().inputs.isLifeBuyback = false;
        NewQuotePresenter.getCurrentInput().inputs.isTpdAddon = true;
        NewQuotePresenter.getCurrentInput().inputs.benefitPeriodType = "Term";
        NewQuotePresenter.getCurrentInput().inputs.occupationType = "AnyOccupation";
        NewQuotePresenter.getCurrentInput().inputs.wopWeekWaitPeriod = 0;
        NewQuotePresenter.getCurrentInput().inputs.booster = false;
        NewQuotePresenter.getCurrentInput().inputs.isTraumaBuyback = false;
        // TODO: 4/30/18 Need clarification end

        if (mIndex != null) {
            List<BenefitProductList> temp = new ArrayList<>();
            Benefit[] benefits = NewQuotePresenter.getBenefits();
            Product[] products = NewQuotePresenter.getProducts();
            Provider[] providers = NewQuotePresenter.getProviders();
            BenefitProductList benefitProductList;
            for (Benefit item : benefits) {
                if (item.name.equals(benefitList[mIndex])) {
                    benefit = item;
                    break;
                }
            }
            if (benefit != null) {
                for (Product ptItem : products) {
                    if (ptItem.benefitId == benefit.benefitId) {
                        benefitProductList = new BenefitProductList();
                        benefitProductList.benefitId = ptItem.benefitId;
                        benefitProductList.productGroupId = ptItem.productGroupId;
                        benefitProductList.productName = ptItem.productName;
                        benefitProductList.providerId = ptItem.providerId;
                        for (Provider pItem : providers) {
                            if (pItem.providerId == benefitProductList.providerId) {
                                benefitProductList.providerName = pItem.providerName;
                                break;
                            }
                        }
                        int amount = 0;
                        if (coverAmount != null) {
                            amount = (int) coverAmount.getRawValue();
                        }
                        benefitProductList.wopCoverAmount = amount;
                        temp.add(benefitProductList);
                    }
                }
            }
            if (benefitProductListMap.size() > 0) {
                boolean skip = false;
                for (Map.Entry<String, BenefitProductList> item :
                        benefitProductListMap.entrySet()) {
                    String key = String.valueOf(benefitList[mIndex]);
                    if (item.getKey().contains(key)) {
                        skip = true;
                        break;
                    }
                }
                for (int i = 0; i < temp.size(); i++) {
                    if (!skip) {
                        String key = String.valueOf(benefitList[mIndex] + i +
                                benefitProductListMap.size());
                        benefitProductListMap.put(key, temp.get(i));
                    }
                }
            } else {
                for (int i = 0; i < temp.size(); i++) {
                    String key = String.valueOf(benefitList[mIndex] + i);
                    benefitProductListMap.put(key, temp.get(i));
                }
            }
            if (mBenefitsCallback != null) {
                mBenefitsCallback.onBenefitUpdate(benefit);
            }
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

    public boolean prepareBenefits() {
        if (benefitProductListMap.size() > 0) {
            BenefitProductList[] benefitProductList = new BenefitProductList[benefitProductListMap.size()];
            int i = 0;
            for (Map.Entry<String, BenefitProductList> item :
                    benefitProductListMap.entrySet()) {
                benefitProductList[i] = item.getValue();
                i++;
            }
            NewQuotePresenter.updateBenefitProductList(benefitProductList);
            Inputs[] inputs = NewQuotePresenter.getData().inputs;
            Log.v("Inputs", String.valueOf(inputs.length));
            return true;
        } else {
            return false;
        }
    }
}
