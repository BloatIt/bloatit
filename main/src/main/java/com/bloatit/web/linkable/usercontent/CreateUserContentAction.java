package com.bloatit.web.linkable.usercontent;

import java.util.Locale;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.model.Team;
import com.bloatit.model.UserContentInterface;
import com.bloatit.web.url.CreateUserContentActionUrl;

@ParamContainer("usercontent/docreate")
public abstract class CreateUserContentAction extends CreateAttachmentAction {

    @RequestParam(role = Role.POST)
    @Optional
    private final Team team;

    @RequestParam(role = Role.POST)
    @Optional
    private final Locale locale;

    private final CreateUserContentActionUrl createUserActionurl;

    public CreateUserContentAction(final CreateUserContentActionUrl url) {
        super(url);
        this.createUserActionurl = url;
        team = url.getTeam();
        locale = url.getLocale();
    }

    @Override
    protected final void transmitParameters() {
        session.addParameter(createUserActionurl.getTeamParameter());
        session.addParameter(createUserActionurl.getLocaleParameter());
        doTransmitParameters();
    }

    protected abstract void doTransmitParameters();

    protected final boolean propagateAsTeamIfPossible(final UserContentInterface<?> content) {
        if (getTeam() != null && content.canAccessAsTeam(getTeam())) {
            try {
                content.setAsTeam(getTeam());
            } catch (final UnauthorizedOperationException e) {
                throw new ShallNotPassException("Yon cannot set AsTeam (Even if y tested it ...)", e);
            }
            return true;
        }
        return false;
    }

    protected final Team getTeam() {
        return team;
    }

    protected final Locale getLocale() {
        return locale;
    }
}
