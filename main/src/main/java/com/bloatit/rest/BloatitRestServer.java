package com.bloatit.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bloatit.framework.rest.RestServer;
import com.bloatit.rest.list.RestMemberList;
import com.bloatit.rest.resources.RestBankTransaction;
import com.bloatit.rest.resources.RestBatch;
import com.bloatit.rest.resources.RestBug;
import com.bloatit.rest.resources.RestComment;
import com.bloatit.rest.resources.RestContribution;
import com.bloatit.rest.resources.RestDemand;
import com.bloatit.rest.resources.RestDescription;
import com.bloatit.rest.resources.RestExternalAccount;
import com.bloatit.rest.resources.RestFileMetadata;
import com.bloatit.rest.resources.RestGroup;
import com.bloatit.rest.resources.RestHighlightDemand;
import com.bloatit.rest.resources.RestInternalAccount;
import com.bloatit.rest.resources.RestJoinGroupInvitation;
import com.bloatit.rest.resources.RestKudos;
import com.bloatit.rest.resources.RestMember;
import com.bloatit.rest.resources.RestOffer;
import com.bloatit.rest.resources.RestProject;
import com.bloatit.rest.resources.RestRelease;
import com.bloatit.rest.resources.RestTransaction;
import com.bloatit.rest.resources.RestTranslation;

public class BloatitRestServer extends RestServer {
    private final Map<String, Class<?>> locations = new HashMap<String, Class<?>>() {
        private static final long serialVersionUID = -5012179845511358309L;
        {
            put("members", RestMember.class);
            put("banktransactions", RestBankTransaction.class);
            put("batchs", RestBatch.class);
            put("bugs", RestBug.class);
            put("comments", RestComment.class);
            put("contributions", RestContribution.class);
            put("demands", RestDemand.class);
            put("descriptions", RestDescription.class);
            put("externalaccounts", RestExternalAccount.class);
            put("filemetadatas", RestFileMetadata.class);
            put("groups", RestGroup.class);
            put("highlightdemands", RestHighlightDemand.class);
            put("internalaccounts", RestInternalAccount.class);
            put("joingroupinvitations", RestJoinGroupInvitation.class);
            put("kudoss", RestKudos.class);
            put("offers", RestOffer.class);
            put("projects", RestProject.class);
            put("releases", RestRelease.class);
            put("transactions", RestTransaction.class);
            put("translations", RestTranslation.class);
        }
    };

    private final Class<?>[] classes = new Class<?>[] { RestMember.class,
                                                       RestBankTransaction.class,
                                                       RestBatch.class,
                                                       RestBug.class,
                                                       RestComment.class,
                                                       RestContribution.class,
                                                       RestDemand.class,
                                                       RestDescription.class,
                                                       RestExternalAccount.class,
                                                       RestFileMetadata.class,
                                                       RestGroup.class,
                                                       RestHighlightDemand.class,
                                                       RestInternalAccount.class,
                                                       RestJoinGroupInvitation.class,
                                                       RestKudos.class,
                                                       RestOffer.class,
                                                       RestProject.class,
                                                       RestRelease.class,
                                                       RestTransaction.class,
                                                       RestTranslation.class,
                                                       RestMemberList.class, };

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
