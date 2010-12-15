package com.bloatit.web.html.pages.master;

import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.server.Context;

public class Footer extends HtmlDiv {

    protected Footer() {
        super();
        setId("footer");
        addText(Context.tr("This website is under GNU Affero Public Licence."));
    }

}
