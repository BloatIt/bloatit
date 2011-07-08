package com.bloatit.framework.oauth;



public abstract class BloatitOAuthRequest {

//    protected Url request;
//    protected OAuthValidator validator;
//    protected Map<String, Class> validators = new HashMap<String, Class>();
//
//    public BloatitOAuthRequest(final Url url) throws OAuthSystemException, OAuthProblemException {
//        this.request = request;
//        validate();
//    }
//
//    protected void validate() throws OAuthSystemException, OAuthProblemException {
//        try {
//            validator = initValidator();
//            validator.validateMethod(request);
//            validator.validateContentType(request);
//            validator.validateRequiredParameters(request);
//        } catch (final OAuthProblemException e) {
//            try {
//                final String redirectUri = request.getParameter(OAuth.OAUTH_REDIRECT_URI);
//                if (!OAuthUtils.isEmpty(redirectUri)) {
//                    e.setRedirectUri(redirectUri);
//                }
//            } catch (final Exception ex) {
//                if (log.isDebugEnabled()) {
//                    log.debug("Cannot read redirect_url from the request: {}", new String[] { ex.getMessage() });
//                }
//            }
//
//            throw e;
//        }
//    }
//
////    protected abstract OAuthValidator initValidator() throws OAuthProblemException, OAuthSystemException;
//
//    public String getParam(final String name) {
//        return request.getStringParameters().look(name).getSimpleValue();
//    }
//
//    public String getRefreshToken() {
//        return getParam(OAuth.OAUTH_REFRESH_TOKEN);
//    }
//
//    public String getClientId() {
//        return getParam(OAuth.OAUTH_CLIENT_ID);
//    }
//
//    public String getRedirectURI() {
//        return getParam(OAuth.OAUTH_REDIRECT_URI);
//    }
//
//    public String getClientSecret() {
//        return getParam(OAuth.OAUTH_CLIENT_SECRET);
//    }
//
//    public Set<String> getScopes() {
//        final String scopes = getParam(OAuth.OAUTH_SCOPE);
//        return OAuthUtils.decodeScopes(scopes);
//    }

}
