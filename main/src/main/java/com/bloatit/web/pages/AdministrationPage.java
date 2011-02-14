package com.bloatit.web.pages;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.Iterator;

import com.bloatit.data.DaoUserContent;
import com.bloatit.data.queries.DaoAbstractListFactory.OrderType;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.components.HtmlGenericElement;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.advanced.HtmlTable;
import com.bloatit.framework.webserver.components.advanced.HtmlTable.HtmlTableModel;
import com.bloatit.framework.webserver.components.form.HtmlCheckbox;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlFormBlock;
import com.bloatit.framework.webserver.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webserver.components.form.HtmlRadioButton;
import com.bloatit.framework.webserver.components.form.HtmlRadioButtonGroup;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.url.PageNotFoundUrl;
import com.bloatit.model.Group;
import com.bloatit.model.Member;
import com.bloatit.model.UserContentAdministration;
import com.bloatit.model.UserContentAdministrationListFactory;
import com.bloatit.web.url.AdministrationPageUrl;

@ParamContainer("administration")
public class AdministrationPage extends LoggedPage {

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean orderByMemberAsc;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean orderByAsGroupAsc;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean orderByMemberDesc;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean orderByAsGroupDesc;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean deletedOnly;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean nonDeletedOnly;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean withoutFile;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean withFile;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean withAnyGroup;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean withNoGroup;

    @RequestParam(level = Level.INFO, role = RequestParam.Role.POST)
    @ParamConstraint(optional = true)
    private Member fromMember;
    @RequestParam(level = Level.INFO, role = RequestParam.Role.POST)
    @ParamConstraint(optional = true)
    private Group fromGroup;

    public AdministrationPage(AdministrationPageUrl url) {
        super(url);
        orderByMemberAsc = url.getOrderByMemberAsc();
        orderByMemberDesc = url.getOrderByMemberDesc();
        orderByAsGroupAsc = url.getOrderByAsGroupAsc();
        orderByAsGroupDesc = url.getOrderByAsGroupDesc();
        deletedOnly = url.getDeletedOnly();
        nonDeletedOnly = url.getNonDeletedOnly();
        withoutFile = url.getWithoutFile();
        withFile = url.getWithFile();
        withAnyGroup = url.getWithAnyGroup();
        withNoGroup = url.getWithNoGroup();
        fromMember = url.getFromMember();
        fromGroup = url.getFromGroup();
    }

    private void addCell(HtmlBranch node, Object obj) {
        if (obj != null) {
            node.add(new HtmlGenericElement("td").addText(obj.toString()));
        } else {
            node.add(new HtmlGenericElement("td").addText("null"));
        }
    }

    @Override
    protected String getPageTitle() {
        return "test";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    public HtmlElement createRestrictedContent() throws RedirectException {

        PlaceHolderElement everything = new PlaceHolderElement();
        if (session.getAuthToken().getMember().getRole() != com.bloatit.data.DaoMember.Role.ADMIN) {
            session.notifyError(tr("You have to be the administrator to access this page."));
            throw new RedirectException(new PageNotFoundUrl());
        }
        AdministrationPageUrl url = new AdministrationPageUrl();
        HtmlForm form = new HtmlForm(url.urlString());
        everything.add(form);

        HtmlRadioButtonGroup groupOrder = new HtmlRadioButtonGroup("order");
        form.add(new HtmlFormBlock("order by").add(groupOrder));
        groupOrder.add(new HtmlRadioButton(url.getOrderByMemberAscParameter().createFormFieldData(), tr("member asc"), LabelPosition.BEFORE));
        groupOrder.add(new HtmlRadioButton(url.getOrderByMemberDescParameter().createFormFieldData(), tr("member desc"), LabelPosition.BEFORE));
        groupOrder.add(new HtmlRadioButton(url.getOrderByAsGroupAscParameter().createFormFieldData(), tr("group asc"), LabelPosition.BEFORE));
        groupOrder.add(new HtmlRadioButton(url.getOrderByAsGroupDescParameter().createFormFieldData(), tr("group desc"), LabelPosition.BEFORE));

        HtmlRadioButtonGroup groupDeleted = new HtmlRadioButtonGroup("deleted");
        form.add(new HtmlFormBlock("Deleted ?").add(groupDeleted));
        groupDeleted.add(new HtmlRadioButton(url.getDeletedOnlyParameter().createFormFieldData(), tr("show only deleted content"),
                LabelPosition.BEFORE));
        groupDeleted.add(new HtmlRadioButton(url.getNonDeletedOnlyParameter().createFormFieldData(), tr("show non deleted content"),
                LabelPosition.BEFORE));

        form.add(new HtmlCheckbox(url.getWithoutFileParameter().createFormFieldData(), tr("show content without file associated"),
                LabelPosition.BEFORE));
        form.add(new HtmlCheckbox(url.getWithFileParameter().createFormFieldData(), tr("show content with file associated"), LabelPosition.BEFORE));
        form.add(new HtmlCheckbox(url.getWithAnyGroupParameter().createFormFieldData(), tr("show content created in the name of a group"),
                LabelPosition.BEFORE));
        form.add(new HtmlCheckbox(url.getWithNoGroupParameter().createFormFieldData(), tr("show content created in the name of a member"),
                LabelPosition.BEFORE));

        UserContentAdministrationListFactory<DaoUserContent> factory = new UserContentAdministrationListFactory<DaoUserContent>();
        if (orderByAsGroupAsc) {
            factory.orderByAsGroup(OrderType.ASC);
        }
        if (orderByAsGroupDesc) {
            factory.orderByAsGroup(OrderType.DESC);
        }
        if (orderByMemberAsc) {
            factory.orderByMember(OrderType.ASC);
            session.notifyGood("member asc");
        }
        if (orderByMemberDesc) {
            factory.orderByMember(OrderType.DESC);
        }
        if (deletedOnly) {
            factory.deletedOnly();
            session.notifyGood("deleted only");
        }
        if (nonDeletedOnly) {
            factory.nonDeletedOnly();
            session.notifyGood("non deleted only");
        }
        if (withoutFile) {
            factory.withoutFile();
            session.notifyGood("without file");
        }
        if (withFile) {
            factory.withFile();
        }
        if (withAnyGroup) {
            factory.withAnyGroup();
            session.notifyGood("with group");
        }
        if (withNoGroup) {
            factory.withNoGroup();
            session.notifyGood("with no group");
        }

        form.add(new HtmlSubmit("Filter"));
        PageIterable<UserContentAdministration<DaoUserContent>> contents = factory.ListUserContents();

        HtmlTable htmlTable = new HtmlTable(new TableModel(contents));
        everything.add(htmlTable);

        return everything;
    }

    @Override
    public String getRefusalReason() {
        return tr("You have to be the administrator to access this page.");
    }

    private static class TableModel extends HtmlTableModel {

        private Iterator<UserContentAdministration<DaoUserContent>> iterator;
        private UserContentAdministration<DaoUserContent> next;

        public TableModel(PageIterable<UserContentAdministration<DaoUserContent>> contents) {
            iterator = contents.iterator();
        }

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public String getHeader(int column) {
            switch (column) {
            case 0:
                return tr("Creation date");
            case 1:
                return tr("Type");
            case 2:
                return tr("Deleted");
            case 3:
                return tr("File number");
            case 4:
                return tr("Author");
            case 5:
                return tr("asGroup");
            default:
                return "Boulet ! ";
            }
        }

        @Override
        public String getBody(int column) {
            switch (column) {
            case 0:
                return next.getCreationDate().toString();
            case 1:
                return next.getType();
            case 2:
                return next.isDeleted().toString();
            case 3:
                return String.valueOf(next.getFilesNumber());
            case 4:
                return next.getAuthor();
            case 5:
                return next.getAsGroup();
            default:
                return "Boulet ! ";
            }
        }

        @Override
        public boolean next() {
            if (iterator.hasNext()) {
                next = iterator.next();
                return true;
            }
            return false;
        }

    }
}
