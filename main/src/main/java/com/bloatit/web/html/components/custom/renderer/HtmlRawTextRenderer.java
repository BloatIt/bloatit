package com.bloatit.web.html.components.custom.renderer;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import com.bloatit.web.html.HtmlTools;

/**
 * <p>Renders a user text the same way he entered it</p>
 */
public class HtmlRawTextRenderer extends HtmlTextRenderer{

	public HtmlRawTextRenderer(String text) {
	    super(text);
    }

	@Override
    protected String doRender(String text) {
		String t = HtmlTools.escape(text);
		StringBuilder sb = new StringBuilder();
		StringCharacterIterator it = new StringCharacterIterator(t);
		
		char previous = Character.MIN_VALUE;
		for(char ch=it.first(); ch != CharacterIterator.DONE; ch=it.next()){
			if(ch == ' ' && previous == ' '){
				sb.append("&nbsp;");
			} else if (ch == '\n'){
				sb.append("<br />\n");
			} else {
				sb.append(ch);
			}
			previous = ch;
		}
	    return sb.toString();
    }
}
