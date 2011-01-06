package com.bloatit.web.html;

/**
 * <p>
 * Class used to directly input text (any format) into the page.
 * </p>
 * <p>
 * Usage another_component.add(new HtmlText("<span class="plop">foo</span>));
 * </p>
 */
public class HtmlText extends HtmlTagText {
	
	/**
	 * Creates a component to add text to a page
	 * @param text the Html text to add to add
	 */
	public HtmlText(final String text) {
		super(HtmlTools.escape(text));
	}
}
