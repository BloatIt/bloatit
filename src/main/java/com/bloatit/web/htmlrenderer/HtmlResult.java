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

import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Request;
import com.bloatit.web.server.Session;

public class HtmlResult extends IndentedText {

    private final Session session;
    private Page redirect = null;
    private int titleCount = 0;
    private boolean valid = true;
    private StringBuilder result;
    private final Request currentRequest;

    public HtmlResult(Session session,Request currentRequest) {
        super();
        this.session = session;
        this.redirect = null;
        this.currentRequest = currentRequest;
    }

    public void setRedirect(Page redirect) {
        this.redirect = redirect;
    }

    public String generate() {

        result = new StringBuilder();
        result.append("Set-Cookie: session_key=");
        result.append(this.session.getKey());
        result.append("; path=/; Max-Age=1296000; Version=1 \r\n");

        if (valid) {
            if (hasContent()) {
                if (currentRequest.isStable()) {
                    session.setLastStablePage(currentRequest);
                    session.setTargetPage(null);
                }

                result.append("Content-Type: text/html\r\n");
                closeHeaders();
                result.append(getText());

            } else {
                if (redirect == null) {
                    if (session.getTargetPage() != null) {
                        writeRedirect(session.getTargetPage());
                    } else {
                        if (session.getLastStablePage() != null) {
                            writeRedirect(session.getLastStablePage());
                        } else {
                            writeRedirect(new IndexPage(session));
                        }
                    }
                } else {
                    writeRedirect(redirect);
                }
                closeHeaders();
            }
        } else {
            if (session.getLastStablePage() == null) {
                writeRedirect(new IndexPage(session));
            } else {
                writeRedirect(session.getLastStablePage());
            }
            closeHeaders();
        }

        return result.toString();
    }

    public String pushTitle() {
        titleCount++;
        return new Integer(titleCount).toString();
    }

    public void popTitle() {
        titleCount--;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    private void writeRedirect(Request redirectPage) {
        result.append("Location: ");
        result.append(redirectPage.getUrl());
        result.append("\r\n");
    }

    private void closeHeaders() {
        result.append("\r\n");
    }
}
