package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class OfferPageUrl extends Url {
    public static String getName() {
        return "offer";
    }

    @Override
    public com.bloatit.web.html.pages.OfferPage createPage() throws RedirectException {
        return new com.bloatit.web.html.pages.OfferPage(this);
    }

    public OfferPageUrl(final Parameters params, final Parameters session) {
        this();
        parseParameters(params, false);
        parseParameters(session, true);
    }

    public OfferPageUrl(final com.bloatit.framework.Demand targetIdea) {
        this();
        this.targetIdea.setValue(targetIdea);
    }

    private OfferPageUrl() {
        super(getName());
    }

    private UrlParameter<com.bloatit.framework.Demand> targetIdea = new UrlParameter<com.bloatit.framework.Demand>("idea", null,
            com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
    private UrlParameter<java.math.BigDecimal> price = new UrlParameter<java.math.BigDecimal>("price", null, java.math.BigDecimal.class,
            Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
    private UrlParameter<com.bloatit.web.utils.BloatitDate> expiryDate = new UrlParameter<com.bloatit.web.utils.BloatitDate>("expiry", null,
            com.bloatit.web.utils.BloatitDate.class, Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
    private UrlParameter<java.lang.String> title = new UrlParameter<java.lang.String>("title", null, java.lang.String.class, Role.SESSION,
            Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
    private UrlParameter<java.lang.String> description = new UrlParameter<java.lang.String>("description", null, java.lang.String.class,
            Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");

    public com.bloatit.framework.Demand getTargetIdea() {
        return this.targetIdea.getValue();
    }

    public void setTargetIdea(final com.bloatit.framework.Demand arg) {
        this.targetIdea.setValue(arg);
    }

    public java.math.BigDecimal getPrice() {
        return this.price.getValue();
    }

    public void setPrice(final java.math.BigDecimal arg) {
        this.price.setValue(arg);
    }

    public com.bloatit.web.utils.BloatitDate getExpiryDate() {
        return this.expiryDate.getValue();
    }

    public void setExpiryDate(final com.bloatit.web.utils.BloatitDate arg) {
        this.expiryDate.setValue(arg);
    }

    public java.lang.String getTitle() {
        return this.title.getValue();
    }

    public void setTitle(final java.lang.String arg) {
        this.title.setValue(arg);
    }

    public java.lang.String getDescription() {
        return this.description.getValue();
    }

    public void setDescription(final java.lang.String arg) {
        this.description.setValue(arg);
    }

    @Override
    protected void doRegister() {
        register(targetIdea);
        register(price);
        register(expiryDate);
        register(title);
        register(description);
    }

    @Override
    public OfferPageUrl clone() {
        final OfferPageUrl other = new OfferPageUrl();
        other.targetIdea = this.targetIdea.clone();
        other.price = this.price.clone();
        other.expiryDate = this.expiryDate.clone();
        other.title = this.title.clone();
        other.description = this.description.clone();
        return other;
    }
}
