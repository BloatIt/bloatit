package com.bloatit.web.pages;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.framework.webserver.components.form.SimpleDropDownElement;
import com.bloatit.web.pages.AdministrationActionManager.Action;

public class AdministrationActionManager {

    public enum Action {
        DELETE("delete"), RESTORE("restore");

        private final String displayName;

        Action(String displayName) {
            this.displayName = displayName;
        }

        String getActionName() {
            return toString();
        }

        String getDisplayName() {
            return displayName;
        }
    }

    public static List<SimpleDropDownElement> userContentActions() {
        ArrayList<SimpleDropDownElement> list = new ArrayList<SimpleDropDownElement>();
        addToList(list, Action.DELETE);
        addToList(list, Action.RESTORE);
        return list;
    }

    private static void addToList(ArrayList<SimpleDropDownElement> list, Action action) {
        list.add(new SimpleDropDownElement(action.getActionName(), action.getDisplayName()));
    }

}
