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
package com.bloatit.web.linkable.features;

// import java.util.Random;
import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.data.search.FeatureSearch;
import com.bloatit.data.search.FeatureSearch.SortMethod;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlRenderer;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlForm.Method;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Feature;
import com.bloatit.model.feature.FeatureList;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.HtmlFeatureSummary;
import com.bloatit.web.components.HtmlFeatureSummary.Compacity;
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.components.SideBarButton;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CreateFeaturePageUrl;
import com.bloatit.web.url.FeatureListPageUrl;

@ParamContainer("feature/list")
public final class FeatureListPage extends MasterPage {

    public static final String FILTER_ALL = "all";
    public static final String FILTER_IN_PROGRESS = "in_progress";
    public static final String FILTER_FINISHED = "finished";
    public static final String FILTER_CODE = "filter";

    @RequestParam(name = FILTER_CODE)
    @Optional(FILTER_IN_PROGRESS)
    private final String filter;

    public static final String SORT_BY_RELEVANCE = "relevance";
    public static final String SORT_BY_CONTRIBUTION = "contribution";
    public static final String SORT_BY_PROGRESS = "progress";
    public static final String SORT_BY_POPULARITY = "popularity";
    public static final String SORT_BY_CREATION_DATE = "creation_date";
    public static final String SORT_BY_EXPIRATION_DATE = "expiration_date";
    public static final String SORT_CODE = "sort";
    @RequestParam(name = SORT_CODE)
    @Optional(SORT_BY_POPULARITY)
    private final String sort;

    public static final String SEARCH_STRING_CODE = "search_string";
    @RequestParam(name = SEARCH_STRING_CODE)
    @Optional("")
    private final String searchString;

    private HtmlPagedList<Feature> pagedFeatureList;
    private final FeatureListPageUrl url;

    public FeatureListPage(final FeatureListPageUrl url) {
        super(url);
        this.url = url;
        this.searchString = url.getSearchString();
        this.filter = url.getFilter();
        this.sort = url.getSort();
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        // Search block
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        // ////////////////////
        // Div feature_search_block
        final HtmlDiv featureSearchBlock = new HtmlDiv("feature_search_block");
        {
            final FeatureListPageUrl formUrl = url.clone();
            formUrl.setSearchString("");
            final HtmlForm searchForm = new HtmlForm(formUrl.urlString(), Method.GET);
            {
                final HtmlTextField searchField = new HtmlTextField(SEARCH_STRING_CODE);
                searchField.setDefaultValue(searchString);

                final HtmlSubmit searchButton = new HtmlSubmit(Context.trc("Search (verb)", "Search a feature"));

                searchForm.add(searchField);
                searchForm.add(searchButton);
            }
            featureSearchBlock.add(searchForm);

            final HtmlDiv featureFilter = new HtmlDiv("feature_filter");
            {
                final FeatureListPageUrl allFilterUrl = url.clone();
                allFilterUrl.setFilter(FILTER_ALL);
                final HtmlLink allFilter = allFilterUrl.getHtmlLink(Context.tr("all"));
                if (filter.equals(FILTER_ALL)) {
                    allFilter.setCssClass("selected");
                }

                final FeatureListPageUrl preparingFilterUrl = url.clone();
                preparingFilterUrl.setFilter(FILTER_IN_PROGRESS);
                final HtmlLink preparingFilter = preparingFilterUrl.getHtmlLink(Context.tr("in progress"));
                if (filter.equals(FILTER_IN_PROGRESS)) {
                    preparingFilter.setCssClass("selected");
                }

                final FeatureListPageUrl finishedFilterUrl = url.clone();
                finishedFilterUrl.setFilter(FILTER_FINISHED);
                final HtmlLink finishedFilter = finishedFilterUrl.getHtmlLink(Context.tr("finished"));
                if (filter.equals(FILTER_FINISHED)) {
                    finishedFilter.setCssClass("selected");
                }

                featureFilter.addText(Context.tr("Filter: "));
                featureFilter.add(allFilter);
                featureFilter.addText(" – ");
                featureFilter.add(preparingFilter);
                featureFilter.addText(" – ");
                featureFilter.add(finishedFilter);
            }
            featureSearchBlock.add(featureFilter);

            final HtmlDiv featureSort = new HtmlDiv("feature_sort");
            {
                // XXX : Do not delete the following comments

                // final FeatureListPageUrl relevanceSortUrl = url.clone();
                // relevanceSortUrl.setSort(SORT_BY_RELEVANCE);
                // final HtmlLink relevanceSort =
                // relevanceSortUrl.getHtmlLink(Context.tr("relevance"));
                // if (sort.equals(SORT_BY_RELEVANCE)) {
                // relevanceSort.setCssClass("selected");
                // }

                final FeatureListPageUrl popularitySortUrl = url.clone();
                popularitySortUrl.setSort(SORT_BY_POPULARITY);
                final HtmlLink popularitySort = popularitySortUrl.getHtmlLink(Context.tr("popularity"));
                if (sort.equals(SORT_BY_POPULARITY)) {
                    popularitySort.setCssClass("selected");
                }

                final FeatureListPageUrl contributionSortUrl = url.clone();
                contributionSortUrl.setSort(SORT_BY_CONTRIBUTION);
                final HtmlLink contributionSort = contributionSortUrl.getHtmlLink(Context.tr("contribution"));
                if (sort.equals(SORT_BY_CONTRIBUTION)) {
                    contributionSort.setCssClass("selected");
                }

                // XXX : Do not delete the following comments

                // final FeatureListPageUrl progressSortUrl = url.clone();
                // progressSortUrl.setSort(SORT_BY_PROGRESS);
                // final HtmlLink progressSort =
                // progressSortUrl.getHtmlLink(Context.tr("progress"));
                // if (sort.equals(SORT_BY_PROGRESS)) {
                // progressSort.setCssClass("selected");
                // }

                final FeatureListPageUrl creationDateSortUrl = url.clone();
                creationDateSortUrl.setSort(SORT_BY_CREATION_DATE);
                final HtmlLink creationDateSort = creationDateSortUrl.getHtmlLink(Context.tr("creation date"));
                if (sort.equals(SORT_BY_CREATION_DATE)) {
                    creationDateSort.setCssClass("selected");
                }

                // XXX : Do not delete the following comments

                // final FeatureListPageUrl expirationDateSortUrl = url.clone();
                // expirationDateSortUrl.setSort(SORT_BY_EXPIRATION_DATE);
                // final HtmlLink expirationDateSort =
                // expirationDateSortUrl.getHtmlLink(Context.tr("expiration date"));
                // if (sort.equals(SORT_BY_EXPIRATION_DATE)) {
                // expirationDateSort.setCssClass("selected");
                // }

                featureSort.addText(Context.tr("Sort by: "));
                featureSort.add(popularitySort);
                featureSort.addText(" – ");
                // featureSort.add(relevanceSort);
                // featureSort.addText(" – ");
                featureSort.add(contributionSort);
                featureSort.addText(" – ");
                // featureSort.add(progressSort);
                // featureSort.addText(" – ");
                featureSort.add(creationDateSort);
                // featureSort.addText(" – ");
                // featureSort.add(expirationDateSort);

            }
            featureSearchBlock.add(featureSort);

            // /////////////////////
            // // Div feature_advanced_search_button
            // final HtmlDiv featureAdvancedSearchButton = new
            // HtmlDiv("feature_advanced_search_button");
            // {
            // final HtmlLink showHideShareBlock = new
            // HtmlLink("javascript:showHide('feature_advanced_search')",
            // Context.tr("+ Advanced search"));
            // featureAdvancedSearchButton.add(showHideShareBlock);
            // }
            // featureSearchBlock.add(featureAdvancedSearchButton);
            //
            // // ////////////////////
            // // Div feature_advanced_search
            // final HtmlDiv featureAdvancedSearch = new
            // HtmlDiv("feature_advanced_search", "feature_advanced_search");
            // {
            //
            // }
            // featureSearchBlock.add(featureAdvancedSearch);

            // Create a feature
            final HtmlDiv createFeatureBlock = new HtmlDiv("feature_create_block");
            {
                createFeatureBlock.addText(Context.tr("If you have an feature or a need about a free software, you can "));
                final HtmlLink creatFeatureLink = new CreateFeaturePageUrl().getHtmlLink(Context.tr("submit a new feature"));
                createFeatureBlock.add(creatFeatureLink);
            }
            featureSearchBlock.add(createFeatureBlock);
        }
        layout.addLeft(featureSearchBlock);

        ///////////////
        // Feature list
        final FeatureList results = searchResult();
        if (results.size() > 0) {
            final HtmlRenderer<Feature> featureItemRenderer = new FeaturesListItem();
            final FeatureListPageUrl clonedUrl = url.clone();
            pagedFeatureList = new HtmlPagedList<Feature>(featureItemRenderer, results, clonedUrl, clonedUrl.getPagedFeatureListUrl());
            layout.addLeft(pagedFeatureList);
        } else {
            final HtmlDiv noResultBlock = new HtmlDiv("no_result_block");
            {
                noResultBlock.addText(Context.tr("No result"));
            }
            layout.addLeft(noResultBlock);
        }

        ////////////
        // Right bar
        layout.addRight(new SideBarButton(Context.tr("Request a feature"), new CreateFeaturePageUrl(), WebConfiguration.getImgIdea()));
        layout.addRight(new SideBarDocumentationBlock("feature"));

        return layout;
    }

    @Override
    public String createPageTitle() {
        return Context.tr("View features - search features");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    static class FeaturesListItem implements HtmlRenderer<Feature> {
        private Feature feature;

        @Override
        public XmlNode generate(final Feature feature) {
            this.feature = feature;
            return generateContent();
        }

        private XmlNode generateContent() {
            return new HtmlFeatureSummary(feature, Compacity.NORMAL);
        }
    };

    private FeatureList searchResult() {

        final FeatureSearch search = new FeatureSearch(searchString);
        if (!filter.equals(FILTER_ALL)) {
            if (filter.equals(FILTER_IN_PROGRESS)) {
                search.addFeatureStateFilter(FeatureState.FINISHED);
                search.addFeatureStateFilter(FeatureState.DISCARDED);
            } else if (filter.equals(FILTER_FINISHED)) {
                search.addFeatureStateFilter(FeatureState.DEVELOPPING);
                search.addFeatureStateFilter(FeatureState.PENDING);
                search.addFeatureStateFilter(FeatureState.PREPARING);
            }
        }

        if (sort.equals(SORT_BY_RELEVANCE)) {
            search.setSortMethod(SortMethod.SORT_BY_RELEVANCE);
        } else if (sort.equals(SORT_BY_CONTRIBUTION)) {
            search.setSortMethod(SortMethod.SORT_BY_CONTRIBUTION);
        } else if (sort.equals(SORT_BY_PROGRESS)) {
            search.setSortMethod(SortMethod.SORT_BY_PROGRESS);
        } else if (sort.equals(SORT_BY_POPULARITY)) {
            search.setSortMethod(SortMethod.SORT_BY_POPULARITY);
        } else if (sort.equals(SORT_BY_CREATION_DATE)) {
            search.setSortMethod(SortMethod.SORT_BY_CREATION_DATE);
        } else if (sort.equals(SORT_BY_EXPIRATION_DATE)) {
            search.setSortMethod(SortMethod.SORT_BY_EXPIRATION_DATE);
        }

        return new FeatureList(search.doSearch());
    }

    public static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();
        final FeatureListPageUrl featureListPageUrl = new FeatureListPageUrl();
        breadcrumb.pushLink(featureListPageUrl.getHtmlLink(tr("Features")));
        return breadcrumb;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return FeatureListPage.generateBreadcrumb();
    }
}
