package app.trial.eaglequotetrial.presenter;

import java.util.ArrayList;
import java.util.List;

import app.trial.eaglequotetrial.model.Data;
import app.trial.eaglequotetrial.model.Provider;
import app.trial.eaglequotetrial.model.callback.ResultCallback;

public class ResultPresenter {
    private static volatile ResultPresenter instance;
    private Data mData;
    private String mRange;
    private Provider[] mProviders;
    private ResultCallback mCallback;

    private static ResultPresenter getInstance() {
        if (instance == null) {
            synchronized (ResultPresenter.class) {
                instance = new ResultPresenter();
            }
        }
        return instance;
    }

    public static Data getData() {
        return getInstance().mData;
    }

    public static void setData(Data data) {
        getInstance().mData = data;
    }

    public static void populateResults() {
        getInstance().mProviders = getInstance().mData.data.data.result.providers;
        Double min = null;
        double max = 0;
        List<Provider> resultProvider = new ArrayList<>();
        List<Provider> noResultProvider = new ArrayList<>();
        for (Provider item : getInstance().mProviders) {
            if (item.errorSummary.length > 0) {
                noResultProvider.add(item);
            } else {
                if (min == null || item.totalPremium < min) {
                    min = item.totalPremium;
                }
                if (item.totalPremium > max) {
                    max = item.totalPremium;
                }
                resultProvider.add(item);
            }
        }
        getInstance().mRange = resultProvider.size() + " Results. $" + min + " - $" + max;
        if (getInstance().mCallback != null) {
            getInstance().mCallback.onRangeResult(getInstance().mRange);
            getInstance().mCallback.onProviderNoResult(noResultProvider);
            getInstance().mCallback.onProviderResult(resultProvider);
        }

    }

    public static void setCallback(ResultCallback callback) {
        getInstance().mCallback = callback;
    }
}
