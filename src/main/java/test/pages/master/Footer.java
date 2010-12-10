package test.pages.master;

import test.Context;
import test.html.components.standard.HtmlBlock;

public class Footer extends HtmlBlock {

    protected Footer() {
        super();
        setId("footer");
        addText(Context.tr("This website is under GNU Affero Public Licence."));
    }

}
