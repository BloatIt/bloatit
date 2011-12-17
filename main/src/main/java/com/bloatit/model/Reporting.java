package com.bloatit.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.bloatit.framework.mailsender.Mail;
import com.bloatit.framework.mailsender.MailServer;
import com.bloatit.framework.utils.datetime.DateUtils;

public class Reporting {

    private static final int ID_REPORTING_TASK = 12;

    private final static class PlannedTaskReport extends PlannedTask {
        private static final long serialVersionUID = -605135073621984416L;

        private PlannedTaskReport(final Date time, final int id) {
            super(time, id);
        }

        @Override
        public void doRun() {
            reporter.report();
            new PlannedTaskReport(DateUtils.tomorrow(), ID_REPORTING_TASK);
        }
    }

    public static Reporting reporter = new Reporting();

    // The key is the category (member creation or team creation etc ...)
    private final Map<String, List<String>> lineToReport = new ConcurrentHashMap<String, List<String>>();
    private Date lastReportDate;

    private Reporting() {
        this.lastReportDate = new Date();
        new PlannedTaskReport(DateUtils.tomorrow(), ID_REPORTING_TASK);
    }

    public void reportServerStart() {
        report("Server start", new Date().toString());
    }

    public void reportServerStop() {
        report("Server stop", new Date().toString());
    }

    public void reportMemberCreation(final String login) {
        report("Member creation", login);
    }

    public void reportTeamCreation(final String name) {
        report("Team creation", name);
    }

    public void reportAccountCharging(final String amountFrom) {
        report("Account charging", amountFrom);
    }

    public void reportNewFeature(final String featureName) {
        report("New Feature", featureName);
    }

    public void reportOffer(final String offerName) {
        report("New Offer", offerName);
    }

    public void report(final String key, final String value) {
        List<String> values = lineToReport.get(key);
        if (values == null) {
            values = new LinkedList<String>();
            lineToReport.put(key, values);
        }
        values.add(value);
    }

    public void report() {
        final Date now = new Date();
        final StringBuilder message = new StringBuilder();

        message.append("Report from ");
        message.append(lastReportDate);
        message.append(" to ");
        message.append(now);
        message.append(".\n");
        message.append("\n");

        if (lineToReport.size() == 0) {
            message.append("Nothing to report !");
        }

        for (final Entry<String, List<String>> entrie : lineToReport.entrySet()) {
            message.append(" - ");
            message.append(entrie.getValue().size());

            message.append("\t");
            message.append(entrie.getKey());
            message.append(" : \n");

            for (final String value : entrie.getValue()) {
                message.append("\t\t\t");
                message.append(value);
                message.append(" ;\n");
            }

            message.append("\n");
        }
        lastReportDate = now;

        // RAZ
        lineToReport.clear();

        final Mail email = new Mail("sysadmin@linkeos.com", "[elveos] daily reporting", message.toString(), "Reporting");
        MailServer.getInstance().send(email);
    }

}
