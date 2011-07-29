package com.bloatit.framework.webprocessor.url;

import java.util.Map.Entry;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.parameters.HttpParameter;
import com.bloatit.framework.utils.parameters.Parameters;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;

public class UrlDump extends Url {

    private final boolean isAction;
    private final String code;
    private final Parameters stringParameters;
    private final Protocol protocol;

    public UrlDump(final Url url) {
        super();

        isAction = url.isAction();
        code = url.getCode();
        stringParameters = url.getStringParameters();
        setAnchor(url.getAnchor());
        protocol = url.getProtocol();
    }

    @Override
    public UrlDump clone() {
        return new UrlDump(this);
    }

    @Override
    protected void doConstructUrl(final StringBuilder sb) {
        for (final Entry<String, HttpParameter> entry : stringParameters.entrySet()) {
            for (final String value : entry.getValue()) {
                writeParameter(sb, entry.getKey(), value);
                sb.append("&");
            }
        }
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '&') {
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    private void writeParameter(final StringBuilder sb, final String name, final String value) {
        final URLCodec urlCodec = new URLCodec();
        try {
            sb.append(urlCodec.encode(name)).append('=').append(urlCodec.encode(value));
        } catch (final EncoderException e) {
            throw new BadProgrammerException(e);
        }
    }

    @Override
    public void addParameter(final String key, final String value) {
        stringParameters.add(key, value);
    }

    public void addOrReplaceParameter(final String key, final String value) {
        stringParameters.addOrReplace(key, value);
    }

    @Override
    public Messages getMessages() {
        return new Messages();
    }

    @Override
    public boolean isAction() {
        return isAction;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    protected void doGetParametersAsStrings(final Parameters parameters) {
        for (final Entry<String, HttpParameter> entry : stringParameters.entrySet()) {
            for (final String value : entry.getValue()) {
                parameters.add(entry.getKey(), value);
            }
        }
    }

    @Override
    public Protocol getProtocol() {
        return protocol;
    }

}
