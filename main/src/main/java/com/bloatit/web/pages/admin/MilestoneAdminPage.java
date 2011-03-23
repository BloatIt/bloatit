package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.EnumSet;

import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoMilestone;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel.ColumnGenerator;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel.StringColumnGenerator;
import com.bloatit.framework.webserver.components.form.FieldData;
import com.bloatit.framework.webserver.components.form.HtmlDropDown;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.model.Milestone;
import com.bloatit.model.Release;
import com.bloatit.model.admin.MilestoneAdminListFactory;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.url.MilestoneAdminPageUrl;

@ParamContainer("admin/milestonees")
public final class MilestoneAdminPage extends IdentifiablesAdminPage<DaoMilestone, Milestone, MilestoneAdminListFactory> {

    @RequestParam(role = RequestParam.Role.POST)
    private final DisplayableMilestoneState milestoneState;

    private final MilestoneAdminPageUrl url;

    public MilestoneAdminPage(final MilestoneAdminPageUrl url) {
        super(url, new MilestoneAdminListFactory());
        this.url = url;
        milestoneState = url.getMilestoneState();

        if (milestoneState != null && milestoneState != DisplayableMilestoneState.NOT_SELECTED) {
            getFactory().stateEquals(DisplayableMilestoneState.getState(milestoneState));
            Context.getSession().addParameter(url.getMilestoneStateParameter());
        }
    }

    @Override
    protected String getPageTitle() {
        return tr("Administration Kudosable");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected void addActions(final HtmlDropDown dropDown, final HtmlBranch block) {
        // Add actions into the drop down
        dropDown.addDropDownElements(new AdminActionManager().milestoneActions());
    }

    @Override
    protected void addFormFilters(final HtmlForm form) {

        final FieldData stateData = url.getMilestoneStateParameter().pickFieldData();
        final HtmlDropDown stateInput = new HtmlDropDown(stateData.getName());
        stateInput.setDefaultValue(stateData.getSuggestedValue());
        stateInput.addErrorMessages(stateData.getErrorMessages());
        stateInput.addDropDownElements(EnumSet.allOf(DisplayableMilestoneState.class));
        stateInput.setLabel(tr("Filter by milestone state"));
        form.add(stateInput);
    }

    @Override
    protected void addColumns(final HtmlGenericTableModel<Milestone> tableModel) {
        final MilestoneAdminPageUrl clonedUrl = url.clone();
        clonedUrl.setOrderByStr("milestoneState");
        tableModel.addColumn(clonedUrl.getHtmlLink(tr("milestoneState")), new StringColumnGenerator<Milestone>() {
            @Override
            public String getStringBody(final Milestone element) {
                return String.valueOf(element.getMilestoneState());
            }
        });
        tableModel.addColumn(tr("description"), new StringColumnGenerator<Milestone>() {
            @Override
            public String getStringBody(final Milestone element) {
                return element.getDescription();
            }
        });
        tableModel.addColumn(tr("Release"), new ColumnGenerator<Milestone>() {
            @Override
            public XmlNode getBody(final Milestone element) {

                final PlaceHolderElement place = new PlaceHolderElement();
                for (final Release release : element.getReleases()) {
                    place.add(new HtmlParagraph(release.getVersion() + " "
                            + Context.getLocalizator().getDate(release.getCreationDate()).toString(FormatStyle.MEDIUM)));
                }
                return place;
            }
        });
        tableModel.addColumn(tr("Should validated"), new ColumnGenerator<Milestone>() {
            @Override
            public XmlNode getBody(final Milestone element) {
                final PlaceHolderElement place = new PlaceHolderElement();
                for (final Level level : EnumSet.allOf(Level.class)) {
                    if (element.partIsValidated(level)) {
                        place.add(new HtmlParagraph(level.toString() + " -> VALIDATED"));
                    } else {
                        if (element.shouldValidatePart(level)) {
                            place.add(new HtmlParagraph(level.toString() + " -> SHOULD"));
                        } else {
                            place.add(new HtmlParagraph(level.toString() + " -> SHOULDN'T"));
                        }
                    }
                }
                return place;
            }
        });
    }

    @Override
    public void processErrors() throws RedirectException {
        session.notifyList(url.getMessages());
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return MilestoneAdminPage.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new MilestoneAdminPageUrl().getHtmlLink(tr("Milestone administration")));

        return breadcrumb;
    }
}