package com.bloatit.web.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RequestParam {

    String name() default "";

    Class<? extends Loader<?>> loader() default Loader.DefaultConvertor.class;

    String message() default "Error: invalid value (%value) for parameter \"%param\"";

    Message.Level level() default Message.Level.ERROR;

    String defaultValue() default "";

}
