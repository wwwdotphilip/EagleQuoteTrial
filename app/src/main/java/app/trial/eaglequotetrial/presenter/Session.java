package app.trial.eaglequotetrial.presenter;

import app.trial.eaglequotetrial.model.Data;

public class Session {
    private static volatile Session instance;
    private Data mData;

    private static Session getInstance(){
        if (instance == null){
            synchronized (Session.class){
                instance = new Session();
            }
        }
        return instance;
    }

    public static void saveSession(Data data){
        getInstance().mData = data;
    }

    public static Data getSession(){
        return getInstance().mData;
    }
}
