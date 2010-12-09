package test.pages.master;

import test.Context;
import test.pages.components.HtmlBlock;

public class Footer extends HtmlBlock {

    protected Footer() {
        super();
        setId("footer");
        addText(Context.tr("This website is under GNU Affero Public Licence."));
    }

}
