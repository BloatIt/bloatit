//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.framework.webprocessor.components.form;

import com.bloatit.framework.webprocessor.url.Messages;

/**
 * <p>
 * An interface used to describe data that can be passed to a form
 * </p>
 * <p>
 * This class is used to link an API used to handle parameters from POST/GET (or
 * even session) with the form fields
 * </p>
 */
public interface FieldData {

    /**
     * @return the name of the field.
     */
    String getName();

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
