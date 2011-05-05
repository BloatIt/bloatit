/**
 *
 */
package com.bloatit.web.linkable.login;

import java.util.Locale;

import com.bloatit.framework.mailsender.Mail;
import com.bloatit.framework.mailsender.MailServer;
import com.bloatit.framework.utils.MailUtils;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webprocessor.url.UrlParameter;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.web.actions.ElveosAction;
import com.bloatit.web.url.MemberActivationActionUrl;
import com.bloatit.web.url.SignUpActionUrl;
import com.bloatit.web.url.SignUpPageUrl;

/**
 * A response to a form used sign into the website (creation of a new user)
 */
@ParamContainer(value="member/dosignup", protocol=Protocol.HTTPS)
public final class SignUpAction extends ElveosAction {
    @RequestParam(role = Role.POST)
    @ParamConstraint(optionalErrorMsg = @tr("Login cannot be blank."),//
                     min = "4", minErrorMsg = @tr("Number of characters for login has to be superior to 3."),//
                     max = "15", maxErrorMsg = @tr("Number of characters for login has to be inferior to 16."))
    private final String login;

    @RequestParam(role = Role.POST)
    @ParamConstraint(optionalErrorMsg = @tr("Password cannot be blank."),//
                     min = "4", minErrorMsg = @tr("Number of characters for password has to be superior to 3."),//
                     max = "15", maxErrorMsg = @tr("Number of characters for password has to be inferior to 16."))
    private final String password;

    @RequestParam(role = Role.POST)
    @ParamConstraint(optionalErrorMsg = @tr("Password check cannot be blank."),//
                     min = "4", minErrorMsg = @tr("Number of characters for password check has to be superior to 3."),//
                     max = "15", maxErrorMsg = @tr("Number of characters for password check has to be inferior to 16."))
    private final String passwordCheck;

    @RequestParam(role = Role.POST)
    @ParamConstraint(optionalErrorMsg = @tr("Email cannot be blank."),//
                     min = "4", minErrorMsg = @tr("Number of characters for email has to be superior to 3."),//
                     max = "254", maxErrorMsg = @tr("Number of characters for email address has to be inferior to 255."))
    private final String email;

    @RequestParam(name = "bloatit_country", role = Role.POST)
    private final String country;

    @RequestParam(name = "bloatit_lang", role = Role.POST)
    private final String lang;

    private final SignUpActionUrl url;

    public SignUpAction(final SignUpActionUrl url) {
        super(url);
        this.url = url;
        this.login = url.getLogin();
        this.password = url.getPassword();
        this.passwordCheck = url.getPasswordCheck();
        this.email = url.getEmail();
        this.lang = url.getLang();
        this.country = url.getCountry();
    }

    @Override
    protected final Url doProcess(final ElveosUserToken token) {

        final Locale locale = new Locale(lang, country);
        final Member m = new Member(login, password, email, locale);
        final String activationKey = m.getActivationKey();
        final MemberActivationActionUrl url = new MemberActivationActionUrl(login, activationKey);

        final String content = Context.tr("Your Elveos.org account ''{0}'' was created. Please click on the following link to activate your account: \n\n {1}",
                                          login,
                                          url.externalUrlString());

        final Mail activationMail = new Mail(email, Context.tr("Elveos.org account activation"), content, "member-docreate");

        MailServer.getInstance().send(activationMail);
        session.notifyGood(Context.tr("Account created, you will receive a mail to activate it."));
        return session.pickPreferredPage();
    }

    @Override
    protected final Url doProcessErrors(final ElveosUserToken token) {
        return new SignUpPageUrl();
    }

    @Override
    protected Url checkRightsAndEverything(final ElveosUserToken token) {
        if (MemberManager.loginExists(login)) {
            session.notifyError(Context.tr("Login ''{0}''already used. Find another login", login));
            url.getLoginParameter().addErrorMessage(Context.tr("Login already used."));
            return doProcessErrors();
        }
        if (MemberManager.emailExists(email)) {
            session.notifyError(Context.tr("Email ''{0}''already used. Find another email or use your old account !", email));
            url.getEmailParameter().addErrorMessage(Context.tr("Email already used."));
            return doProcessErrors();
        }
        if (!MailUtils.isValidEmail(email)) {
            session.notifyError(Context.tr("Invalid email address: {0}.", email));
            url.getEmailParameter().addErrorMessage(Context.tr("Invalid email."));
            return doProcessErrors();
        }

        if (!login.matches("[^\\p{Space}]+")) {
            session.notifyError(Context.tr("Invalid login: {0}. Make sure it doesn't contain space characters.", login));
            url.getLoginParameter().addErrorMessage(Context.tr("Login contains spaces."));
            return doProcessErrors();
        }

        if (!password.equals(passwordCheck)) {
            session.notifyError(Context.tr("Password doesn't match confirmation."));
            url.getPasswordParameter().addErrorMessage(Context.tr("Password doesn't match confirmation."));
            url.getPasswordCheckParameter().addErrorMessage(Context.tr("Confirmation doesn't match password."));
            return doProcessErrors();
        }

        return NO_ERROR;
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getEmailParameter());
        session.addParameter(url.getLoginParameter());
        final UrlParameter<String, String> passwordParameter = url.getPasswordParameter();
        if (passwordParameter.getValue() != null) {
            if (passwordParameter.getValue().length() > 3) {
                passwordParameter.setValue("xxxx");
            }
        }
        session.addParameter(passwordParameter);

        final UrlParameter<String, String> passwordCheckParameter = url.getPasswordCheckParameter();
        if (passwordCheckParameter.getValue() != null) {
            if (passwordCheckParameter.getValue().length() > 3) {
                passwordCheckParameter.setValue("xxxx");
            }
        }
        session.addParameter(passwordCheckParameter);
        session.addParameter(url.getCountryParameter());
        session.addParameter(url.getLangParameter());

    }
}
