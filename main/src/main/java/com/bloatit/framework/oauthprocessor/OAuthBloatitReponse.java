package com.bloatit.framework.oauthprocessor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.bloatit.framework.xcgiserver.HttpResponse;

public abstract class OAuthBloatitReponse {

    public static class AuthorizationException extends Exception {
        private static final long serialVersionUID = -744818410096731179L;

        public AuthorizationException() {
            super();
        }

        public AuthorizationException(final String message, final Throwable cause) {
            super(message, cause);
        }

        public AuthorizationException(final String message) {
            super(message);
        }

        public AuthorizationException(final Throwable cause) {
            super(cause);
        }
    }

    public abstract void process(HttpServletRequest request, HttpResponse response) throws IOException;

}
