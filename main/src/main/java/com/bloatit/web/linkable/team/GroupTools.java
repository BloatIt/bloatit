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

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlImage;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Group;
import com.bloatit.web.url.FileResourceUrl;

public class GroupTools {
    
    public static HtmlElement getGroupAvatar(Group group) {

        HtmlDiv avatarDiv = new HtmlDiv("avatar_block");
        if (group.getAvatar() == null) {
            avatarDiv.add(new HtmlImage(new Image("none.png", Image.ImageType.LOCAL), tr("Group avatar"), "avatar"));
        } else {
            final FileResourceUrl imageUrl = new FileResourceUrl(group.getAvatar());
            avatarDiv.add(new HtmlImage(imageUrl, tr("Group avatar"), "avatar"));
        }

        return avatarDiv;
    } 
}
