package com.bloatit.framework.webserver.components.form;

/**
 * <p>
 * An interface to be implemented by elements that want to be directly inserted
 * into a and HtmlDropDown
 * </p>
 * 
 * @see HtmlDropDown
 */
public interface DropDownElement {
    /**
     * The value that will be displayed to the user in the HtmlDropDown
     * 
     * @return the displayed value
     */
    public String getName();

    /**
     * A value, hidden to the user, that will be used to understand the meaning
     * of the dropDown selection
     * 
     * @return the hidden code
     */
    public String getCode();
}
