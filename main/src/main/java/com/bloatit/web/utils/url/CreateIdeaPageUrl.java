package com.bloatit.web.utils.url;

import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class CreateIdeaPageUrl extends Url {
    public static String getName() {
        return "idea/create";
    }

    @Override
    public com.bloatit.web.html.pages.CreateIdeaPage createPage() throws RedirectException {
        return new com.bloatit.web.html.pages.CreateIdeaPage(this);
    }

    public CreateIdeaPageUrl(final Parameters params, final Parameters session) {
        this();
        parseParameters(params, false);
        parseParameters(session, true);
    }

    public CreateIdeaPageUrl() {
        super(getName());
    }

    @Override
    protected void doRegister() {
    }

    @Override
    public CreateIdeaPageUrl clone() {
        final CreateIdeaPageUrl other = new CreateIdeaPageUrl();
        return other;
    }
}
