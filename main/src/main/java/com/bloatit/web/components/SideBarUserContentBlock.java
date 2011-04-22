package com.bloatit.web.components;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoUserContent;
import com.bloatit.model.UserContentInterface;
import com.bloatit.web.pages.master.sidebar.TitleSideBarElementLayout;

public class SideBarUserContentBlock extends TitleSideBarElementLayout {
    public SideBarUserContentBlock(final UserContentInterface userContent) {
        setTitle(tr("User content abstract"));
    }
}
