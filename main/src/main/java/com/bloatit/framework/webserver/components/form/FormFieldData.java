package com.bloatit.framework.webserver.components.form;

import com.bloatit.framework.webserver.url.Messages;

/**
 * <p>
 * An interface used to describe data that can be passed to a form
 * </p>
 * <p>
 * This class is used to link an API used to handle parameters from POST/GET (or
 * even session) with the form fields
 * </p>
 *
 * @param <T> the underlying type of the FormFieldData
 */
public interface FormFieldData<T> {

    /**
     * @return the name of the field.
     */
    String getFieldName();

    /**
     * When a user input wrong value, you have to regenerated the form with
     * error messages.
     *
     * @return the error messages on the field.
     */
    Messages getErrorMessages();

    /**
     * @return the suggested value to put into the field, or a null if there is
     *         no suggested value
     */
    String getSuggestedValue();
}
