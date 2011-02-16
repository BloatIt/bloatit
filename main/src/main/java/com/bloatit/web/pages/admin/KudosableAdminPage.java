package com.bloatit.web.pages.admin;

import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.web.url.UserContentAdminPageUrl;

@ParamContainer("admin/kudosable")
public class KudosableAdminPage extends UserContentAdminPage{

    public KudosableAdminPage(UserContentAdminPageUrl url) {
        super(url);
    }

}
