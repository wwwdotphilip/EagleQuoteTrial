package app.trial.eaglequotetrial.presenter;

import app.trial.eaglequotetrial.model.Data;

public class ResultPresenter {
    private static volatile ResultPresenter instance;
    private Data mData;

    private static ResultPresenter getInstance(){
        if (instance == null){
            synchronized (ResultPresenter.class){
                instance = new ResultPresenter();
            }
        }
        return instance;
    }

    public static void setData(Data data){
        getInstance().mData = data;
    }

    public static Data getData(){
        return getInstance().mData;
    }
}
