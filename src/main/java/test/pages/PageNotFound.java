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
package test.pages;

import test.RedirectException;
import test.Request;
import test.html.components.standard.HtmlTitle;
import test.pages.master.Page;

public class PageNotFound extends Page {

    public PageNotFound(final Request request) throws RedirectException {
        super(request);
        generateContent();
    }

    private void generateContent() {
        final HtmlTitle errorTitle = new HtmlTitle(session.tr("Page not found"));

        add(errorTitle);

    }

    @Override
    protected String getTitle() {
        return "Page not found";
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
