package app.trial.eaglequotetrial.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import app.trial.eaglequotetrial.R;
import app.trial.eaglequotetrial.model.Client;
import app.trial.eaglequotetrial.presenter.NewQuotePresenter;
import app.trial.eaglequotetrial.presenter.Session;

public class AddClientActivity extends AppCompatActivity {
    private Spinner mAgeSpinner, mGenderSpinner, mOccupationSpinner, mEmploymentStatusSpinner;
    private Switch mSmoker;
    private EditText mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NewQuotePresenter.clearClient();
        mName = findViewById(R.id.etName);
        mAgeSpinner = findViewById(R.id.sAge);
        mGenderSpinner = findViewById(R.id.sGender);
        mOccupationSpinner = findViewById(R.id.sOccupation);
        mEmploymentStatusSpinner = findViewById(R.id.sEmploymentStatus);
        mSmoker = findViewById(R.id.sSmoker);

        populateView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateView() {
        List<Integer> spinnerArray = new ArrayList<>();
        for (int i = 0; i < 76; i++) {
            spinnerArray.add(i);
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAgeSpinner.setAdapter(adapter);
    }

    public void showOccupationalGuide(View view) {
        startActivity(new Intent(this, OccupationalGuide.class));
    }

    public void next(View view) {
        Client client = new Client();
        client.age = mAgeSpinner.getSelectedItem().toString();
        client.gender = mGenderSpinner.getSelectedItem().toString().substring(0, 1);
        client.clientId = "1";
        client.employedStatus = mEmploymentStatusSpinner.getSelectedItem().toString();
        client.isChild = false;
        client.isPrimary = true;
        client.isSmoker = mSmoker.isChecked();
        client.name = mName.getText().toString();
        client.occupationId = mOccupationSpinner.getSelectedItemPosition() + 1;

        NewQuotePresenter.addClient(client);
        NewQuotePresenter.getData().userId = Session.getSession().user.id;
        NewQuotePresenter.getData().quoteId = 0;

        startActivity(new Intent(this, BenefitsActivity.class));
    }
}
