package app.trial.eaglequotetrial.model;

public class Provider {
    public int providerId;
    public String providerName;
    public Product product;
    public double policyFee;
    public double totalPremium;
    public ClientBreakdown[] clientBreakdown;
    public ErrorSummary[] errorSummary;
    public ProviderPremium providerPremiums;
    public int errorId;
    //todo There are two status object in Provider a boolean and int
    // From get provider https://staging.blackfin.technology/mobile/provider
    // Update quote https://staging.blackfin.technology/mobile/quote
}
