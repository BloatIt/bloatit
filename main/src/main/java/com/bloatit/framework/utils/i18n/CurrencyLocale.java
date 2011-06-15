/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.utils.i18n;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.ConfigurationManager.PropertiesRetriever;
import com.bloatit.common.ConfigurationManager.PropertiesType;
import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.context.Context;

/**
 * Class to handle localization of money amounts.
 */
public final class CurrencyLocale {
    private static final String DEFAULT_CURRENCY_SYMBOL = "€";
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_DOWN;
    private static final int DISPLAY_PRECISION = 0;
    private static final int DISPLAY_PRECISION_DECIMAL = 2;
    private static final Currency DEFAULT_CURRENCY = Currency.getInstance("EUR");

    private static Date lastParse = new Date();
    private static Map<Currency, BigDecimal> currencies = Collections.synchronizedMap(new HashMap<Currency, BigDecimal>());

    private final Locale targetLocale;
    private final BigDecimal euroAmount;
    private Currency currency;

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
     * @param euroAmount the amount of money in the default application currency
     *            (euro)
     * @param targetLocale the <code>Locale</code> that represents the currency
     *            of the
     * @throws CurrencyNotAvailableException whenever <code>targetLocale</code>
     *             currency is not in the list of available currencies
     */
    protected CurrencyLocale(final BigDecimal euroAmount, final Locale targetLocale) throws CurrencyNotAvailableException {
        this.euroAmount = euroAmount;
        this.targetLocale = targetLocale;
        try {
            this.currency = Currency.getInstance(targetLocale);
            parseRate();
            if (!currencies.containsKey(currency)) {
                throw new CurrencyNotAvailableException();
            }
        } catch (final IllegalArgumentException e) {
            try {
                Context.getSession().notifyBad(Context.tr("We can't handle properly the country you selected, considering you're from the US."));
                Log.framework().error("Country " + targetLocale.getCountry() + " selected by user is not valid", e);
                this.currency = Currency.getInstance(Locale.US);
            } catch (final IllegalArgumentException iae) {
                throw new BadProgrammerException("US is not a valid country on this system ... please change system");
            }
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
        return NumberFormat.getInstance(targetLocale).format(getConvertedAmount().setScale(DISPLAY_PRECISION, ROUNDING_MODE)) + getLocaleSymbol();
    }

    /**
     * Returns the displayed amount within the default currency of the
     * application (currently euro)
     * 
     * @return a String representing the <code>amount</code> of money in the
     *         application default currency
     */
    public String getSimpleEuroString() {
        return NumberFormat.getInstance(targetLocale).format(this.euroAmount.setScale(DISPLAY_PRECISION, ROUNDING_MODE)) + " " + DEFAULT_CURRENCY_SYMBOL;
    }

    /**
     * Returns the localized version of the amount with 2 decimal digits, i.e. :
     * converted to the locale money, and with the locale symbol.
     * 
     * @return the localized string
     */
    public String getDecimalLocaleString() {
        NumberFormat numberFormat = NumberFormat.getInstance(targetLocale);
        numberFormat.setMinimumFractionDigits(DISPLAY_PRECISION_DECIMAL);
        numberFormat.setMaximumFractionDigits(DISPLAY_PRECISION_DECIMAL);
        numberFormat.setRoundingMode(RoundingMode.HALF_EVEN);
        return numberFormat.getInstance(targetLocale).format(getConvertedAmount().setScale(DISPLAY_PRECISION_DECIMAL, ROUNDING_MODE)) + getLocaleSymbol();
    }

    /**
     * Returns the displayed amount with 2 decimal digits within the default
     * currency of the application (currently euro)
     * 
     * @return a String representing the <code>amount</code> of money in the
     *         application default currency
     */
    public String getTwoDecimalEuroString() {
        NumberFormat numberFormat = NumberFormat.getInstance(targetLocale);
        numberFormat.setMinimumFractionDigits(DISPLAY_PRECISION_DECIMAL);
        numberFormat.setMaximumFractionDigits(DISPLAY_PRECISION_DECIMAL);
        numberFormat.setRoundingMode(RoundingMode.HALF_EVEN);
        return numberFormat.format(this.euroAmount.setScale(DISPLAY_PRECISION_DECIMAL, ROUNDING_MODE)) + " " + DEFAULT_CURRENCY_SYMBOL;
    }

    /**
     * Returns the localized version of the amount, i.e. : converted to the
     * locale money, and with the locale symbol
     * 
     * @return the localized string
     */
    @Override
    public String toString() {
        return getLocaleString();
    }

    /**
     * Checks wether the target currency is handled
     * 
     * @return <i>true</i> if currency is handled, <i>false</i> otherwise
     */
    public boolean availableTargetCurrency() {
        return currencies.containsKey(currency);
    }

    public boolean isDefaultCurrency() {
        return currency.equals(DEFAULT_CURRENCY);
    }

    /**
     * Returns the current targetLocale
     */
    public Locale getTargetLocale() {
        return targetLocale;
    }

    /**
     * Checks if a currency is handled
     * 
     * @param locale the currency to check
     * @return <i>true</i> if currency is handled, <i>false</i> otherwise
     */
    public static boolean availableCurrency(final Locale locale) {
        return availableCurrency(Currency.getInstance(locale));
    }

    /**
     * Checks if a currency is handled
     * 
     * @param currency the currency to check
     * @return <i>true</i> if currency is handled, <i>false</i> otherwise
     */
    private static boolean availableCurrency(final Currency currency) {
        return currencies.containsKey(currency);
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
        final PropertiesRetriever retriever = ConfigurationManager.loadProperties("locales/rates.properties", PropertiesType.SHARE);
        if (lastParse.before(retriever.getModificationDate())) {
            return;
        }

        final Properties prop = retriever.getProperties();
        for (final Entry<Object, Object> entry : prop.entrySet()) {
            final String currencyCode = (String) entry.getKey();
            final BigDecimal rate = new BigDecimal((String) entry.getValue());
            currencies.put(Currency.getInstance(currencyCode), rate);
        }
        lastParse = new Date();
    }
}
