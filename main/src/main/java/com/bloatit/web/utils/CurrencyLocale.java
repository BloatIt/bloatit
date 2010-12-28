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
package com.bloatit.web.utils;

import com.bloatit.common.FatalErrorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Class to handle localization of money amounts.
 */
public class CurrencyLocale {
    private final static String DEFAULT_CURRENCY = "EURO";
    private final static String DEFAULT_CURRENCY_SYMBOL = "€";
    private final static int INTERNAL_PRECISION = 6;
    private final static RoundingMode ROUNDING_MODE = RoundingMode.HALF_DOWN;
    private final static int DISPLAY_PRECISION = 2;

    private final Locale targetLocale;
    private final BigDecimal euroAmount;
    private final Map<Currency, BigDecimal> currencies = new HashMap<Currency, BigDecimal>();
    private final Currency currency;

    public CurrencyLocale(BigDecimal euroAmount, Locale targetLocale) {
        this.euroAmount = euroAmount;
        this.targetLocale = targetLocale;
        this.currency = Currency.getInstance(targetLocale);
        this.parseRate();
    }

    /**
     * Converts the amount
     */
    public BigDecimal getConvertedAmount() {
        return euroAmount.multiply(currencies.get(currency));
    }

    /**
     * 
     * @return
     */
    public String getLocaleSymbol() {
        return currency.getSymbol();
    }

    /**
     *
     * @return
     */
    public String getLocaleString() {
        return getConvertedAmount().setScale(DISPLAY_PRECISION, ROUNDING_MODE) + getLocaleSymbol();
    }

    /**
     *
     * @return
     */
    public String getDefaultString() {
        return this.euroAmount.toPlainString() + " " + DEFAULT_CURRENCY_SYMBOL;
    }

    private void parseRate() {
        BufferedReader br = null;
        try {
            File file = new File("../locales/rates");

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
        } catch (IOException ex) {
            throw new FatalErrorException(ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
            }
        }
    }
}
