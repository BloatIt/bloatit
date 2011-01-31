package com.bloatit.web.pages.master;

import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;

public class Footer extends HtmlDiv {

    protected Footer() {
        super();
        setId("footer");
        addText(Context.tr("This website is under GNU Affero Public Licence."));
    }

}
