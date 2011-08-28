package com.bloatit.framework.bank;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cxf.helpers.IOUtils;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;
import com.bloatit.framework.utils.Pair;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Configuration;

/**
 * A class that handles interactions with the Mercanet API.
 * <p>
 * To use this class, use the method
 * {@link MercanetAPI#createTransaction(BigDecimal, String, Url, Url, Url)}.
 * </p>
 * 
 * @see MercanetTransaction
 */
public class MercanetAPI {
    private final static String API_PATH = "/home/fred/bloatit/mercanet";
    private final static String PATHFILE_PATH = "/home/fred/.config/bloatit/pathfile";
    private final static String REQUEST_BIN = API_PATH + "/bin/request";
    private final static String RESPONSE_BIN = API_PATH + "/bin/response";

    public enum PaymentMethod {
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
     * Initializes a MercanetTransaction with all the correct values to start a
     * payment.
     * <p>
     * <b>NOTE: </b> This method requires a valid context to be used, including
     * a valid user language.
     * </p>
     * 
     * @param amount the amount of the transaction. Must be a eurovalue
     * @param userData a string that will be returned as is at the end of the
     *            transaction. Must not contain the following characters : '|',
     *            ';', ':', '"'
     * @param normalReturnUrl the url where the user will be redirected when
     *            transaction completes normally
     * @param cancelReturnUrl the url where the user will be redirected when he
     *            decides to cancel the transaction
     * @param automaticResponseUrl the url that will be called by the server
     * @throws BadProgrammerException if userData contains any of the forbidden
     *             characters
     * @throws ExternalErrorException if errors occurs when using the API binary
     *             file
     */
    public static MercanetTransaction createTransaction(int transactionId,
                                                        BigDecimal amount,
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

    /**
     * Checks if the string <code>returnContext</code> is a valid value for the
     * <i>return_context</i> field
     * 
     * @param returnContext the string to validate
     * @return the string if it is valid
     * @throws BadProgrammerException if the string is not valid
     */
    private static String checkReturnContext(String returnContext) {
        String[] forbiddenChars = { "|", ";", ":", "\"" };

        for (String forbiddenChar : forbiddenChars) {
            if (returnContext.contains(forbiddenChar)) {
                throw new BadProgrammerException("The user data contains forbidden chars (" + forbiddenChars + ") : " + returnContext);
            }
        }
        return returnContext;
    }

    /**
     * Checks if the string <code>lanuageCode</code> is a valid language code
     * (i.e: it is handled by the payment API)
     * 
     * @param languageCode the code to validate
     * @return <code>languageCode</code> if it is valid, <i>en</i> otherwise
     */
    private static String filterLanguage(String languageCode) {
        if (!supportedLanguages.contains(languageCode)) {
            return "en";
        }
        return languageCode;
    }

    /**
     * Executes a request to the Mercanet API, and returns a pair containing the
     * return of the API.
     * <p>
     * Example of use :
     * 
     * <pre>
     * Pair&lt;String, String&gt; executionResultPairOfString = executeRequest(params);
     * String data = executionResultPairOfString.first;
     * String baseUrl = executionResultPairOfString.second;
     * </pre>
     * 
     * </p>
     * 
     * @returns a pair containing first the data, second the url to go for the
     *          payment
     */
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

        System.out.println(query.toString());
        
        Runtime runtime = Runtime.getRuntime();
        String response;
        try {
            Process proc = runtime.exec(query.toString());
            response = IOUtils.toString(proc.getInputStream(), "UTF-8");

            System.out.println(response);
            if (proc.waitFor() != 0) {
                throw new ExternalErrorException("Failure during execution of Merc@net binary: " + query.toString() + " - exit value: "
                        + proc.exitValue());
            }
        } catch (IOException e) {
            throw new ExternalErrorException("Failed to execute Merc@net binary: " + query.toString(), e);
        } catch (InterruptedException e) {
            throw new ExternalErrorException("No luck, you have been hit by a signal !", e);
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
}
