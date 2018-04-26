package app.trial.eaglequotetrial.model;

public class Client {
    private static volatile Client instance;
    private Details mDetails;
    private Benefits mBenefits;

    private static Client getInstance(){
        if (instance == null){
            synchronized (Client.class){
                instance = new Client();
            }
        }
        return instance;
    }

    public static void destroyClient(){
        instance = null;
    }

    public static void setClientDetails(Details details){
        getInstance().mDetails = details;
    }

    public static void setBenefits(Benefits benefits){
        getInstance().mBenefits = benefits;
    }

    public static Details getClientDetails(){
        return getInstance().mDetails;
    }

    public static Benefits getBenefits(){
        return getInstance().mBenefits;
    }

    public static class Details{
        public String name;
        public int age;
        public String gender;
        public String occupation;
        public String employmentStatus;
        public boolean smoker;
    }
}
