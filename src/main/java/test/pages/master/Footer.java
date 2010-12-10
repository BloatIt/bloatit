package test.pages.master;

import test.Context;
import test.html.components.standard.HtmlDiv;

public class Footer extends HtmlDiv {

    protected Footer() {
        super();
        setId("footer");
        addText(Context.tr("This website is under GNU Affero Public Licence."));
    }

}
