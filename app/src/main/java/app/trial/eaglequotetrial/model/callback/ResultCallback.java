package app.trial.eaglequotetrial.model.callback;

import java.util.List;

import app.trial.eaglequotetrial.model.Provider;

public interface ResultCallback {
    void onRangeResult(String range);
    void onProviderResult(List<Provider> providers);
    void onProviderNoResult(List<Provider> providers);
}
