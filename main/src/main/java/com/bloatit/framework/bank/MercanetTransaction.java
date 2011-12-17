package com.bloatit.framework.bank;

import java.util.HashMap;
import java.util.Map;

import com.bloatit.framework.bank.MercanetAPI.PaymentMethod;

/**
 *
 */
public class MercanetTransaction {

    private final String url;
    private final String data;

    // FIXME why the transactionId is not used ?
    // private final int transactionId;

    protected MercanetTransaction(final String data, final String url, final int transactionId) {
        this.data = data;
        this.url = url;
        // this.transactionId = transactionId;
    }

    public String getData() {
        return data;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHiddenParameters(final PaymentMethod method) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("DATA", data);
        params.put(method.toString() + ".x", "0");
        params.put(method.toString() + ".y", "0");
        return params;
    }
}
