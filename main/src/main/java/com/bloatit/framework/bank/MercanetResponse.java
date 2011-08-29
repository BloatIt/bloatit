package com.bloatit.framework.bank;

import java.util.EnumSet;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.components.form.Displayable;
import com.bloatit.framework.webprocessor.context.Context;

public class MercanetResponse {

    private final static int CODE_INDEX = 0;
    private final static int ERROR_INDEX = 1;
    private final static int MERCHANT_ID_INDEX = 2;
    private final static int MERCHANT_COUNTRY_INDEX = 3;
    private final static int AMOUNT_INDEX = 4;
    private final static int TRANSACTION_ID_INDEX = 5;
    private final static int PAYMENT_MEANS_INDEX = 6;
    private final static int TRANSMISSION_DATE_INDEX = 7;
    private final static int PAYMENT_TIME_INDEX = 8;
    private final static int PAYMENT_DATE_INDEX = 9;
    private final static int RESPONSE_CODE_INDEX = 10;
    private final static int PAYMENT_CERTIFICATE_INDEX = 11;
    private final static int AUTHORIZATION_ID_INDEX = 12;
    private final static int CURRENCY_CODE_INDEX = 13;
    private final static int CARD_NUMBER_INDEX = 14;
    private final static int CVV_FLAG_INDEX = 15;
    private final static int CVV_RESPONSE_CODE_INDEX = 16;
    private final static int BANK_RESPONSE_CODE_INDEX = 17;
    private final static int COMPLEMENTARY_CODE_INDEX = 18;
    private final static int COMPLEMENTARY_INFO_INDEX = 19;
    private final static int RETURN_CONTEXT_INDEX = 20;
    private final static int CADDIE_INDEX = 21;
    private final static int RECEIPT_COMPLEMENT_INDEX = 22;
    private final static int MERCHANT_LANGUAGE_INDEX = 23;
    private final static int LANGUAGE_INDEX = 24;
    private final static int CUSTOMER_ID_INDEX = 25;
    private final static int ORDER_ID_INDEX = 26;
    private final static int CUSTOMER_EMAIL_INDEX = 27;
    private final static int CUSTOMER_IP_ADDRESS_INDEX = 28;
    private final static int CAPTURE_DAY_INDEX = 29;
    private final static int CAPTURE_MODE_INDEX = 30;
    private final static int DATA_INDEX = 31;
    private final static int ORDER_VALIDITY_INDEX = 32;
    private final static int TRANSACTION_CONDITION_INDEX = 33;
    private final static int STATEMENT_REFERENCE_INDEX = 34;
    private final static int CARD_VALIDITY_INDEX = 35;
    private final static int SCORE_VALUE_INDEX = 36;
    private final static int SCORE_COLOR_INDEX = 37;
    private final static int SCORE_INFO_INDEX = 38;
    private final static int SCORE_THRESHOLD_INDEX = 39;
    private final static int SCORE_PROFILE_INDEX = 40;
    private final String[] responseFields;

    public enum ResponseCode implements Displayable {
        //@formatter:off
        AUTHORISATION_ACCEPTED("00", tr("Payment accepted")),
        THRESHOLD("02", tr("Credit card threshold crossed. Sorry, but the L annex is missing so we don't understand this response code.")),
        INVALID_MERCHANT_ID("03", tr("Invalid merchant id")),
        AUTHORISATION_REFUSED("05", tr("Invalid merchant id")),
        TRANSACTION_INVALID("12", tr("Invalid transaction parameters")),
        CANCELED_BY_CUSTOMER("17", tr("You canceled the transaction")),
        FORMAT_ERROR("30", tr("We made an error communicating with the bank")),
        SUSPECTED_FRAUD("34", tr("Fraud suspected !") ),
        TOO_MANY_RETRIES("75", tr("Too many retries with the same credit card")),
        SERVICE_NOT_AVAILABLE("90", tr("The payment service is not available")),
        UNKNOWN_CODE("??", tr("Unknown error"));
        //@formatter:on

        public final String code;
        public final String label;

        private ResponseCode(String code, String label) {
            this.code = code;
            this.label = label;
        }

        public static ResponseCode create(String code) {
            for (ResponseCode e : EnumSet.allOf(ResponseCode.class)) {
                if (e.code.equals(code)) {
                    return e;
                }
            }
            Log.payment().warn("Unknown response code: " + code);
            return UNKNOWN_CODE;
        }

        @Override
        public String getDisplayName() {
            return Context.tr(label);
        }

        // Fake tr
        private static String tr(final String fake) {
            return fake;
        }

    }

    public MercanetResponse(String response) {

        if (response == null) {
            throw new BadProgrammerException("The Merc@net response is null");
        }

        responseFields = response.substring(1).split("!");

        if (hasError()) {
            Log.payment().fatal("Error with payment API: " + getError());
        }
    }

    public String getError() {
        return responseFields[ERROR_INDEX];
    }

    public boolean hasError() {
        return !responseFields[CODE_INDEX].equals("0");
    }

    public ResponseCode getResponseCode() {
        return ResponseCode.create(responseFields[RESPONSE_CODE_INDEX]);
    }

}
