package com.bloatit.web.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RequestParam {

    static public final String defaultDefaultValue = "42!$%*/;19901481602plqsdjcjuh$*ù^88a71599aABCD";

    public enum Role {
        POST, GET, PRETTY, SESSION
    }

    String name() default "";

    tr notFoundMsg() default @tr("Error: invalid value (%value) for parameter \"%param\"");

    tr malformedMsg() default @tr("Error: invalid value (%value) for parameter \"%param\"");

    Message.Level level() default Message.Level.ERROR;

    String defaultValue() default defaultDefaultValue;

    Role role() default Role.GET;

    String generatedFrom() default "";
}
