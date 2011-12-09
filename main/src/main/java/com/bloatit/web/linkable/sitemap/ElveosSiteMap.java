package com.bloatit.web.linkable.sitemap;

import java.io.File;
import java.io.FilenameFilter;

import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.masters.SiteMap;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Bug;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.model.Release;
import com.bloatit.model.Software;
import com.bloatit.model.Team;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.model.managers.BugManager;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.managers.ReleaseManager;
import com.bloatit.model.managers.SoftwareManager;
import com.bloatit.model.managers.TeamManager;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.ChangeLanguagePageUrl;
import com.bloatit.web.url.DocumentationPageUrl;
import com.bloatit.web.url.DocumentationRootPageUrl;
import com.bloatit.web.url.FeatureListPageUrl;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.LostPasswordPageUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.MembersListPageUrl;
import com.bloatit.web.url.MetaFeedbackListPageUrl;
import com.bloatit.web.url.ReleasePageUrl;
import com.bloatit.web.url.SignUpPageUrl;
import com.bloatit.web.url.SoftwareListPageUrl;
import com.bloatit.web.url.SoftwarePageUrl;
import com.bloatit.web.url.TeamPageUrl;
import com.bloatit.web.url.TeamsPageUrl;

@ParamContainer("sitemap.xml")
public class ElveosSiteMap extends SiteMap {
    @SuppressWarnings("unused")
    private final Url privateUrl;

    public ElveosSiteMap(final Url url) {
        super();
        this.privateUrl = url;
        generate();
    }

    private void generate() {
        // Index page
        addSiteMapEntry(new SiteMapEntry(new IndexPageUrl(), ChangeFrequency.DAILY, null, 1.0f));

        // Master pages
        addSiteMapEntry(new SiteMapEntry(new FeatureListPageUrl(), ChangeFrequency.DAILY, null, 0.9f));
        addSiteMapEntry(new SiteMapEntry(new SoftwareListPageUrl(), ChangeFrequency.DAILY, null, 0.8f));
        addSiteMapEntry(new SiteMapEntry(new MembersListPageUrl(), ChangeFrequency.DAILY, null, 0.8f));
        addSiteMapEntry(new SiteMapEntry(new TeamsPageUrl(), ChangeFrequency.DAILY, null, 0.8f));
        addSiteMapEntry(new SiteMapEntry(new DocumentationRootPageUrl(), ChangeFrequency.DAILY, null, 0.8f));

        // Features
        final PageIterable<Feature> features = FeatureManager.getFeatures();
        for (final Feature feature : features) {
            addSiteMapEntry(new SiteMapEntry(new FeaturePageUrl(feature, FeatureTabKey.description), ChangeFrequency.DAILY, null, 0.7f));
            addSiteMapEntry(new SiteMapEntry(new FeaturePageUrl(feature, FeatureTabKey.contributions), ChangeFrequency.DAILY, null, 0.5f));
            addSiteMapEntry(new SiteMapEntry(new FeaturePageUrl(feature, FeatureTabKey.offers), ChangeFrequency.DAILY, null, 0.5f));
            addSiteMapEntry(new SiteMapEntry(new FeaturePageUrl(feature, FeatureTabKey.bugs), ChangeFrequency.DAILY, null, 0.5f));
        }

        // Software
        final PageIterable<Software> softwares = SoftwareManager.getAll();
        for (final Software software : softwares) {
            addSiteMapEntry(new SiteMapEntry(new SoftwarePageUrl(software), ChangeFrequency.DAILY, null, 0.6f));
        }

        // Members
        final PageIterable<Member> members = MemberManager.getAll();
        for (final Member member : members) {
            addSiteMapEntry(new SiteMapEntry(new MemberPageUrl(member), ChangeFrequency.DAILY, null, 0.5f));
        }

        // Teams
        final PageIterable<Team> teams = TeamManager.getAll();
        for (final Team team : teams) {
            addSiteMapEntry(new SiteMapEntry(new TeamPageUrl(team), ChangeFrequency.DAILY, null, 0.5f));
        }

        // Bugs
        final PageIterable<Bug> bugs = BugManager.getAll();
        for (final Bug bug : bugs) {
            addSiteMapEntry(new SiteMapEntry(new BugPageUrl(bug), ChangeFrequency.DAILY, null, 0.4f));
        }

        // Release
        final PageIterable<Release> releases = ReleaseManager.getAll();
        for (final Release release : releases) {
            addSiteMapEntry(new SiteMapEntry(new ReleasePageUrl(release), ChangeFrequency.DAILY, null, 0.4f));
        }

        // Tools
        addSiteMapEntry(new SiteMapEntry(new LoginPageUrl(), ChangeFrequency.WEEKLY, null, 0.4f));
        addSiteMapEntry(new SiteMapEntry(new SignUpPageUrl(), ChangeFrequency.WEEKLY, null, 0.4f));
        addSiteMapEntry(new SiteMapEntry(new LostPasswordPageUrl(), ChangeFrequency.WEEKLY, null, 0.4f));
        addSiteMapEntry(new SiteMapEntry(new ChangeLanguagePageUrl(), ChangeFrequency.WEEKLY, null, 0.4f));
        addSiteMapEntry(new SiteMapEntry(new MetaFeedbackListPageUrl(), ChangeFrequency.WEEKLY, null, 0.4f));

        // Documentation
        final File docDir = new File(FrameworkConfiguration.getDocumentationDir() + "/main");
        final String[] docFileList = docDir.list(new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
                if (name.endsWith("_en")) {
                    return true;
                }
                return false;
            }
        });

        for (final String docFile : docFileList) {
            final String docCode = docFile.substring(0, docFile.length() - 3);
            addSiteMapEntry(new SiteMapEntry(new DocumentationPageUrl(docCode), ChangeFrequency.WEEKLY, null, 0.6f));
        }

    }

}
