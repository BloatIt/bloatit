package com.bloatit.web.utils.url;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.annotations.RequestParamSetter.Messages;

public abstract class Url extends UrlComponent {

    private final String name;
    private List<UrlComponent> components = new ArrayList<UrlComponent>();

    protected Url(String name) {
        super();
        this.name = name;
    }


    protected void register(UrlComponent component) {
        components.add(component);
    }

    @Override
    final void parseParameters(Parameters params) {
        super.parseParameters(params); // doRegister done in super class.
        for (UrlComponent comp : components) {
            comp.parseParameters(params);
        }
    }

    @Override
    protected void constructUrl(StringBuilder sb) {
        if (Context.getSession() != null) {
            sb.append("/").append(Context.getSession().getLanguage().getCode());
        }
        sb.append("/").append(name);
        for (UrlComponent comp : components) {
            sb.append(comp.toString());
        }
        super.constructUrl(sb);
    }

    @Override
    public final Messages getMessages(){
        Messages messages = super.getMessages();
        for (UrlComponent cmp : components) {
            messages.addAll(cmp.getMessages());
        }
        return messages;
    }
    
    public HtmlNode getHtmlLink(HtmlNode data) {
        return new HtmlLink(this.toString(), data);
    }

    public HtmlNode getHtmlLink(String text) {
        return new HtmlLink(this.toString(), new HtmlText(text));
    }

}
