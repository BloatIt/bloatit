package com.bloatit.framework.feedbackworker;

import java.util.Locale;

import com.bloatit.framework.utils.i18n.Localizator;

public abstract class FeedBackWorker<T> {

    private final Class<T> theClass;
    private Localizator localizator;

    public FeedBackWorker(Class<T> theClass, Locale l) {
        this(theClass);
        setLocal(l);
    }

    public FeedBackWorker(Class<T> theClass) {
        this.theClass = theClass;
        this.localizator = null;
    }

    public void setLocal(Locale l) {
        localizator = new Localizator(l);
    }

    @SuppressWarnings("unchecked")
    public boolean work(Object object) {
        if (object.getClass().isAssignableFrom(theClass)) {
            return doWork((T) object);
        }
        return false;
    }

    protected abstract boolean doWork(T object);

    protected Localizator getLocalizator() {
        return localizator;
    }

    protected Locale getLocal() {
        return getLocalizator().getLocale();
    }

    protected String tr(String toTranslate) {
        return localizator.tr(toTranslate);
    }

    protected String tr(String toTranslate, Object... parameters) {
        return localizator.tr(toTranslate, parameters);
    }

    protected String trn(String singular, String plural, long amount) {
        return localizator.trn(singular, plural, amount);
    }

    protected String trn(String singular, String plural, long amount, Object... parameters) {
        return localizator.trn(singular, plural, amount, parameters);
    }

    protected String trc(String context, String text) {
        return localizator.trc(context, text);
    }
}
