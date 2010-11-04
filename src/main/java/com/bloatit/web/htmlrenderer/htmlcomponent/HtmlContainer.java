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

package com.bloatit.web.htmlrenderer.htmlcomponent;

import java.util.ArrayList;

import com.bloatit.web.htmlrenderer.HtmlResult;

/**
 * A component used to store other components
 */
public class HtmlContainer extends HtmlComponent {
    private final ArrayList<HtmlComponent> components;

    public HtmlContainer() {
        components = new ArrayList<HtmlComponent>();
    }

    /**
     * Add a new component at the end of the list of components
     * 
     * @param newComponent the added component
     */
    public void add(HtmlComponent newComponent) {
        components.add(newComponent);

    }

    @Override
    public void generate(HtmlResult htmlResult) {
        for (final HtmlComponent component : components) {
            component.generate(htmlResult);
        }
    }
}
