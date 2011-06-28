package com.bloatit.web.linkable.admin.moderation;

import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.admin.master.AdminAction;
import com.bloatit.web.url.FeatureListPageUrl;
import com.bloatit.web.url.FeatureModerationActionUrl;
import com.bloatit.web.url.FeatureModerationPageUrl;

@ParamContainer("domoderatefeature/%feature%")
public class FeatureModerationAction extends AdminAction {

    @NonOptional(@tr("You have to specify the feature you want to delete."))
    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the feature number: ''%value%''."))
    private final Feature feature;

    public FeatureModerationAction(FeatureModerationActionUrl url) {
        super(url);
        this.feature = url.getFeature();
    }

    @Override
    protected Url doProcessAdmin() throws UnauthorizedOperationException {
        feature.delete();
        session.notifyGood(Context.tr("The feature has been successfully deleted"));
        return new FeatureListPageUrl();
    }

    @Override
    protected Url checkRightsAndEverything(Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors(ElveosUserToken userToken) {
        if (feature == null) {
            session.notifyError("I need a feature to delete");
            return new PageNotFoundUrl();
        }
        return new FeatureModerationPageUrl(feature);
    }

    @Override
    protected void transmitParameters() {
        // nada
    }
}
