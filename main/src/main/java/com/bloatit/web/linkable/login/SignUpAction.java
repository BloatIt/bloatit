/**
 *
 */
package com.bloatit.web.linkable.login;

import java.util.Locale;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.mailsender.Mail;
import com.bloatit.framework.mailsender.MailServer;
import com.bloatit.framework.utils.MailUtils;
import com.bloatit.framework.webprocessor.annotations.LengthConstraint;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webprocessor.url.UrlParameter;
import com.bloatit.model.Member;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.web.linkable.master.ElveosAction;
import com.bloatit.web.url.MemberActivationActionUrl;
import com.bloatit.web.url.SignUpActionUrl;
import com.bloatit.web.url.SignUpPageUrl;

/**
 * A response to a form used sign into the website (creation of a new user)
 */
@ParamContainer(value = "member/dosignup", protocol = Protocol.HTTPS)
public final class SignUpAction extends ElveosAction {

    @RequestParam
    @Optional
    private final Boolean invoice;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("Login cannot be blank."))
    @MinConstraint(min = 2, message = @tr("The login must have at least %constraint% chars."))
    @MaxConstraint(max = 15, message = @tr("The login must be %constraint% chars length max."))
    private final String login;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("Password cannot be blank."))
    @MinConstraint(min = 7, message = @tr("The password must have at least %constraint% chars."))
    @MaxConstraint(max = 255, message = @tr("The password must be %constraint% chars length max."))
    private final String password;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("Password check cannot be blank."))
    @MinConstraint(min = 7, message = @tr("The password check must have at least %constraint% chars."))
    @MaxConstraint(max = 255, message = @tr("The password check must be %constraint% chars length max."))
    private final String passwordCheck;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("Email cannot be blank."))
    @MinConstraint(min = 4, message = @tr("The email must have at least %constraint% chars."))
    @MaxConstraint(max = 254, message = @tr("The email must be %constraint% chars length max."))
    private final String email;

    @RequestParam(name = "bloatit_country", role = Role.POST)
    @LengthConstraint(length = 2, message = @tr("The country code must be %constraint% length."))
    @NonOptional(@tr("You have to specify a country."))
    private final String country;

    @RequestParam(name = "bloatit_lang", role = Role.POST)
    private final String lang;

    // Invoicing informations
    @RequestParam(role = Role.POST)
    @Optional
    private final String name;

    @RequestParam(role = Role.POST)
    @Optional
    private final String street;

    @RequestParam(role = Role.POST)
    @Optional
    private final String postalCode;

    @RequestParam(role = Role.POST)
    @Optional
    private final String city;

    @RequestParam(role = Role.POST)
    @Optional
    private final String extras;

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

        // Invoicing informations
        this.name = url.getName();
        this.city = url.getCity();
        this.street = url.getStreet();
        this.extras = url.getExtras();
        this.postalCode = url.getPostalCode();

        this.invoice = url.getInvoice();
    }

    @Override
    protected final Url doProcess() {

        final Locale locale = new Locale(lang, country);
        final Member m = new Member(login, password, email, locale);
        final String activationKey = m.getActivationKey();
        final MemberActivationActionUrl url = new MemberActivationActionUrl(activationKey, login);

        final String content = Context.tr("Your Elveos.org account ''{0}'' was created. Please click on the following link to activate your account: \n\n {1}",
                                          login,
                                          url.externalUrlString());

        final Mail activationMail = new Mail(email, Context.tr("Elveos.org account activation"), content, "member-docreate");

        MailServer.getInstance().send(activationMail);
        AuthToken.authenticate(m);
        session.notifyGood(Context.tr("Account created, you are now logged. You will receive a mail to activate it."));
        Context.getLocalizator().forceMemberChoice();

        // Try to set Invoicing informations
        if (invoice!=null && invoice) {
            try {
                m.getContact().setName(name);
                m.getContact().setStreet(street);
                m.getContact().setExtras(extras);
                m.getContact().setPostalCode(postalCode);
                m.getContact().setCity(city);
                m.getContact().setCountry(country);
            } catch (UnauthorizedPrivateAccessException e) {
                throw new BadProgrammerException("Fail to update a invoicing contact of a member", e);
            }
        }

        return session.pickPreferredPage();
    }

    @Override
    protected final Url doProcessErrors() {
        SignUpPageUrl signUpPageUrl = new SignUpPageUrl();
        signUpPageUrl.setInvoice(invoice);
        return signUpPageUrl;

    }

    @Override
    protected Url checkRightsAndEverything() {
        boolean isOk = true;
        
        if (MemberManager.loginExists(login)) {
            session.notifyError(Context.tr("Login ''{0}''already used. Find another login", login));
            url.getLoginParameter().addErrorMessage(Context.tr("Login already used."));
            isOk = false;
        }

        if (MemberManager.emailExists(email)) {
            session.notifyError(Context.tr("Email ''{0}''already used. Find another email or use your old account !", email));
            url.getEmailParameter().addErrorMessage(Context.tr("Email already used."));
            isOk = false;
        }

        if (!MailUtils.isValidEmail(email)) {
            session.notifyError(Context.tr("Invalid email address: {0}.", email));
            url.getEmailParameter().addErrorMessage(Context.tr("Invalid email."));
            isOk = false;
        }

        if (!login.matches("[^\\p{Space}]+")) {
            session.notifyError(Context.tr("Invalid login: {0}. Make sure it doesn't contain space characters.", login));
            url.getLoginParameter().addErrorMessage(Context.tr("Login contains spaces."));
            isOk = false;
        }

        if (!password.equals(passwordCheck)) {
            session.notifyError(Context.tr("Password doesn't match confirmation."));
            url.getPasswordParameter().addErrorMessage(Context.tr("Password doesn't match confirmation."));
            url.getPasswordCheckParameter().addErrorMessage(Context.tr("Confirmation doesn't match password."));
            isOk = false;
        }

        if (invoice!= null && invoice) {
            isOk &= checkOptional(this.name, Context.tr("You must add a name."), url.getNameParameter());
            isOk &= checkOptional(this.street, Context.tr("You must add a street."), url.getStreetParameter());
            isOk &= checkOptional(this.postalCode, Context.tr("You must add a Postcode."), url.getPostalCodeParameter());
            isOk &= checkOptional(this.city, Context.tr("You must add a city."), url.getCityParameter());
        }
        
        if(isOk) {
            return NO_ERROR;
        } else {
            return doProcessErrors();
        }
    }
    
    private boolean checkOptional(Object object, String errorText, UrlParameter<?, ?> parameter) {
        if (object == null) {
            parameter.addErrorMessage(errorText);
            session.notifyError(errorText);
            return false;
        }
        return true;
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getEmailParameter());
        session.addParameter(url.getLoginParameter());
        final UrlParameter<String, String> passwordParameter = url.getPasswordParameter();
        if (passwordParameter.getValue() != null) {
            if (passwordParameter.getValue().length() > 3) {
                passwordParameter.setValue("xxxxxxxxxx");
            }
        }
        session.addParameter(passwordParameter);

        final UrlParameter<String, String> passwordCheckParameter = url.getPasswordCheckParameter();
        if (passwordCheckParameter.getValue() != null) {
            if (passwordCheckParameter.getValue().length() > 3) {
                passwordCheckParameter.setValue("xxxxxxxxxx");
            }
        }
        session.addParameter(passwordCheckParameter);
        session.addParameter(url.getCountryParameter());
        session.addParameter(url.getLangParameter());
        
        session.addParameter(url.getNameParameter());
        session.addParameter(url.getStreetParameter());
        session.addParameter(url.getExtrasParameter());
        session.addParameter(url.getPostalCodeParameter());
        session.addParameter(url.getCityParameter());

    }
}
