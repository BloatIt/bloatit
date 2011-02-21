package com.bloatit.framework.webserver.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface ParamConstraint {

    public static final String DEFAULT_MIN_STR = "";
    // WARNING !! Do not use Integer here !!
    public static final int DEFAULT_MIN = Integer.MIN_VALUE;
    public static final String DEFAULT_MAX_STR = "";
    public static final int DEFAULT_MAX = Integer.MAX_VALUE;
    public static final String DEFAULT_ERROR_MSG = "Error ! value (%value) invalide for parameter %param.";
    public static final int DEFAULT_PRECISION = Integer.MAX_VALUE;
    public static final boolean DEFAULT_OPTIONAL = false;
    public static final int DEFAULT_LENGTH = Integer.MAX_VALUE;

    boolean minIsExclusive() default false;

    String min() default DEFAULT_MIN_STR;

    tr minErrorMsg() default @tr(DEFAULT_ERROR_MSG);

    boolean maxIsExclusive() default false;

    String max() default DEFAULT_MAX_STR;

    tr maxErrorMsg() default @tr(DEFAULT_ERROR_MSG);

    boolean optional() default DEFAULT_OPTIONAL;

    tr optionalErrorMsg() default @tr(DEFAULT_ERROR_MSG);

    int precision() default DEFAULT_PRECISION;

    tr precisionErrorMsg() default @tr(DEFAULT_ERROR_MSG);

    int length() default Integer.MAX_VALUE;

    tr LengthErrorMsg() default @tr(DEFAULT_ERROR_MSG);
}
