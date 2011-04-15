package com.bloatit.web.linkable.members;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.model.Actor;
import com.bloatit.model.Member;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.url.FileResourceUrl;

public class MembersTools {

    public static HtmlElement getMemberAvatar(final Actor<?> actor) {

        final HtmlDiv avatarDiv = new HtmlDiv("avatar_block");
        if (actor.getAvatar() == null) {
            if (actor instanceof Member) {
                avatarDiv.add(new HtmlImage(new Image(WebConfiguration.getImgNoAvatar()), tr("Member avatar"), "avatar"));
            } else {
                avatarDiv.add(new HtmlImage(new Image(WebConfiguration.getImgNoAvatar()), tr("Team avatar"), "avatar"));
            }
        } else {
            final FileResourceUrl imageUrl = new FileResourceUrl(actor.getAvatar());
            avatarDiv.add(new HtmlImage(imageUrl, tr("Member avatar"), "avatar"));
        }

        return avatarDiv;
    }

    public static HtmlElement getMemberAvatarSmall(final Member member) {

        final HtmlDiv avatarDiv = new HtmlDiv("avatar_small_block");
        if (member.getAvatar() == null) {
            avatarDiv.add(new HtmlImage(new Image(WebConfiguration.getImgNoAvatar()), tr("Member avatar"), "avatar_small"));
        } else {
            final FileResourceUrl imageUrl = new FileResourceUrl(member.getAvatar());
            avatarDiv.add(new HtmlImage(imageUrl, tr("Member avatar"), "avatar_small"));
        }

        return avatarDiv;
    }

}
