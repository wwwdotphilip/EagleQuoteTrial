package app.trial.eaglequotetrial.presenter;

import app.trial.eaglequotetrial.model.Benefit;
import app.trial.eaglequotetrial.model.Client;
import app.trial.eaglequotetrial.model.Data;
import app.trial.eaglequotetrial.model.Inputs;
import app.trial.eaglequotetrial.model.Product;
import app.trial.eaglequotetrial.model.Provider;

public class NewQuotePresenter {
    private static volatile NewQuotePresenter instance;
    private Data mData;
    private Benefit[] benefits;
    private Product[] products;
    private Provider[] providers;

    private static NewQuotePresenter getInstance() {
        if (instance == null) {
            synchronized (NewQuotePresenter.class) {
                instance = new NewQuotePresenter();
                instance.mData = new Data();
                instance.mData.clients = new Client[1];
            }
        }
        return instance;
    }

    public static Data getData() {
        return getInstance().mData;
    }

    public static Client getClient() {
        return getInstance().mData.clients[0];
    }

    public static void setClient(Client client) {
        getInstance().mData.clients[0] = client;
    }

    public static Benefit[] getBenefits() {
        return getInstance().benefits;
    }

    public static void setBenefits(Benefit[] benefits) {
        getInstance().benefits = benefits;
    }

    public static Product[] getProducts() {
        return getInstance().products;
    }

    public static void setProducts(Product[] products) {
        getInstance().products = products;
    }

    public static Provider[] getProviders() {
        return getInstance().providers;
    }

    public static void setProviders(Provider[] providers) {
        getInstance().providers = providers;
    }

    public static void addInput(Inputs input) {
        if (getInstance().mData.inputs != null) {
            Inputs[] tempInputs = getInstance().mData.inputs;
            getInstance().mData.inputs = new Inputs[tempInputs.length + 1];
            for (int i = 0; i < tempInputs.length; i++) {
                getInstance().mData.inputs[i] = tempInputs[i];
            }
            getInstance().mData.inputs[tempInputs.length] = input;
        } else {
            getInstance().mData.inputs = new Inputs[1];
            getInstance().mData.inputs[0] = input;
        }
    }

    public static void overrideInput(Inputs inputs) {
        if (getInstance().mData.inputs != null) {
            for (int i = 0; i < getInstance().mData.inputs.length; i++) {
                if (getInstance().mData.inputs[i].clientId == inputs.clientId) {
                    getInstance().mData.inputs[i] = inputs;
                }
            }
        } else {
            addInput(inputs);
        }
    }
}
