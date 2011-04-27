package com.bloatit.framework.webprocessor.url;

import java.util.Map.Entry;

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
        for (Entry<String, HttpParameter> entry : stringParameters.entrySet()) {
            for (String value : entry.getValue()) {
                writeParameter(sb, entry.getKey(), value);
            }
        }
    }

    private void writeParameter(StringBuilder sb, String name, String value) {
        sb.append("/").append(name).append("-").append(value);
    }

    @Override
    public void addParameter(final String key, final String value) {
        stringParameters.add(key, value);
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
        for (Entry<String, HttpParameter> entry : stringParameters.entrySet()) {
            for (String value : entry.getValue()) {
                parameters.add(entry.getKey(), value);
            }
        }
    }

    @Override
    public Protocol getProtocol() {
        return protocol;
    }

}
