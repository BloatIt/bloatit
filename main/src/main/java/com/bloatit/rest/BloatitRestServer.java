/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bloatit.framework.restprocessor.RestServer;
import com.bloatit.rest.list.RestBankTransactionList;
import com.bloatit.rest.list.RestBugList;
import com.bloatit.rest.list.RestCommentList;
import com.bloatit.rest.list.RestContributionList;
import com.bloatit.rest.list.RestDescriptionList;
import com.bloatit.rest.list.RestExternalAccountList;
import com.bloatit.rest.list.RestFeatureList;
import com.bloatit.rest.list.RestFeatureListExpanded;
import com.bloatit.rest.list.RestFileMetadataList;
import com.bloatit.rest.list.RestHighlightFeatureList;
import com.bloatit.rest.list.RestInternalAccountList;
import com.bloatit.rest.list.RestJoinTeamInvitationList;
import com.bloatit.rest.list.RestKudosList;
import com.bloatit.rest.list.RestMemberList;
import com.bloatit.rest.list.RestMilestoneList;
import com.bloatit.rest.list.RestOfferList;
import com.bloatit.rest.list.RestReleaseList;
import com.bloatit.rest.list.RestSoftwareList;
import com.bloatit.rest.list.RestTeamList;
import com.bloatit.rest.list.RestTransactionList;
import com.bloatit.rest.list.RestTranslationList;
import com.bloatit.rest.resources.RestBankTransaction;
import com.bloatit.rest.resources.RestBankTransactionSum;
import com.bloatit.rest.resources.RestBug;
import com.bloatit.rest.resources.RestComment;
import com.bloatit.rest.resources.RestContribution;
import com.bloatit.rest.resources.RestDescription;
import com.bloatit.rest.resources.RestExternalAccount;
import com.bloatit.rest.resources.RestFeature;
import com.bloatit.rest.resources.RestFileMetadata;
import com.bloatit.rest.resources.RestHighlightFeature;
import com.bloatit.rest.resources.RestInternalAccount;
import com.bloatit.rest.resources.RestJoinTeamInvitation;
import com.bloatit.rest.resources.RestKudos;
import com.bloatit.rest.resources.RestMember;
import com.bloatit.rest.resources.RestMilestone;
import com.bloatit.rest.resources.RestOffer;
import com.bloatit.rest.resources.RestRelease;
import com.bloatit.rest.resources.RestSoftware;
import com.bloatit.rest.resources.RestTeam;
import com.bloatit.rest.resources.RestTransaction;
import com.bloatit.rest.resources.RestTranslation;

public class BloatitRestServer extends RestServer {

    /**
     * A map containing for a given entity name, the class mapping the Rest to
     * the entity
     */
    private final Map<String, Class<?>> locations = new HashMap<String, Class<?>>() {
        private static final long serialVersionUID = -5012179845511358309L;
        {
            put("members", RestMember.class);
            put("banktransactions", RestBankTransaction.class);
            put("milestones", RestMilestone.class);
            put("bugs", RestBug.class);
            put("comments", RestComment.class);
            put("contributions", RestContribution.class);
            put("features", RestFeature.class);
            put("descriptions", RestDescription.class);
            put("externalaccounts", RestExternalAccount.class);
            put("filemetadatas", RestFileMetadata.class);
            put("teams", RestTeam.class);
            put("highlightfeatures", RestHighlightFeature.class);
            put("internalaccounts", RestInternalAccount.class);
            put("jointeaminvitations", RestJoinTeamInvitation.class);
            put("kudos", RestKudos.class);
            put("offers", RestOffer.class);
            put("softwares", RestSoftware.class);
            put("releases", RestRelease.class);
            put("transactions", RestTransaction.class);
            put("translations", RestTranslation.class);
        }
    };

    /**
     * All the classes that have to be marshalled by JAX
     */
    private final Class<?>[] classes = new Class<?>[] {
                                                       // ENTITIES
                                                       RestMember.class,
                                                       RestBankTransaction.class,
                                                       RestBankTransactionSum.class,
                                                       RestMilestone.class,
                                                       RestBug.class,
                                                       RestComment.class,
                                                       RestContribution.class,
                                                       RestFeature.class,
                                                       RestDescription.class,
                                                       RestExternalAccount.class,
                                                       RestFileMetadata.class,
                                                       RestTeam.class,
                                                       RestHighlightFeature.class,
                                                       RestInternalAccount.class,
                                                       RestJoinTeamInvitation.class,
                                                       RestKudos.class,
                                                       RestOffer.class,
                                                       RestSoftware.class,
                                                       RestRelease.class,
                                                       RestTransaction.class,
                                                       RestTranslation.class,

                                                       // LISTS
                                                       RestMemberList.class,
                                                       RestMemberList.class,
                                                       RestBankTransactionList.class,
                                                       RestMilestoneList.class,
                                                       RestBugList.class,
                                                       RestCommentList.class,
                                                       RestContributionList.class,
                                                       RestFeatureList.class,
                                                       RestDescriptionList.class,
                                                       RestExternalAccountList.class,
                                                       RestFileMetadataList.class,
                                                       RestTeamList.class,
                                                       RestHighlightFeatureList.class,
                                                       RestInternalAccountList.class,
                                                       RestJoinTeamInvitationList.class,
                                                       RestKudosList.class,
                                                       RestOfferList.class,
                                                       RestSoftwareList.class,
                                                       RestReleaseList.class,
                                                       RestTransactionList.class,
                                                       RestTranslationList.class,

                                                       // EXPANDED LISTS
                                                       RestFeatureListExpanded.class, };

    @Override
    protected Set<String> getResourcesDirectories() {
        final HashSet<String> directories = new HashSet<String>();
        directories.add("rest");
        return directories;
    }

    @Override
    protected Class<?> getClass(final String forResource) {
        return locations.get(forResource);
    }

    @Override
    protected boolean isValidResource(final String forResource) {
        return locations.containsKey(forResource);
    }

    @Override
    protected Class<?>[] getJAXClasses() {
        return classes;
    }

    @Override
    public boolean initialize() {
        return true;
    }
}
