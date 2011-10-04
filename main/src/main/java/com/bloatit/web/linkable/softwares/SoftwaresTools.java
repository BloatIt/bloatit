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
package com.bloatit.web.linkable.softwares;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Image;
import com.bloatit.model.Software;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.SoftwarePageUrl;

public class SoftwaresTools {

    public static class Logo extends HtmlDiv {
        public Logo(final Software software) {
            super("software_logo_block");
            if (software == null || software.getImage() == null) {
                add(new HtmlImage(new Image(WebConfiguration.getImgSoftwareNoLogo()), tr("Software logo"), "software_logo"));
            } else {
                final FileResourceUrl imageUrl = new FileResourceUrl(software.getImage());
                HtmlLink softwareLink = new SoftwarePageUrl(software).getHtmlLink();
                add(softwareLink);
                softwareLink.add(new HtmlImage(imageUrl, tr("Software logo"), "software_logo"));
            }
        }
    }

    public static class SmallLogo extends HtmlDiv {
        public SmallLogo(final Software software) {
            super("software_logo_block");
            if (software == null || software.getImage() == null) {
                add(new HtmlImage(new Image(WebConfiguration.getImgSoftwareNoLogo()), tr("Software logo"), "software_logo_small"));
            } else {
                final FileResourceUrl imageUrl = new FileResourceUrl(software.getImage());
                add(new HtmlImage(imageUrl, tr("Software logo"), "software_logo_small"));
            }
        }
    }

    public static class Link extends HtmlSpan {
        public Link(final Software software) {
            super("software_link");
            if (software != null) {
                add(new SoftwarePageUrl(software).getHtmlLink(software.getName()));
            } else {
                addText(Context.tr("No software"));
            }
        }
    }
}
