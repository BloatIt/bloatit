package com.bloatit.web.linkable.admin.exception;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;
import com.bloatit.framework.exceptions.highlevel.MeanUserException;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.admin.AdminAction;
import com.bloatit.web.linkable.admin.exception.ExceptionAdministrationPage.ErrorType;
import com.bloatit.web.url.ExceptionAdministrationActionUrl;
import com.bloatit.web.url.ExceptionAdministrationPageUrl;

@ParamContainer("admin/doexception")
public class ExceptionAdministrationAction extends AdminAction {
    private ExceptionAdministrationActionUrl url;

    @RequestParam(role = Role.GET)
    private final ErrorType level;

    public ExceptionAdministrationAction(ExceptionAdministrationActionUrl url) {
        super(url);
        this.url = url;
        this.level = url.getLevel();
    }

    @Override
    protected Url doProcessAdmin() throws UnauthorizedOperationException {
        switch (level) {
            case BAD_PROGRAMMER:
                throw new BadProgrammerException("This is a TEST exception generated from an administration page. Please IGNORE me.");
            case MEAN_USER:
                throw new MeanUserException("This is a TEST exception generated from an administration page. Please IGNORE me.");
            case SHALL_NOT_PASS:
                throw new ShallNotPassException("This is a TEST exception generated from an administration page. Please IGNORE me.");
            case EXTERNAL_ERROR:
                throw new ExternalErrorException("This is a TEST exception generated from an administration page. Please IGNORE me.");
        }
        return new ExceptionAdministrationPageUrl();
    }

    @Override
    protected Url checkRightsAndEverything(Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors(ElveosUserToken userToken) {
        return NO_ERROR;
    }

    @Override
    protected void transmitParameters() {
        // Osef
    }
}
