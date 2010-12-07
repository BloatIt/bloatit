package test.pages.components;

public class HtmlInput extends test.HtmlElement {

    public HtmlInput(String type) {
        super("input");
        addAttribute("type", type);
    }

    public HtmlInput setName(String name) {
        addAttribute("name", name).addAttribute("id", name);
        return this;
    }

}
