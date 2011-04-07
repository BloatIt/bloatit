/**
 *
 */
package com.bloatit.web.linkable.login;

import java.util.Locale;

import com.bloatit.framework.mailsender.Mail;
import com.bloatit.framework.mailsender.MailServer;
import com.bloatit.framework.utils.MailUtils;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.masters.Action;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.web.url.MemberActivationActionUrl;
import com.bloatit.web.url.SignUpActionUrl;
import com.bloatit.web.url.SignUpPageUrl;

/**
 * A response to a form used sign into the website (creation of a new user)
 */
@ParamContainer("member/dosignup")
public class SignUpAction extends Action {

    public static final String LOGIN_CODE = "bloatit_login";
    public static final String PASSWORD_CODE = "bloatit_password";
    public static final String EMAIL_CODE = "bloatit_email";
    public static final String COUNTRY_CODE = "bloatit_country";
    public static final String LANGUAGE_CODE = "bloatit_lang";

    @RequestParam(name = SignUpAction.LOGIN_CODE, role = Role.POST)
    @ParamConstraint(min = "4", minErrorMsg = @tr("Number of characters for login has to be superior to 4"),//
    max = "15", maxErrorMsg = @tr("Number of characters for login has to be inferior to 15"))
    private final String login;

    @RequestParam(name = SignUpAction.PASSWORD_CODE, role = Role.POST)
    @ParamConstraint(min = "4", minErrorMsg = @tr("Number of characters for password has to be superior to 4"),//
    max = "15", maxErrorMsg = @tr("Number of characters for password has to be inferior to 15"))
    private final String password;

    @RequestParam(name = SignUpAction.EMAIL_CODE, role = Role.POST)
    @ParamConstraint(min = "4", minErrorMsg = @tr("Number of characters for email has to be superior to 5"),//
    max = "30", maxErrorMsg = @tr("Number of characters for email address has to be inferior to 30"))
    private final String email;

    @RequestParam(name = SignUpAction.COUNTRY_CODE, role = Role.POST)
    private final String country;

    @RequestParam(name = SignUpAction.LANGUAGE_CODE, role = Role.POST)
    private final String lang;
    private final SignUpActionUrl url;

    public SignUpAction(final SignUpActionUrl url) {
        super(url);
        this.url = url;
        this.login = url.getLogin();
        this.password = url.getPassword();
        this.email = url.getEmail();
        this.lang = url.getLang();
        this.country = url.getCountry();
    }

    @Override
    protected final Url doProcess() {

        if (MemberManager.loginExists(login)) {
            session.notifyError(Context.tr("Login ''{0}''already used. Find another login", login));
            return sendError();
        }

        if (MemberManager.emailExists(email)) {
            session.notifyError(Context.tr("Email ''{0}''already used. Find another email or use your old account !", email));
            return sendError();
        }

        final String userEmail = email.trim();
        if (!MailUtils.isValidEmail(userEmail)) {
            session.notifyError(Context.tr("Invalid email address : " + userEmail));
            return sendError();
        }

        final Locale locale = new Locale(lang, country);
        // TODO verify duplicate to avoid crashes
        final Member m = new Member(login, password, email, locale);
        final String activationKey = m.getActivationKey();
        final MemberActivationActionUrl url = new MemberActivationActionUrl(login, activationKey);

        final String content = Context.tr("Your Elveos.org account ''{0}'' was created. Please click on the following link to activate your account: \n\n {1}",
                                          login,
                                          url.externalUrlString(Context.getHeader().getHttpHeader()));

        final Mail activationMail = new Mail(email, Context.tr("Elveos.org account activation"), content, "member-docreate");

        MailServer.getInstance().send(activationMail);

        session.notifyGood(Context.tr("Account created, you will receive a mail for activate it."));

        return session.pickPreferredPage();
    }

    public SignUpPageUrl sendError() {
        session.addParameter(url.getEmailParameter());
        session.addParameter(url.getLoginParameter());
        session.addParameter(url.getPasswordParameter());
        session.addParameter(url.getCountryParameter());
        session.addParameter(url.getLangParameter());
        return new SignUpPageUrl();
    }

    @Override
    protected final Url doProcessErrors() {
        session.notifyList(url.getMessages());
        session.addParameter(url.getEmailParameter());
        session.addParameter(url.getLoginParameter());
        session.addParameter(url.getPasswordParameter());
        session.addParameter(url.getCountryParameter());
        session.addParameter(url.getLangParameter());
        return new SignUpPageUrl();
    }
}
