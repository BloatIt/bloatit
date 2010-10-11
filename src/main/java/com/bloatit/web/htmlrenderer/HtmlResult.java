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

package com.bloatit.web.htmlrenderer;

import com.bloatit.web.server.Request;
import com.bloatit.web.server.Session;


public class HtmlResult extends IndentedText{
    private Session session;
    private String redirect;

    public HtmlResult(Session session) {
        super();
        this.session = session;
        this.redirect = null;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String generate() {
        String result;

        result = "Set-Cookie: session_key="+this.session.getKey()+"; path=/; Max-Age=1296000; Version=1 \r\n";
        
        if (this.redirect != null ){
            result += "Location: "+this.redirect+"\r\n";
        }

        String text = this.getText();

        if(text.length() > 0){
            result += "Content-Type: text/html\r\n";
        }

        result += "\r\n";
        result += text;

        return result;
    }
}