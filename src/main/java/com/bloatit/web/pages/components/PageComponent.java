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

package com.bloatit.web.pages.components;

import com.bloatit.web.htmlrenderer.HtmlResult;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.server.Session;


public abstract  class PageComponent extends HtmlComponent{

    public Session session;

    public PageComponent(Session session) {
        this.session = session;
    }

    @Override
    public void generate(HtmlResult htmlResult) {
        extractData();
        produce().generate(htmlResult);
    }


   
    protected abstract HtmlComponent produce();
    protected abstract void extractData();
    
    
}
