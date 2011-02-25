/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.rest.adapter;

import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.bloatit.framework.rest.exception.RestException;
import com.bloatit.framework.webserver.masters.HttpResponse.StatusCode;

public class LocaleAdapter extends XmlAdapter<String, Locale> {

    /*
     * (non-Javadoc)
     * @see
     * javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    @Override
    public Locale unmarshal(String localeString) throws Exception {
        if(localeString.isEmpty()){
            throw new RestException(StatusCode.ERROR_403_FORBIDDEN, "Locale mustn't be empty !");
        }
        
        String sep = " ";
        if (localeString.contains("_")) {
            sep = "_";
        } else if( localeString.contains("-") ){
            sep = "-";
        } 

        if ( sep.equals(" ")) {
            return new Locale(localeString);
        }
        
        String[] parts = localeString.split(sep);
        if(parts.length > 1){
            return new Locale(parts[0], parts[1]);
        }
        
        return new Locale(parts[0]);
    }

    /*
     * (non-Javadoc)
     * @see
     * javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public String marshal(Locale locale) throws Exception {
        return locale.toString();
    }
}
