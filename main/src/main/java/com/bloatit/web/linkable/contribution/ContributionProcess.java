package com.bloatit.web.linkable.contribution;

import java.math.BigDecimal;

import javax.mail.IllegalWriteException;

import com.bloatit.framework.webprocessor.PaymentProcess;
import com.bloatit.framework.webprocessor.WebProcess;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Feature;
import com.bloatit.model.Team;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.model.managers.TeamManager;
import com.bloatit.web.linkable.money.PaylineProcess;
import com.bloatit.web.url.CheckContributionPageUrl;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributionActionUrl;
import com.bloatit.web.url.ContributionProcessUrl;

@ParamContainer("contribution/process")
public class ContributionProcess extends PaymentProcess {

    @RequestParam
    private Feature feature;

    private Team team;
    private BigDecimal amount = new BigDecimal("0");
    private BigDecimal amountToPay = new BigDecimal("0");
    private BigDecimal amountToCharge = new BigDecimal("0");
    private String comment = "";
    private final ContributionProcessUrl url;

    private boolean locked = false;

    public ContributionProcess(final ContributionProcessUrl url) {
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

    public void setAmount(final BigDecimal amount) throws IllegalWriteException {
        if (locked) {
            throw new IllegalWriteException();
        }
        this.amount = amount;
    }

    public void setAmountToPay(final BigDecimal amount) throws IllegalWriteException {
        if (locked) {
            throw new IllegalWriteException();
        }
        this.amountToPay = amount;
    }

    public void setAmountToCharge(final BigDecimal amount) throws IllegalWriteException {
        if (locked) {
            throw new IllegalWriteException();
        }
        this.amountToCharge = amount;
    }

    public void setComment(final String comment) throws IllegalWriteException {
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
    public void doLoad() {
        feature = FeatureManager.getFeatureById(feature.getId());
        if (team != null) {
            team = TeamManager.getById(team.getId());
        }
    }

    @Override
    public void notifyChildAdded(final WebProcess subProcess) {
        if (subProcess.getClass().equals(PaylineProcess.class)) {
            locked = true;
        }
    }

    public void setLock(final boolean islocked) {
        locked = islocked;
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
            locked = false;
            return new CheckContributionPageUrl(this);
        }
        return null;
    }

    public BigDecimal getAmountToCharge() {
        return amountToCharge;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setTeam(final Team team) throws IllegalWriteException {
        if (locked) {
            throw new IllegalWriteException();
        }
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }
}
