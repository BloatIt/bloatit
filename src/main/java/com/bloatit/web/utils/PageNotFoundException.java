package com.bloatit.web.utils;

import com.bloatit.common.FatalErrorException;

public class PageNotFoundException extends FatalErrorException {
    private static final long serialVersionUID = 1284977798216878288L;

    public PageNotFoundException(String string, Throwable cause) {
        super(string, cause);
    }

}
