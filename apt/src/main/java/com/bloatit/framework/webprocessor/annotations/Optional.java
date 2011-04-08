package com.bloatit.framework.webprocessor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface Optional {

    static public final String DEFAULT_DEFAULT_VALUE = "42!$%*/;19901481602plqsdjcjuh$*ù^88a71599aABCD";

    /**
     * The default value when not found
     */
    String value() default DEFAULT_DEFAULT_VALUE;
}
