/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloatit.framework.webprocessor.url;

import java.util.HashMap;

/**
 * @author fred
 */
public class GenericMessageFormater extends HashMap<String, String> {
    private static final long serialVersionUID = 8978382885335844666L;

    GenericMessageFormater(final String paramName, final String value) {
        put("%paramName%", paramName);
        put("%value%", value);
    }

}
