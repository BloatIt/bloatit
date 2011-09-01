/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.web.components;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;

public class HtmlProgressBar extends HtmlDiv {

    public HtmlProgressBar(final float... progressList) {
        this(null, null, progressList);
    }

    public HtmlProgressBar(final XmlNode label, final String styleSuffix, final float... progressList) {
        super("progress_bar_block" + (styleSuffix != null ? "_" + styleSuffix : ""));

        add(new HtmlDiv("progress_bar_background"));

        int progressIndex = progressList.length;
        final float previousProgress = 0;
        for (int i = progressList.length - 1; i >= 0; i--) {

            final float progress = progressList[i];

            if (progress > previousProgress) {
                final HtmlDiv progressBarState = new HtmlDiv("progress_bar_state_" + progressIndex);
                {
                    progressBarState.addAttribute("style", "width: " + progress + "%;");
                }
                add(progressBarState);
            }

            progressIndex--;
        }

        if (label != null) {
            add(new HtmlDiv("progress_bar_label").add(label));
        }

    }
}
