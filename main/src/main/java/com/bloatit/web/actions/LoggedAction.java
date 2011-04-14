package com.bloatit.web.actions;

import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.web.url.LoginPageUrl;

/**
 * <p>
 * The mother of all actions that require the user to be logged
 * </p>
 * <p>
 * All implementing classes need to implement
 * <li>doProcessRestricted: The standard (expected) behavior of the action :
 * user is logged and no error have been detected in the
 * <code>RequestParam</code></li>
 * <li>doProcessErrors: called whenever one of the <code>mandatory</code>
 * request param contains a null value, or whenever a request param (either
 * optional or mandatory) contains a non valid value</li>
 * <li>getRefusalReason: Used to inform the user while he has to be logged to do
 * the action</li>
 * <li>transmitParameters: called when user is not logged. In this method,
 * children classes should most likely save all parameters into the session</li>
 * </p>
 */
public abstract class LoggedAction extends Action {
    private final Url meUrl;
    private final Member member;

    public LoggedAction(final Url url) {
        super(url);
        this.meUrl = url;
        if (session.isLogged()) {
            this.member = session.getAuthToken().getMember();
        } else {
            this.member = null;
        }
    }

    @Override
    protected final Url doProcess() {
        if (session.isLogged()) {
            return doProcessRestricted(member);
        }
        session.notifyBad(getRefusalReason());
        session.setTargetPage(meUrl);
        transmitParameters();
        return new LoginPageUrl();
    }

    @Override
    protected final Url checkRightsAndEverything() {
        if (member != null) {
            return doCheckRightsAndEverything(member);
        }
        return new LoginPageUrl();
    }

    protected abstract Url doCheckRightsAndEverything(Member me);

    /**
     * Called when user is correctly authentified
     * 
     * @param me TODO
     */
    protected abstract Url doProcessRestricted(Member me);

    /**
     * Called when some RequestParams contain erroneous parameters.
     */
    @Override
    protected abstract Url doProcessErrors();

    /**
     * <b>Do not forget to localize</p>
     * 
     * @return the error message to dislay to the user, informing him while he
     *         couldn't access the page
     */
    protected abstract String getRefusalReason();

}
