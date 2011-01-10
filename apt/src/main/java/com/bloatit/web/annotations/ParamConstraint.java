package com.bloatit.web.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ParamConstraint {

    public static final String DEFAULT_MIN_STR = "";
    public static final int DEFAULT_MIN = Integer.MIN_VALUE;
    public static final String DEFAULT_MAX_STR = "";
    public static final int DEFAULT_MAX = Integer.MAX_VALUE;
    public static final String DEFAULT_ERROR_MSG = "";
    public static final int DEFAULT_PRECISION = Integer.MAX_VALUE;
    public static final boolean DEFAULT_OPTIONAL = false;
    public static final Integer DEFAULT_LENGTH = Integer.MAX_VALUE;

    String min() default DEFAULT_MIN_STR;
    tr minErrorMsg() default @tr(DEFAULT_ERROR_MSG);

    String max() default DEFAULT_MAX_STR;
    tr maxErrorMsg() default @tr(DEFAULT_ERROR_MSG);

    boolean optional() default DEFAULT_OPTIONAL;
    tr optionalErrorMsg() default @tr(DEFAULT_ERROR_MSG);

    int precision() default DEFAULT_PRECISION;
    tr precisionErrorMsg() default @tr(DEFAULT_ERROR_MSG);

    int length() default Integer.MAX_VALUE;
    tr LengthErrorMsg() default @tr(DEFAULT_ERROR_MSG);
}
