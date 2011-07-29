package com.bloatit.web.linkable.admin.withdraw;

import com.bloatit.data.DaoMoneyWithdrawal.State;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.advanced.HtmlSimpleLineTable;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.MoneyWithdrawal;
import com.bloatit.model.lists.MoneyWithdrawalList;
import com.bloatit.model.managers.MoneyWithdrawalManager;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.admin.master.AdminPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.url.AdminHomePageUrl;
import com.bloatit.web.url.MoneyWithdrawalAdminActionUrl;
import com.bloatit.web.url.MoneyWithdrawalAdminPageUrl;

@ParamContainer("admin/withdraw")
public class MoneyWithdrawalAdminPage extends AdminPage {
    @SuppressWarnings("unused")
    private final MoneyWithdrawalAdminPageUrl url;

    private static final String FILTER_TREATED = "treated";
    private static final String FILTER_REQUESTED = "requested";
    private static final String FILTER_COMPLETE = "complete";
    private static final String FILTER_CANCELED = "canceled";
    private static final String FILTER_REFUSED = "refused";
    private static final String FILTER_ALL = "all";

    @RequestParam(name = "filter")
    @Optional(FILTER_REQUESTED)
    private final String filter;

    public MoneyWithdrawalAdminPage(final MoneyWithdrawalAdminPageUrl url) {
        super(url);
        this.url = url;
        this.filter = url.getFilter();
    }

    @Override
    protected HtmlElement createAdminContent() throws UnauthorizedOperationException {
        final HtmlDiv master = new HtmlDiv("padding_box");
        master.add(generateMain());
        return master;
    }

    private HtmlElement generateMain() throws UnauthorizedOperationException {
        final HtmlDiv master = new HtmlDiv();
        final HtmlTitle title = new HtmlTitle(1);
        title.addText(Context.tr("Money withdrawal administration"));
        master.add(title);

        final HtmlTitle filterTitle = new HtmlTitle(2);
        filterTitle.addText(Context.tr("Filter withdrawal requests"));
        master.add(filterTitle);

        final MoneyWithdrawalAdminPageUrl allUrl = new MoneyWithdrawalAdminPageUrl();
        allUrl.setFilter(FILTER_ALL);
        final HtmlLink allLink = allUrl.getHtmlLink(Context.tr("All"));
        if (filter.equals(FILTER_ALL)) {
            allLink.setCssClass("selected");
        }

        final MoneyWithdrawalAdminPageUrl requestedUrl = new MoneyWithdrawalAdminPageUrl();
        requestedUrl.setFilter(FILTER_REQUESTED);
        final HtmlLink requestedLink = requestedUrl.getHtmlLink(Context.tr("Requested"));
        if (filter.equals(FILTER_REQUESTED)) {
            requestedLink.setCssClass("selected");
        }

        final MoneyWithdrawalAdminPageUrl treatedUrl = new MoneyWithdrawalAdminPageUrl();
        treatedUrl.setFilter(FILTER_TREATED);
        final HtmlLink treatedLink = treatedUrl.getHtmlLink(Context.tr("Treated"));
        if (filter.equals(FILTER_TREATED)) {
            treatedLink.setCssClass("selected");
        }

        final MoneyWithdrawalAdminPageUrl completeUrl = new MoneyWithdrawalAdminPageUrl();
        completeUrl.setFilter(FILTER_COMPLETE);
        final HtmlLink completeLink = completeUrl.getHtmlLink(Context.tr("Complete"));
        if (filter.equals(FILTER_COMPLETE)) {
            completeLink.setCssClass("selected");
        }

        final MoneyWithdrawalAdminPageUrl canceledUrl = new MoneyWithdrawalAdminPageUrl();
        canceledUrl.setFilter(FILTER_CANCELED);
        final HtmlLink canceledLink = canceledUrl.getHtmlLink(Context.tr("Canceled"));
        if (filter.equals(FILTER_CANCELED)) {
            canceledLink.setCssClass("selected");
        }

        final MoneyWithdrawalAdminPageUrl refusedUrl = new MoneyWithdrawalAdminPageUrl();
        refusedUrl.setFilter(FILTER_REFUSED);
        final HtmlLink refusedLink = refusedUrl.getHtmlLink(Context.tr("Refused"));
        if (filter.equals(FILTER_REFUSED)) {
            refusedLink.setCssClass("selected");
        }

        final HtmlMixedText mixed = new HtmlMixedText(Context.tr("Filter: <0::>, <1::>, <2::>, <3::>, <4::>, <5::>"),
                                                      allLink,
                                                      requestedLink,
                                                      treatedLink,
                                                      completeLink,
                                                      canceledLink,
                                                      refusedLink);
        master.add(mixed);

        final State state = getState(filter);
        MoneyWithdrawalList list;
        if (state == null) {
            list = MoneyWithdrawalManager.getAll();
        } else {
            list = MoneyWithdrawalManager.getByState(state);
        }

        final HtmlSimpleLineTable table = new HtmlSimpleLineTable();
        master.add(table);
        table.addHeader("Author", "Amount", "State", "IBAN", "Action", "Last Modified");
        for (final MoneyWithdrawal mw : list) {
            final MoneyWithdrawalAdminActionUrl targetUrl = new MoneyWithdrawalAdminActionUrl(getSession().getShortKey(), filter, mw);
            HtmlForm form = null;

            final FieldData stateFieldData = targetUrl.getNewStateParameter().pickFieldData();
            final HtmlDropDown dd = new HtmlDropDown(stateFieldData.getName());

            switch (mw.getState()) {
                case CANCELED:
                case COMPLETE:
                case REFUSED:
                    break;
                case REQUESTED:
                    form = new HtmlForm(targetUrl.urlString());
                    dd.addDropDownElement(State.TREATED.toString(), "TREATED");
                    dd.addDropDownElement(State.REFUSED.toString(), "REFUSED");
                    form.add(dd);
                    form.add(new HtmlSubmit(Context.tr("Submit")));
                    break;
                case TREATED:
                    form = new HtmlForm(targetUrl.urlString());
                    dd.addDropDownElement(State.COMPLETE.toString(), "COMPLETE");
                    dd.addDropDownElement(State.REFUSED.toString(), "REFUSED");
                    form.add(dd);
                    form.add(new HtmlSubmit(Context.tr("Submit")));
                    break;
            }
            final String timeString = Context.getLocalizator()
                                             .getDate(mw.getLastModificationDate())
                                             .toDateTimeString(FormatStyle.SHORT, FormatStyle.SHORT);

            table.addLine(mw.getActor().getDisplayName(), mw.getAmountWithdrawn(), mw.getState(), mw.getIBAN(), form, timeString);
        }

        return master;
    }

    private State getState(final String filter) {
        if (filter.equals(FILTER_REQUESTED)) {
            return State.REQUESTED;
        } else if (filter.equals(FILTER_TREATED)) {
            return State.TREATED;
        } else if (filter.equals(FILTER_COMPLETE)) {
            return State.COMPLETE;
        } else if (filter.equals(FILTER_CANCELED)) {
            return State.CANCELED;
        } else if (filter.equals(FILTER_REFUSED)) {
            return State.REFUSED;
        }
        return null;
    }

    @Override
    protected String createPageTitle() {
        return "Money withdrawal administration";
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        final Breadcrumb crumb = new Breadcrumb();
        crumb.pushLink(new AdminHomePageUrl().getHtmlLink("admin"));
        crumb.pushLink(new MoneyWithdrawalAdminPageUrl().getHtmlLink("withdraw"));
        return crumb;
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
