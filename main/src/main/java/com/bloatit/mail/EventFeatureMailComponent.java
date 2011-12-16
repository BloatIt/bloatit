package com.bloatit.mail;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.text.NumberFormat;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.url.UrlString;
import com.bloatit.model.Feature;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.MoneyDisplayComponent;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.SoftwarePageUrl;

public class EventFeatureMailComponent extends PlaceHolderElement {

    private HtmlGenericElement bodyContent;

    public EventFeatureMailComponent(Feature f, Localizator l) {

        float progression = f.getProgression();
        if (progression == Float.POSITIVE_INFINITY) {
            progression = 0;
        }
        if (progression > 100) {
            progression = 100;
        }

        if (progression < 5 && progression > 0) {
            progression = 5;
        }

        HtmlBranch progress = new HtmlGenericElement("table");
        super.add(progress);
        {
            progress.addAttribute("style", "margin-top: 15px; border-collapse: collapse; padding: 0px; width: 80%;  max-width: 600px;  height: 5px; border-top: 1px solid rgb(225, 225, 225);border-left: 1px solid rgb(225, 225, 225); background: rgb(237,237,237)");
            
            HtmlBranch progressRow = new HtmlGenericElement("tr");
            progress.add(progressRow);
            {
                String backgroundColor = "188,199,254";
                if (f.getFeatureState() == FeatureState.FINISHED) {
                    backgroundColor = "126,200,135";
                }
                
                HtmlBranch progressFilled = new HtmlGenericElement("td");
                progressRow.add(progressFilled);
                progressFilled.addAttribute("style", "width: " + String.valueOf(progression) + "%; height: 5px;padding-top: 5px; background: rgb("+backgroundColor+");");
                HtmlBranch progressBackground = new HtmlGenericElement("td");
                if(progression == 100) {
                    progressBackground.addAttribute("style", "background: rgb("+backgroundColor+");");
                }
                progressRow.add(progressBackground);
            }
        }

        HtmlBranch progressText = new HtmlGenericElement("table");
        super.add(progressText);
        {
            
            String backgroundColor = "237,237,237";
            
            HtmlBranch progressTextRow = new HtmlGenericElement("tr");
            progressText.add(progressTextRow);
            {
                HtmlBranch progressTextLeft = new HtmlGenericElement("td");
                progressTextRow.add(progressTextLeft);
                progressTextLeft.addAttribute("style", "padding-left: 10px;");
                HtmlBranch progressTextCenter = new HtmlGenericElement("td");
                progressTextRow.add(progressTextCenter);
                progressTextCenter.addAttribute("style", "width: 100%;text-align: center;");
                HtmlBranch progressTextRight = new HtmlGenericElement("td");
                progressTextRight.addAttribute("style", "padding-right: 10px;");
                progressTextRow.add(progressTextRight);

                if (f.getFeatureState() == FeatureState.PENDING) {
                    progressTextLeft.add(add(new MoneyDisplayComponent(f.getContribution(), l)).addText(l.tr("&nbsp;financed")));
                    progressTextRight.add(new HtmlDiv("event-progress-no-offer").addText(l.tr("no offer")));
                } else if (f.getFeatureState() == FeatureState.PREPARING) {
                    progressTextLeft.add(new MoneyDisplayComponent(f.getContribution(), l)).addText(l.tr("&nbsp;financed"));
                    final NumberFormat format = l.getNumberFormat();
                    format.setMinimumFractionDigits(0);
                    progressTextCenter.addText(format.format(f.getProgression()) + " %");
                    progressTextRight.add(new MoneyDisplayComponent(f.getSelectedOffer().getAmount(), l)).addText(l.tr("&nbsp;requested"));
                } else if (f.getFeatureState() == FeatureState.DEVELOPPING) {
                    progressTextLeft.add(new MoneyDisplayComponent(f.getContribution(), l)).addText(l.tr("&nbsp;financed"));
                    backgroundColor = "188,199,254";
                    progressTextRight.addText(l.tr("In&nbsp;development"));
                } else if (f.getFeatureState() == FeatureState.FINISHED) {
                    progressTextLeft.add(new MoneyDisplayComponent(f.getContribution(), l)).addText(l.tr("&nbsp;financed"));
                    progressTextRight.addText(l.tr("success"));
                    backgroundColor = "126,200,135";
                } else if (f.getFeatureState() == FeatureState.DISCARDED) {
                    throw new NotImplementedException();
                }

            }
            
            
            progressText.addAttribute("style", "width: 80%; max-width: 600px;padding-bottom: 5px; font-size: 0.9em; border-left: 1px solid rgb(225, 225, 225); background: rgb("+backgroundColor+"); color: rgb(89,89,89);");
            
        }
        HtmlBranch body = new HtmlGenericElement("table");
        body.addAttribute("style", "margin-bottom: 15px;width: 80%; max-width: 600px; background: rgb(247,247,247); padding-bottom: 10px; padding-top: 5px;");
        super.add(body);
        {
            HtmlBranch bodyRow = new HtmlGenericElement("tr");
            body.add(bodyRow);
            {
                HtmlBranch bodyLogo = new HtmlGenericElement("td");
                bodyRow.add(bodyLogo);
                {
                    
                    bodyLogo.addAttribute("style", "max-width: 40px; width: 40px;  ");
                    
                    
                    if (f.getSoftware() == null || f.getSoftware().getImage() == null) {
                        UrlString urlString = new UrlString(WebConfiguration.getImgSoftwareNoLogo());

                        HtmlImage image = new HtmlImage(urlString.externalUrlString(), tr("New software"), "software_logo");
                        image.addAttribute("style", "max-width: 40px; width: 40px;font-size: 0.7em; color: rgb(89,89,89); text-decoration: none; ");
                        bodyLogo.add(image);
                    } else {
                        final FileResourceUrl imageUrl = new FileResourceUrl(f.getSoftware().getImage());
                        HtmlLink softwareLink = new SoftwarePageUrl(f.getSoftware()).getHtmlExternalLink();
                        bodyLogo.add(softwareLink);
                        HtmlImage image = new HtmlImage(imageUrl.externalUrlString(), f.getSoftware().getName(), "software_logo");
                        image.addAttribute("style", "max-width: 40px; width: 40px;font-size: 0.7em; color: rgb(89,89,89); text-decoration: none;");
                        softwareLink.add(image);
                    }
                }

                HtmlBranch bodyContentContainer = new HtmlGenericElement("td");
                bodyRow.add(bodyContentContainer);
                {
                    bodyContentContainer.addAttribute("style", "padding-left: 20px;");
                    
                    HtmlLink htmlLink = new FeaturePageUrl(f, FeatureTabKey.description).getHtmlLink(f.getTitle(l.getLocale()));
                    htmlLink.addAttribute("style", "color: rgb(0,82,108); text-decoration: none;");
                    bodyContentContainer.add(htmlLink);
                    
                    bodyContent = new HtmlGenericElement("table");
                    bodyContentContainer.add(bodyContent);
                    {

                    }
                }
            }

        }

    }

    @Override
    public HtmlBranch add(HtmlNode html) {
        return bodyContent.add(html);
    }

}
