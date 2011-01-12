package com.bloatit.web.actions;

import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.utils.url.LoginPageUrl;
import com.bloatit.web.utils.url.Url;

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
    private Url meUrl;

    public LoggedAction(final Url url) {
        super(url);
        this.meUrl = url;
    }

    @Override
    protected final Url doProcess() throws RedirectException {
        if (session.isLogged()) {
            return doProcessRestricted();
        } else {
            session.notifyBad(getRefusalReason());
            session.setTargetPage(meUrl);
            transmitParameters();
            return new LoginPageUrl();
        }
    }

    /**
     * Called when user is correctly authentified
     */
    public abstract Url doProcessRestricted() throws RedirectException;

    /**
     * Called when some RequestParams contain erroneous parameters.
     */
    @Override
    protected abstract Url doProcessErrors() throws RedirectException;

    /**
     * <b>Do not forget to localize</p>
     * 
     * @return the error message to dislay to the user, informing him while he
     *         couldn't access the page
     */
    protected abstract String getRefusalReason();

    /**
     * Override and save all your parameters into session
     */
    protected abstract void transmitParameters();
}
