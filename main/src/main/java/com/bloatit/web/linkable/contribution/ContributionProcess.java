package com.bloatit.web.linkable.contribution;

import java.math.BigDecimal;

import javax.mail.IllegalWriteException;

import com.bloatit.framework.webprocessor.PaymentProcess;
import com.bloatit.framework.webprocessor.WebProcess;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Feature;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.web.linkable.money.PaylineProcess;
import com.bloatit.web.url.CheckContributionPageUrl;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributionActionUrl;
import com.bloatit.web.url.ContributionProcessUrl;

@ParamContainer("contribution/process")
public class ContributionProcess extends PaymentProcess {

    @RequestParam
    private Feature feature;

    private BigDecimal amount = new BigDecimal("0");
    private String comment = "";

    public ContributionProcess(final ContributionProcessUrl url) {
        super(url);
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

    public void setAmount(final BigDecimal amount) throws IllegalWriteException {
        if (isLocked()) {
            throw new IllegalWriteException();
        }
        this.amount = amount;
    }

    public void setComment(final String comment) throws IllegalWriteException {
        if (isLocked()) {
            throw new IllegalWriteException();
        }
        this.comment = comment;
    }

    @Override
    protected Url doProcess() {
        return new ContributePageUrl(this);
    }

    @Override
    public void doDoLoad() {
        feature = FeatureManager.getFeatureById(feature.getId());
    }

    @Override
    public Url notifyChildClosed(final WebProcess subProcess) {
        if (subProcess.getClass().equals(PaylineProcess.class)) {
            final PaylineProcess subPro = (PaylineProcess) subProcess;
            if (subPro.isSuccessful()) {
                // Redirects to the contribution action which will perform the
                // actual contribution
                return new ContributionActionUrl(this);
            }
            unlock();
            return new CheckContributionPageUrl(this);
        }
        return null;
    }
}
