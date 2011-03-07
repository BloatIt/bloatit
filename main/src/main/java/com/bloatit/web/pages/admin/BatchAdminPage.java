package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.EnumSet;

import com.bloatit.data.DaoBatch;
import com.bloatit.data.DaoBug.Level;
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
import com.bloatit.model.Batch;
import com.bloatit.model.Release;
import com.bloatit.model.admin.BatchAdminListFactory;
import com.bloatit.web.url.BatchAdminPageUrl;

@ParamContainer("admin/batches")
public final class BatchAdminPage extends IdentifiablesAdminPage<DaoBatch, Batch, BatchAdminListFactory> {

    @RequestParam(role = RequestParam.Role.POST)
    private DisplayableBatchState batchState;

    private final BatchAdminPageUrl url;

    public BatchAdminPage(final BatchAdminPageUrl url) {
        super(url, new BatchAdminListFactory());
        this.url = url;
        batchState = url.getBatchState();

        if (batchState != null && batchState != DisplayableBatchState.NOT_SELECTED) {
            getFactory().stateEquals(DisplayableBatchState.getState(batchState));
            Context.getSession().addParameter(url.getBatchStateParameter());
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
        dropDown.addDropDownElements(new AdminActionManager().batchActions());
    }

    @Override
    protected void addFormFilters(final HtmlForm form) {

        final FieldData stateData = url.getBatchStateParameter().fieldData();
        final HtmlDropDown stateInput = new HtmlDropDown(stateData.getName());
        stateInput.setDefaultValue(stateData.getSuggestedValue());
        stateInput.addErrorMessages(stateData.getErrorMessages());
        stateInput.addDropDownElements(EnumSet.allOf(DisplayableBatchState.class));
        stateInput.setLabel(tr("Filter by batch state"));
        form.add(stateInput);
    }

    @Override
    protected void addColumns(final HtmlGenericTableModel<Batch> tableModel) {
        final BatchAdminPageUrl clonedUrl = url.clone();
        clonedUrl.setOrderByStr("batchState");
        tableModel.addColumn(clonedUrl.getHtmlLink(tr("batchState")), new StringColumnGenerator<Batch>() {
            @Override
            public String getStringBody(final Batch element) {
                return String.valueOf(element.getBatchState());
            }
        });
        tableModel.addColumn(tr("description"), new StringColumnGenerator<Batch>() {
            @Override
            public String getStringBody(final Batch element) {
                return element.getDescription();
            }
        });
        tableModel.addColumn(tr("Release"), new ColumnGenerator<Batch>() {
            @Override
            public XmlNode getBody(final Batch element) {

                final PlaceHolderElement place = new PlaceHolderElement();
                for (final Release release : element.getReleases()) {
                    place.add(new HtmlParagraph(release.getVersion() + " "
                            + Context.getLocalizator().getDate(release.getCreationDate()).toString(FormatStyle.MEDIUM)));
                }
                return place;
            }
        });
        tableModel.addColumn(tr("Should validated"), new ColumnGenerator<Batch>() {
            @Override
            public XmlNode getBody(final Batch element) {
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
}
