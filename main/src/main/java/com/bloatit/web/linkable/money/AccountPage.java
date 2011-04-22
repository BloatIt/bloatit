/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.money;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.PageNotFoundException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.model.Actor;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.AccountComponent;
import com.bloatit.web.components.SideBarButton;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.linkable.team.TeamPage;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.HtmlDefineParagraph;
import com.bloatit.web.pages.master.sidebar.TitleSideBarElementLayout;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.AccountChargingProcessUrl;
import com.bloatit.web.url.AccountPageUrl;

/**
 * <p>
 * A page used to display logged member informations.
 * </p>
 */
@ParamContainer("account")
public final class AccountPage extends LoggedPage {

    @RequestParam(conversionErrorMsg = @tr("I cannot find the team number: ''%value%''."))
    @Optional
    private final Team team;

    private final AccountPageUrl url;

    public AccountPage(final AccountPageUrl url) {
        super(url);
        this.url = url;
        team = url.getTeam();
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Account informations");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to show your account informations.");
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws PageNotFoundException {
        try {
            Actor<?> currentActor = loggedUser;
            if (isTeamAccount()) {
                if (loggedUser.hasBankTeamRight(team)) {
                    currentActor = team;
                } else {
                    session.notifyBad(tr("You haven't the right to see ''{0}'' group account.", team.getLogin()));
                    throw new PageNotFoundException();
                }
            }

            final TwoColumnLayout layout = new TwoColumnLayout(true, url);

            if (isTeamAccount()) {
                layout.addLeft(new AccountComponent(team));
            } else {
                layout.addLeft(new AccountComponent(loggedUser));
            }

            layout.addRight(new SideBarDocumentationBlock("internal_account"));
            layout.addRight(new SideBarLoadAccountBlock(team));
            layout.addRight(new SideBarWithdrawMoneyBlock());

            return layout;
        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException("Right error.", e);
        }
    }

    private boolean isTeamAccount() {
        return team != null;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        if (isTeamAccount()) {
            return AccountPage.generateBreadcrumb(team);
        }
        return AccountPage.generateBreadcrumb(session.getAuthToken().getMember());

    }

    public static Breadcrumb generateBreadcrumb(final Team team) {
        final Breadcrumb breadcrumb = TeamPage.generateBreadcrumb(team);
        breadcrumb.pushLink(new AccountPageUrl().getHtmlLink(tr("Account informations")));
        return breadcrumb;
    }

    public static Breadcrumb generateBreadcrumb(final Member member) {
        final Breadcrumb breadcrumb = MemberPage.generateBreadcrumb(member);
        breadcrumb.pushLink(new AccountPageUrl().getHtmlLink(tr("Account informations")));
        return breadcrumb;
    }

    public static class SideBarLoadAccountBlock extends TitleSideBarElementLayout {

        SideBarLoadAccountBlock(final Team asTeam) {
            setTitle(tr("Load account"));

            add(new HtmlParagraph(tr("You can charge your account with a credit card using the following link: ")));
            // TODO good URL
            final AccountChargingProcessUrl chargingAccountUrl = new AccountChargingProcessUrl();
            chargingAccountUrl.setTeam(asTeam);
            add(new SideBarButton(tr("Charge your account"), chargingAccountUrl, WebConfiguration.getImgAccountCharge()).asElement());
            add(new HtmlDefineParagraph(tr("Note: "),
                                        tr("We have charge to pay every time you charge your account, hence we will perceive our 10% commission, even if you withdraw the money as soon as you have loaded it.")));
        }
    }

    public static class SideBarWithdrawMoneyBlock extends TitleSideBarElementLayout {

        SideBarWithdrawMoneyBlock() {
            setTitle(tr("Withdraw money"));

            add(new HtmlParagraph(tr("You can withdraw money from you elveos account and get a bank transfer to your personal bank account using the following link:")));
            // TODO good URL
            add(new SideBarButton(tr("Withdraw money"), new PageNotFoundUrl(), WebConfiguration.getImgAccountWithdraw()).asElement());
            add(new HtmlDefineParagraph(tr("Note: "),
                                        tr("Note : Do not withdraw money if you are planning to contribute to a project in the future, this will prevent you from paying our commission again later.\n"
                                                + "Oh, and by the way, we don't like when you withdraw money, not because it costs us money (it does but well that's OK), but because you could as well use this money to contribute to other open source projects.")));

        }
    }
}
