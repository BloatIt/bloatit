package com.bloatit.framework.fcgiserver;
/*
 * @(#)FCGIMessage.java
 *
 *
 *      FastCGi compatibility package Interface
 *
 *
 *  Copyright (c) 1996 Open Market, Inc.
 *
 * See the file "LICENSE.TERMS" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 *
 * $Id: FCGIMessage.java,v 1.4 2000/10/02 15:09:07 robs Exp $
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * <p>
 * This class handles reading and building the fastcgi messages.
 * </p>
 * <p>
 * For reading incoming mesages, we pass the input stream as a param to the
 * constructor rather than to each method. Methods that build messages use and
 * return internal buffers, so they dont need a stream.
 * </p>
 */
public class FCGIMessage {
    /*
     * Instance variables
     */
    /*
     * FCGI Message Records The logical structures of the FCGI Message Records.
     * Fields are originally 1 unsigned byte in message unless otherwise noted.
     */
    /*
     * FCGI Header
     */
    private int h_version;
    private int h_type;
    private int h_requestID; // 2 bytes
    private int h_contentLength; // 2 bytes
    private int h_paddingLength;
    /*
     * FCGI BeginRequest body.
     */
    private int br_role; // 2 bytes
    private int br_flags;

    private FCGIInputStream in;

    /*
     * constructor - Java would do this implicitly.
     */
    public FCGIMessage() {
        super();
    }

    /**
     * constructor - get the stream.
     */
    public FCGIMessage(FCGIInputStream instream) {
        in = instream;
    }

    /*
     * Message Reading Methods
     */

    /**
     * Interpret the FCGI Message Header. Processes FCGI BeginRequest and
     * Management messages. Param hdr is the header. The calling routine has to
     * keep track of the stream reading management or use FCGIInputStream.fill()
     * which does just that.
     */
    public int processHeader(byte[] hdr) throws IOException {
        processHeaderBytes(hdr);
        if (h_version != FCGIGlobalDefs.FCGI_Version1) {
            return (FCGIGlobalDefs.FCGI_UNSUPPORTED_VERSION);
        }
        in.contentLen = h_contentLength;
        in.paddingLen = h_paddingLength;
        if (h_type == FCGIGlobalDefs.FCGI_BEGIN_REQUEST) {
            return processBeginRecord(h_requestID);
        }
        if (h_requestID == FCGIGlobalDefs.FCGI_NULL_REQUEST_ID) {
            return processManagementRecord(h_type);
        }
        if (h_requestID != in.request.requestID) {
            return (FCGIGlobalDefs.FCGI_SKIP);
        }
        if (h_type != in.type) {
            return (FCGIGlobalDefs.FCGI_PROTOCOL_ERROR);
        }
        return (FCGIGlobalDefs.FCGI_STREAM_RECORD);
    }

    /**
     * Put the unsigned bytes in the incoming FCGI header into integer form for
     * Java, concatinating bytes when needed. Because Java has no unsigned byte
     * type, we have to be careful about signed numeric promotion to int.
     */
    private void processHeaderBytes(byte[] hdrBuf) {
        h_version = hdrBuf[0] & 0xFF;
        h_type = hdrBuf[1] & 0xFF;
        h_requestID = ((hdrBuf[2] & 0xFF) << 8) | (hdrBuf[3] & 0xFF);
        h_contentLength = ((hdrBuf[4] & 0xFF) << 8) | (hdrBuf[5] & 0xFF);
        h_paddingLength = hdrBuf[6] & 0xFF;
    }

    /**
     * Reads FCGI Begin Request Record.
     */
    public int processBeginRecord(int requestID) throws IOException {
        byte beginReqBody[];
        byte endReqMsg[];
        if (requestID == 0 || in.contentLen != FCGIGlobalDefs.FCGI_END_REQ_BODY_LEN) {
            return FCGIGlobalDefs.FCGI_PROTOCOL_ERROR;
        }
        /*
         * If the webserver is multiplexing the connection, this library can't
         * deal with it, so repond with FCGIEndReq message with protocolStatus
         * FCGICantMpxConn
         */
        if (in.request.isBeginProcessed) {
            endReqMsg = new byte[FCGIGlobalDefs.FCGI_HEADER_LEN + FCGIGlobalDefs.FCGI_END_REQ_BODY_LEN];
            System.arraycopy(makeHeader(FCGIGlobalDefs.FCGI_END_REQUEST, requestID, FCGIGlobalDefs.FCGI_END_REQ_BODY_LEN, 0), 0, endReqMsg, 0,
                    FCGIGlobalDefs.FCGI_HEADER_LEN);
            System.arraycopy(makeEndrequestBody(0, FCGIGlobalDefs.FCGI_CANT_MPX_CONN), 0, endReqMsg, FCGIGlobalDefs.FCGI_HEADER_LEN,
                    FCGIGlobalDefs.FCGI_END_REQ_BODY_LEN);
            /*
             * since isBeginProcessed is first set below,this can't be out first
             * call, so request.out is properly set
             */
            try {
                in.request.outStream.write(endReqMsg, 0, FCGIGlobalDefs.FCGI_HEADER_LEN + FCGIGlobalDefs.FCGI_END_REQ_BODY_LEN);
            } catch (IOException e) {
                in.request.outStream.setException(e);
                return -1;
            }
        }
        /*
         * Accept this new request. Read the record body
         */
        in.request.requestID = requestID;
        beginReqBody = new byte[FCGIGlobalDefs.FCGI_BEGIN_REQ_BODY_LEN];
        if (in.read(beginReqBody, 0, FCGIGlobalDefs.FCGI_BEGIN_REQ_BODY_LEN) != FCGIGlobalDefs.FCGI_BEGIN_REQ_BODY_LEN) {
            return FCGIGlobalDefs.FCGI_PROTOCOL_ERROR;
        }
        br_flags = beginReqBody[2] & 0xFF;
        in.request.keepConnection = (br_flags & FCGIGlobalDefs.FCGI_KEEP_CONN) != 0;
        br_role = ((beginReqBody[0] & 0xFF) << 8) | (beginReqBody[1] & 0xFF);
        in.request.role = br_role;
        in.request.isBeginProcessed = true;
        return FCGIGlobalDefs.FCGI_BEGIN_RECORD;
    }

    /**
     * Reads and Responds to a Management Message. The only type of management
     * message this library understands is FCGIGetValues. The only variables
     * that this library's FCGIGetValues understands are def_FCGIMaxConns,
     * def_FCGIMaxReqs, and def_FCGIMpxsConns. Ignore the other management
     * variables, and respond to other management messages with FCGIUnknownType.
     */
    public int processManagementRecord(int type) throws IOException {

        byte[] response = new byte[64];
        int wrndx = response[FCGIGlobalDefs.FCGI_HEADER_LEN];
        int len, plen;
        if (type == FCGIGlobalDefs.FCGI_GET_VALUES) {
            Properties tmpProps = new Properties();
            readParams(tmpProps);

            if (in.getFCGIError() != 0 || in.contentLen != 0) {
                return FCGIGlobalDefs.FCGI_PROTOCOL_ERROR;
            }
            if (tmpProps.containsKey(FCGIGlobalDefs.FCGI_MAX_CONNS)) {
                makeNameVal(FCGIGlobalDefs.FCGI_MAX_CONNS, "1", response, wrndx);
            } else {
                if (tmpProps.containsKey(FCGIGlobalDefs.FCGI_MAX_REQS)) {
                    makeNameVal(FCGIGlobalDefs.FCGI_MAX_REQS, "1", response, wrndx);
                } else {
                    if (tmpProps.containsKey(FCGIGlobalDefs.FCGI_MAX_CONNS)) {
                        makeNameVal(FCGIGlobalDefs.FCGI_MPXS_CONNS, "0", response, wrndx);
                    }
                }
            }
            plen = 64 - wrndx;
            len = wrndx - FCGIGlobalDefs.FCGI_HEADER_LEN;
            System.arraycopy(makeHeader(FCGIGlobalDefs.FCGI_GET_VALUES_RESULT, FCGIGlobalDefs.FCGI_NULL_REQUEST_ID, len, plen), 0, response, 0,
                    FCGIGlobalDefs.FCGI_HEADER_LEN);
        } else {
            plen = len = FCGIGlobalDefs.FCGI_UNKNOWN_BODY_TYPE_BODY_LEN;
            System.arraycopy(makeHeader(FCGIGlobalDefs.FCGI_UNKNOWN_TYPE, FCGIGlobalDefs.FCGI_NULL_REQUEST_ID, len, 0), 0, response, 0,
                    FCGIGlobalDefs.FCGI_HEADER_LEN);
            System.arraycopy(makeUnknownTypeBodyBody(h_type), 0, response, FCGIGlobalDefs.FCGI_HEADER_LEN,
                    FCGIGlobalDefs.FCGI_UNKNOWN_BODY_TYPE_BODY_LEN);
        }
        /*
         * No guarantee that we have a request yet, so dont use fcgi output
         * stream to reference socket, instead use the FileInputStream that
         * refrences it. Also nowhere to save exception, since this is not FCGI
         * stream.
         */

        try {
            in.request.socket.getOutputStream().write(response, 0, FCGIGlobalDefs.FCGI_HEADER_LEN + FCGIGlobalDefs.FCGI_UNKNOWN_BODY_TYPE_BODY_LEN);

        } catch (IOException e) {
            return -1;
        }
        return FCGIGlobalDefs.FCGI_MGMT_RECORD;
    }

    /**
     * Makes a name/value with name = string of some length, and value a 1 byte
     * integer. Pretty specific to what we are doing above.
     */
    void makeNameVal(String name, String value, byte[] dest, int pos) {
        int nameLen = name.length();
        if (nameLen < 0x80) {
            dest[pos++] = (byte) nameLen;
        } else {
            dest[pos++] = (byte) (((nameLen >> 24) | 0x80) & 0xff);
            dest[pos++] = (byte) ((nameLen >> 16) & 0xff);
            dest[pos++] = (byte) ((nameLen >> 8) & 0xff);
            dest[pos++] = (byte) nameLen;
        }
        int valLen = value.length();
        if (valLen < 0x80) {
            dest[pos++] = (byte) valLen;
        } else {
            dest[pos++] = (byte) (((valLen >> 24) | 0x80) & 0xff);
            dest[pos++] = (byte) ((valLen >> 16) & 0xff);
            dest[pos++] = (byte) ((valLen >> 8) & 0xff);
            dest[pos++] = (byte) valLen;
        }

        try {
            System.arraycopy(name.getBytes("UTF-8"), 0, dest, pos, nameLen);
            pos += nameLen;

            System.arraycopy(value.getBytes("UTF-8"), 0, dest, pos, valLen);
            pos += valLen;
        } catch (UnsupportedEncodingException x) {
        }
    }

    /**
     * Read FCGI name-value pairs from a stream until EOF. Put them into a
     * Properties object, storing both as strings.
     */
    public int readParams(Properties props) throws IOException {
        int nameLen, valueLen;
        byte lenBuff[] = new byte[3];
        int i = 1;

        while ((nameLen = in.read()) != -1) {
            i++;
            if ((nameLen & 0x80) != 0) {
                if ((in.read(lenBuff, 0, 3)) != 3) {
                    in.setFCGIError(FCGIGlobalDefs.FCGI_PARAMS_ERROR);
                    return -1;
                }
                nameLen = ((nameLen & 0x7f) << 24) | ((lenBuff[0] & 0xFF) << 16) | ((lenBuff[1] & 0xFF) << 8) | (lenBuff[2] & 0xFF);
            }

            if ((valueLen = in.read()) == -1) {
                in.setFCGIError(FCGIGlobalDefs.FCGI_PARAMS_ERROR);
                return -1;
            }
            if ((valueLen & 0x80) != 0) {
                if ((in.read(lenBuff, 0, 3)) != 3) {
                    in.setFCGIError(FCGIGlobalDefs.FCGI_PARAMS_ERROR);
                    return -1;
                }
                valueLen = ((valueLen & 0x7f) << 24) | ((lenBuff[0] & 0xFF) << 16) | ((lenBuff[1] & 0xFF) << 8) | (lenBuff[2] & 0xFF);
            }

            /*
             * nameLen and valueLen are now valid; read the name and the value
             * from the stream and construct a standard environmental entity
             */
            byte[] name = new byte[nameLen];
            byte[] value = new byte[valueLen];
            if (in.read(name, 0, nameLen) != nameLen) {
                in.setFCGIError(FCGIGlobalDefs.FCGI_PARAMS_ERROR);
                return -1;
            }

            if (in.read(value, 0, valueLen) != valueLen) {
                in.setFCGIError(FCGIGlobalDefs.FCGI_PARAMS_ERROR);
                return -1;
            }
            String strName = new String(name);
            String strValue = new String(value);
            props.put(strName, strValue);
        }
        return 0;

    }

    /*
     * Message Building Methods
     */

    /**
     * Build an FCGI Message Header -
     */
    public byte[] makeHeader(int type, int requestId, int contentLength, int paddingLength) {
        byte[] header = new byte[FCGIGlobalDefs.FCGI_HEADER_LEN];
        header[0] = (byte) FCGIGlobalDefs.FCGI_Version1;
        header[1] = (byte) type;
        header[2] = (byte) ((requestId >> 8) & 0xff);
        header[3] = (byte) ((requestId) & 0xff);
        header[4] = (byte) ((contentLength >> 8) & 0xff);
        header[5] = (byte) ((contentLength) & 0xff);
        header[6] = (byte) paddingLength;
        header[7] = 0; // reserved byte
        return header;
    }

    /**
     * Build an FCGI Message End Request Body
     */
    public byte[] makeEndrequestBody(int appStatus, int protocolStatus) {
        byte body[] = new byte[FCGIGlobalDefs.FCGI_END_REQ_BODY_LEN];
        body[0] = (byte) ((appStatus >> 24) & 0xff);
        body[1] = (byte) ((appStatus >> 16) & 0xff);
        body[2] = (byte) ((appStatus >> 8) & 0xff);
        body[3] = (byte) ((appStatus) & 0xff);
        body[4] = (byte) protocolStatus;
        for (int i = 5; i < 8; i++) {
            body[i] = 0;
        }
        return body;
    }

    /**
     * Build an FCGI Message UnknownTypeBodyBody
     */
    public byte[] makeUnknownTypeBodyBody(int type) {
        byte body[] = new byte[FCGIGlobalDefs.FCGI_UNKNOWN_BODY_TYPE_BODY_LEN];
        body[0] = (byte) type;
        for (int i = 1; i < FCGIGlobalDefs.FCGI_UNKNOWN_BODY_TYPE_BODY_LEN; i++) {
            body[i] = 0;
        }
        return body;
    }

} // end class
