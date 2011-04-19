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
package com.bloatit.web.linkable.team;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.model.Team;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.url.FileResourceUrl;

public class TeamTools {

    public static HtmlElement getTeamAvatar(final Team team) {

        final HtmlDiv avatarDiv = new HtmlDiv("avatar_block");
        if (team.getAvatar() == null) {
            avatarDiv.add(new HtmlImage(new Image(WebConfiguration.getImgNoAvatar()), tr("Team avatar"), "avatar"));
        } else {
            final FileResourceUrl imageUrl = new FileResourceUrl(team.getAvatar().getMetadata());
            avatarDiv.add(new HtmlImage(imageUrl, tr("Team avatar"), "avatar"));
        }

        return avatarDiv;
    }
}
