package app.trial.eaglequotetrial.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import app.trial.eaglequotetrial.R;
import app.trial.eaglequotetrial.model.Client;
import app.trial.eaglequotetrial.model.ClientBreakdown;
import app.trial.eaglequotetrial.model.ProductPremium;
import app.trial.eaglequotetrial.model.Provider;
import app.trial.eaglequotetrial.presenter.NewQuotePresenter;

public class BreakdownActivity extends AppCompatActivity {
    private Provider mProvider;
    private ClientBreakdown mClientBreakdown;
    private TextView mName, mInfo, mPolicyFee, mTotalPremium;
    private ImageView mLogo, mPhoto, mError;
    private LinearLayout mPremiums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakdown);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            String breakdownData = getIntent().getStringExtra("provider");
            mProvider = new Gson().fromJson(breakdownData, Provider.class);
            for (ClientBreakdown item : mProvider.clientBreakdown) {
                if (item.clientId.equals(NewQuotePresenter.getCurrentClient().clientId)) {
                    mClientBreakdown = item;
                    break;
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        mName = findViewById(R.id.tvName);
        mInfo = findViewById(R.id.tvInfo);
        mPolicyFee = findViewById(R.id.tvPolicyFee);
        mTotalPremium = findViewById(R.id.tvTotalPremium);
        mLogo = findViewById(R.id.ivLogo);
        mPhoto = findViewById(R.id.ivPhoto);
        mPremiums = findViewById(R.id.llPremiums);

        mLogo.setImageDrawable(getResources().getDrawable(
                getIntent().getIntExtra("logo", R.drawable.ic_launcher)));

        loadPremiums();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Client currentClient = NewQuotePresenter.getCurrentClient();
        mName.setText(currentClient.name);
        String[] occupation = getResources().getStringArray(R.array.occupation);
        String gender = currentClient.gender.equals("M") ? "Male" : "Female";
        String smoker = currentClient.isSmoker ? "Smoker" : "Non-Smoker";
        String info = currentClient.age + ", " + gender + ", " + smoker + ", " +
                occupation[currentClient.occupationId - 1] + ", " + currentClient.employedStatus;
        mInfo.setText(info);
        mPolicyFee.setText("$" + mProvider.policyFee);
        mTotalPremium.setText("$" + mProvider.totalPremium);
        mPhoto.setImageDrawable(getResources().getDrawable(
                currentClient.gender.equals("M") ? R.drawable.icon_male : R.drawable.icon_female));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadPremiums() {
        ProductPremium[] productPremiums = new ProductPremium[0];
        for (ClientBreakdown clientBreakdown : mProvider.clientBreakdown) {
            if (clientBreakdown.clientId.equals(NewQuotePresenter.getCurrentClient().clientId)) {
                productPremiums = clientBreakdown.productPremiums;
                break;
            }
        }
        if (productPremiums.length > 0) {
            LayoutInflater inflater = LayoutInflater.from(BreakdownActivity.this);
            View[] layout = new View[productPremiums.length];
            TextView[] productName = new TextView[productPremiums.length];
            TextView[] benefitName = new TextView[productPremiums.length];
            TextView[] productPremiumPrice = new TextView[productPremiums.length];

            int index = 0;
            for (ProductPremium productPremium : productPremiums) {

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 20, 0, 20);

                layout[index] = inflater.inflate(R.layout.breakdown_premium, null, false);
                productName[index] = layout[index].findViewById(R.id.tvProductName);
                productName[index].setText(productPremium.productName);
                benefitName[index] = layout[index].findViewById(R.id.tvBenefitName);
                benefitName[index].setText(productPremium.benefitName);
                productPremiumPrice[index] = layout[index].findViewById(R.id.tvProductPremium);
                productPremiumPrice[index].setText("$" + productPremium.premium);
                mPremiums.addView(layout[index], layoutParams);
                index++;
            }
        }
    }
}
