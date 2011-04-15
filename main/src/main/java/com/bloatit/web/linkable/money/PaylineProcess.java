package com.bloatit.web.linkable.money;

import java.math.BigDecimal;

import com.bloatit.framework.webprocessor.WebProcess;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Actor;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.managers.TeamManager;
import com.bloatit.web.url.PaylineActionUrl;
import com.bloatit.web.url.PaylineProcessUrl;

@ParamContainer("payline/process")
public class PaylineProcess extends WebProcess {
    @RequestParam
    private final PaymentProcess parentProcess;

    @RequestParam
    private Actor<?> actor;

    private boolean success = false;

    private final PaylineProcessUrl url;

    public PaylineProcess(final PaylineProcessUrl url) {
        super(url);
        this.url = url;
        parentProcess = url.getParentProcess();
        actor = url.getActor();
    }

    public BigDecimal getAmount() {
        return parentProcess.getAmountToPay();
    }

    public WebProcess getParentProcess() {
        return parentProcess;
    }

    @Override
    protected Url doProcess() {
        parentProcess.beginSubProcess(this);
        return new PaylineActionUrl(this);
    }

    @Override
    protected Url doProcessErrors() {
        return session.getLastVisitedPage();
    }

    @Override
    public void load() {
        parentProcess.load();
        if (actor instanceof Member) {
            actor = MemberManager.getById(actor.getId());
        } else if (actor instanceof Team) {
            actor = TeamManager.getById(actor.getId());
        }
        // actor = (Actor<?>) DBRequests.getById(DaoActor.class,
        // actor.getId()).accept(new DataVisitorConstructor());
    }

    /**
     * Indicates that the payment process is successful
     */
    public void setSuccessful() {
        this.success = true;
    }

    /**
     * Indicates if the process was successful or not.
     * <p>
     * Note: To be successful, a call to {@link #setSuccessful()} must be done
     * before. The default value is not successful.
     * </p>
     * 
     * @return <i>true</i> when the process is successful, <i>false</i>
     *         otherwise
     */
    public boolean isSuccessful() {
        return success;
    }

    public Actor<?> getActor() {
        return actor;
    }
}
