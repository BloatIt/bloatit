package com.bloatit.framework.webprocessor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RequestParam {

    static public final String DEFAULT_SUGGESTED_VALUE = "42!$%*/;19901481602plqsdjcjuh$*ù^88a71599aABCD";
    static public final String DEFAULT_ERROR_MSG = "Error: invalid value (%value%) for parameter %paramName%";

    public enum Role {
        POST, GET, PRETTY, SESSION , POSTGET
    }

    String name() default "";

    tr conversionErrorMsg() default @tr(DEFAULT_ERROR_MSG);

    String suggestedValue() default DEFAULT_SUGGESTED_VALUE;

    Role role() default Role.GET;

    String generatedFrom() default "";
}
