package com.bloatit.framework.rest.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.bloatit.framework.rest.RestServer.RequestMethod;

/**
 * <p>
 * Annotation used to describe methods that can be used via ReST.
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface REST {
    /**
     * @return
     */
    String name();

    RequestMethod method();

    public String[] params() default {};
}
