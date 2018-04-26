package app.trial.eaglequotetrial.model.callback;

import app.trial.eaglequotetrial.model.HealthCover;
import app.trial.eaglequotetrial.model.LifeCover;

public interface BenefitsCallback {
    void onHealthCoverUpdate(HealthCover healthCover);
    void onLifeCoverUpdate(LifeCover lifeCover);
}
