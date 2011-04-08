package com.bloatit.web.linkable.contribution;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;

import javax.mail.IllegalWriteException;

import com.bloatit.framework.webprocessor.WebProcess;
import com.bloatit.framework.webprocessor.WebProcess.PaymentProcess;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.model.Feature;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.web.linkable.money.PaylineProcess;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributionActionUrl;
import com.bloatit.web.url.ContributionProcessUrl;

@ParamContainer("contribution/process")
public class ContributionProcess extends PaymentProcess {

    @RequestParam
    private Feature feature;

    private BigDecimal amount  = new BigDecimal("0");
    private BigDecimal amountToPay = new BigDecimal("0");
    private BigDecimal amountToCharge = new BigDecimal("0");
    private String comment  = "";
    private final ContributionProcessUrl url;

    private boolean locked = false;

    public ContributionProcess(ContributionProcessUrl url) {
        super(url);
        this.url = url;
        feature = url.getFeature();
    }

    public String getComment() {
        return comment;
    }

    @Override
    public BigDecimal getAmountToPay() {
        return amountToPay;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setAmount(BigDecimal amount) throws IllegalWriteException {
        if (locked) {
            throw new IllegalWriteException();
        }

        this.amount = amount;
    }

    public void setAmountToPay(BigDecimal amount) throws IllegalWriteException {
        if (locked) {
            throw new IllegalWriteException();
        }

        this.amountToPay = amount;
    }


    public void setAmountToCharge(BigDecimal amount) throws IllegalWriteException {
        if (locked) {
            throw new IllegalWriteException();
        }

        this.amountToCharge = amount;
    }





    public void setComment(String comment) throws IllegalWriteException {
        if (locked) {
            throw new IllegalWriteException();
        }

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

    @Override
    public void beginSubProcess(WebProcess subProcess) {

        if (subProcess.getClass().equals(PaylineProcess.class)) {
            locked = true;
        }

    }

    @Override
    public Url endSubProcess(WebProcess subProcess) {

        if (subProcess.getClass().equals(PaylineProcess.class)) {
            if(amountToCharge.compareTo(BigDecimal.ZERO) > 0) {
                Context.getSession().notifyGood(tr("Your account has been credited."));
            }
            return new ContributionActionUrl(this);
        }

        return null;
    }

    public BigDecimal getAmountToCharge() {
        return amountToCharge;
    }

    public boolean isLocked() {
        return locked;
    }

}
