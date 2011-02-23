package com.bloatit.web.members;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlImage;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Member;
import com.bloatit.web.url.FileResourceUrl;

public class MembersTools {

    public static HtmlElement getMemberAvatar(Member member) {

        HtmlDiv avatarDiv = new HtmlDiv("avatar_block");
        if (member.getAvatar() == null) {
            avatarDiv.add(new HtmlImage(new Image("none.png", Image.ImageType.LOCAL), tr("Member avatar"), "avatar"));
        } else {
            final FileResourceUrl imageUrl = new FileResourceUrl(member.getAvatar());
            avatarDiv.add(new HtmlImage(imageUrl, tr("Member avatar"), "avatar"));
        }

        return avatarDiv;
    }
}
