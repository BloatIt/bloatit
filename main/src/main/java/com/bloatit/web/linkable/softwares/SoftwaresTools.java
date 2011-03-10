package com.bloatit.web.linkable.softwares;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlImage;
import com.bloatit.framework.webserver.components.HtmlSpan;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Software;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.SoftwarePageUrl;

public class SoftwaresTools {

    public static HtmlElement getSoftwareLogo(Software software) throws UnauthorizedOperationException {
        HtmlDiv logoDiv = new HtmlDiv("software_logo_block");
        if (software.getImage() == null) {
            logoDiv.add(new HtmlImage(new Image("idea.png", Image.ImageType.LOCAL), tr("Software logo"), "software_logo"));
        } else {
            final FileResourceUrl imageUrl = new FileResourceUrl(software.getImage());
            logoDiv.add(new HtmlImage(imageUrl, tr("Software logo"), "software_logo"));
        }

        return logoDiv;
    }

    public static HtmlElement getSoftwareLink(Software software) throws UnauthorizedOperationException {
        final HtmlSpan softwareSpan = new HtmlSpan("software_link");
        softwareSpan.add(new SoftwarePageUrl(software).getHtmlLink(software.getName()));

        return softwareSpan;
    }
}
