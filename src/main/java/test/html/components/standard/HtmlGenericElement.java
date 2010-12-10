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

package test.html.components.standard;

import test.pages.HtmlContainerElement;

/**
 * <p>Can be used to create any Html element by just passing the name of the
 * markup in the constructor</p>
 * <p>
 * The following example will create a blank textarea :
 * <pre>
 * HtmlElement element = new HtmlGenericElement("textarea");
 * </pre>
 * </p>
 * <p> Methods such as add or addatribute will be available on such element</p>
 * <p> Note, this class should be used carefully</p>
 */
public class HtmlGenericElement extends HtmlContainerElement{

    public HtmlGenericElement(String tag){
        super(tag);
    }
}