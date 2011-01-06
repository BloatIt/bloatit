package com.bloatit.web.html.components.custom.renderer;

import com.bloatit.web.html.HtmlTagText;

public abstract class HtmlTextRenderer extends HtmlTagText{
	
	public HtmlTextRenderer(String text) {
	    super();
	    super.content = doRender(text);
    }
	
	protected abstract String doRender(String text);
}
