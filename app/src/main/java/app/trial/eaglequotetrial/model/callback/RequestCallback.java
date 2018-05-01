package app.trial.eaglequotetrial.model.callback;

public interface RequestCallback {
    void onSuccess(String result);
    void onError(String error);
}
