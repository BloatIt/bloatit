package com.bloatit.web.linkable.softwares;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.model.Software;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.SoftwarePageUrl;

public class SoftwaresTools {

    public static HtmlElement getSoftwareLogo(final Software software) throws UnauthorizedOperationException {
        final HtmlDiv logoDiv = new HtmlDiv("software_logo_block");
        if (software.getImage() == null) {
            logoDiv.add(new HtmlImage(new Image(WebConfiguration.getImgSoftwareNoLogo()), tr("Software logo"), "software_logo"));
        } else {
            final FileResourceUrl imageUrl = new FileResourceUrl(software.getImage());
            logoDiv.add(new HtmlImage(imageUrl, tr("Software logo"), "software_logo"));
        }

        return logoDiv;
    }

    public static HtmlElement getSoftwareLogoSmall(final Software software) throws UnauthorizedOperationException {
        final HtmlDiv logoDiv = new HtmlDiv("software_logo_small_block");
        if (software.getImage() == null) {
            logoDiv.add(new HtmlImage(new Image(WebConfiguration.getImgSoftwareNoLogo()), tr("Software logo"), "software_logo_small"));
        } else {
            final FileResourceUrl imageUrl = new FileResourceUrl(software.getImage());
            logoDiv.add(new HtmlImage(imageUrl, tr("Software logo"), "software_logo_small"));
        }

        return logoDiv;
    }

    public static HtmlElement getSoftwareLink(final Software software) throws UnauthorizedOperationException {
        final HtmlSpan softwareSpan = new HtmlSpan("software_link");
        softwareSpan.add(new SoftwarePageUrl(software).getHtmlLink(software.getName()));

        return softwareSpan;
    }
}
