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

package com.bloatit.web.components;

import java.io.IOException;

import com.bloatit.common.Log;
import com.bloatit.common.TemplateFile;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.utils.RandomString;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.advanced.HtmlScript;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Actor;
import com.bloatit.model.Feature;
import com.bloatit.model.FollowActor;
import com.bloatit.model.FollowFeature;
import com.bloatit.model.FollowSoftware;
import com.bloatit.model.Software;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.web.url.FollowActorActionUrl;
import com.bloatit.web.url.FollowAllActionUrl;
import com.bloatit.web.url.FollowFeatureActionUrl;
import com.bloatit.web.url.FollowSoftwareActionUrl;

public abstract class HtmlFollowButton extends HtmlDiv {

    public HtmlFollowButton() {
        super("follow-button");
    }

    void generate() {

        if (isFollowing()) {
            HtmlLink followWithoutMail = getUnfollowUrl().getHtmlLink();
            followWithoutMail.setCssClass("follow-selected-without-mail");
            followWithoutMail.addText("Following");
            add(followWithoutMail);

            if (isFollowingWithMail()) {
                HtmlLink followWithMail = getFollowUrl().getHtmlLink();
                followWithMail.setCssClass("follow-selected-with-mail");
                add(followWithMail);
            } else {
                HtmlLink followWithMail = getFollowWithMailUrl().getHtmlLink();
                followWithMail.setCssClass("follow-unselected-with-mail");
                add(followWithMail);
            }

        } else {
            HtmlLink followWithoutMail = getFollowUrl().getHtmlLink();
            followWithoutMail.setCssClass("follow-unselected-without-mail");
            followWithoutMail.addText("Follow");
            HtmlLink followWithMail = getFollowWithMailUrl().getHtmlLink();
            followWithMail.setCssClass("follow-unselected-with-mail");
            add(followWithoutMail);
            add(followWithMail);
        }

        generateScript();

    }

    protected abstract Url getFollowUrl();

    protected abstract Url getFollowWithMailUrl();

    protected abstract Url getUnfollowUrl();

    protected abstract boolean isFollowing();

    protected abstract boolean isFollowingWithMail();

    protected abstract void generateScript();

    public static class HtmlFollowFeatureButton extends HtmlFollowButton {

        final Feature feature;

        public HtmlFollowFeatureButton(Feature feature) {
            this.feature = feature;
            generate();
        }

        @Override
        protected boolean isFollowing() {
            if (!AuthToken.isAuthenticated()) {
                return false;
            }

            return AuthToken.getMember().isFollowing(feature);
        }

        @Override
        protected boolean isFollowingWithMail() {
            FollowFeature followOrGetFeature = AuthToken.getMember().followOrGetFeature(feature);
            return followOrGetFeature.isMail();
        }

        @Override
        protected Url getFollowUrl() {
            return new FollowFeatureActionUrl(Context.getSession().getShortKey(), feature, true, true, true, false);
        }

        @Override
        protected Url getFollowWithMailUrl() {
            return new FollowFeatureActionUrl(Context.getSession().getShortKey(), feature, true, true, true, true);
        }

        @Override
        protected Url getUnfollowUrl() {
            return new FollowFeatureActionUrl(Context.getSession().getShortKey(), feature, false, false, false, false);
        }

        @Override
        protected void generateScript() {

            if (!AuthToken.isAuthenticated()) {
                return;
            }

            final HtmlScript script = new HtmlScript();
            final RandomString rng = new RandomString(10);
            if (this.getId() == null) {
                this.setId(rng.nextString());
            }

            final TemplateFile quotationUpdateScriptTemplate = new TemplateFile("feature_follow_button.js");
            quotationUpdateScriptTemplate.addNamedParameter("hostname", Context.getHeader().getHttpHost());
            quotationUpdateScriptTemplate.addNamedParameter("protocol", (FrameworkConfiguration.isHttpsEnabled() ? "https" : "http"));
            quotationUpdateScriptTemplate.addNamedParameter("follow_text", Context.tr("Follow"));
            quotationUpdateScriptTemplate.addNamedParameter("following_text", Context.tr("Following"));
            quotationUpdateScriptTemplate.addNamedParameter("unfollow_text", Context.tr("Unfollow"));

            try {
                script.append(quotationUpdateScriptTemplate.getContent(null));
            } catch (final IOException e) {
                Log.web().error("Fail to generate elveos button generation script", e);
            }

            if (isFollowing()) {
                FollowFeature followOrGetFeature = AuthToken.getMember().followOrGetFeature(feature);
                script.append("elveos_bindFollowFeatureButton('" + this.getId() + "', '" + AuthToken.getMember().getId() + "', '" + feature.getId()
                        + "', " + followOrGetFeature.isFeatureComment() + ", " + followOrGetFeature.isBugComment() + ");\n");
            } else {
                script.append("elveos_bindFollowFeatureButton('" + this.getId() + "', '" + AuthToken.getMember().getId() + "', '" + feature.getId()
                        + "', true, true);\n");
            }

            this.add(script);
        }

    }

    public static class HtmlFollowActorButton extends HtmlFollowButton {

        final Actor actor;

        public HtmlFollowActorButton(Actor actor) {
            this.actor = actor;
            generate();
        }

        @Override
        protected boolean isFollowing() {
            if (!AuthToken.isAuthenticated()) {
                return false;
            }

            return AuthToken.getMember().isFollowing(actor);
        }

        @Override
        protected boolean isFollowingWithMail() {
            FollowActor followOrGetActor;
            try {
                followOrGetActor = AuthToken.getMember().followOrGetActor(actor);
            } catch (UnauthorizedOperationException e) {
                return false;
            }
            return followOrGetActor.isMail();
        }

        @Override
        protected Url getFollowUrl() {
            return new FollowActorActionUrl(Context.getSession().getShortKey(), actor, true, false);
        }

        @Override
        protected Url getFollowWithMailUrl() {
            return new FollowActorActionUrl(Context.getSession().getShortKey(), actor, true, true);
        }

        @Override
        protected Url getUnfollowUrl() {
            return new FollowActorActionUrl(Context.getSession().getShortKey(), actor, false, false);
        }

        @Override
        protected void generateScript() {
            // TODO Auto-generated method stub

        }

    }

    public static class HtmlFollowSoftwareButton extends HtmlFollowButton {

        final Software software;

        public HtmlFollowSoftwareButton(Software software) {
            this.software = software;
            generate();
        }

        @Override
        protected boolean isFollowing() {
            if (!AuthToken.isAuthenticated()) {
                return false;
            }

            return AuthToken.getMember().isFollowing(software);
        }

        @Override
        protected boolean isFollowingWithMail() {
            FollowSoftware followOrGetSoftware;
            try {
                followOrGetSoftware = AuthToken.getMember().followOrGetSoftware(software);
            } catch (UnauthorizedOperationException e) {
                return false;
            }
            return followOrGetSoftware.isMail();
        }

        @Override
        protected Url getFollowUrl() {
            return new FollowSoftwareActionUrl(Context.getSession().getShortKey(), true, false, software);
        }

        @Override
        protected Url getFollowWithMailUrl() {
            return new FollowSoftwareActionUrl(Context.getSession().getShortKey(), true, true, software);
        }

        @Override
        protected Url getUnfollowUrl() {
            return new FollowSoftwareActionUrl(Context.getSession().getShortKey(), false, false, software);
        }

        @Override
        protected void generateScript() {

            if (!AuthToken.isAuthenticated()) {
                return;
            }

            final HtmlScript script = new HtmlScript();
            final RandomString rng = new RandomString(10);
            if (this.getId() == null) {
                this.setId(rng.nextString());
            }

            final TemplateFile quotationUpdateScriptTemplate = new TemplateFile("software_follow_button.js");
            quotationUpdateScriptTemplate.addNamedParameter("hostname", Context.getHeader().getHttpHost());
            quotationUpdateScriptTemplate.addNamedParameter("protocol", (FrameworkConfiguration.isHttpsEnabled() ? "https" : "http"));
            quotationUpdateScriptTemplate.addNamedParameter("follow_text", Context.tr("Follow"));
            quotationUpdateScriptTemplate.addNamedParameter("following_text", Context.tr("Following"));
            quotationUpdateScriptTemplate.addNamedParameter("unfollow_text", Context.tr("Unfollow"));

            try {
                script.append(quotationUpdateScriptTemplate.getContent(null));
            } catch (final IOException e) {
                Log.web().error("Fail to generate elveos button generation script", e);
            }

            script.append("elveos_bindFollowSoftwareButton('" + this.getId() + "', '" + AuthToken.getMember().getId() + "', '" + software.getId()
                    + "');\n");

            this.add(script);
        }

    }

    public static class HtmlFollowAllButton extends HtmlFollowButton {

        public HtmlFollowAllButton() {
            generate();
        }

        @Override
        protected boolean isFollowing() {
            if (!AuthToken.isAuthenticated()) {
                return false;
            }

            try {
                return AuthToken.getMember().isGlobalFollow();
            } catch (UnauthorizedPrivateAccessException e) {
                return false;
            }
        }

        @Override
        protected boolean isFollowingWithMail() {
            if (!AuthToken.isAuthenticated()) {
                return false;
            }

            try {
                return AuthToken.getMember().isGlobalFollowWithMail();
            } catch (UnauthorizedPrivateAccessException e) {
                return false;
            }
        }

        @Override
        protected Url getFollowUrl() {
            return new FollowAllActionUrl(Context.getSession().getShortKey(), true, false);
        }

        @Override
        protected Url getFollowWithMailUrl() {
            return new FollowAllActionUrl(Context.getSession().getShortKey(), true, true);
        }

        @Override
        protected Url getUnfollowUrl() {
            return new FollowAllActionUrl(Context.getSession().getShortKey(), false, false);
        }

        @Override
        protected void generateScript() {

            if (!AuthToken.isAuthenticated()) {
                return;
            }

            final HtmlScript script = new HtmlScript();
            final RandomString rng = new RandomString(10);
            if (this.getId() == null) {
                this.setId(rng.nextString());
            }

            final TemplateFile quotationUpdateScriptTemplate = new TemplateFile("all_follow_button.js");
            quotationUpdateScriptTemplate.addNamedParameter("hostname", Context.getHeader().getHttpHost());
            quotationUpdateScriptTemplate.addNamedParameter("protocol", (FrameworkConfiguration.isHttpsEnabled() ? "https" : "http"));
            quotationUpdateScriptTemplate.addNamedParameter("follow_text", Context.tr("Follow"));
            quotationUpdateScriptTemplate.addNamedParameter("following_text", Context.tr("Following"));
            quotationUpdateScriptTemplate.addNamedParameter("unfollow_text", Context.tr("Unfollow"));

            try {
                script.append(quotationUpdateScriptTemplate.getContent(null));
            } catch (final IOException e) {
                Log.web().error("Fail to generate elveos button generation script", e);
            }

            script.append("elveos_bindFollowAllButton('" + this.getId() + "', '" + AuthToken.getMember().getId() + "');\n");

            this.add(script);
        }

    }

}
