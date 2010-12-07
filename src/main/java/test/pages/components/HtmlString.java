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

package test.pages.components;

import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.Translatable;
import com.bloatit.web.utils.TranslationManipulator;

public class HtmlString {

    private final StringBuilder stringBuilder = new StringBuilder();
    private Session session = null;

    public HtmlString() {
    }

    public HtmlString(Session session) {
        this.session = session;
    }

    public HtmlString add(String string) {
        stringBuilder.append(string);
        return this;
    }

    public HtmlString secure(String string) {
        stringBuilder.append(HtmlTools.escapeUrlString(string));
        return this;
    }

    public HtmlString add(Translatable translatable) {
        final TranslationManipulator tm = new TranslationManipulator(session.getPreferredLangs());
        return add(tm.tr(translatable).getText()); // TODO correct me !
    }

    public HtmlString secure(Translatable translatable) {
        final TranslationManipulator tm = new TranslationManipulator(session.getPreferredLangs());
        return secure(tm.tr(translatable).getText()); // TODO correct me !
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    public static String Translate(Session session, Translatable text) {
        final HtmlString string = new HtmlString(session);
        string.add(text);
        return string.toString();
    }

}
