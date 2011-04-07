package com.bloatit.web.pages.tools;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.model.BankTransaction;
import com.bloatit.model.Bug;
import com.bloatit.model.Comment;
import com.bloatit.model.Contribution;
import com.bloatit.model.Description;
import com.bloatit.model.ExternalAccount;
import com.bloatit.model.Feature;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.HighlightFeature;
import com.bloatit.model.InternalAccount;
import com.bloatit.model.JoinTeamInvitation;
import com.bloatit.model.Kudos;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.ModelClassVisitor;
import com.bloatit.model.Offer;
import com.bloatit.model.Release;
import com.bloatit.model.Software;
import com.bloatit.model.Team;
import com.bloatit.model.Transaction;
import com.bloatit.model.Translation;
import com.bloatit.model.UserContentInterface;
import com.bloatit.web.linkable.bugs.BugPage;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.linkable.release.ReleasePage;
import com.bloatit.web.linkable.softwares.SoftwarePage;
import com.bloatit.web.linkable.team.TeamPage;
import com.bloatit.web.pages.master.Breadcrumb;

public class BreadcrumbTools {

    public static Breadcrumb generateBreadcrumb(UserContentInterface userContent) {

        return userContent.accept(new ModelClassVisitor<Breadcrumb>() {

            @Override
            public Breadcrumb visit(ExternalAccount model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(InternalAccount model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(Member model) {
                return MemberPage.generateBreadcrumb(model);
            }

            @Override
            public Breadcrumb visit(BankTransaction model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(Milestone model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(Description model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(Team model) {
                return TeamPage.generateBreadcrumb(model);
            }

            @Override
            public Breadcrumb visit(HighlightFeature model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(JoinTeamInvitation model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(Software model) {
                return SoftwarePage.generateBreadcrumb(model);
            }

            @Override
            public Breadcrumb visit(Transaction model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(Bug model) {
                return BugPage.generateBreadcrumb(model);
            }

            @Override
            public Breadcrumb visit(Contribution model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(FileMetadata model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(Kudos model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(Comment model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(Feature model) {
                return FeaturePage.generateBreadcrumb(model);
            }

            @Override
            public Breadcrumb visit(Offer model) {
                return FeaturePage.generateBreadcrumbOffers(model.getFeature());
            }

            @Override
            public Breadcrumb visit(Translation model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(Release model) {
                return ReleasePage.generateBreadcrumb(model);
            }});
    }

}
