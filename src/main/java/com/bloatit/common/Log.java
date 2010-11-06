package com.bloatit.common;

import org.apache.log4j.Logger;

// TRACE
// DEBUG
// INFO 
// WARN  -> not correct behavior but non so serious
// ERROR -> should be reported to the admin
// FATAL -> should be reported to the admin

public class Log {
    public static final Logger framework = Logger.getLogger("com.bloatit.framework");
    public static final Logger data = Logger.getLogger("com.bloatit.model.data");
    
    public static Logger framework() {
        return framework;
    }

    public static Logger data() {
        return data;
    }
}