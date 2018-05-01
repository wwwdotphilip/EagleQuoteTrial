package app.trial.eaglequotetrial.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import app.trial.eaglequotetrial.R;
import app.trial.eaglequotetrial.model.Data;
import app.trial.eaglequotetrial.model.Provider;
import app.trial.eaglequotetrial.model.callback.RequestCallback;
import app.trial.eaglequotetrial.model.callback.ResultCallback;
import app.trial.eaglequotetrial.presenter.NewQuotePresenter;
import app.trial.eaglequotetrial.presenter.Request;
import app.trial.eaglequotetrial.presenter.ResultPresenter;

public class ResultActivity extends AppCompatActivity {
    private TextView mPremiumRange;
    private LinearLayout mProviderResults, mProviderNoResults, mParent;
    private int[] bg = {R.drawable.side_border_blue, R.drawable.side_border_red,
            R.drawable.side_border_yellow, R.drawable.side_border_green};
    private Map<String, Integer> mProviderIdentifier;
    private ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPremiumRange = findViewById(R.id.tvPremiumRange);
        mParent = findViewById(R.id.llParent);
        mProviderResults = findViewById(R.id.llResult);
        mProviderNoResults = findViewById(R.id.llNoResult);
        mLoading = findViewById(R.id.pbLoading);

        mProviderIdentifier = new HashMap<>();
        mProviderIdentifier.put("Fidelity nib", R.drawable.ic_fidelitynib);
        mProviderIdentifier.put("Sovereign", R.drawable.ic_sovereign);
        mProviderIdentifier.put("Southern Cross", R.drawable.ic_southerncross);
        mProviderIdentifier.put("Partners Life", R.drawable.ic_partnerslife);
        mProviderIdentifier.put("OnePath", R.drawable.ic_onepath);
        mProviderIdentifier.put("nib", R.drawable.ic_nib);
        mProviderIdentifier.put("Fidelity Life", R.drawable.ic_fidelity);
        mProviderIdentifier.put("Asteron", R.drawable.ic_asteron);
        mProviderIdentifier.put("AMP RPP", R.drawable.ic_amprpp);
        mProviderIdentifier.put("AMP", R.drawable.ic_amp);
        mProviderIdentifier.put("AIA", R.drawable.ic_aia);
        mProviderIdentifier.put("Accuro", R.drawable.ic_accuro);


        Request.setCallback(new RequestCallback() {
            @Override
            public void onSuccess(String result) {
                Data data = new Gson().fromJson(result, Data.class);
                ResultPresenter.setData(data);
                ResultPresenter.populateResults();
            }

            @Override
            public void onError(String error) {
                Log.e(getClass().getSimpleName(), error);
            }
        });

        ResultPresenter.setCallback(new ResultCallback() {
            @Override
            public void onRangeResult(String range) {
                runOnUiThread(() -> {
                    mLoading.setVisibility(View.GONE);
                    mParent.setVisibility(View.VISIBLE);
                    mPremiumRange.setText(range);
                });
            }

            @Override
            public void onProviderResult(List<Provider> providers) {
                runOnUiThread(() -> populateProviderView(mProviderResults, providers, false));
            }

            @Override
            public void onProviderNoResult(List<Provider> providers) {
                runOnUiThread(() -> populateProviderView(mProviderNoResults, providers, true));
            }
        });

        Request.Quote();

//        String jsonData = Device.loadJSONFromAsset(
//                ResultActivity.this, "data.json");
//        Data data = new Gson().fromJson(jsonData, Data.class);
//        ResultPresenter.setData(data);
//        ResultPresenter.populateResults();
    }

    private void populateProviderView(LinearLayout parent, List<Provider> providers, boolean greyed) {
        LayoutInflater inflater = LayoutInflater.from(ResultActivity.this);

        View[] layout = new View[providers.size()];
        ImageView[] logo = new ImageView[providers.size()];
        ImageView[] error = new ImageView[providers.size()];
        TextView[] price = new TextView[providers.size()];
        TextView[] providerName = new TextView[providers.size()];
        Random random = new Random();
        int min = 0, max = 3;

        int index = 0;
        for (Provider item : providers) {
            for (Map.Entry<String, Integer> mItem : mProviderIdentifier.entrySet()) {
                if (item.providerName.equals(mItem.getKey())) {
                    layout[index] = inflater.inflate(R.layout.result_item, null, false);
                    layout[index].setBackground(getResources().getDrawable(bg[random.nextInt(max - min + 1) + min]));
                    layout[index].setOnClickListener(v -> {
                        String errorMessage = NewQuotePresenter.getErrorMessage(item);
                        if (errorMessage != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
                            builder.setTitle(item.providerName);
                            builder.setMessage(errorMessage);
                            builder.setPositiveButton("Ok", (dialog, which) -> {

                            });
                            builder.show();
                        } else {
                            int val = mItem.getValue();
                            Intent intent = new Intent(ResultActivity.this, BreakdownActivity.class);
                            intent.putExtra("provider", new Gson().toJson(item));
                            intent.putExtra("logo", val);
                            startActivity(intent);
                        }
                    });
                    if (greyed) {
                        layout[index].setAlpha((float) 0.5);
                    }

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 20, 0, 20);

                    logo[index] = layout[index].findViewById(R.id.ivLogo);
                    logo[index].setImageDrawable(getResources().getDrawable(mItem.getValue()));
                    price[index] = layout[index].findViewById(R.id.tvPrice);
                    price[index].setText("$" + item.totalPremium);
                    providerName[index] = layout[index].findViewById(R.id.tvProviderName);
                    providerName[index].setText(item.providerName);
                    parent.addView(layout[index], layoutParams);
                    error[index] = layout[index].findViewById(R.id.ivError);
                    if (greyed) {
                        error[index].setVisibility(View.VISIBLE);
                    }
                }
            }
            index++;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
