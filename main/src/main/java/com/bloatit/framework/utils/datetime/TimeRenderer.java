package com.bloatit.framework.utils.datetime;

import static com.bloatit.framework.webprocessor.context.Context.trn;

import java.util.Date;

import com.bloatit.framework.utils.i18n.DateLocale;
import com.bloatit.framework.webprocessor.context.Context;

/**
 * A class used to render time
 */
public class TimeRenderer {

    public enum TimeBase {
        WEEK(1000 * DateUtils.SECOND_PER_WEEK), //
        DAY(1000 * DateUtils.SECOND_PER_DAY), //
        HOUR(1000 * DateUtils.SECOND_PER_HOUR), //
        MINUTE(1000 * DateUtils.SECOND_PER_MINUTE), //
        SECOND(1000), //
        MILLI(1), //
        ;

        private final long coef;

        private TimeBase(final long coef) {
            this.coef = coef;
        }

        public long getCoef() {
            return coef;
        }

    }

    private final long milliseconds;
    private final TimeRenderer.TimeBase base;

    public TimeRenderer(final long miliseconds) {
        super();
        this.milliseconds = miliseconds;
        base = computeBestResolution();
    }

    public TimeRenderer(final Date date) {
        this(date.getTime());
    }

    private TimeRenderer.TimeBase computeBestResolution() {
        final long positiveMillisecond = Math.abs(milliseconds);
        if (positiveMillisecond < 1000) {
            return TimeBase.MILLI;
        }
        final long second = positiveMillisecond / 1000;
        if (second > DateUtils.SECOND_PER_WEEK) {
            return TimeBase.WEEK;
        }
        if (second > DateUtils.SECOND_PER_DAY) {
            return TimeBase.DAY;
        }
        if (second > DateUtils.SECOND_PER_HOUR) {
            return TimeBase.HOUR;
        }
        if (second > DateUtils.SECOND_PER_MINUTE) {
            return TimeBase.MINUTE;
        }
        return TimeBase.SECOND;
    }

    public String getTimeString() {
        if (getSubBase() != null) {
            return printTime(getTime(), getBase()) + " " + printTime(getSubTime(), getSubBase());
        }
        return printTime(getTime(), getBase());
    }

    private String printTime(final long time, final TimeBase theBase) {
        switch (theBase) {
            case WEEK:
                return trn("{0} week", "{0} weeks", time, time);
            case DAY:
                return trn("{0} day", "{0} days", time, time);
            case HOUR:
                return trn("{0} hour", "{0} hours", time, time);
            case MINUTE:
                return trn("{0} minute", "{0} minutes", time, time);
            case SECOND:
                return trn("{0} second", "{0} seconds", time, time);
            case MILLI:
                return trn("{0} millisecond", "{0} milliseconds", time, time);
            default:
                return trn("{0} second", "{0} seconds", time, time);
        }
    }

    public TimeRenderer.TimeBase getBase() {
        return base;
    }

    public long getTime() {
        return milliseconds / base.getCoef();
    }

    public long getSubTime() {
        if (getSubBase() == null) {
            return 0;
        }
        return (milliseconds - (getTime() * base.getCoef())) / getSubBase().getCoef();
    }

    // private long getTime(TimeBase base, long time){
    // return time / base.getCoef();
    // }

    public TimeRenderer.TimeBase getSubBase() {
        switch (base) {
            case WEEK:
                return TimeBase.DAY;
            case DAY:
                return TimeBase.HOUR;
            case HOUR:
                return TimeBase.MINUTE;
            case MINUTE:
                return TimeBase.SECOND;
            case SECOND:
                return TimeBase.MILLI;
            case MILLI:
                return null;
            default:
                break;
        }
        return null;
    }

    /**
     * Renders as a time from now
     * 
     * @param limit the limit after which the date will be rendered as a date
     * @param style the style applied to date rendering when limit is reached
     */
    @SuppressWarnings("synthetic-access")
    public String renderRange(final TimeBase limit, final DateLocale.FormatStyle style) {
        if (milliseconds > limit.coef) {
            return new DateLocale(new Date(milliseconds), Context.getLocalizator().getLocale()).toDateTimeString(style, style);
        }
        // TODO improve rendering
        return getTimeString();
    }
}
