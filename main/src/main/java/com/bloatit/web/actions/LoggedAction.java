package com.bloatit.web.actions;

import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.utils.url.LoginPageUrl;
import com.bloatit.web.utils.url.Url;

public abstract class LoggedAction extends Action{
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
	 * @return the error message to dislay to the user, informing him while he couldn't access the page
	 */
	protected abstract String getRefusalReason();
	
	/**
	 * Override and save all your parameters into session
	 */
	protected abstract void transmitParameters();
}
