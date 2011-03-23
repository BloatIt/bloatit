package com.bloatit.web.linkable.contribution;

import java.math.BigDecimal;

import com.bloatit.framework.webserver.WebProcess;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Feature;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributionProcessUrl;

@ParamContainer("contribution/process")
public class ContributionProcess extends WebProcess {

    @RequestParam
    private Feature feature;

    private BigDecimal amount = null;
    private String comment = null;

    private final ContributionProcessUrl url;

    public ContributionProcess(ContributionProcessUrl url) {
        super(url);
        this.url = url;
        feature = url.getFeature();
    }

    public String getComment() {
        return comment;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    protected Url doProcess() {
        return new ContributePageUrl(this);
    }

    @Override
    protected Url doProcessErrors() {
        session.notifyList(url.getMessages());
        return session.getLastVisitedPage();
    }

    @Override
    public void load() {
        feature = FeatureManager.getFeatureById(feature.getId());
    }

}
