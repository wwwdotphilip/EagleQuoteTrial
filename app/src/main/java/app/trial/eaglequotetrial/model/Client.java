package app.trial.eaglequotetrial.model;

public class Client {
    private static volatile Client instance;
    private Details mDetails;

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

    public static void addClientDetails(Details details){
        getInstance().mDetails = details;
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
