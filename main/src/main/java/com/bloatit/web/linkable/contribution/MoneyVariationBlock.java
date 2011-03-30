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
        String diffId="";

        int compareTo = before.compareTo(after);
        if(compareTo > 0) {
            imageId="moneyDown";
            beforeId="up";
            afterId="down";
            diffId="down";
        } else if( compareTo < 0) {
            imageId="moneyUp";
            beforeId="down";
            afterId="up";
            diffId="up";
        } else {
            imageId="moneyEqual";
            beforeId="equal";
            afterId="equal";
            diffId="equal";
        }

        add(new HtmlDiv("money_variation_before_"+beforeId).addText(Context.getLocalizator().getCurrency(before).getDefaultString()));
        add(new HtmlDiv().setCssClass("money_variation_image").add(new HtmlImage(new Image(imageId+".png", ImageType.LOCAL), imageId)));
        HtmlDiv afterBlock = new HtmlDiv("money_variation_after_"+afterId);
        afterBlock.addText(Context.getLocalizator().getCurrency(after).getDefaultString());
        afterBlock.add(new HtmlDiv("money_diff_"+diffId).addText((diffId.equals("up")?"+":"")+Context.getLocalizator().getCurrency(after.subtract(before)).getDefaultString()));
        add(afterBlock);
    }

}