//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.common;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

// TRACE
// DEBUG
// INFO
// WARN -> not correct behavior but non so serious
// ERROR -> should be reported to the admin
// FATAL -> should be reported to the admin NOW !

public final class Log {

    public static interface LoggerInterface {
        void fatal(final Object message);

        void fatal(final Object message, final Throwable e);

        void error(final Object message);

        void error(final Object message, final Throwable e);

        void warn(final Object message);

        void warn(final Object message, final Throwable e);

        void info(final Object message);

        void info(final Object message, final Throwable e);

        void debug(final Object message);

        void debug(final Object message, final Throwable e);

        void trace(final Object message);

        void trace(final Object message, final Throwable e);

        boolean isInfoEnabled();

        boolean isDebugEnabled();

        boolean isTraceEnabled();
    }

    public static class BloatitLogger implements LoggerInterface {

        private final Logger log;

        private BloatitLogger(final Logger log) {
            super();
            this.log = log;
        }

        @Override
        public void fatal(final Object message) {
            log.fatal(message + getStackTrace());
        }

        private String getStackTrace() {
            final StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            final StringBuilder sb = new StringBuilder();

            for (int i = 3; i < trace.length; i++) {
                sb.append("\n\t").append(trace[i].toString());
            }

            return sb.toString();
        }

        private String generateId(final Throwable e) {
            return generateId(e.getStackTrace(), e.toString());
        }

        private String generateId(final StackTraceElement[] stack, final String append) {
            final StringBuilder sb = new StringBuilder();
            sb.append(append);
            for (final StackTraceElement elem : stack) {
                sb.append(elem.getClassName());
                sb.append(elem.getMethodName());
            }
            final String hash = DigestUtils.md5Hex(sb.toString());
            return hash.substring(0, 5);
        }

        @Override
        public void fatal(final Object message, final Throwable e) {
            log.fatal(message + " [" + generateId(e) + "]", e);
        }

        @Override
        public void error(final Object message) {
            log.error(message + getStackTrace());
        }

        @Override
        public void error(final Object message, final Throwable e) {
            log.error(message, e);
        }

        @Override
        public void warn(final Object message) {
            log.warn(message);
        }

        @Override
        public void warn(final Object message, final Throwable e) {
            log.warn(message + " [" + generateId(e) + "]", e);
        }

        @Override
        public void info(final Object message) {
            log.info(message);
        }

        @Override
        public void info(final Object message, final Throwable e) {
            log.info(message + " [" + generateId(e) + "]", e);
        }

        @Override
        public void debug(final Object message) {
            log.debug(message);
        }

        @Override
        public void debug(final Object message, final Throwable e) {
            log.debug(message + " [" + generateId(e) + "]", e);
        }

        @Override
        public void trace(final Object message) {
            log.trace(message);
        }

        @Override
        public void trace(final Object message, final Throwable e) {
            log.trace(message + " [" + generateId(e) + "]", e);
        }

        @Override
        public boolean isInfoEnabled() {
            return log.isInfoEnabled();
        }

        @Override
        public boolean isDebugEnabled() {
            return log.isDebugEnabled();
        }

        @Override
        public boolean isTraceEnabled() {
            return log.isTraceEnabled();
        }
    }

    private Log() {
        // disactivate default ctor
    }

    private static final LoggerInterface MODEL = new BloatitLogger(Logger.getLogger("com.bloatit.model"));
    private static final LoggerInterface DATA = new BloatitLogger(Logger.getLogger("com.bloatit.data"));
    private static final LoggerInterface WEB = new BloatitLogger(Logger.getLogger("com.bloatit.web"));
    private static final LoggerInterface FRAMEWORK = new BloatitLogger(Logger.getLogger("com.bloatit.framework"));
    private static final LoggerInterface MAIL = new BloatitLogger(Logger.getLogger("com.bloatit.mail"));
    private static final LoggerInterface REST = new BloatitLogger(Logger.getLogger("com.bloatit.rest"));
    private static final LoggerInterface RESOURCE = new BloatitLogger(Logger.getLogger("com.bloatit.resource"));
    private static final LoggerInterface CACHE = new BloatitLogger(Logger.getLogger("com.bloatit.cache"));
    private static final LoggerInterface PAYMENT = new BloatitLogger(Logger.getLogger("com.bloatit.payment"));
    private static final Logger FAKESSHGUARD = Logger.getLogger("com.bloatit.fakesshguard");

    public static LoggerInterface model() {
        return MODEL;
    }

    public static LoggerInterface data() {
        return DATA;
    }

    public static LoggerInterface web() {
        return WEB;
    }

    public static LoggerInterface framework() {
        return FRAMEWORK;
    }

    public static LoggerInterface mail() {
        return MAIL;
    }

    public static LoggerInterface rest() {
        return REST;
    }

    public static LoggerInterface resources() {
        return RESOURCE;
    }

    public static LoggerInterface cache() {
        return CACHE;
    }
    
    public static LoggerInterface payment() {
        return PAYMENT;
    }

    public static Logger fakesshguard() {
        return FAKESSHGUARD;
    }

}
