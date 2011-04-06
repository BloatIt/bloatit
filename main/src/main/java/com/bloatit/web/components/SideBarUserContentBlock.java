package com.bloatit.web.components;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.model.UserContentInterface;
import com.bloatit.web.pages.master.TitleSideBarElementLayout;

public class SideBarUserContentBlock extends TitleSideBarElementLayout {

    public SideBarUserContentBlock(UserContentInterface userContent) {
        setTitle(tr("User content abstract"));


    }

}
