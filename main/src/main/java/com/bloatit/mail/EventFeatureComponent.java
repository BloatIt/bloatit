package com.bloatit.mail;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.model.Feature;
import com.bloatit.model.Image;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.MoneyDisplayComponent;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.SoftwarePageUrl;

public class EventFeatureComponent extends HtmlDiv {

    private HtmlDiv entriesDiv;

    public EventFeatureComponent(Feature f, Localizator l) {
        super("event-feature");
        HtmlDiv progressBar = new HtmlDiv("event-progress");
        super.add(progressBar);
        float progression = f.getProgression();
        if (progression == Float.POSITIVE_INFINITY) {
            progression = 0;
        }
        if (progression > 100) {
            progression = 100;
        }
        progressBar.add(new HtmlDiv("event-progress-filled").addAttribute("percent", String.valueOf(progression)));
        HtmlDiv progressBarText = new HtmlDiv("event-progress-text");
        progressBar.add(progressBarText);
        if (f.getFeatureState() == FeatureState.PENDING) {
            progressBarText.add(new HtmlDiv("event-progress-money").add(new MoneyDisplayComponent(f.getContribution(), l)).addText(l.tr("financed")));
            progressBarText.add(new HtmlDiv("event-progress-no-offer").addText(l.tr("no offer")));
        } else if (f.getFeatureState() == FeatureState.PREPARING) {
            progressBarText.add(new HtmlDiv("event-progress-money").add(new MoneyDisplayComponent(f.getContribution(), l)).addText(l.tr("financed")));
            progressBarText.add(new HtmlDiv("event-progress-percent").addText(String.valueOf(f.getProgression()) + " %"));
            progressBarText.add(new HtmlDiv("event-progress-pledged").add(new MoneyDisplayComponent(f.getSelectedOffer().getAmount(), l))
                                                                     .addText(l.tr("pledged")));
        } else if (f.getFeatureState() == FeatureState.DEVELOPPING) {
            progressBarText.add(new HtmlDiv("event-progress-money").add(new MoneyDisplayComponent(f.getContribution(), l)).addText(l.tr("financed")));
            progressBarText.add(new HtmlDiv("event-progress-developing").addText(l.tr("in development")));
        } else if (f.getFeatureState() == FeatureState.FINISHED) {
            progressBarText.add(new HtmlDiv("event-progress-money").add(new MoneyDisplayComponent(f.getContribution(), l)).addText(l.tr("financed")));
            progressBarText.add(new HtmlDiv("event-progress-success").addText(l.tr("success")));
        } else if (f.getFeatureState() == FeatureState.DISCARDED) {
            throw new NotImplementedException();
        }

        HtmlDiv logodiv = new HtmlDiv("software_logo_block");
        if (f.getSoftware() == null || f.getSoftware().getImage() == null) {
            logodiv.add(new HtmlImage(new Image(WebConfiguration.getImgSoftwareNoLogo()), tr("Software logo"), "software_logo"));
        } else {
            final FileResourceUrl imageUrl = new FileResourceUrl(f.getSoftware().getImage());
            HtmlLink softwareLink = new SoftwarePageUrl(f.getSoftware()).getHtmlLink();
            logodiv.add(softwareLink);
            softwareLink.add(new HtmlImage(imageUrl, l.tr("Software logo"), "software_logo"));
        }

        super.add(new HtmlDiv("event-feature-logo").add(logodiv));
        super.add(new HtmlTitle(2).addText(f.getTitle(l.getLocale())));
        entriesDiv = new HtmlDiv("event-feature-entries");
        super.add(entriesDiv);
    }

    @Override
    public HtmlBranch add(HtmlNode html) {
        return entriesDiv.add(html);
    }

}