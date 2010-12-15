package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.server.Linkable;

public class OldUrl {

    public static String getPageName(final Class<? extends Linkable> linkable) {
        if (linkable.getAnnotation(ParamContainer.class) != null) {
            return linkable.getAnnotation(ParamContainer.class).value();
        } else {
            return linkable.getSimpleName().toLowerCase();
        }
    }

    // public static void main(String[] args) {
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("id", "12");
    // params.put("title",
    // "Hello ceci est---un ^¨ \n --& titre.               ---  ");
    // params.put("demand_tab_key", "key");
    //
    // DemandUrl demandUrl = new DemandUrl(params);
    //
    // System.out.println(demandUrl);
    //
    // System.out.println(demandUrl.getDemand());
    //
    // System.out.println(demandUrl.getMessages().hasMessage(Level.ERROR));
    // }

}
