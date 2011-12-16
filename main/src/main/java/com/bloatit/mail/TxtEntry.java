package com.bloatit.mail;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.webprocessor.url.Url;

public class TxtEntry {

    private final Date date;
    private final String tr;
    private final Url url;
    private final Localizator l;

    public TxtEntry(Date date, String tr, Localizator l) {
        this(date, tr, l, null);
    }

    public TxtEntry(Date date, String tr, Localizator l, Url url) {
        this.date = date;
        this.tr = tr;
        this.url = url;
        this.l = l;
    }

    public String generate() {
        if (url != null) {
            return "    " + new SimpleDateFormat("EEEE HH:mm", l.getLocale()).format(date).toString() + " — " + tr + url.externalUrlString();
        }
        return "    " + new SimpleDateFormat("EEEE HH:mm", l.getLocale()).format(date).toString() + " — " + tr;
    }

}
