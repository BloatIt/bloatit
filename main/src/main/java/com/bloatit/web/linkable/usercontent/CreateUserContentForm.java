package com.bloatit.web.linkable.usercontent;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlFileInput;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.Action;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.url.CreateUserContentActionUrl;
import com.bloatit.web.url.CreateUserContentFormUrl;

/**
 * @author thomas
 */
@ParamContainer("usercontent/create")
public abstract class CreateUserContentForm extends LoggedPage {

    private final CreateUserContentActionUrl targetUrl;

    public CreateUserContentForm(final CreateUserContentFormUrl url, final CreateUserContentActionUrl targetUrl) {
        super(url);
        this.targetUrl = targetUrl;
    }

    @Override
    public boolean isStable() {
        return false;
    }

    /**
     * Add a asTeam field into the form (see: {@link #getForm()}).
     * 
     * @param me The user creating this userContent
     * @param right The type of action we are doing. For example if this
     *            userContent is a comment you should use
     *            {@link UserTeamRight#TALK} ; But if it is a contribution, you
     *            have to use the {@link UserTeamRight#BANK}.
     * @param label The asTeam field label.
     * @param comment The comment of the field.
     */
    protected void addAsTeamForm(final HtmlForm form, final Member me, final UserTeamRight right, final String label, final String comment) {
        // Offering on the behalf of
        if (me.canAccessTeams(Action.READ)) {
            try {
                final PageIterable<Team> teams = me.getTeams();
                final FieldData teamData = targetUrl.getTeamParameter().pickFieldData();
                final HtmlDropDown teamInput = new HtmlDropDown(teamData.getName(), label);
                teamInput.setDefaultValue(teamData.getSuggestedValue());
                teamInput.addErrorMessages(teamData.getErrorMessages());
                teamInput.setComment(comment);
                teamInput.addDropDownElement("", Context.tr("Myself"));
                int nbTeam = 0;
                for (final Team team : teams) {
                    if (team.getUserTeamRight(me).contains(right)) {
                        teamInput.addDropDownElement(team.getId().toString(), team.getLogin());
                        nbTeam++;
                    }
                }
                if (nbTeam > 0) {
                    form.add(teamInput);
                }
            } catch (final UnauthorizedOperationException e) {
                session.notifyError(Context.tr("An error prevented us from displaying you some information. Please notify us"));
                throw new ShallNotPassException("Can't access current user teams (I checked before tho)", e);
            }
        }
    }

    /**
     * Add a language selector into the {@link #getForm()} form.
     * 
     * @param label the label of the language selector.
     * @param comment the comment on the language selector field.
     */
    protected void addLanguageForm(final HtmlForm form, final String label, final String comment) {
        // locale
        final FieldData localeData = targetUrl.getLocaleParameter().pickFieldData();
        final LanguageSelector localeInput = new LanguageSelector(localeData.getName(), label);
        localeInput.setDefaultValue(localeData.getSuggestedValue(), Context.getLocalizator().getLanguageCode());
        localeInput.addErrorMessages(localeData.getErrorMessages());
        localeInput.setComment(comment);
        form.add(localeInput);
    }

    protected void addAddAttachmentForm(final HtmlForm form,
                                        final String attachmentLabel,
                                        final String attachmentComment,
                                        final String descriptionLabel,
                                        final String descriptionComment) {
        // Attachment
        final FieldData attachedFileData = targetUrl.getAttachmentParameter().pickFieldData();
        final HtmlFileInput attachedFileInput = new HtmlFileInput(attachedFileData.getName(), attachmentLabel);
        attachedFileInput.setDefaultValue(attachedFileData.getSuggestedValue());
        attachedFileInput.addErrorMessages(attachedFileData.getErrorMessages());
        attachedFileInput.setComment(attachmentComment);
        form.add(attachedFileInput);

        final FieldData attachmentDescriptiondData = targetUrl.getAttachmentDescriptionParameter().pickFieldData();
        final HtmlTextField attachmentDescriptionInput = new HtmlTextField(attachmentDescriptiondData.getName(), descriptionLabel);
        attachmentDescriptionInput.setDefaultValue(attachmentDescriptiondData.getSuggestedValue());
        attachmentDescriptionInput.addErrorMessages(attachmentDescriptiondData.getErrorMessages());
        attachmentDescriptionInput.setComment(descriptionComment);
        form.add(attachmentDescriptionInput);
    }

    protected final CreateUserContentActionUrl getTargetUrl() {
        return targetUrl;
    }
}
