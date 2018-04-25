package app.trial.eaglequotetrial.model;

public class Benefits {
    public HealthCover healthCover;

    public class HealthCover {
        public String excess;
        public boolean specialistAndTest;
        public boolean gpAndPrescriptions;
        public boolean dentalAndOptical;
        public String loading;
    }
}
