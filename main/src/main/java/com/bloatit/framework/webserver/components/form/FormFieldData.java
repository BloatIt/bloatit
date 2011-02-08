package com.bloatit.framework.webserver.components.form;

import com.bloatit.framework.webserver.url.Messages;

public interface FormFieldData<T> {

    /**
     * @return the name of the field.
     */
    String getFieldName();

    /**
     * When a user input wrong value, you have to regenerated the form with error
     * messages.
     *
     * @return the error messages on the field.
     */
    Messages getFieldMessages();


    /**
     * @return the default value to put into the field, or an empty string if there is no
     *         empty field.
     */
    T getFieldDefaultValue();

    String getFieldDefaultValueAsString();

}
