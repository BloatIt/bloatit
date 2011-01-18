/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.utils.i18n;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.bloatit.common.FatalErrorException;
import com.bloatit.common.Log;

/**
 * Class to handle localization of money amounts.
 */
public final class CurrencyLocale {
    private static final String DEFAULT_CURRENCY_SYMBOL = "€";
    private static final String RATES_PATH = "../locales/rates";
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_DOWN;
    private static final int DISPLAY_PRECISION = 0;

    private static Date lastParse = null;
    private static Map<Currency, BigDecimal> currencies = new HashMap<Currency, BigDecimal>();

    private final Locale targetLocale;
    private final BigDecimal euroAmount;
    private final Currency currency;


    /**
     * <p>
     * Creates a new <code>CurrencyLocale</code>
     * </p>
     * <p>
     * Uses java <code>Locale</code>s to find the appropriate
     * <code>target</code> currency. Conversion rate is obtained from an
     * external source.
     * </p>
     *
     * @param euroAmount
     *            the amount of money in the default application currency (euro)
     * @param targetLocale
     *            the <code>Locale</code> that represents the currency of the
     * @throws CurrencyNotAvailableException
     *             whenever <code>targetLocale</code> currency is not in the
     *             list of available currencies
     */
    public CurrencyLocale(BigDecimal euroAmount, Locale targetLocale) throws CurrencyNotAvailableException {
        this.euroAmount = euroAmount;
        this.targetLocale = targetLocale;
        this.currency = Currency.getInstance(targetLocale);
        parseRate();
        if (!currencies.containsKey(currency)) {
            throw new CurrencyNotAvailableException();
        }
    }

    /**
     * <p>
     * Converts the euro amount to the locale amount
     * </p>
     * <p>
     * Conversion will be done with a 7 digits precision and rounding HALF_ELVEN
     * (meaning it will round to the closest neighbor unless both are
     * equidistant in which case it will round to the closest even number) which
     * is the IEEE 754R default
     * </p>
     *
     * @return the locale amount
     */
    public BigDecimal getConvertedAmount() {
        return euroAmount.multiply(currencies.get(currency), MathContext.DECIMAL32);
    }

    /**
     * Finds the symbol used for this money in the given locale
     *
     * @return the currency symbol
     */
    public String getLocaleSymbol() {
        return currency.getSymbol();
    }

    /**
     * Returns the localized version of the amount, i.e. : converted to the
     * locale money, and with the locale symbol
     *
     * @return the localized string
     */
    public String getLocaleString() {
        return getConvertedAmount().setScale(DISPLAY_PRECISION, ROUNDING_MODE) + getLocaleSymbol();
    }

    /**
     * Returns the displayed amount within the default currency of the
     * application (currently euro)
     *
     * @return a String representing the <code>amount</code> of money in the
     *         application default currency
     */
    public String getDefaultString() {
        return this.euroAmount.setScale(DISPLAY_PRECISION, ROUNDING_MODE).toPlainString() + " " + DEFAULT_CURRENCY_SYMBOL;
    }

    /**
     * Returns the localized version of the amount, i.e. : converted to the
     * locale money, and with the locale symbol
     *
     * @return the localized string
     */
    @Override
    public String toString() {
        return this.getLocaleString();
    }

    /**
     * Checks wether the target currency is handled
     *
     * @return <i>true</i> if currency is handled, <i>false</i> otherwise
     */
    public boolean availableTargetCurrency() {
        return currencies.containsKey(currency);
    }

    /**
     * Checks if a currency is handled
     *
     * @param currency
     *            the currency to check
     * @return <i>true</i> if currency is handled, <i>false</i> otherwise
     */
    public static boolean availableCurrency(Locale locale) {
        return availableCurrency(Currency.getInstance(locale));
    }

    /**
     * Checks if a currency is handled
     *
     * @param currency
     *            the currency to check
     * @return <i>true</i> if currency is handled, <i>false</i> otherwise
     */
    public static boolean availableCurrency(Currency currency) {
        return currencies.containsKey(currency);
    }

    /**
     * Returns the current targetLocale
     */
    public Locale getTargetLocale() {
        return targetLocale;
    }

    /**
     * <p>
     * Parses the rate file and initializes the currency array
     * </p>
     * <p>
     * This parsing will occur only if file has been modified since last parse
     * or if no parse ever occured
     * </p>
     */
    private static void parseRate() {
        BufferedReader br = null;
        try {
            File file = new File(RATES_PATH);
            if (lastParse == null || lastParse.before(new Date(file.lastModified()))) {
                // Only parse if the file has been updated in the meantime
                lastParse = new Date();

                br = new BufferedReader(new FileReader(file));
                while (br.ready()) {
                    String line = br.readLine();
                    if (line.charAt(0) != '#') {
                        String data = line.split("#", 1)[0];
                        String code = data.split("\t")[0];
                        BigDecimal value = new BigDecimal(data.split("\t")[1]);

                        currencies.put(Currency.getInstance(code), value);
                    }
                }
            }
        } catch (IOException ex) {
            throw new FatalErrorException(ex);
        } finally {
            try {
                if (br != null){
                    br.close();
                }
            } catch (Exception ex) {
                Log.web().warn("Error clothing file: " + RATES_PATH);
            }
        }
    }
}
