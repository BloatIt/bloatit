/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloatit.framework.webprocessor.url;

import java.util.HashMap;
import org.hibernate.mapping.Map;

/**
 *
 * @author fred
 */
public class GenericMessageFormater extends HashMap<String, String>{
    
    GenericMessageFormater(String paramName, String value) {
        put("%paramName%%", paramName);
        put("%value%%", value);
    }

}
