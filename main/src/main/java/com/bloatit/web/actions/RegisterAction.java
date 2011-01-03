/**
 * 
 */
package com.bloatit.web.actions;

import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.utils.url.RegisterActionUrl;

@ParamContainer("member/docreate")
public class RegisterAction extends Action {
	
	public static final String LOGIN_CODE = "bloatit_login";
	public static final String PASSWORD_CODE = "bloatit_password";
	public static final String EMAIL_CODE = "bloatit_email";
	public static final String COUNTRY_CODE = "bloatit_country";
	public static final String LANGUAGE_CODE = "bloatit_lang";
	
    @RequestParam(name = RegisterAction.LOGIN_CODE, role = Role.POST)
    private final String login;
    
    @RequestParam(name = RegisterAction.PASSWORD_CODE, role = Role.POST)
    private final String password;
    
    @RequestParam(name = RegisterAction.EMAIL_CODE, role = Role.POST)
    private final String email;
    
    @RequestParam(name = RegisterAction.COUNTRY_CODE, role = Role.POST)
    private final String country;
    
    @RequestParam(name = RegisterAction.LANGUAGE_CODE, role = Role.POST)
    private final String lang;
    
	private final RegisterActionUrl url;

	/**
	 * @param url
	 */
	public RegisterAction(RegisterActionUrl url) {
		super(url);
		this.url = url;
		this.login = url.getLogin();
		this.password = url.getPassword();
		this.email = url.getEmail();
		this.lang = url.getLang();
		this.country = url.getCountry();
	}

	@Override
	protected String doProcess() throws RedirectException {
		// TODO Auto-generated method stub
		return null;
	}

}
