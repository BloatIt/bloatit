package com.bloatit.web.linkable.contribution;

import java.math.BigDecimal;

import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.Image.ImageType;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlImage;

public class MoneyVariationBlock extends HtmlDiv {


    public MoneyVariationBlock(BigDecimal before, BigDecimal after) {
        super("money_variation");

        String imageId="";
        String beforeId="";
        String afterId="";

        int compareTo = before.compareTo(after);
        if(compareTo > 0) {
            imageId="moneyDown";
            beforeId="up";
            afterId="down";
        } else if( compareTo < 0) {
            imageId="moneyUp";
            beforeId="down";
            afterId="up";
        } else {
            imageId="moneyEqual";
            beforeId="equal";
            afterId="equal";
        }

        add(new HtmlDiv("money_variation_before_"+beforeId).addText(Context.getLocalizator().getCurrency(before).getDefaultString()));
        add(new HtmlDiv().setCssClass("money_variation_image").add(new HtmlImage(new Image(imageId+".png", ImageType.LOCAL), imageId)));
        add(new HtmlDiv("money_variation_after_"+afterId).addText(Context.getLocalizator().getCurrency(after).getDefaultString()));
    }

}
