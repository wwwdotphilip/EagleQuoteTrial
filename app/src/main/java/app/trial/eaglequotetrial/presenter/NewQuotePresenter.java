package app.trial.eaglequotetrial.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import app.trial.eaglequotetrial.model.Benefit;
import app.trial.eaglequotetrial.model.BenefitProductList;
import app.trial.eaglequotetrial.model.Client;
import app.trial.eaglequotetrial.model.ClientBreakdown;
import app.trial.eaglequotetrial.model.Data;
import app.trial.eaglequotetrial.model.Input;
import app.trial.eaglequotetrial.model.Inputs;
import app.trial.eaglequotetrial.model.Product;
import app.trial.eaglequotetrial.model.ProductPremium;
import app.trial.eaglequotetrial.model.Provider;

public class NewQuotePresenter {
    private static final String NAME = "eaglequote";
    private static final String BENEFIT_KEY = "benefit";
    private static final String PROVIDER_KEY = "provider";
    private static final String PRODUCT_KEY = "product";
    private static volatile NewQuotePresenter instance;
    private Data mData;
    private Benefit[] benefits;
    private Product[] products;
    private Provider[] providers;
    private SharedPreferences pref;
    private Client currentClient;

    private static NewQuotePresenter getInstance() {
        if (instance == null) {
            synchronized (NewQuotePresenter.class) {
                instance = new NewQuotePresenter();
                instance.mData = new Data();
            }
        }
        return instance;
    }

    public static Data getData() {
        return getInstance().mData;
    }

    public static Client[] getClients() {
        return getInstance().mData.clients;
    }

    public static void addClient(Client client) {
        if (getInstance().mData.clients != null) {
            int length = getInstance().mData.clients.length + 1;
            Client[] tempClient = getInstance().mData.clients;
            getInstance().mData.clients = new Client[length];
            for (int i = 0; i < length - 1; i++) {
                getInstance().mData.clients[i] = tempClient[i];
            }
            getInstance().mData.clients[length - 1] = client;
        } else {
            instance.mData.clients = new Client[1];
            getInstance().mData.clients[0] = client;
        }
        getInstance().currentClient = client;
    }

    public static void clearClient(){
        getInstance().mData.clients = null;
        getInstance().currentClient = null;
    }

    public static Inputs getCurrentInput() {
        if (getInstance().mData.inputs == null) {
            getInstance().mData.inputs = new Inputs[getClients().length];
            for (int i = 0; i < getClients().length; i++) {
                getInstance().mData.inputs[i] = new Inputs();
                getInstance().mData.inputs[i].inputs = new Input();
                getInstance().mData.inputs[i].clientId = Integer.parseInt(getClients()[i].clientId);
            }
        }
        for (int j = 0; j < getInstance().mData.inputs.length; j++) {
            if (getCurrentClient().clientId.equals(String.valueOf(
                    getInstance().mData.inputs[j].clientId))) {
                return getInstance().mData.inputs[j];
            }
        }
        return getInstance().mData.inputs[0];
    }

    public static Client getCurrentClient() {
        return getInstance().currentClient;
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

    public static void updateBenefitProductList(BenefitProductList[] benefitProductLists) {
        if (getInstance().mData.inputs != null) {
            int index = 0;
            for (Inputs item : getInstance().mData.inputs) {
                if (String.valueOf(item.clientId).equals(getCurrentClient().clientId)) {
                    getInstance().mData.inputs[index].inputs.benefitProductList = benefitProductLists;
                }
                index++;
            }
        }
    }

    public static void storeData(Context context) {
        getInstance().pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        if (getInstance().benefits != null) {
            getInstance().pref.edit().putString(BENEFIT_KEY,
                    new Gson().toJson(getInstance().benefits)).apply();
        }
        if (getInstance().products != null) {
            getInstance().pref.edit().putString(PRODUCT_KEY,
                    new Gson().toJson(getInstance().products)).apply();
        }
        if (getInstance().providers != null) {
            getInstance().pref.edit().putString(PROVIDER_KEY,
                    new Gson().toJson(getInstance().providers)).apply();
        }
    }

    public static void loadData(Context context) {
        getInstance().pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String benefit = getInstance().pref.getString(BENEFIT_KEY, null);
        String product = getInstance().pref.getString(PRODUCT_KEY, null);
        String provider = getInstance().pref.getString(PROVIDER_KEY, null);
        if (benefit != null) {
            getInstance().benefits = new Gson().fromJson(benefit, Benefit[].class);
        }
        if (product != null) {
            getInstance().products = new Gson().fromJson(product, Product[].class);
        }
        if (provider != null) {
            getInstance().providers = new Gson().fromJson(provider, Provider[].class);
        }
    }

    public static String getErrorMessage(Provider provider) {
        String error = null;
        for (ClientBreakdown clientBreakdown : provider.clientBreakdown) {
            if (clientBreakdown.clientId.equals(getCurrentClient().clientId)) {
                for (ProductPremium productPremium :
                        clientBreakdown.productPremiums) {
                    error = productPremium.errorMessage;
                    if (error != null && error.isEmpty()) {
                        error = null;
                    }
                }
            }
        }
        return error;
    }

    public static void clearInputs(){
        getInstance().mData.inputs = null;
    }

    public static void destroy(){
        instance = null;
    }
}
