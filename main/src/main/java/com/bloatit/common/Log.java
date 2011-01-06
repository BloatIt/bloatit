package com.bloatit.common;

import org.apache.log4j.Logger;

// TRACE
// DEBUG
// INFO
// WARN -> not correct behavior but non so serious
// ERROR -> should be reported to the admin
// FATAL -> should be reported to the admin

public final class Log {

    private Log() {
        // disactivate default ctor
    }

    private static final Logger FRAMEWORK = Logger.getLogger("com.bloatit.framework");
    private static final Logger DATA = Logger.getLogger("com.bloatit.model.data");
    private static final Logger WEB = Logger.getLogger("com.bloatit.web");

    public static Logger framework() {
        return FRAMEWORK;
    }

    public static Logger data() {
        return DATA;
    }

    public static Logger web() {
        return WEB;
    }
}