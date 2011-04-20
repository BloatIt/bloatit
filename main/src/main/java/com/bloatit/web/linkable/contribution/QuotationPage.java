package com.bloatit.web.linkable.contribution;

import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.web.linkable.usercontent.CreateUserContentPage;
import com.bloatit.web.url.QuotationPageUrl;
import com.bloatit.web.url.UserContentActionUrl;

@ParamContainer("quotationPage")
public abstract class QuotationPage extends CreateUserContentPage {
    
    @RequestParam(conversionErrorMsg = @tr("The process is closed, expired, missing or invalid."))
    @ParamConstraint(optionalErrorMsg = @tr("The process is closed, expired, missing or invalid."))
    protected final ContributionProcess process;

    @Optional("false")
    @RequestParam(name = "show_fees_detail")
    private Boolean showFeesDetails;

    protected QuotationPage(final QuotationPageUrl url, final UserContentActionUrl targetUrl) {
        super(url, targetUrl);
        showFeesDetails = url.getShowFeesDetails();
        process = url.getProcess();
    }

    protected boolean hasToShowFeeDetails() {
        return showFeesDetails;
    }
}
