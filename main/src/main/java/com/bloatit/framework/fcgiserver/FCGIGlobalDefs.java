package com.bloatit.framework.fcgiserver;
/*
 * @(#)FCGIGlobalDefs.java
 *
 *
 *     FastCGi compatibility package Interface
 *
 *
 *  Copyright (c) 1996 Open Market, Inc.
 *
 * See the file "LICENSE.TERMS" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 *
 * $Id: FCGIGlobalDefs.java,v 1.3 2000/03/21 12:12:25 robs Exp $
 */

/**
 * This class contains FCGI global definitions corresponding to the #defs in the
 * C version.
 */
public abstract class FCGIGlobalDefs {
    public static final int FCGI_MAX_LEN = 0xffff;

    // Define Length of FCGI message bodies in bytes
    public static final int FCGI_HEADER_LEN = 8;
    public static final int FCGI_END_REQ_BODY_LEN = 8;
    public static final int FCGI_BEGIN_REQ_BODY_LEN = 8;
    public static final int FCGI_UNKNOWN_BODY_TYPE_BODY_LEN = 8;

    // Header defines
    public static int FCGI_Version1 = 1;
    // FCGI Record Types
    public static final int FCGI_BEGIN_REQUEST = 1;
    public static final int FCGI_ABORT_REQUEST = 2;
    public static final int FCGI_END_REQUEST = 3;
    public static final int FCGI_PARAMS = 4;
    public static final int FCGI_STDIN = 5;
    public static final int FCGI_STDOUT = 6;
    public static final int FCGI_STDERR = 7;
    public static final int FCGI_DATA = 8;
    public static final int FCGI_GET_VALUES = 9;
    public static final int FCGI_GET_VALUES_RESULT = 10;
    public static final int FCGI_UNKNOWN_TYPE = 11;
    public static final int FCGI_MAX_TYPE = FCGI_UNKNOWN_TYPE;
    // Request ID Values
    public static final int FCGI_NULL_REQUEST_ID = 0;

    // Begin Request defines

    // Mask flags
    public static int FCGI_KEEP_CONN = 1;
    // Roles
    public static final int FCGI_RESPONDER = 1;
    public static final int FCGI_AUTHORIZER = 2;
    public static final int FCGI_FILTER = 3;

    // End Request defines

    // Protocol status
    public static final int FCGI_REQUEST_COMPLETE = 0;
    public static final int FCGI_CANT_MPX_CONN = 1;
    public static final int FCGI_OVERLOAD = 2;
    public static final int FCGI_UNKNOWN_ROLE = 3;

    // Get Values, Get Values Results defines
    public static final String FCGI_MAX_CONNS = "FCGI_MAX_CONNS";
    public static final String FCGI_MAX_REQS = "FCGI_MAX_REQS";
    public static final String FCGI_MPXS_CONNS = "FCGI_MPXS_CONNS";

    // Return codes for Process functions
    public static final int FCGI_STREAM_RECORD = 0;
    public static final int FCGI_SKIP = 1;
    public static final int FCGI_BEGIN_RECORD = 2;
    public static final int FCGI_MGMT_RECORD = 3;

    // Error Codes
    public static final int FCGI_UNSUPPORTED_VERSION = -2;
    public static final int FCGI_PROTOCOL_ERROR = -3;
    public static final int FCGI_PARAMS_ERROR = -4;
    public static final int FCGI_CALL_SEQ_ERROR = -5;
}
