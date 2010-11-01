package com.bloatit.common;

import org.apache.log4j.Logger;

// TRACE
// DEBUG
// INFO 
// WARN  -> not correct behavior but non so serious
// ERROR -> should be reported to the admin
// FATAL -> should be reported to the admin

public class Log {
    public static Logger framework() {
        return Logger.getLogger("com.bloatit.framework");
    }

    public static Logger data() {
        return Logger.getLogger("com.bloatit.model.data");
    }
}