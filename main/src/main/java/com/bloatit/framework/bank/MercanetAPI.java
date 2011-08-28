package com.bloatit.framework.bank;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cxf.helpers.IOUtils;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;
import com.bloatit.framework.utils.Pair;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.Session;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webprocessor.url.UrlString;

public class MercanetAPI {
    private final static String API_PATH = "/home/fred/bloatit/mercanet";
    private final static String PATHFILE_PATH = "/home/fred/.config/bloatit/pathfile";
    private final static String REQUEST_BIN = API_PATH + "/bin/request";
    private final static String RESPONSE_BIN = API_PATH + "/bin/response";

    enum PaymentMethod {
        CB, VISA, MASTERCARD
    }

    private final static Set<String> supportedLanguages = new HashSet<String>() {
        private static final long serialVersionUID = -7322981103982506459L;

        {
            add("fr");
            add("ge");
            add("en");
            add("sp");
            add("it");
        }
    };

    private final static AtomicInteger nextTransactionId = new AtomicInteger(0);

    /**
     * @return
     */
    public static MercanetTransaction createTransaction(BigDecimal amount,
                                                        String userData,
                                                        Url normalReturnUrl,
                                                        Url cancelReturnUrl,
                                                        Url automaticResponseUrl) {
        Map<String, String> params = new HashMap<String, String>();

        // Static informations
        params.put("merchant_id", "082584341411111");
        params.put("merchant_country", "fr");
        params.put("currency_code", "978");
        params.put("pathfile", PATHFILE_PATH);

        // Dynamics informations
        params.put("amount", amount.multiply(new BigDecimal("100")).setScale(0).toPlainString());

        int transactionId = getNextTransactionId();
        params.put("transaction_id", Integer.toString(transactionId));
        params.put("normal_return_url", normalReturnUrl.externalUrlString());
        params.put("cancel_return_url", cancelReturnUrl.externalUrlString());
        params.put("automatic_response_url", automaticResponseUrl.externalUrlString());

        params.put("language", filterLanguage(Context.getLocalizator().getLanguageCode()));
        params.put("return_context", checkReturnContext(userData));

        
        
        
        Pair<String, String> executionResultPairOfString = executeRequest(params);
        String data = executionResultPairOfString.first;
        String baseUrl = executionResultPairOfString.second;
        return new MercanetTransaction(data, baseUrl, transactionId);
    }

    private static String checkReturnContext(String returnContext) {
        String[] forbiddenChars = { "|", ";", ":", "\"" };

        for (String forbiddenChar : forbiddenChars) {
            if (returnContext.contains(forbiddenChar)) {
                throw new BadProgrammerException("The user data contains forbidden chars (" + forbiddenChars + ") : " + returnContext);
            }
        }
        return returnContext;
    }

    private static String filterLanguage(String languageCode) {
        if (!supportedLanguages.contains(languageCode)) {
            return "en";
        }
        return languageCode;
    }

    private static Pair<String, String> executeRequest(Map<String, String> params) {
        // Execute binary
        StringBuilder query = new StringBuilder();
        query.append(REQUEST_BIN);

        for (Entry<String, String> param : params.entrySet()) {
            query.append(" ");
            query.append(param.getKey());
            query.append("=");
            query.append(param.getValue());
        }

        Runtime runtime = Runtime.getRuntime();
        String response;
        try {
            Process proc = runtime.exec(query.toString());
            response = IOUtils.toString(proc.getInputStream(), "UTF-8");

            if (proc.exitValue() != 0) {
                throw new ExternalErrorException("Failure during execution of Merc@net binary: " + query.toString() + " - exit value: "
                        + proc.exitValue());
            }
        } catch (IOException e) {
            throw new ExternalErrorException("Failed to execute Merc@net binary: " + query.toString(), e);
        }

        // Extract data
        Pattern p = Pattern.compile("^.*<FORM METHOD=POST ACTION=\"([^\"]+)\".*<INPUT TYPE=HIDDEN NAME=DATA VALUE=\"([a-f0-9]+)\".*$");
        Matcher m = p.matcher(response);
        if (!m.matches()) {
            throw new ExternalErrorException("Failed to parse Merc@net binary response.\nQuery:" + query.toString() + "\nResponse:" + response);
        }

        String baseUrl = m.group(1);
        String data = m.group(2);

        return new Pair<String, String>(data, baseUrl);
    }

    private static synchronized int getNextTransactionId() {
        int id = nextTransactionId.getAndIncrement();
        if (id == 999999) {
            nextTransactionId.set(0);
        }
        return id;
    }

    public static void main(String[] args) {
        MercanetTransaction transaction = createTransaction(new BigDecimal("523.24"),
                                                            "sdlfkj|slk",
                                                            new UrlString("https://elveos.org/1"),
                                                            new UrlString("https://elveos.org/2"),
                                                            new UrlString("https://elveos.org/3"));

        String data = transaction.getData();
        String url = transaction.getUrl();
        Map<String, String> hiddenParameters = transaction.getHiddenParameters(PaymentMethod.CB);

    }
}
