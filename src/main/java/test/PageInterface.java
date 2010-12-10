package test;

import test.html.HtmlElement;

import com.bloatit.web.utils.RequestParamSetter.Messages;

public interface PageInterface {

    public HtmlElement create();

    public String getName();

    public String getCustomCss();

    public void addNotifications(Messages messages);

}
