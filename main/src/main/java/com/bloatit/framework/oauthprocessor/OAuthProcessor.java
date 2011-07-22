package com.bloatit.framework.oauthprocessor;

import java.io.IOException;

import com.bloatit.framework.model.ModelAccessor;
import com.bloatit.framework.utils.parameters.Parameters;
import com.bloatit.framework.xcgiserver.HttpBloatitRequest;
import com.bloatit.framework.xcgiserver.HttpHeader;
import com.bloatit.framework.xcgiserver.HttpPost;
import com.bloatit.framework.xcgiserver.HttpResponse;
import com.bloatit.framework.xcgiserver.RequestKey;
import com.bloatit.framework.xcgiserver.XcgiProcessor;

public class OAuthProcessor implements XcgiProcessor {

    public static final String GET_AUTHTOKEN_PAGE_NAME = "get_authtoken";
    public static final String DENY_AUTHORIZATION_PAGE_NAME = "deny_authorization";
    public static final String GET_AUTHORIZATION_PAGE_NAME = "get_authorization";
    public static final String PASSWORD_CODE = "password";
    public static final String LOGIN_CODE = "login";
    public static final String OAUTH_GET_CREDENTIAL_PAGENAME = "oauth_credential";

    private final OAuthGetAuthorization authGetAuthorization;
    private final OAuthGetToken authGetToken;

    public OAuthProcessor(OAuthGetAuthorization authGetAuthorization, OAuthGetToken authGetToken) {
        super();
        this.authGetAuthorization = authGetAuthorization;
        this.authGetToken = authGetToken;
    }

    @Override
    public boolean initialize() {
        return true;
    }

    @Override
    public boolean process(RequestKey key, HttpHeader header, HttpPost postData, HttpResponse response) throws IOException {

        // TODO verify httpS

        final Parameters parameters = new Parameters();
        parameters.putAll(header.getGetParameters());
        parameters.putAll(postData.getParameters());

        ModelAccessor.open();

        final HttpBloatitRequest request = new HttpBloatitRequest(header, parameters);

        if (header.getPageName().equals(GET_AUTHORIZATION_PAGE_NAME)) {
            authGetAuthorization.process(request, response);
        } else if (header.getPageName().equals(DENY_AUTHORIZATION_PAGE_NAME)) {
            new OAuthDenyAuthorization().process(request, response);
        } else if (header.getPageName().equals(GET_AUTHTOKEN_PAGE_NAME)) {
            authGetToken.process(request, response);
        } else {
            return false;
        }

        return true;
    }
}
