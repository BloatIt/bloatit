package com.bloatit.web.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bloatit.web.utils.Message;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RequestParam {

    public enum Role {
        POST, GET, PRETTY
    }

    String name() default "";

    Class<? extends Loader<?>> loader() default Loaders.DefaultConvertor.class;

    tr message() default @tr("Error: invalid value (%value) for parameter \"%param\"");

    Message.Level level() default Message.Level.ERROR;

    String defaultValue() default "";

    Role role() default Role.GET;

}