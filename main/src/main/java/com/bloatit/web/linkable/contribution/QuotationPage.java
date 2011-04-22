package com.bloatit.web.linkable.contribution;

import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.url.QuotationPageUrl;

@ParamContainer("quotationPage")
public abstract class QuotationPage extends LoggedPage {
    
    @Optional("false")
    @RequestParam(name = "show_fees_detail")
    private Boolean showFeesDetails;

    protected QuotationPage(final QuotationPageUrl url) {
        super(url);
        showFeesDetails = url.getShowFeesDetails();
    }

    protected boolean hasToShowFeeDetails() {
        return showFeesDetails;
    }
}
